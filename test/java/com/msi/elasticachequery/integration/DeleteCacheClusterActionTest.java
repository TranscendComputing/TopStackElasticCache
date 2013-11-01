package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.DeleteCacheClusterRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class DeleteCacheClusterActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DeleteCacheClusterActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    @Autowired
    CacheClusterHelper cacheClusterHelper;
    
    String name1 = "ec-del-1-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheClusterNoArgs() throws Exception {
        final DeleteCacheClusterRequest request = new DeleteCacheClusterRequest();
        getElastiCacheClient().deleteCacheCluster(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDeleteCacheClusterInvalidParameters() throws Exception {
        final DeleteCacheClusterRequest request = new DeleteCacheClusterRequest();
        request.withCacheClusterId("invalidName");
        getElastiCacheClient().deleteCacheCluster(request);
    }


    @Test
    public void testGoodDeleteCacheCluster() throws Exception {
    	logger.info("Creating cacheCluster: " + name1);
    	name1 = cacheClusterHelper.getOrCreateCacheCluster(name1);
    	logger.info("Waiting for CacheCluster" + name1);
    	boolean res = cacheClusterHelper.waitForCacheCluster(name1);
    	logger.info("Cluster started successfully: " + res);
    	
    	logger.info("Deleting CacheCluster");
    	cacheClusterHelper.delete(name1);
    }


}

