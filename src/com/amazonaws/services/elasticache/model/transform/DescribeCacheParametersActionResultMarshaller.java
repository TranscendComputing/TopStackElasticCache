package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificParameter;
import com.transcend.elasticache.message.ElastiCacheMessage.Parameter;

public class DescribeCacheParametersActionResultMarshaller implements
		Marshaller<String, DescribeCacheParametersActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DescribeCacheParametersActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeCacheParametersActionResult> <CacheParameterGroups> <Marker>
	 * <CacheNodeTypeSpecificParameters> <Parameters> </CacheParameterGroups>
	 * </DescribeCacheParametersActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeCacheParametersActionResponse>
	 * 
	 * @param CacheParameterGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(
			final DescribeCacheParametersActionResultMessage in) {

		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DescribeCacheParametersActionResponse");

		// add DescribeCacheParametersActionResult
		final XMLNode nodeResults = new XMLNode("DescribeCacheParametersActionResult");
		nodeRoot.addNode(nodeResults);

		// MarshallingUtils.marshallString(nodeResults, "Marker",
		// pResult.getMarker());
		final String marker = in.getMarker();
		if (!marker.isEmpty()) {
			QueryUtil.addNode(nodeResults, Constants.MARKER,
					marker);
		}

		final List<CacheNodeTypeSpecificParameter> cnSpecificList = in
				.getCacheNodeTypeSpecificParametersList();
		if (cnSpecificList != null) {

			final XMLNode mcnsParams = MarshallingUtils
					.marshallCacheNodeTypeSpecificParameters(cnSpecificList);

			nodeResults.addNode(mcnsParams);
		}

		final List<Parameter> paramList = in.getParametersList();
		if (paramList != null) {

			final XMLNode params = MarshallingUtils
					.marshallParameters(paramList);
			nodeResults.addNode(params);
		}

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
