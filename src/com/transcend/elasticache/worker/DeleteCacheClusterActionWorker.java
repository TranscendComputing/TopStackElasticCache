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
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.CFUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionResultMessage;

public class DeleteCacheClusterActionWorker extends 
		AbstractWorker<DeleteCacheClusterActionRequestMessage, DeleteCacheClusterActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DeleteCacheClusterActionWorker.class
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
   public DeleteCacheClusterActionResultMessage doWork(
           DeleteCacheClusterActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DeleteCacheClusterAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DeleteCacheClusterActionResultMessage doWork0(DeleteCacheClusterActionRequestMessage req,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		DeleteCacheClusterActionResultMessage.Builder result = DeleteCacheClusterActionResultMessage.newBuilder();
		final String clusterId = req.getCacheClusterId();
		logger.debug("deleteCacheClusters");
		logger.debug("ClusterID = " + clusterId);

		// Validate cache cluster exists
		final CacheClusterBean cc = EcacheUtil.getCacheClusterBean(session,
				account.getId(), clusterId);
		if (cc == null) {
			throw ElasticacheFaults.CacheClusterNotFound();
		}

		// Validate State
		// if (currentCluster.getCacheClusterStatus() !=
		// CacheClusterStatus.available) {
		// throw ElasticacheFaults.InvalidCacheClusterState(requestId);
		// }

		cc.setCacheClusterStatus("deleting");
		session.save(cc);

		final List<CacheNodeBean> lnb = EcacheUtil.selectCacheNodeBean(session,
				cc.getId());
		for (final CacheNodeBean nb : lnb) {
			nb.setNodeStatus("deleting");
			session.save(nb);
		}

		result.setCacheCluster(EcacheUtilV2.toAwsCacheCluster(session, cc));

		CFUtil.deleteAsyncStackResources(AccountUtil.toAccount(account),
				cc.getStackId(), null, null);
		return result.buildPartial();
	}
}

