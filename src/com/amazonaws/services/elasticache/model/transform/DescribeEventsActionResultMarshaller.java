package com.amazonaws.services.elasticache.model.transform;

import java.util.List;

import com.amazonaws.services.elasticache.model.common.MarshallingUtils;
import com.amazonaws.transform.Marshaller;
import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.EcacheUtil;
import com.transcend.elasticache.message.DescribeEventsActionMessage.DescribeEventsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.Event;

public class DescribeEventsActionResultMarshaller implements
		Marshaller<String, DescribeEventsActionResultMessage> {

	/**
	 * Marshall from the CacheCluster response java bean into a XML string
	 * document for return
	 * 
	 * <DescribeEventsActionResult
	 * xmlns="http://elasticache.amazonaws.com/doc/2011-07-15/">
	 * 
	 * <DescribeEventsActionResult> <Events> <Event> details of Event </Event>
	 * </Events> </DescribeEventsActionResult>
	 * 
	 * <ResponseMetadata>
	 * <RequestId>f270d58f-b7fb-11e0-9326-b7275b9d4a6c</RequestId>
	 * </ResponseMetadata> </DescribeEventsActionResult>
	 * 
	 * @param DescribeEventsActionResult
	 *            java bean
	 * @return XML String document
	 */
	@Override
	public String marshall(final DescribeEventsActionResultMessage in) {

		final XMLNode nodeRoot = EcacheUtil.getRootNode("DescribeEventsActionResult");
		QueryUtil.addNode(nodeRoot, "Marker", in.getMarker());
		final XMLNode evs = QueryUtil.addNode(nodeRoot, "Events");

		final List<Event> eventsList = in.getEventsList();
		if (eventsList != null) {
			for (final Event evt : eventsList) {
				final XMLNode ev = QueryUtil.addNode(evs, "Event");
				QueryUtil.addNode(ev, "SourceType", evt.getSourceType());
				QueryUtil.addNode(ev, "SourceIdentifier",
						evt.getSourceIdentifier());
				QueryUtil.addNode(ev, "Message", evt.getMessage());
				QueryUtil.addNode(ev, "Date", evt.getDate());
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
