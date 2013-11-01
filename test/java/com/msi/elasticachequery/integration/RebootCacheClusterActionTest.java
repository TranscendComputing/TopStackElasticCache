package com.msi.elasticachequery.integration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.ParameterNameValue;
import com.amazonaws.services.elasticache.model.RebootCacheClusterRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class RebootCacheClusterActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(RebootCacheClusterActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);
    @Autowired
    CacheClusterHelper cacheClusterHelper;
    
    String name1 = "ec-reb-" + baseName;
 
    @Test (expected = AmazonServiceException.class)
    public void testRebootCacheClusterNoArgs() throws Exception {
        final RebootCacheClusterRequest request = new RebootCacheClusterRequest();
        getElastiCacheClient().rebootCacheCluster(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testRebootCacheClusterInvalidParameters() throws Exception {
        final RebootCacheClusterRequest request = new RebootCacheClusterRequest();
        request.withCacheClusterId("invalidName");
        Collection<String> nodeIds = new LinkedList<String>();
        nodeIds.add(new String("0001"));
        request.withCacheNodeIdsToReboot(nodeIds);
        getElastiCacheClient().rebootCacheCluster(request);
    }


    @Test
    public void testGoodRebootCacheCluster() throws Exception {
    	logger.info("Creating cacheCluster: " + name1);
    	name1 = cacheClusterHelper.getOrCreateCacheCluster(name1);
    	logger.info("Waiting for CacheCluster" + name1);
    	boolean res = cacheClusterHelper.waitForCacheCluster(name1, 400);
    	logger.info("Cluster started successfully: " + res);
    	
        logger.info("Rebooting cache cluster");
        final RebootCacheClusterRequest request = new RebootCacheClusterRequest();
        request.withCacheClusterId(name1);
        Collection<String> nodeIds = new LinkedList<String>();
        nodeIds.add(cacheClusterHelper.getAClusterId(name1));
        request.withCacheNodeIdsToReboot(nodeIds);
        getElastiCacheClient().rebootCacheCluster(request);
    }


}

