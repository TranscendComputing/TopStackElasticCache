package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionResultMessage;

public class CreateCacheSecurityGroupActionResultMarshaller implements
		Marshaller<String, CreateCacheSecurityGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <CreateCacheSecurityGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <CreateCacheSecurityGroupActionResult> <CacheSecurityGroup> details of cache
	 * cluster </CacheSecurityGroup> </CreateCacheSecurityGroupActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </CreateCacheSecurityGroupActionResponse>
	 * 
	 * @param CacheSecurityGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final CreateCacheSecurityGroupActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("CreateCacheSecurityGroupActionResponse");

		// add CreateCacheSecurityGroupActionResult
		final XMLNode nodeResults = new XMLNode(
				"CreateCacheSecurityGroupActionResult");
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
