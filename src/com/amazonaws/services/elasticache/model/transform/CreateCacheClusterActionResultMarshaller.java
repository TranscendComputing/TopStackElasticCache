package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionResultMessage;

public class CreateCacheClusterActionResultMarshaller implements
		Marshaller<String, CreateCacheClusterActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <CreateCacheClusterActionsResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <CreateCacheClusterActionsResult> <CacheCluster> details of cache cluster
	 * </CacheCluster> </CreateCacheClusterActionsResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </CreateCacheClusterActionsResponse>
	 * 
	 * @param CacheCluster
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final CreateCacheClusterActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("CreateCacheClusterActionsResponse");

		// add CreateCacheClusterActionsResult
		final XMLNode nodeResults = new XMLNode("CreateCacheClusterActionsResult");
		nodeRoot.addNode(nodeResults);

		final XMLNode nodeCacheCluster = MarshallingUtils.marshallCacheCluster(
				in.getCacheCluster(), false);
		nodeResults.addNode(nodeCacheCluster);

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
