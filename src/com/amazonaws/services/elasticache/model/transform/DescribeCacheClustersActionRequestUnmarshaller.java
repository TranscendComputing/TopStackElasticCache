/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeCacheClusterAction from XML document to 
 * the DescribeCacheClusterActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionRequestMessage;

public class DescribeCacheClustersActionRequestUnmarshaller implements
		Unmarshaller<DescribeCacheClustersActionRequestMessage, Map<String, String[]>> {

	private static DescribeCacheClustersActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeCacheClustersActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeCacheClustersActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeCacheClusterActions java bean.
	 * 
	 * CacheClisterID String optional Marker String optional MaxRecords Integer
	 * optional default = 100 ShowCacheNodeInfo Boolean optional
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeCacheClusterActions java bean
	 */
	@Override
	public DescribeCacheClustersActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DescribeCacheClustersActionRequestMessage.Builder request = DescribeCacheClustersActionRequestMessage.newBuilder();

		request.setCacheClusterId(QueryUtil.requiredString(mapIn, "CacheClusterId"));

		request.setMarker(Strings.nullToEmpty(QueryUtil.getString(mapIn, "Marker")));

		// 20..100, validated in DescribeCacheClusterActionsAction
		request.setMaxRecords(QueryUtil.getInt(mapIn, Constants.MAXRECORDS));

		request.setShowCacheNodeInfo(QueryUtil.getBoolean(mapIn,
				"ShowCacheNodeInfo", false));

		return request.buildPartial();
	}
}
