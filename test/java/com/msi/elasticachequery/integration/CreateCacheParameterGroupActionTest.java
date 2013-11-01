package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class CreateCacheParameterGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(CreateCacheParameterGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheParameterGroupNoArgs() throws Exception {
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        getElastiCacheClient().createCacheParameterGroup(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheParameterGroupInvalidParameters() throws Exception {
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached");
        request.withCacheParameterGroupName("someName");
        request.withDescription("Invalid parameter group family provided");
        getElastiCacheClient().createCacheParameterGroup(request);
    }


    @Test
    public void testGoodCreateCacheParameterGroup() throws Exception {
        logger.info("Creating cache parameter group with name = " + name1);
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        request.withCacheParameterGroupName(name1);
        request.withDescription("This is a test parameter group");
        getElastiCacheClient().createCacheParameterGroup(request);
        
        logger.info("Deleting cahce parameter group " + name1);
        final DeleteCacheParameterGroupRequest delRequest = new DeleteCacheParameterGroupRequest();
        delRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(delRequest);
    }


}

