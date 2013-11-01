package com.transcend.elasticache.worker;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DeleteCacheParameterGroupActionMessage.DeleteCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.DeleteCacheParameterGroupActionMessage.DeleteCacheParameterGroupActionResultMessage;

public class DeleteCacheParameterGroupActionWorker extends 
		AbstractWorker<DeleteCacheParameterGroupActionRequestMessage, DeleteCacheParameterGroupActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DeleteCacheParameterGroupActionWorker.class
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
   public DeleteCacheParameterGroupActionResultMessage doWork(
           DeleteCacheParameterGroupActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DeleteCacheParameterGroupAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DeleteCacheParameterGroupActionResultMessage doWork0(DeleteCacheParameterGroupActionRequestMessage req,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0");
		DeleteCacheParameterGroupActionResultMessage.Builder result = DeleteCacheParameterGroupActionResultMessage.newBuilder();
		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		final String paramGrpName = req.getCacheParameterGroupName();

		logger.debug("DeleteCacheParameterGroup");
		logger.debug("ParameterGroupName = " + paramGrpName);

		final CacheParameterGroupBean pgrp = EcacheUtil
				.getCacheParameterGroupBean(session, account.getId(),
						paramGrpName);

		// Validate Exists
		if (pgrp == null) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}

		final List<CacheClusterBean> ccbs = EcacheUtil.selectCacheClusterBean(
				session, account.getId(), null);
		boolean used = false;
		for (final CacheClusterBean ccb : ccbs) {
			if (ccb.getParameterGroupId() == pgrp.getId()) {
				used = true;
				break;
			}
		}
		if (used) {
			throw ElasticacheFaults.InvalidCacheParameterGroupState();
		}

		EcacheUtil.deleteParameterGroupBean(session, account.getId(),
				paramGrpName);
		return result.buildPartial();
	}
}

