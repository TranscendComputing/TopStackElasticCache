/**
 * Marshals DescribeCacheClusterAction java bean into XML for return
 * 
 * @author Stephen Dean
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.core.Appctx;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;

public class DescribeCacheClustersActionResultMarshaller implements
		Marshaller<String, DescribeCacheClustersActionResultMessage> {

	/**
	 * Marshall from the DescribeCacheClusterActionResponse java bean into a XML
	 * string document for return
	 * 
	 * <DescribeCacheClusterActionsResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeCacheClusterActionsResult> <CacheClusters> <CacheCluster> details of
	 * cache cluster </CacheCluster> </CacheClusters>
	 * </DescribeCacheClusterActionsResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeCacheClusterActionsResponse>
	 * 
	 * @param DescribeCacheClusterActionResult
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final DescribeCacheClustersActionResultMessage in) {

		Boolean showNode = (Boolean) Appctx
				.getThreadMap("ShowCacheNodeInfo");

		if(showNode == null)
			showNode = false;
		
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DescribeCacheClusterActionsResponse");

		// add DescribeCacheClusterActionsResult
		final XMLNode nodeResults = new XMLNode("DescribeCacheClusterActionsResult");
		nodeRoot.addNode(nodeResults);

		// add CacheClusters
		final List<CacheCluster> ccList = in.getCacheClustersList();
		if (ccList != null) {

			final XMLNode nodeCacheClusters = new XMLNode("CacheClusters");
			nodeResults.addNode(nodeCacheClusters);
			for (final CacheCluster cc : ccList) {
				final XMLNode nodeCacheCluster = MarshallingUtils
						.marshallCacheCluster(cc, showNode);
				nodeCacheClusters.addNode(nodeCacheCluster);
			}
		}

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}

}
