package com.msi.elasticachequery.integration.helper;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.services.elasticache.AmazonElastiCacheClient;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.CreateCacheClusterRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheClusterRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersResult;
import com.msi.tough.core.Appctx;
import com.msi.tough.helper.AbstractHelper;
import com.msi.tough.workflow.WorkflowSubmitter;

@Component
@Scope("prototype")
public class CacheClusterHelper extends AbstractHelper<String> {
    private final static Logger logger = Appctx
            .getLogger(CacheClusterHelper.class.getName());

	@Resource(name = "elastiCacheClient")
    private AmazonElastiCacheClient elasticacheClient;

    
    private static final int MAX_WAIT_SECS = 300;
    private static final int WAIT_SECS = 15;

    /**
    *
    */
   public CacheClusterHelper() {
       super();
   }

   
    /**
     * Construct a minimal valid CacheCluster request.
     *
     * @param cacheName
     * @return
     */
    public CreateCacheClusterRequest createCacheClusterRequest(String cacheName) {
        final CreateCacheClusterRequest request = new CreateCacheClusterRequest();
        request.withCacheClusterId(cacheName);
        request.withCacheNodeType("cache.m1.small");
        request.withEngine("memcached");
        request.withNumCacheNodes(1);
    	Collection<String> cacheSecurityGroups = new ArrayList<String>();
    	cacheSecurityGroups.add("default");
        request.withCacheSecurityGroupNames(cacheSecurityGroups);
        return request;
    }

    /**
     * Create a cacheCluster.
     *
     * @param cacheName
     */
    public void createCacheCluster(String cacheName) {
    	CreateCacheClusterRequest request = createCacheClusterRequest(cacheName);
    	elasticacheClient.createCacheCluster(request);
    	addEntity(cacheName);
    }
    
    /**
     * Return name of available cacheCluster, if none, create a new one
     *
     * @throws InterruptedException
     */
    public String getOrCreateCacheCluster(String cacheName) throws InterruptedException {
    	Collection<String> existing = getExistingEntities();
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        DescribeCacheClustersResult cacheClRes;
    	//Checking if cluster already available
    	for(String nextClusterName : existing) {
    		try {
    			request.withCacheClusterId(nextClusterName);
    			cacheClRes = elasticacheClient.describeCacheClusters(request);
    			if(cacheClRes.getCacheClusters().get(0).getCacheClusterStatus().equals("available"))
            		return nextClusterName;
    		}
        	
        	catch(Exception e) {
        			logger.debug("Instance " + nextClusterName + " not found");
        	}
    	}
    	
    	//No instance found, create a new one
    	createCacheCluster(cacheName);
    	return cacheName;
    }
    
    /**
     * Wait for CacheCluster to be in available state
     *
     * @throws InterruptedException
     */
    public boolean waitForCacheCluster(String cacheName) throws InterruptedException {
    	return waitForCacheCluster(cacheName, MAX_WAIT_SECS);
    }
    /**
     * Wait for CacheCluster to be in available state
     *
     * @throws InterruptedException
     */
    public boolean waitForCacheCluster(String cacheName, int maxWait) throws InterruptedException {
    	DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
    	DescribeCacheClustersResult result;
    	CacheCluster cacheCl;
        request.withCacheClusterId(cacheName);
        for (int count = 0; count < maxWait; count += WAIT_SECS) {
            try {
            	
                result = elasticacheClient.describeCacheClusters(request);
                if (result.getCacheClusters().size() == 0)
                    	return false;
                else {
                	cacheCl = result.getCacheClusters().get(0);
                	if(cacheCl.getCacheClusterStatus().equals("available"))
                    	return true;
                }
            	logger.debug("CacheCluster status: " + cacheCl.getCacheClusterStatus() + " time passed: " + count + " seconds");
                
            } catch (Exception e) {
                return false;
            }
            Thread.sleep(1000 * WAIT_SECS);
        }
        logger.debug("Instance creation");
        return false;
    }
    
    /**
     * return a cacheCluster id (via describe).
     *
     * @param cacheName
     */
    public String getAClusterId(String cacheName) {
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        request.withCacheClusterId(cacheName);
        DescribeCacheClustersResult result =
        		elasticacheClient.describeCacheClusters(request);
        if (result.getCacheClusters().size() > 0) {
            return result.getCacheClusters().get(0).getCacheClusterId();
        }
        return null;
    }
    
    
    /**
     * Find a cacheCluster (via describe).
     *
     * @param cacheName
     */
    public CacheCluster findCacheCluster(String cacheName) {
        final DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
        request.withCacheClusterId(cacheName);
        DescribeCacheClustersResult result =
        		elasticacheClient.describeCacheClusters(request);
        if (result.getCacheClusters().size() > 0) {
            return result.getCacheClusters().get(0);
        }
        return null;
    }
    
    /**
     * Construct a delete cacheCluster request.
     *
     * @param userName
     * @return
     */
    private DeleteCacheClusterRequest deleteCacheClusterRequest(String name) {
        final DeleteCacheClusterRequest request = new DeleteCacheClusterRequest();
        request.withCacheClusterId(name);
        return request;
    }
    
     /**
      * Delete a created cluster
      */
    public void deleteSingleCluster(String name) throws Exception {
    	final DeleteCacheClusterRequest request = deleteCacheClusterRequest(name);
    	elasticacheClient.deleteCacheCluster(request);
        removeEntity(name);
    }

	@Override
	public String entityName() {
		return "CacheClusterHelper";
	}


	@Override
	public void create(String identifier) throws Exception {
		createCacheCluster(identifier);		
	}


	@Override
	public void delete(String identifier) throws Exception {
		deleteSingleCluster(identifier);
	}


	@Override
	public void setWorkflowSubmitter(WorkflowSubmitter submitter) {	
	}
}
