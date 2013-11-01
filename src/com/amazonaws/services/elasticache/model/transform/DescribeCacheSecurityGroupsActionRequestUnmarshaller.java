/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeCacheSecurityGroupsAction from XML document to 
 * the DescribeCacheSecurityGroupsActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionRequestMessage;

public class DescribeCacheSecurityGroupsActionRequestUnmarshaller implements
		Unmarshaller<DescribeCacheSecurityGroupsActionRequestMessage, Map<String, String[]>> {

	private static DescribeCacheSecurityGroupsActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeCacheSecurityGroupsActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeCacheSecurityGroupsActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeCacheSecurityGroupsAction java bean.
	 * 
	 * CacheSecurityGroupName String optional Marker String optional MaxRecords
	 * Int optional default = 100
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeCacheSecurityGroupsAction java bean
	 */
	@Override
	public DescribeCacheSecurityGroupsActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DescribeCacheSecurityGroupsActionRequestMessage.Builder request = DescribeCacheSecurityGroupsActionRequestMessage.newBuilder();

		request.setCacheSecurityGroupName(QueryUtil.getString(mapIn,
				Constants.CACHESECURITYGROUPNAME));
		request.setMarker(Strings.nullToEmpty(QueryUtil.getString(mapIn, Constants.MARKER)));
		request.setMaxRecords(QueryUtil
				.getInt(mapIn, Constants.MAXRECORDS, 100));

		return request.buildPartial();
	}
}
