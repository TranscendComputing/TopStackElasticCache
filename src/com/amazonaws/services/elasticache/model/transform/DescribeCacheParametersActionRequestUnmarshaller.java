/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeCacheParametersAction from XML document to 
 * the DescribeCacheParametersActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionRequestMessage;

public class DescribeCacheParametersActionRequestUnmarshaller implements
		Unmarshaller<DescribeCacheParametersActionRequestMessage, Map<String, String[]>> {

	private static DescribeCacheParametersActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeCacheParametersActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeCacheParametersActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeCacheParametersActions java bean.
	 * 
	 * CacheParameterGroupName String required Marker String optional MaxRecords
	 * Integer optional default = 100 Source String optional
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeCacheParametersActions java bean
	 */
	@Override
	public DescribeCacheParametersActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DescribeCacheParametersActionRequestMessage.Builder request = DescribeCacheParametersActionRequestMessage.newBuilder();

		request.setCacheParameterGroupName(QueryUtil.requiredString(mapIn,
				Constants.CACHEPARAMETERGROUPNAME));

		request.setMarker(Strings.nullToEmpty(QueryUtil.getString(mapIn, Constants.MARKER)));

		request.setMaxRecords(QueryUtil.getInt(mapIn, Constants.MAXCACHEMEMORY,
				100));

		if(QueryUtil.getString(mapIn, Constants.SOURCE) != null) {
			request.setSource(QueryUtil.getString(mapIn, Constants.SOURCE));
		}
		return request.buildPartial();
	}
}
