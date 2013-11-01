package com.msi.elasticachequery.integration;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.elasticache.model.AuthorizeCacheSecurityGroupIngressRequest;
import com.amazonaws.services.elasticache.model.CreateCacheSecurityGroupRequest;
import com.amazonaws.services.elasticache.model.DeleteCacheSecurityGroupRequest;
import com.amazonaws.services.elasticache.model.RevokeCacheSecurityGroupIngressRequest;
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.SecurityGroupsHelper;
import com.msi.tough.core.Appctx;

public class RevokeCacheSecurityGroupIngressActionTest extends AbstractBaseElastiCacheTest {
    @Autowired
    private SecurityGroupsHelper secGrpHelper;
    
    private static Logger logger = Appctx.getLogger
    		(RevokeCacheSecurityGroupIngressActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testRevokeCacheSecurityGroupIngressNoArgs() throws Exception {
        final RevokeCacheSecurityGroupIngressRequest request = new RevokeCacheSecurityGroupIngressRequest();
        getElastiCacheClient().revokeCacheSecurityGroupIngress(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testRevokeCacheSecurityGroupIngressInvalidParameters() throws Exception {
        final RevokeCacheSecurityGroupIngressRequest request = new RevokeCacheSecurityGroupIngressRequest();
        request.withCacheSecurityGroupName("invalidName");
        getElastiCacheClient().revokeCacheSecurityGroupIngress(request);
    }


    @Test
    public void testGoodRevokeCacheSecurityGroupIngress() throws Exception {
        
        SecurityGroup myGroup = secGrpHelper.getEC2SecGroup();
        createECacheGroup(name1, myGroup);

         logger.info("Revoking DBSecGroup " + name1);
         final RevokeCacheSecurityGroupIngressRequest revRequest = new RevokeCacheSecurityGroupIngressRequest();
         revRequest.withCacheSecurityGroupName(name1);
         revRequest.withEC2SecurityGroupName(myGroup.getGroupName());
         revRequest.withEC2SecurityGroupOwnerId(myGroup.getOwnerId());
         getElastiCacheClient().revokeCacheSecurityGroupIngress(revRequest);
         deleteEcacheGroup(name1);
         
         
    }
    
    private void createECacheGroup(String name, SecurityGroup myGroup) {
        logger.info("Creating Cache Security " + name1 + " to authorize");
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("This is a test Cache security group");
        request.withCacheSecurityGroupName(name1);
        getElastiCacheClient().createCacheSecurityGroup(request);
        
        logger.info("Authorizing ChacheSecGroup with name = " + name1);
        final AuthorizeCacheSecurityGroupIngressRequest authRequest = new AuthorizeCacheSecurityGroupIngressRequest();
        authRequest.withCacheSecurityGroupName(name1);
        authRequest.withEC2SecurityGroupName(myGroup.getGroupName());
        authRequest.withEC2SecurityGroupOwnerId(myGroup.getOwnerId());
        getElastiCacheClient().authorizeCacheSecurityGroupIngress(authRequest);
    }
    
    private void deleteEcacheGroup(String name) {
        logger.info("Deleting CacheSecGroup " + name1);
        final DeleteCacheSecurityGroupRequest delRequest = new DeleteCacheSecurityGroupRequest();
        delRequest.withCacheSecurityGroupName(name1);
        getElastiCacheClient().deleteCacheSecurityGroup(delRequest);
    }


}

