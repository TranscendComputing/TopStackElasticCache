package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionResultMessage;

public class ModifyCacheClusterActionResultMarshaller implements
		Marshaller<String, ModifyCacheClusterActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <ModifyCacheClusterActionsResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <ModifyCacheClusterActionsResult> <CacheCluster> details of cache cluster
	 * </CacheCluster> </ModifyCacheClusterActionsResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </ModifyCacheClusterActionsResponse>
	 * 
	 * @param CacheCluster
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final ModifyCacheClusterActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("ModifyCacheClusterActionsResponse");

		// add ModifyCacheClusterActionsResult
		final XMLNode nodeResults = new XMLNode("ModifyCacheClusterActionsResult");
		nodeRoot.addNode(nodeResults);

		final CacheCluster cc = in.getCacheCluster();
		final XMLNode nodeCacheCluster = MarshallingUtils.marshallCacheCluster(
				cc, true);
		nodeResults.addNode(nodeCacheCluster);

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());

		return nodeRoot.toString();
	}
}
