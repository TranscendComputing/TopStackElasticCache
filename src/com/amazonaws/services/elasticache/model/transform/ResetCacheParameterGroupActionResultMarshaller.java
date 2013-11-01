package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionResultMessage;

public class ResetCacheParameterGroupActionResultMarshaller implements
		Marshaller<String, ResetCacheParameterGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <ResetCacheParameterGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <ResetCacheParameterGroupActionResult> <CacheParameterGroupName>
	 * </ResetCacheParameterGroupActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </ResetCacheParameterGroupActionResponse>
	 * 
	 * @param ResetCacheParameterGroupActionResult
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(
			final ResetCacheParameterGroupActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("ResetCacheParameterGroupActionResponse");

		// add ResetCacheParameterGroupActionResult
		final XMLNode nodeResults = new XMLNode(
				"ResetCacheParameterGroupActionResult");
		nodeRoot.addNode(nodeResults);

		MarshallingUtils.marshallString(nodeResults, "CacheParameterGroupName",
				in.getCacheParameterGroupName());

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
