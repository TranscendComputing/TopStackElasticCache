package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeEngineDefaultParametersActionMessage.DescribeEngineDefaultParametersActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificParameter;
import com.transcend.elasticache.message.ElastiCacheMessage.Parameter;

public class DescribeEngineDefaultParametersActionResultMarshaller implements
		Marshaller<String, DescribeEngineDefaultParametersActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DescribeEngineDefaultParametersActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeEngineDefaultParametersActionResult> <EngineDefaults>
	 * <CacheParameterGroupFamily> <Marker> <CacheNodeTypeSpecificParameters>
	 * <Parameters> </EngineDefaults> </DescribeEngineDefaultParametersActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeEngineDefaultParametersAction>
	 * 
	 * @param DescribeEngineDefaultParametersAction
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final DescribeEngineDefaultParametersActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DescribeEngineDefaultParametersActionResponse");

		// add DescribeEngineDefaultParametersAction
		final XMLNode nodeResults = new XMLNode(
				"DescribeEngineDefaultParametersActionResult");
		nodeRoot.addNode(nodeResults);
		
		QueryUtil.addNode(nodeResults, Constants.CACHEPARAMETERGROUPFAMILY,
				in.getCacheParameterGroupFamily());

		QueryUtil.addNode(nodeResults, Constants.MARKER,
				in.getMarker());

		//add EngineDefaults node
		final XMLNode nodeEngineDefaults= new XMLNode("EngineDefaults");
		nodeResults.addNode(nodeEngineDefaults);
				
		final List<CacheNodeTypeSpecificParameter> cnSpecificList = in
				.getCacheNodeTypeSpecificParametersList();
		if (cnSpecificList != null) {

			final XMLNode mcnsParams = MarshallingUtils
					.marshallCacheNodeTypeSpecificParameters(cnSpecificList);
			nodeEngineDefaults.addNode(mcnsParams);
		}

		final List<Parameter> paramList = in.getParametersList();
		if (paramList != null) {

			final XMLNode params = MarshallingUtils
					.marshallParameters(paramList);
			nodeEngineDefaults.addNode(params);
		}

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		QueryUtil.addNode(nodeMetaData, "RequestId", in.getRequestId());

		return nodeRoot.toString();
	}
}
