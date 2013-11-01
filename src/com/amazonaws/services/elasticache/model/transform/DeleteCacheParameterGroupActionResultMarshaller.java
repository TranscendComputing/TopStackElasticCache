package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DeleteCacheParameterGroupActionMessage.DeleteCacheParameterGroupActionResultMessage;

public class DeleteCacheParameterGroupActionResultMarshaller implements
		Marshaller<String, DeleteCacheParameterGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DeleteCacheParameterGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DeleteCacheParameterGroupActionResponse>
	 * 
	 * @param CacheParameterGroup
	 *            java bean
	 * @return XML String document
	 * 
	 *         Note: The response document is an empty String
	 */
	@Override
	public String marshall(final DeleteCacheParameterGroupActionResultMessage in) {
		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DeleteCacheParameterGroupActionResponse");

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());

		return nodeRoot.toString();
	}
}
