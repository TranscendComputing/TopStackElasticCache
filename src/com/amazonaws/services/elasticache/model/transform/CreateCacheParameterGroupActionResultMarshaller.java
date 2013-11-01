package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionResultMessage;

public class CreateCacheParameterGroupActionResultMarshaller implements
		Marshaller<String, CreateCacheParameterGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <CreateCacheParameterGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <CreateCacheParameterGroupActionResult> <CacheParameterGroup> details of
	 * Parameter Group </CacheParameterGroup> </CreateCacheParameterGroupActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </CreateCacheParameterGroupActionResponse>
	 * 
	 * @param CacheParameterGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final CreateCacheParameterGroupActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("CreateCacheParameterGroupActionResponse");

		// add CreateCacheParameterGroupActionResult
		final XMLNode nodeResults = new XMLNode(
				"CreateCacheParameterGroupActionResult");
		nodeRoot.addNode(nodeResults);

		final XMLNode nodeCacheParameterGroup = MarshallingUtils
				.marshallCacheParameterGroup(in.getCacheParameterGroup());
		nodeResults.addNode(nodeCacheParameterGroup);

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
