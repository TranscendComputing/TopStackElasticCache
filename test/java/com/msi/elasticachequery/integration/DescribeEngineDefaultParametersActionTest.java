package com.msi.elasticachequery.integration;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.DescribeEngineDefaultParametersRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DescribeEngineDefaultParametersActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DescribeEngineDefaultParametersActionTest.class.getName());


    @Test (expected = AmazonServiceException.class)
    public void testDescribeEngineDefaultParametersNoArgs() throws Exception {
        final DescribeEngineDefaultParametersRequest request = new DescribeEngineDefaultParametersRequest();
        getElastiCacheClient().describeEngineDefaultParameters(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeEngineDefaultParametersInvalidParameters() throws Exception {
        final DescribeEngineDefaultParametersRequest request = new DescribeEngineDefaultParametersRequest();
        request.withCacheParameterGroupFamily("InvalidGroupFamily");
        getElastiCacheClient().describeEngineDefaultParameters(request);
    }


    @Test
    public void testGoodDescribeEngineDefaultParameters() throws Exception {
        logger.info("Describing engine default parameters");
        final DescribeEngineDefaultParametersRequest request = new DescribeEngineDefaultParametersRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        getElastiCacheClient().describeEngineDefaultParameters(request);
    }


}

