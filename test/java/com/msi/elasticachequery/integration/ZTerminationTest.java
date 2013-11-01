package com.msi.elasticachequery.integration;


import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.CacheClusterHelper;
import com.msi.tough.core.Appctx;

public class ZTerminationTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(ZTerminationTest.class.getName());

    @Autowired
    private CacheClusterHelper cacheClusterHelper;

    
    //Terminating running CacheClusters
    @Test
    public void testGoodTerminateClusters() throws Exception {
    	logger.debug("Terminating CacheClusters");
    	cacheClusterHelper.finalDestroy();
    }
    
}

