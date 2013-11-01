package com.msi.elasticachequery.integration;

import java.util.LinkedList;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheParameterGroupRequest;
import com.amazonaws.services.elasticache.model.ParameterNameValue;
import com.amazonaws.services.elasticache.model.ResetCacheParameterGroupRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class ResetCacheParameterGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(ResetCacheParameterGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;


    @Test (expected = AmazonServiceException.class)
    public void testResetCacheParameterGroupNoArgs() throws Exception {
        final ResetCacheParameterGroupRequest request = new ResetCacheParameterGroupRequest();
        getElastiCacheClient().resetCacheParameterGroup(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testResetCacheParameterGroupInvalidParameters() throws Exception {
        final ResetCacheParameterGroupRequest request = new ResetCacheParameterGroupRequest();
        request.withCacheParameterGroupName(name1);
        getElastiCacheClient().resetCacheParameterGroup(request);
    }


    @Test
    public void testGoodResetCacheParameterGroup() throws Exception {
        logger.info("Creating cache parameter group: " + name1);
        final CreateCacheParameterGroupRequest request = new CreateCacheParameterGroupRequest();
        request.withCacheParameterGroupFamily("memcached1.4");
        request.withCacheParameterGroupName(name1);
        request.withDescription("This is a test parameter group");
        getElastiCacheClient().createCacheParameterGroup(request);
        
        logger.info("Resetting Cache parameters for group: " + name1);
        final ResetCacheParameterGroupRequest modRequest = new ResetCacheParameterGroupRequest();
        ParameterNameValue param = new ParameterNameValue();
        LinkedList<ParameterNameValue> parList = new LinkedList<ParameterNameValue>();
        parList.add(param);
        modRequest.withCacheParameterGroupName(name1);
        modRequest.withParameterNameValues(parList);
        modRequest.withResetAllParameters(true);
        getElastiCacheClient().resetCacheParameterGroup(modRequest);
        
        logger.info("Deleting cahce parameter group " + name1);
        final DeleteCacheParameterGroupRequest delRequest = new DeleteCacheParameterGroupRequest();
        delRequest.withCacheParameterGroupName(name1);
        getElastiCacheClient().deleteCacheParameterGroup(delRequest); 
        
    }


}

