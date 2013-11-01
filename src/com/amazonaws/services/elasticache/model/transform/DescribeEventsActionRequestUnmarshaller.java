/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DescribeEventsAction from XML document to 
 * the DescribeEventsActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.elasticache.model.common.UnMarshallingUtils;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.util.DateUtils;
import com.msi.tough.query.ErrorResponse;
import com.transcend.elasticache.message.DescribeEventsActionMessage.DescribeEventsActionRequestMessage;

public class DescribeEventsActionRequestUnmarshaller implements
		Unmarshaller<DescribeEventsActionRequestMessage, Map<String, String[]>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());
	private static DescribeEventsActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DescribeEventsActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DescribeEventsActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DescribeEventsAction java bean.
	 * 
	 * SourceIdentifier String optional SourceType String optional StartTime
	 * DateTime optional EndTime DateTime optional Duration Integer optional
	 * Marker String optional MaxRecords Integer optional default = 100
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DescribeEventsAction java bean
	 */
	@Override
	public DescribeEventsActionRequestMessage unmarshall(final Map<String, String[]> mapIn)
			throws ErrorResponse {

		final DescribeEventsActionRequestMessage.Builder request = DescribeEventsActionRequestMessage.newBuilder();
		
		request.setSourceType(UnMarshallingUtils.unmarshallString(mapIn,
					"SourceIdentifier", false, "", logger));

		request.setStartTime(UnMarshallingUtils.unmarshallString(mapIn,
				"StartTime", false, "", logger));

		request.setEndTime(UnMarshallingUtils.unmarshallString(mapIn, "EndTime",
				false, "", logger));

		request.setDuration(UnMarshallingUtils.unmarshallInt(mapIn, "Duration",
				false, "-1", logger));

		request.setMarker(UnMarshallingUtils.unmarshallString(mapIn, "Marker",
				false, "", logger));

		request.setMaxRecords(UnMarshallingUtils.unmarshallInt(mapIn,
				"MaxRecords", false, "100", logger));

		return request.buildPartial();
	}
}
