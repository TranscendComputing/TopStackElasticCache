package com.transcend.elasticache.worker;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheSecurityGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;

public class CreateCacheSecurityGroupActionWorker extends 
		AbstractWorker<CreateCacheSecurityGroupActionRequestMessage, CreateCacheSecurityGroupActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(CreateCacheSecurityGroupActionWorker.class
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
   public CreateCacheSecurityGroupActionResultMessage doWork(
           CreateCacheSecurityGroupActionRequestMessage req) throws Exception {
       logger.debug("Performing work for CreateCacheSecurityGroupAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected CreateCacheSecurityGroupActionResultMessage doWork0(CreateCacheSecurityGroupActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		final String secGrpName = request.getCacheSecurityGroupName();
		logger.debug("SecurityGroupName = " + secGrpName);

		EcacheUtil.ensureDefaultSecurityGroup(session, account.getId());

		CacheSecurityGroupBean secGrp = EcacheUtil.getCacheSecurityGroupBean(
				session, account.getId(), secGrpName);
		if (secGrp != null) {
			throw ElasticacheFaults.CacheSecurityGroupAlreadyExists();
		}
		secGrp = EcacheUtil.createSecurityGroup(session, account.getId(),
				secGrpName, request.getDescription(), null, null);

		final CreateCacheSecurityGroupActionResultMessage.Builder result = CreateCacheSecurityGroupActionResultMessage.newBuilder();
		CacheSecurityGroup.Builder cacheSecGp = CacheSecurityGroup.newBuilder();
		cacheSecGp.setCacheSecurityGroupName(secGrpName);
		cacheSecGp.setDescription(request.getDescription());
		cacheSecGp.setOwnerId("" + account.getId());
		result.setCacheSecurityGroup(cacheSecGp);
		return result.buildPartial();
	}
}

