package com.transcend.elasticache.worker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.core.CommaObject;
import com.msi.tough.core.StringHelper;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheNodeTypeBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.CFUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;

public class CreateCacheClusterActionWorker extends 
		AbstractWorker<CreateCacheClusterActionRequestMessage, CreateCacheClusterActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(CreateCacheClusterActionWorker.class
					.getName());
	
    /**
    * We need a local copy of this doWork to provide the transactional
    * annotation.  Transaction management is handled by the annotation, which
    * can only be on a concrete class.
    * @param req
    * @return
    * @throws Exception
    */
   @Transactional
   public CreateCacheClusterActionResultMessage doWork(
           CreateCacheClusterActionRequestMessage req) throws Exception {
       logger.debug("Performing work for CreateCacheClusterAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected CreateCacheClusterActionResultMessage doWork0(CreateCacheClusterActionRequestMessage req,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0 - Create Cache Cluster");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		String preferredAvZone = req.getPreferredAvailabilityZone();
		if("".equals(preferredAvZone))
			preferredAvZone = null;
		CreateCacheClusterActionResultMessage.Builder result = CreateCacheClusterActionResultMessage.newBuilder();
		EcacheUtil.ensureDefaultSecurityGroup(session, account.getId());
		{
			final CacheClusterBean cb = EcacheUtil.getCacheClusterBean(session,
					account.getId(), req.getCacheClusterId());
			if (cb != null) {
				throw ElasticacheFaults.CacheClusterAlreadyExists();
			}
		}

		// unmarshall request XML into a java bean
		final Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put(Constants.AUTOMINORVERSIONUPGRADE, req
				.getAutoMinorVersionUpgrade() + "");
		parameterValues.put(Constants.CACHENODETYPE, req.getCacheNodeType());
		String paramGrp = req.getCacheParameterGroupName();
		if (paramGrp == null || "".equals(paramGrp)) {
			paramGrp = "default";
		}
		parameterValues.put(Constants.CACHEPARAMETERGROUPNAME, paramGrp);
		parameterValues.put(Constants.CACHESECURITYGROUPNAMES,
				req.getCacheSecurityGroupNamesList());
		if (req.getEngine() != null && !"".equals(req.getEngine())) {
			parameterValues.put(Constants.ENGINE, req.getEngine());
		} else {
			parameterValues.put(Constants.ENGINE, "memcached");
		}
		if (req.getEngineVersion() != null && !"".equals(req.getEngineVersion())) {
			parameterValues.put(Constants.ENGINEVERSION, req.getEngineVersion());
		} else {
			parameterValues.put(Constants.ENGINEVERSION, "1.4");
		}

		parameterValues.put(Constants.NOTIFICATIONTOPICARN,
				req.getNotificationTopicArn());
		parameterValues.put(Constants.NUMCACHENODES, req.getNumCacheNodes() + "");
		parameterValues.put(Constants.PORT, req.getPort() + "");
		parameterValues.put(Constants.PREFERREDAVAILABILITYZONE,
				preferredAvZone);
		parameterValues.put(Constants.PREFERREDMAINENANCEWINDOW,
				req.getPreferredMaintenanceWindow());

		// save basic data
		final CacheClusterBean cacheCluster = new CacheClusterBean();
		cacheCluster.setAcid(account.getId());
		cacheCluster.setCreatedTime(new Date());
		cacheCluster.setCacheClusterStatus("creating");
		cacheCluster.setName(req.getCacheClusterId());
		cacheCluster.setNodeCount(req.getNumCacheNodes());
		cacheCluster.setEngine(req.getEngine().toString());
		cacheCluster.setEngineVersion(req.getEngineVersion());
		final CacheNodeTypeBean cacheNodeType = EcacheUtil
				.getCacheNodeTypeBean(session, req.getCacheNodeType());
		if (cacheNodeType == null || "".equals(cacheNodeType)) {
			throw ErrorResponse.invlidData("CacheNodeType not found:"
					+ req.getCacheNodeType());
		}
		cacheCluster.setNodeTypeId((long) cacheNodeType.getId());
		final CacheParameterGroupBean parameterGroup = EcacheUtil
				.getCacheParameterGroupBean(session, account.getId(), paramGrp);
		if (parameterGroup == null || "".equals(parameterGroup)) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}
		cacheCluster.setParameterGroupId((long) parameterGroup.getId());
		cacheCluster.setPreferredAvailabilityZone(preferredAvZone);
		final CommaObject requestedSecurityGroups = new CommaObject(
				req.getCacheSecurityGroupNamesList());
		cacheCluster.setSecurityGroups(requestedSecurityGroups.toString());
		final String stackId = "__ecache_"
				+ StringHelper.randomStringFromTime();
		cacheCluster.setStackId(stackId);
		session.save(cacheCluster);

		CFUtil.createResourceAsync(AccountUtil.toAccount(account), stackId,
				com.msi.tough.engine.aws.elasticache.CacheCluster.TYPE, null,
				req.getCacheClusterId(), parameterValues);

		final CacheClusterBean cb = EcacheUtil.getCacheClusterBean(session,
				account.getId(), req.getCacheClusterId());
		if (cb == null) {
			throw QueryFaults.internalFailure();
		}
		final CacheCluster awsCacheCluster = EcacheUtilV2.toAwsCacheCluster(
				session, cb);

		logger.debug("return from process0");
		result.setCacheCluster(awsCacheCluster);
		return result.buildPartial();
	}
}

