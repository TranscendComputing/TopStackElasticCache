package com.transcend.elasticache.worker;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.RebootCacheClusterActionMessage.RebootCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.RebootCacheClusterActionMessage.RebootCacheClusterActionResultMessage;

public class RebootCacheClusterActionWorker extends 
		AbstractWorker<RebootCacheClusterActionRequestMessage, RebootCacheClusterActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(RebootCacheClusterActionWorker.class
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
   public RebootCacheClusterActionResultMessage doWork(
           RebootCacheClusterActionRequestMessage req) throws Exception {
       logger.debug("Performing work for RebootCacheClusterAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected RebootCacheClusterActionResultMessage doWork0(RebootCacheClusterActionRequestMessage request,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		final String clusterId = request.getCacheClusterId();
		RebootCacheClusterActionResultMessage.Builder result = RebootCacheClusterActionResultMessage.newBuilder();
		logger.debug("ClusterID = " + clusterId);

		// Validate cache cluster exists
		final CacheClusterBean cc = EcacheUtil.getCacheClusterBean(session,
				account.getId(), clusterId);
		if (cc == null) {
			throw ElasticacheFaults.CacheClusterNotFound();
		}

		// Validate State
		if (!cc.getCacheClusterStatus().equals("available")
				 && !cc.getCacheClusterStatus().equals("modifying")) {
			throw ElasticacheFaults.InvalidCacheClusterState();
		}
		EcacheUtil.rebootCluster(session, cc, AccountUtil.toAccount(account));
		result.setCacheCluster(EcacheUtilV2.toAwsCacheCluster(session, cc));
		return result.buildPartial();
	}
}

