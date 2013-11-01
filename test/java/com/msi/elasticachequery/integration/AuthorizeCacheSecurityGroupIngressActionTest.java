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
import com.msi.elasticachequery.integration.AbstractBaseElastiCacheTest;
import com.msi.elasticachequery.integration.helper.SecurityGroupsHelper;
import com.msi.tough.core.Appctx;

public class AuthorizeCacheSecurityGroupIngressActionTest extends AbstractBaseElastiCacheTest {

    @Autowired
    private SecurityGroupsHelper secGrpHelper;
    
    private static Logger logger = Appctx.getLogger
    		(AuthorizeCacheSecurityGroupIngressActionTest.class.getName());

    private final String baseName = UUID.randomUUID().toString()
            .substring(0, 8);

    String name1 = "ecache-autho-1-" + baseName;

    @Test (expected = AmazonServiceException.class)
    public void testAuthorizeCacheSecurityGroupIngressNoArgs() throws Exception {
        final AuthorizeCacheSecurityGroupIngressRequest request = new AuthorizeCacheSecurityGroupIngressRequest();
        getElastiCacheClient().authorizeCacheSecurityGroupIngress(request);
    }



    @Test (expected = AmazonServiceException.class)
    public void testAuthorizeCacheSecurityGroupIngressInvalidParameters() throws Exception {
    	SecurityGroup mygroup = secGrpHelper.getEC2SecGroup();
    	
        final AuthorizeCacheSecurityGroupIngressRequest request = new AuthorizeCacheSecurityGroupIngressRequest();
        request.withCacheSecurityGroupName("invalidName");
        request.withEC2SecurityGroupName(mygroup.getGroupName());
        request.withEC2SecurityGroupOwnerId(mygroup.getGroupId());
        getElastiCacheClient().authorizeCacheSecurityGroupIngress(request);
    }


    @Test
    public void testGoodAuthorizeCacheSecurityGroupIngress() throws Exception {
        logger.info("Creating Cache Security " + name1 + " to authorize");
        final CreateCacheSecurityGroupRequest request = new CreateCacheSecurityGroupRequest();
        request.withDescription("This is a test Cache security group");
        request.withCacheSecurityGroupName(name1);
        getElastiCacheClient().createCacheSecurityGroup(request);
        
        SecurityGroup mygroup = secGrpHelper.getEC2SecGroup();

        logger.info("Authorizing ChacheSecGroup with name = " + name1);
        final AuthorizeCacheSecurityGroupIngressRequest authRequest = new AuthorizeCacheSecurityGroupIngressRequest();
        authRequest.withCacheSecurityGroupName(name1);
        authRequest.withEC2SecurityGroupName(mygroup.getGroupName());
        authRequest.withEC2SecurityGroupOwnerId(mygroup.getOwnerId());
        getElastiCacheClient().authorizeCacheSecurityGroupIngress(authRequest);
        
        logger.info("Deleting CacheSecGroup " + name1);
        //final DeleteCacheSecurityGroupRequest delRequest = new DeleteCacheSecurityGroupRequest();
        //delRequest.withCacheSecurityGroupName(name1);
        //getElastiCacheClient().deleteCacheSecurityGroup(delRequest);
    }


}

