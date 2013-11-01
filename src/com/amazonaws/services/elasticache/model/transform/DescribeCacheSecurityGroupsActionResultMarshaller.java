package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;

public class DescribeCacheSecurityGroupsActionResultMarshaller implements
		Marshaller<String, DescribeCacheSecurityGroupsActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DescribeCacheSecurityGroupsActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeCacheSecurityGroupsActionResult> <CacheSecurityGroups>
	 * <CacheSecurityGroup> details of security group </CacheSecurityGroup>
	 * </CacheSecurityGroups> </DescribeCacheSecurityGroupsActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeCacheSecurityGroupsActionResponse>
	 * 
	 * @param CacheSecurityGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(
			final DescribeCacheSecurityGroupsActionResultMessage in) {

		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DescribeCacheSecurityGroupsActionResponse");

		// add DescribeCacheSecurityGroupsActionResult
		final XMLNode nodeResults = new XMLNode(
				"DescribeCacheSecurityGroupsActionResult");
		nodeRoot.addNode(nodeResults);

		final List<CacheSecurityGroup> grpList = in.getCacheSecurityGroupsList();
		if (grpList != null) {

			final XMLNode nodeCacheSecurityGroups = new XMLNode(
					"CacheSecurityGroups");
			nodeResults.addNode(nodeCacheSecurityGroups);

			for (final CacheSecurityGroup sg : grpList) {

				final XMLNode nodeCacheSecurityGroup = MarshallingUtils
						.marshallCacheSecurityGroup(sg);
				nodeCacheSecurityGroups.addNode(nodeCacheSecurityGroup);
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
