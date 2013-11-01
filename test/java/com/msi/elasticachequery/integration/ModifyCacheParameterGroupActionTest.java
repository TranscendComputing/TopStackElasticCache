package com.msi.elasticachequery.integration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.ModifyCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.ParameterNameValue;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class ModifyCacheParameterGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(ModifyCacheParameterGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-mod-1-" + baseName;


    @Test (expected = AmazonServiceException.class)
    public void testModifyCacheParameterGroupNoArgs() throws Exception {
        final ModifyCacheParameterGroupRequest request = new ModifyCacheParameterGroupRequest();
        getElastiCacheClient().modifyCacheParameterGroup(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testModifyCacheParameterGroupInvalidParameters() throws Exception {
        final ModifyCacheParameterGroupRequest request = new ModifyCacheParameterGroupRequest();
        request.withCacheParameterGroupName(name1);
        getElastiCacheClient().modifyCacheParameterGroup(request);
    }


    @Test
    public void testGoodModifyCacheParameterGroup() throws Exception {
        logger.info("Creating cache parameter group: " + name1);
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        request.withCacheParameterGroupName(name1);
        request.withDescription("This is a test parameter group");
        getElastiCacheClient().createCacheParameterGroup(request);
        
        logger.info("Modifying Cache parameters for group: " + name1);
        final ModifyCacheParameterGroupRequest modRequest = new ModifyCacheParameterGroupRequest();
        ParameterNameValue param = new ParameterNameValue();
        param.setParameterValue("description");
        param.setParameterName("newDescription");
        Collection<ParameterNameValue>  parList = new LinkedList<ParameterNameValue>();
        parList.add(param);
        modRequest.withCacheParameterGroupName(name1);
        modRequest.withParameterNameValues(parList);
        getElastiCacheClient().modifyCacheParameterGroup(modRequest);
        
        logger.info("Deleting cahce parameter group " + name1);
        final DeleteCacheParameterGroupRequest delRequest = new DeleteCacheParameterGroupRequest();
        delRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(delRequest);     
    }
}

