/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeCacheParameterGroupsAction from XML document to 
 * the DescribeCacheParameterGroupsActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionRequestMessage;

public class DescribeCacheParameterGroupsActionRequestUnmarshaller
		implements
		Unmarshaller<DescribeCacheParameterGroupsActionRequestMessage, Map<String, String[]>> {

	private static DescribeCacheParameterGroupsActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeCacheParameterGroupsActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeCacheParameterGroupsActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeCacheParameterGroupsActions java bean.
	 * 
	 * CacheParameterGroupName String optional Marker String optional MaxRecords
	 * Integer optional default = 100
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeCacheParameterGroupsActions java bean
	 */
	@Override
	public DescribeCacheParameterGroupsActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DescribeCacheParameterGroupsActionRequestMessage.Builder request = DescribeCacheParameterGroupsActionRequestMessage.newBuilder();
		request.setCacheParameterGroupName(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				Constants.CACHEPARAMETERGROUPNAME)));

		request.setMarker(Strings.nullToEmpty(QueryUtil.getString(mapIn, Constants.MARKER)));

		request.setMaxRecords(QueryUtil.getInt(mapIn, Constants.MAXRECORDS));

		return request.buildPartial();
	}
}
