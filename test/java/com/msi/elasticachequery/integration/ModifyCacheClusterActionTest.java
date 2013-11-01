package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.amazonaws.services.elasticache.model.ModifyCacheClusterRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class ModifyCacheClusterActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(ModifyCacheClusterActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ec-mod-1-" + baseName;

    @Autowired
    CacheClusterHelper cacheClusterHelper;


    @Test (expected = AmazonServiceException.class)
    public void testModifyCacheClusterNoArgs() throws Exception {
        final ModifyCacheClusterRequest request = new ModifyCacheClusterRequest();
        getElastiCacheClient().modifyCacheCluster(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testModifyCacheClusterInvalidParameters() throws Exception {
        final ModifyCacheClusterRequest request = new ModifyCacheClusterRequest();
        request.withCacheClusterId("InvalidName");
        getElastiCacheClient().modifyCacheCluster(request);
    }


    @Test
    public void testGoodModifyCacheCluster() throws Exception {
    	logger.info("Creating cacheCluster: " + name1);
    	name1 = cacheClusterHelper.getOrCreateCacheCluster(name1);
    	logger.info("Waiting for CacheCluster" + name1);
    	boolean res = cacheClusterHelper.waitForCacheCluster(name1, 400);
    	logger.info("Cluster started successfully: " + res);
    	
        logger.info("Modifying CacheCluster " + name1);
        final ModifyCacheClusterRequest request = new ModifyCacheClusterRequest();
        request.withCacheClusterId(name1);
        request.withApplyImmediately(true);
        getElastiCacheClient().modifyCacheCluster(request);
        
    }


}

