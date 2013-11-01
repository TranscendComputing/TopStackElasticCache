package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheParametersRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DescribeCacheParametersActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DescribeCacheParametersActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;


    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheParametersNoArgs() throws Exception {
        final DescribeCacheParametersRequest request = new DescribeCacheParametersRequest();
        getElastiCacheClient().describeCacheParameters(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheParametersInvalidParameters() throws Exception {
        final DescribeCacheParametersRequest request = new DescribeCacheParametersRequest();
        request.withCacheParameterGroupName(name1);
        request.withSource("user");
        getElastiCacheClient().describeCacheParameters(request);
    }


    @Test
    public void testGoodDescribeCacheParameters() throws Exception {
        logger.info("Creating cache parameter group: " + name1);
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        request.withCacheParameterGroupName(name1);
        request.withDescription("This is a test parameter group");
        getElastiCacheClient().createCacheParameterGroup(request);
        
        logger.info("Describing Cache parameters for group: " + name1);
        final DescribeCacheParametersRequest descRequest = new DescribeCacheParametersRequest();
        descRequest.withCacheParameterGroupName(name1);
        descRequest.withSource("user");
        getElastiCacheClient().describeCacheParameters(descRequest);
        
        logger.info("Deleting cahce parameter group " + name1);
        final DeleteCacheParameterGroupRequest delRequest = new DeleteCacheParameterGroupRequest();
        delRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(delRequest);

    }


}

