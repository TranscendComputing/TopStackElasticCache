package com.msi.elasticachequery.integration;

import java.util.UUID;


import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheSecurityGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheSecurityGroupRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheSecurityGroupsRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DescribeCacheSecurityGroupsActionTest extends AbstractBaseElastiCacheTest {
    
    private static Logger logger = Appctx.getLogger
    		(DescribeCacheSecurityGroupsActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheSecurityGroupsNoArgs() throws Exception {
        final DescribeCacheSecurityGroupsRequest request = new DescribeCacheSecurityGroupsRequest();
        getElastiCacheClient().describeCacheSecurityGroups(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheSecurityGroupsInvalidParameters() throws Exception {
        final DescribeCacheSecurityGroupsRequest request = new DescribeCacheSecurityGroupsRequest();
        request.withCacheSecurityGroupName("invalidName");
        getElastiCacheClient().describeCacheSecurityGroups(request);
    }


    @Test
    public void testGoodDescribeCacheSecurityGroups() throws Exception {
        logger.info("Creating Cache Security " + name1 + " to authorize");
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("This is a test Cache security group");
        request.withCacheSecurityGroupName(name1);
        getElastiCacheClient().createCacheSecurityGroup(request);

        logger.info("Describing ChacheSecGroup with name = " + name1);
        final DescribeCacheSecurityGroupsRequest descRequest = new DescribeCacheSecurityGroupsRequest();
        descRequest.withCacheSecurityGroupName(name1);
        descRequest.withMaxRecords(20);
        getElastiCacheClient().describeCacheSecurityGroups(descRequest);
        
        logger.info("Deleting CacheSecGroup " + name1);
        final DeleteCacheSecurityGroupRequest delRequest = new DeleteCacheSecurityGroupRequest();
        delRequest.withCacheSecurityGroupName(name1);
        getElastiCacheClient().deleteCacheSecurityGroup(delRequest);
    }


}

