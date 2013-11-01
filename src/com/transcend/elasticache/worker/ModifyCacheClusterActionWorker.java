package com.transcend.elasticache.worker;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheNodeBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionResultMessage;

public class ModifyCacheClusterActionWorker extends 
		AbstractWorker<ModifyCacheClusterActionRequestMessage, ModifyCacheClusterActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(ModifyCacheClusterActionWorker.class
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
   public ModifyCacheClusterActionResultMessage doWork(
           ModifyCacheClusterActionRequestMessage req) throws Exception {
       logger.debug("Performing work for ModifyCacheClusterAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected ModifyCacheClusterActionResultMessage doWork0(ModifyCacheClusterActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0 - Modify CacheClusterAction");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		ModifyCacheClusterActionResultMessage.Builder result = ModifyCacheClusterActionResultMessage.newBuilder();
		final String clusterId = request.getCacheClusterId();
		logger.debug("deleteCacheClusters");
		logger.debug("ClusterID = " + clusterId);

		// Validate cache cluster exists
		final CacheClusterBean cc = EcacheUtil.getCacheClusterBean(session,
				account.getId(), clusterId);
		if (cc == null) {
			throw ElasticacheFaults.CacheClusterNotFound();
		}

		// Validate State
		if (!cc.getCacheClusterStatus().equals("available")) {
			throw ElasticacheFaults.InvalidCacheClusterState();
		}

		boolean updateStatus = false;
		if (request.getAutoMinorVersionUpgrade() != false) {
			cc.setAutoMinorVersionUpgrade(request.getAutoMinorVersionUpgrade());
		}
		if (request.getEngineVersion() != null
				&& !request.getEngineVersion().equals(cc.getEngineVersion())) {
			cc.setNewEngineVersion(request.getEngineVersion());
			updateStatus = true;
		}
		if (request.getNumCacheNodes() != 0
				&& request.getNumCacheNodes() != cc.getNodeCount()) {
			if (request.getNumCacheNodes() < cc.getNodeCount()) {
				if (request.getCacheNodeIdsToRemoveList() != null) {
					final List<String> remove = request
							.getCacheNodeIdsToRemoveList();
					final List<CacheNodeBean> cnbs = EcacheUtil
							.selectCacheNodeBean(session, cc.getId());
					int i = 0;
					int noDel = 0;
					for (final CacheNodeBean cnb : cnbs) {
						i++;
						for (final String rn : remove) {
							if (i == Integer.parseInt(rn)) {
								cnb.setNodeStatus("removing");
								session.save(cnb);
								noDel++;
							}
						}
					}
				}
			}
			cc.setNewNodeCount(request.getNumCacheNodes());
			updateStatus = true;
		}
		if (updateStatus) {
			cc.setCacheClusterStatus("modifying");
		}
		if (request.getCacheParameterGroupName() != null && !"".equals(request.getCacheParameterGroupName())) {
			final CacheParameterGroupBean parameterGroup = EcacheUtil
					.getCacheParameterGroupBean(session, account.getId(),
							request.getCacheParameterGroupName());
			if (parameterGroup == null && !"".equals(parameterGroup)) {
				throw ElasticacheFaults.CacheParameterGroupNotFound();
			}
			if (parameterGroup.getId() != cc.getParameterGroupId()) {
				cc.setParameterGroupId((long) parameterGroup.getId());
				updateStatus = true;
			}
		}

		session.save(cc);

		if (request.getApplyImmediately()) {
			EcacheUtil.rebootCluster(session, cc,
					AccountUtil.toAccount(account));
		}

		result.setCacheCluster(EcacheUtilV2.toAwsCacheCluster(session, cc));
		return result.buildPartial();
	}
}

