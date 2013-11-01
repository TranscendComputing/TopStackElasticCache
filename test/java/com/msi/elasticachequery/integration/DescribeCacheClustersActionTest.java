package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class DescribeCacheClustersActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(DescribeCacheClustersActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    @Autowired
    CacheClusterHelper cacheClusterHelper;
    
    String name1 = "ec-desc-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheClustersNoArgs() throws Exception {
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        getElastiCacheClient().describeCacheClusters(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testDescribeCacheClustersInvalidParameters() throws Exception {
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        request.withCacheClusterId("InvalidName");
        getElastiCacheClient().describeCacheClusters(request);
    }


    @Test
    public void testGoodDescribeCacheClusters() throws Exception {
    	logger.info("Creating cacheCluster: " + name1);
    	name1 = cacheClusterHelper.getOrCreateCacheCluster(name1);
    	logger.info("Waiting for CacheCluster" + name1);
    	boolean res = cacheClusterHelper.waitForCacheCluster(name1, 400);
    	logger.info("Cluster started successfully: " + res);
    	
        logger.info("Describing CacheCluster " + name1);
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        request.withCacheClusterId(name1);
        request.withShowCacheNodeInfo(true);
        getElastiCacheClient().describeCacheClusters(request);
    }


}

