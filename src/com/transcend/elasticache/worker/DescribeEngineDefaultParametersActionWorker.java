package com.transcend.elasticache.worker;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.model.elasticache.CacheParameterGroupFamilyBean;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionResultMessage;
import com.transcend.elasticache.message.DescribeEngineDefaultParametersActionMessage.DescribeEngineDefaultParametersActionRequestMessage;
import com.transcend.elasticache.message.DescribeEngineDefaultParametersActionMessage.DescribeEngineDefaultParametersActionResultMessage;

public class DescribeEngineDefaultParametersActionWorker extends 
		AbstractWorker<DescribeEngineDefaultParametersActionRequestMessage, DescribeEngineDefaultParametersActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeEngineDefaultParametersActionWorker.class
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
   public DescribeEngineDefaultParametersActionResultMessage doWork(
           DescribeEngineDefaultParametersActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeEngineDefaultParametersAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeEngineDefaultParametersActionResultMessage doWork0(DescribeEngineDefaultParametersActionRequestMessage request,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0");

		final Session 	  session = getSession();

		final String name = request.getCacheParameterGroupFamily();
		final String marker = request.getMarker();
		final int maxRecords = request.getMaxRecords();
		logger.debug("DescribeCacheParameterGroups");
		logger.debug("ParameterGroupFamilyName = " + name);
		logger.debug("Marker = " + marker);
		logger.debug("MaxRecord = " + maxRecords);

		// Validate the family
		final CacheParameterGroupFamilyBean family = EcacheUtil
				.getParameterGroupFamily(session, name);
		if (family == null) {
			throw QueryFaults.InvalidParameterValue();
		}

		final CacheParameterGroupBean group = EcacheUtil
				.getFamilyParameterGroup(session, name);

		if (group == null) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}

		final DescribeEngineDefaultParametersActionResultMessage.Builder result = DescribeEngineDefaultParametersActionResultMessage.newBuilder();
		result.setCacheParameterGroupFamily(name);
		final DescribeCacheParametersActionResultMessage.Builder cret = EcacheUtilV2
				.toDescribeCacheParametersResult(session, group.getId(), null);
		result.addAllCacheNodeTypeSpecificParameters(cret
				.getCacheNodeTypeSpecificParametersList());
		result.addAllParameters(cret.getParametersList());

		result.setMarker(request.getMarker());
		return result.buildPartial();
	}
}

