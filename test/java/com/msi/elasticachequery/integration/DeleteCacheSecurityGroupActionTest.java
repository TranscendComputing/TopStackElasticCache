package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheSecurityGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheSecurityGroupRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DeleteCacheSecurityGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DeleteCacheSecurityGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;


    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheSecurityGroupNoArgs() throws Exception {
        final DeleteCacheSecurityGroupRequest request = new DeleteCacheSecurityGroupRequest();
        getElastiCacheClient().deleteCacheSecurityGroup(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheSecurityGroupInvalidParameters() throws Exception {
        final DeleteCacheSecurityGroupRequest request = new DeleteCacheSecurityGroupRequest();
        request.withCacheSecurityGroupName("invalidName");
        getElastiCacheClient().deleteCacheSecurityGroup(request);
    }
    
    public void createCacheSecGroup(String name) {
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("This is a test Cache security group");
        request.withCacheSecurityGroupName(name);
        getElastiCacheClient().createCacheSecurityGroup(request);
    }


    @Test
    public void testGoodDeleteCacheSecurityGroup() throws Exception {
        logger.info("Creating CacheSecurityGroup to be deleted: " + name1);
        createCacheSecGroup(name1);
        logger.info(name1 + " created");
        final DeleteCacheSecurityGroupRequest request = new DeleteCacheSecurityGroupRequest();
        request.withCacheSecurityGroupName(name1);
        getElastiCacheClient().deleteCacheSecurityGroup(request);
    }


}

