package com.amazonaws.services.elasticache.model.transform;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.ModifyCacheParameterGroupActionMessage.ModifyCacheParameterGroupActionResultMessage;

public class ModifyCacheParameterGroupActionResultMarshaller implements
		Marshaller<String, ModifyCacheParameterGroupActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <ModifyCacheParameterGroupActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <ModifyCacheParameterGroupActionResult> <CacheParameterGroupName>
	 * </ModifyCacheParameterGroupActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </ModifyCacheParameterGroupActionResponse>
	 * 
	 * @param ModifyCacheParameterGroupActionResult
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(
			final ModifyCacheParameterGroupActionResultMessage in) {

		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("ModifyCacheParameterGroupActionResponse");

		// add ModifyCacheParameterGroupActionResult
		final XMLNode nodeResults = QueryUtil.addNode(nodeRoot,
				"ModifyCacheParameterGroupActionResult");

		QueryUtil.addNode(nodeResults, Constants.CACHEPARAMETERGROUPNAME,
				in.getCacheParameterGroupName());

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());
		return nodeRoot.toString();
	}
}
