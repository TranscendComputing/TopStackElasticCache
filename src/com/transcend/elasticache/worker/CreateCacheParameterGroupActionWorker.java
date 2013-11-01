package com.transcend.elasticache.worker;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.model.elasticache.CacheParameterGroupFamilyBean;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroup;

public class CreateCacheParameterGroupActionWorker extends 
		AbstractWorker<CreateCacheParameterGroupActionRequestMessage, CreateCacheParameterGroupActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(CreateCacheParameterGroupActionWorker.class
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
   public CreateCacheParameterGroupActionResultMessage doWork(
           CreateCacheParameterGroupActionRequestMessage req) throws Exception {
       logger.debug("Performing work for CreateCacheParameterGroupAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected CreateCacheParameterGroupActionResultMessage doWork0(CreateCacheParameterGroupActionRequestMessage request,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		final CacheParameterGroupFamilyBean family = EcacheUtil
				.getParameterGroupFamily(session,
						request.getCacheParameterGroupFamily());
		if (family == null) {
			throw QueryFaults.InvalidParameterValue();
		}

		final String paramGrpName = request.getCacheParameterGroupName();

		final CacheParameterGroupBean pgrp = EcacheUtil
				.getCacheParameterGroupBean(session, account.getId(),
						paramGrpName);

		// Validate Exists
		if (pgrp != null) {
			throw ElasticacheFaults.CacheParameterGroupAlreadyExists();
		}

		final CacheParameterGroupBean cacheParameterGroup = EcacheUtil
				.createParameterGroup(session, account.getId(),
						request.getCacheParameterGroupFamily(),
						request.getCacheParameterGroupName(),
						request.getDescription());

		logger.debug("CreateCacheParameterGroup");
		logger.debug("ParameterGroupName = " + paramGrpName);

		final CreateCacheParameterGroupActionResultMessage.Builder awsResponse = CreateCacheParameterGroupActionResultMessage.newBuilder();
		final CacheParameterGroup.Builder cacheParGp = CacheParameterGroup.newBuilder();
		cacheParGp.setCacheParameterGroupFamily(request
				.getCacheParameterGroupFamily());
		cacheParGp.setCacheParameterGroupName(paramGrpName);
		cacheParGp.setDescription(cacheParameterGroup.getDescription());

		logger.debug("return from process0");
		awsResponse.setCacheParameterGroup(cacheParGp);
		return awsResponse.buildPartial();
	}
}

