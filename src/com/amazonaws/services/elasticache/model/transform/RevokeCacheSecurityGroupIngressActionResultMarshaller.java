package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.RevokeCacheSecurityGroupIngressActionMessage.RevokeCacheSecurityGroupIngressActionResultMessage;

public class RevokeCacheSecurityGroupIngressActionResultMarshaller implements
		Marshaller<String, RevokeCacheSecurityGroupIngressActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <RevokeCacheSecurityGroupIngressActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <RevokeCacheSecurityGroupIngressActionResult> <CacheSecurityGroup> details of
	 * security group </CacheSecurityGroup>
	 * </RevokeCacheSecurityGroupIngressActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </RevokeCacheSecurityGroupIngressActionResponse>
	 * 
	 * @param CacheSecurityGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final RevokeCacheSecurityGroupIngressActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("RevokeCacheSecurityGroupIngressActionResponse");

		// add RevokeCacheSecurityGroupIngressActionResult
		final XMLNode nodeResults = new XMLNode(
				"RevokeCacheSecurityGroupIngressActionResult");
		nodeRoot.addNode(nodeResults);

		final XMLNode nodeCacheSecurityGroup = MarshallingUtils
				.marshallCacheSecurityGroup(in.getCacheSecurityGroup());
		nodeResults.addNode(nodeCacheSecurityGroup);

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
