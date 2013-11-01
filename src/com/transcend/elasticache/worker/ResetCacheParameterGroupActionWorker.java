package com.transcend.elasticache.worker;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionResultMessage;

public class ResetCacheParameterGroupActionWorker extends 
		AbstractWorker<ResetCacheParameterGroupActionRequestMessage, ResetCacheParameterGroupActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(ResetCacheParameterGroupActionWorker.class
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
   public ResetCacheParameterGroupActionResultMessage doWork(
           ResetCacheParameterGroupActionRequestMessage req) throws Exception {
       logger.debug("Performing work for ResetCacheParameterGroupAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected ResetCacheParameterGroupActionResultMessage doWork0(ResetCacheParameterGroupActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0 - Reset Cache Parameter Group");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();

		final String paramGrpName = request.getCacheParameterGroupName();
		logger.debug("ParameterGroupName = " + paramGrpName);

		final ResetCacheParameterGroupActionResultMessage.Builder result = ResetCacheParameterGroupActionResultMessage.newBuilder();

		final CacheParameterGroupBean pgrp = EcacheUtil
				.getCacheParameterGroupBean(session, account.getId(),
						paramGrpName);

		// Validate Exists
		if (pgrp == null) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}

		EcacheUtilV2.resetParameters(session, AccountUtil.toAccount(account),
				paramGrpName, request.getParameterNameValuesList(),
				request.getResetAllParameters());

		final List<CacheClusterBean> ccl = EcacheUtil.selectCacheClusterBean(
				session, account.getId(), null);

		for (final CacheClusterBean ccb : ccl) {
			if (ccb.getParameterGroupId() != pgrp.getId()) {
				continue;
			}
			ccb.setParameterGroupStatus("applying");
		}

		result.setCacheParameterGroupName(paramGrpName);
		return result.buildPartial();
	}
}

