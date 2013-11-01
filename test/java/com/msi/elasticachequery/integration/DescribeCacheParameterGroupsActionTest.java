package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheParameterGroupsRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class DescribeCacheParameterGroupsActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DescribeCacheParameterGroupsActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;

    @Test
    public void testDescribeCacheParameterGroupsNoArgs() throws Exception {
        final DescribeCacheParameterGroupsRequest request = new DescribeCacheParameterGroupsRequest();
        getElastiCacheClient().describeCacheParameterGroups(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheParameterGroupsInvalidParameters() throws Exception {
        final DescribeCacheParameterGroupsRequest request = new DescribeCacheParameterGroupsRequest();
        request.withCacheParameterGroupName(name1);
        getElastiCacheClient().describeCacheParameterGroups(request);
    }


    @Test
    public void testGoodDescribeCacheParameterGroups() throws Exception {
        logger.info("Creating cache parameter group to be described: " + name1);
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        request.withCacheParameterGroupName(name1);
        request.withDescription("This is a test parameter group");
        getElastiCacheClient().createCacheParameterGroup(request);
        
        logger.info("Describing Cache parameter group: " + name1);
        final DescribeCacheParameterGroupsRequest descRequest = new DescribeCacheParameterGroupsRequest();
        descRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().describeCacheParameterGroups(descRequest);
        
        logger.info("Deleting cahce parameter group " + name1);
        final DeleteCacheParameterGroupRequest delRequest = new DeleteCacheParameterGroupRequest();
        delRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(delRequest);
        
    }


}

