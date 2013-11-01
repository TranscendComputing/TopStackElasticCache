package com.msi.elasticachequery.integration;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.DescribeEventsRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DescribeEventsActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DescribeEventsActionTest.class.getName());


    @Test
    public void testDescribeEventsNoArgs() throws Exception {
        final DescribeEventsRequest request = new DescribeEventsRequest();
        getElastiCacheClient().describeEvents(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeEventsInvalidParameters() throws Exception {
        final DescribeEventsRequest request = new DescribeEventsRequest();
        request.withMaxRecords(1);
        getElastiCacheClient().describeEvents(request);
    }


    @Test
    public void testGoodDescribeEvents() throws Exception {
        logger.info("Describing Events");
        final DescribeEventsRequest request = new DescribeEventsRequest();
        request.withMaxRecords(20);
        getElastiCacheClient().describeEvents(request);
    }
}

