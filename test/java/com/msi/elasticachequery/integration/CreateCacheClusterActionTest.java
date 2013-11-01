package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheClusterRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class CreateCacheClusterActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(CreateCacheClusterActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ec-crcl-" + baseName;
    
    @Autowired
    CacheClusterHelper cacheClusterHelper;
    

    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheClusterNoArgs() throws Exception {
        final CreateCacheClusterRequest request = new CreateCacheClusterRequest();
        getElastiCacheClient().createCacheCluster(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheClusterInvalidParameters() throws Exception {
        final CreateCacheClusterRequest request = new CreateCacheClusterRequest();
        request.withCacheClusterId("");
        getElastiCacheClient().createCacheCluster(request);
    }


    @Test
    public void testGoodCreateCacheCluster() throws Exception {
        
    	logger.info("Creating cacheCluster: " + name1);
    	cacheClusterHelper.createCacheCluster(name1);
    	logger.info("Waiting for CacheCluster" + name1);
    	boolean res = cacheClusterHelper.waitForCacheCluster(name1);
    	logger.info("Cluster started successfully: " + res);
    	
    	logger.info("Deleting CacheCluster");
    	cacheClusterHelper.delete(name1);

    }
}

