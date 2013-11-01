/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeEngineDefaultParametersAction from XML document to 
 * the DescribeEngineDefaultParametersActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DescribeEngineDefaultParametersActionMessage.DescribeEngineDefaultParametersActionRequestMessage;

public class DescribeEngineDefaultParametersActionRequestUnmarshaller
		implements
		Unmarshaller<DescribeEngineDefaultParametersActionRequestMessage, Map<String, String[]>> {

	private static DescribeEngineDefaultParametersActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeEngineDefaultParametersActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeEngineDefaultParametersActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeEngineDefaultParametersAction java bean.
	 * 
	 * CacheParameterGroupFamily String required Marker String optional
	 * MaxRecords Integer optional default = 100
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeEngineDefaultParametersAction java bean
	 */
	@Override
	public DescribeEngineDefaultParametersActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DescribeEngineDefaultParametersActionRequestMessage.Builder request = DescribeEngineDefaultParametersActionRequestMessage.newBuilder();

		request.setCacheParameterGroupFamily(QueryUtil.requiredString(mapIn,
				Constants.CACHEPARAMETERGROUPFAMILY));

		request.setMarker(Strings.nullToEmpty(QueryUtil.getString(mapIn, Constants.MARKER)));

		request.setMaxRecords(QueryUtil
				.getInt(mapIn, Constants.MAXRECORDS, 100));

		return request.buildPartial();
	}
}
