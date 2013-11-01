package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionResultMessage;

public class DeleteCacheClusterActionResultMarshaller implements
		Marshaller<String, DeleteCacheClusterActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DeleteCacheClusterActionsResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DeleteCacheClusterActionsResult> <CacheCluster> details of cache cluster
	 * </CacheCluster> </DeleteCacheClusterActionsResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DeleteCacheClusterActionsResponse>
	 * 
	 * @param CacheCluster
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final DeleteCacheClusterActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DeleteCacheClusterActionsResponse");

		// add DeleteCacheClusterActionsResult
		final XMLNode nodeResults = new XMLNode("DeleteCacheClusterActionsResult");
		nodeRoot.addNode(nodeResults);

		final XMLNode nodeCacheCluster = MarshallingUtils.marshallCacheCluster(
				in.getCacheCluster(), true);
		nodeResults.addNode(nodeCacheCluster);

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
