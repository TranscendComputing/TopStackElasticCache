package com.msi.elasticachequery.integration;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.elasticache.AmazonElastiCacheClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-elasticacheContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public abstract class AbstractBaseElastiCacheTest {

	@Resource(name = "basicAWSCredentials")
	private AWSCredentials creds;

	@Resource(name = "elastiCacheClient")
	private AmazonElastiCacheClient elastiCacheClient;
    
	@Autowired
	private String defaultAvailabilityZone;

	public AWSCredentials getCreds() {
		return creds;
	}

	public void setCreds(AWSCredentials creds) {
		this.creds = creds;
	}

	public AmazonElastiCacheClient getElastiCacheClient() {
		return elastiCacheClient;
	}

	public void setElastiCacheClient(AmazonElastiCacheClient elasticache) {
		this.elastiCacheClient = elasticache;
	}

	public String getDefaultAvailabilityZone() {
		return defaultAvailabilityZone;
	}

	public void setDefaultAvailabilityZone(String defaultAvailabilityZone) {
		this.defaultAvailabilityZone = defaultAvailabilityZone;
	}

}
