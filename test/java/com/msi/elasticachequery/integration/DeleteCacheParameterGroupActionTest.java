package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DeleteCacheParameterGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DeleteCacheParameterGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;


    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheParameterGroupNoArgs() throws Exception {
        final DeleteCacheParameterGroupRequest request = new DeleteCacheParameterGroupRequest();
        getElastiCacheClient().deleteCacheParameterGroup(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheParameterGroupInvalidParameters() throws Exception {
        final DeleteCacheParameterGroupRequest request = new DeleteCacheParameterGroupRequest();
        request.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(request);
    }


    @Test
    public void testGoodDeleteCacheParameterGroup() throws Exception {
        logger.info("Creating cache parameter group to be deleted: " + name1);
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

