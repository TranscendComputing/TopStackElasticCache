package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticache.model.CreateCacheSecurityGroupRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.tough.core.Appctx;

public class CreateCacheSecurityGroupActionTest extends AbstractBaseElastiCacheTest {

    private static Logger logger = Appctx.getLogger
    		(CreateCacheSecurityGroupActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecq-autho-1-" + baseName;

    //SecurityGroupName, description are required
    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheSecurityGroupNoArgs() throws Exception {
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        getElastiCacheClient().createCacheSecurityGroup(request);
    }
    

    //Security group name must not be set to Default
    @Test (expected = AmazonServiceException.class)
    public void testCreateCacheSecurityGroupInvalidParameters() throws Exception {
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("Invalid security group");
        request.withCacheSecurityGroupName("Default");
        getElastiCacheClient().createCacheSecurityGroup(request);
    }


   @Test
    public void testGoodCreateCacheSecurityGroup() throws Exception {
        logger.info("Creating Cache Security Group " + name1);
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("This is a test Cache security group");
        request.withCacheSecurityGroupName(name1);
        getElastiCacheClient().createCacheSecurityGroup(request);
    }


}

