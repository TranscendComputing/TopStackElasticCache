package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionResultMessage;

public class DeleteCacheSecurityGroupActionResultMarshaller implements
		Marshaller<String, DeleteCacheSecurityGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DeleteCacheSecurityGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DeleteCacheSecurityGroupActionResponse>
	 * 
	 * Note: DeleteSecurityGroup returns a null response
	 * 
	 * @param CacheSecurityGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final DeleteCacheSecurityGroupActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DeleteCacheSecurityGroupActionResponse");

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
