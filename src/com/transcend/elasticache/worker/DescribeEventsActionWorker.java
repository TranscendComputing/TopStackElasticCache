package com.transcend.elasticache.worker;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.DateUtils;
import com.google.common.base.Strings;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.EventLogBean;
import com.msi.tough.model.EventLogTagBean;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EventUtil;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.DescribeEventsActionMessage.DescribeEventsActionRequestMessage;
import com.transcend.elasticache.message.DescribeEventsActionMessage.DescribeEventsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.Event;

public class DescribeEventsActionWorker extends 
		AbstractWorker<DescribeEventsActionRequestMessage, DescribeEventsActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeEventsActionWorker.class
					.getName());
	
    /**
    * We need a local copy of this doWork to provide the transactional
    * annotation.  Transaction management is handled by the annotation, which
    * can only be on a concrete class.
    * @param req
    * @return
    * @throws Exception
    */
   @Transactional
   public DescribeEventsActionResultMessage doWork(
           DescribeEventsActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeEventsAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeEventsActionResultMessage doWork0(DescribeEventsActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();

		logger.debug("DescribeEvents: " + request.toString());

		Integer duration = request.getDuration();
		Date startTime = null;
		Date endTime   = null;
		if(!"".equals(request.getStartTime()))
			startTime = new DateUtils().parseIso8601Date(request.getStartTime());
		if(!"".equals(request.getEndTime()))
			endTime = new DateUtils().parseIso8601Date(request.getEndTime());
		final String marker = request.getMarker();
		final Integer maxRecords = request.getMaxRecords();
		final String sourceId = request.getSourceIdentifier();
		String sourceType = request.getSourceType().toString();

		if (duration != -1 && (startTime != null || endTime != null)) {
			logger.debug("User passed Duration parameter with StartTime and/or EndTime parameters.");
			throw QueryFaults
					.InvalidParameterCombination("If Duration is specified, both StartTime and EndTime must be omitted.");
		}
		if (maxRecords < 20 || maxRecords > 100) {
			logger.debug("The passed value for MaxRecord parameter is out of range. The value must be in [20-100] range.");
			throw QueryFaults
					.InvalidParameterValue("MaxRecord has to be equal to or greater than 20 while being less or equal to 100.");
		}
		if ((sourceId != null && sourceType == null) || (!"".equals(sourceId) && sourceType == "")) {
			logger.debug("User passed SourceIdentifier parameter, but failed to supply SourceType parameter.");
			throw QueryFaults
					.MissingParameter("If SourceIdentifier is supplied, SourceType has to be provided.");
		}
		if (sourceType == null || "".equals(sourceType)) {
			sourceType = "cache-cluster";
		}
		if (!"".equals(sourceType) && sourceType != null && !sourceType.equals("cache-cluster")) {
			logger.debug("SourceType " + sourceType + " value is not valid.");
			throw QueryFaults
					.InvalidParameterValue("SourceType is specified with invalid parameter value.");
		}
		if (!"".equals(startTime) && !"".equals(endTime) && startTime != null && endTime != null
				&& startTime.getTime() > endTime.getTime()) {
			logger.debug("User StartTime > EndTime parameters.");
			throw QueryFaults
					.InvalidParameterCombination("StartTime cannot be > EndTime.");
		}

		// convert the Duration into StartTime if Duration is being used instead
		// of StartTime/EndTime
		if (startTime == null && endTime == null) {
			if (duration == -1) {
				duration = 60;
			}
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			final int amt = -duration;
			cal.add(Calendar.MINUTE, amt);
			startTime = cal.getTime();
			logger.debug("Duration is applied to startTime: "
					+ startTime.toString());
		}

		Collection<Event> events = null;
		final List<EventLogTagBean> eventTags = EventUtil.queryEventLogTags(
				session, account.getId(), sourceId, startTime, endTime, marker,
				maxRecords);
		logger.debug(eventTags.size() + " event log tags are returned.");
		events = new LinkedList<Event>();
		for (final EventLogTagBean temp : eventTags) {
			// logger.debug("EventLogTag: " + temp.getEventId() + "; " +
			// temp.getTag());
			final EventLogBean event = (EventLogBean) session.load(
					EventLogBean.class.getName(), temp.getEventId());
			if (event == null) {
				logger.debug("Failed to load EventLogBean with id="
						+ temp.getEventId());
			} else if ("".equals(sourceType) || sourceType == null || event.getType().equals(sourceType)) {
				Event.Builder newEvent = Event.newBuilder();
				newEvent.setDate(new DateUtils().formatIso8601Date(event.getCreatedTime()));
				newEvent.setMessage(event.getMessage());
				newEvent.setSourceIdentifier(temp.getTag());
				newEvent.setSourceType(Strings.nullToEmpty(event.getType()));				

				events.add(newEvent.buildPartial());
			}
		}

		final DescribeEventsActionResultMessage.Builder result = DescribeEventsActionResultMessage.newBuilder();
		result.addAllEvents(events);

		return result.buildPartial();
	}
}

