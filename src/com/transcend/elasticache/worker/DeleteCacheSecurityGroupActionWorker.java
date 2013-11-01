package com.transcend.elasticache.worker;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheSecurityGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionRequestMessage;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionResultMessage;

public class DeleteCacheSecurityGroupActionWorker extends 
		AbstractWorker<DeleteCacheSecurityGroupActionRequestMessage, DeleteCacheSecurityGroupActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DeleteCacheSecurityGroupActionWorker.class
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
   public DeleteCacheSecurityGroupActionResultMessage doWork(
           DeleteCacheSecurityGroupActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DeleteCacheSecurityGroupAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DeleteCacheSecurityGroupActionResultMessage doWork0(DeleteCacheSecurityGroupActionRequestMessage req,
			ServiceRequestContext context) throws Exception {

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		DeleteCacheSecurityGroupActionResultMessage.Builder result = DeleteCacheSecurityGroupActionResultMessage.newBuilder();
		final String secGrpName = req.getCacheSecurityGroupName();
		logger.debug("SecurityGroupName = " + secGrpName);

		final CacheSecurityGroupBean securityGroup = EcacheUtil
				.getCacheSecurityGroupBean(session, account.getId(), secGrpName);

		// Validate Exists
		if (securityGroup == null) {
			throw ElasticacheFaults.CacheSecurityGroupNotFound();
		}

		final List<CacheClusterBean> ccbs = EcacheUtil.selectCacheClusterBean(
				session, account.getId(), null);
		boolean used = false;
		for (final CacheClusterBean ccb : ccbs) {
			if (ccb.getSecurityGroups().contains(secGrpName)) {
				used = true;
				break;
			}
		}
		if (used) {
			throw ElasticacheFaults.InvalidCacheSecurityGroupState();
		}

		EcacheUtil.deleteSecurityGroupBean(session,
				AccountUtil.toAccount(account), secGrpName);
		return result.buildPartial();
	}
}

