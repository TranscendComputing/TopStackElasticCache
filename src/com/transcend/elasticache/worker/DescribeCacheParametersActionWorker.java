package com.transcend.elasticache.worker;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionResultMessage;

public class DescribeCacheParametersActionWorker extends 
		AbstractWorker<DescribeCacheParametersActionRequestMessage, DescribeCacheParametersActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeCacheParametersActionWorker.class
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
   public DescribeCacheParametersActionResultMessage doWork(
           DescribeCacheParametersActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeCacheParametersAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeCacheParametersActionResultMessage doWork0(DescribeCacheParametersActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0");


		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		final String paramGrpName = request.getCacheParameterGroupName();
		final String source = request.getSource().toString();
		final String marker = request.getMarker();
		final int maxRecords = request.getMaxRecords();
		final boolean hasMarker = marker != null || paramGrpName == null;
		//TODO 
		//getOtherData().put("ShowMarker", hasMarker);

		logger.debug("DescribeCacheParameterGroups");
		logger.debug("ParameterGroupName = " + paramGrpName);
		logger.debug("Source = " + source);
		logger.debug("Marker = " + marker);
		logger.debug("MaxRecord = " + maxRecords);

		// Validate the Parameter Group Exists
		final CacheParameterGroupBean parameterGroup = EcacheUtil
				.getCacheParameterGroupBean(session, account.getId(),
						paramGrpName);
		if (parameterGroup == null) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}

		final DescribeCacheParametersActionResultMessage.Builder result = EcacheUtilV2
				.toDescribeCacheParametersResult(session,
						parameterGroup.getId(), source);
		if (paramGrpName == null) {
			result.setMarker(request.getMarker());
		}
		return result.buildPartial();
	}
}

