package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroup;

public class DescribeCacheParameterGroupsActionResultMarshaller implements
		Marshaller<String, DescribeCacheParameterGroupsActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DescribeCacheParameterGroupsActionResponse
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeCacheParameterGroupsActionResult> <CacheParameterGroups>
	 * <CacheParameterGroup> details of Parameter group </CacheParameterGroup>
	 * </CacheParameterGroups> </DescribeCacheParameterGroupsActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeCacheParameterGroupsActionResponse>
	 * 
	 * @param CacheParameterGroup
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(
			final DescribeCacheParameterGroupsActionResultMessage in) {

		final XMLNode nodeRoot = EcacheUtil
				.getRootNode("DescribeCacheParameterGroupsActionResponse");

		// add DescribeCacheParameterGroupsActionResult
		final XMLNode nodeResults = new XMLNode(
				"DescribeCacheParameterGroupsActionResult");
		nodeRoot.addNode(nodeResults);

		final List<CacheParameterGroup> grpList = in.getCacheParameterGroupsList();
		if (grpList != null) {

			final XMLNode nodeCacheParameterGroups = new XMLNode(
					"CacheParameterGroups");
			nodeResults.addNode(nodeCacheParameterGroups);

			for (final CacheParameterGroup sg : grpList) {

				final XMLNode nodeCacheParameterGroup = MarshallingUtils
						.marshallCacheParameterGroup(sg);
				nodeCacheParameterGroups.addNode(nodeCacheParameterGroup);
			}
		}

		// TODO: Can't prove that this is where the marker element actually
		// belongs as it can't be tested on AWS. Account limit is 20, min
		// maxRecords is 20
		// (maxRecords must be 20..100)
		final String marker = in.getMarker();
		if (!marker.isEmpty()) {
			QueryUtil.addNode(nodeResults, Constants.MARKER, marker);
		}

		// add metadata
		final XMLNode nodeMetaData = new XMLNode("ResponseMetaData");
		nodeRoot.addNode(nodeMetaData);
		MarshallingUtils.marshallString(nodeMetaData, "RequestId",
				in.getRequestId());

		return nodeRoot.toString();
	}
}
