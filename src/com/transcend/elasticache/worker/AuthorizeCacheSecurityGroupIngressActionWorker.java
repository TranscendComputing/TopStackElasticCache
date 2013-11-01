package com.transcend.elasticache.worker;

import java.util.ArrayList;
import java.util.Collection;

import org.dasein.cloud.CloudProvider;
import org.dasein.cloud.network.Firewall;
import org.dasein.cloud.network.FirewallSupport;
import org.dasein.cloud.network.NetworkServices;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msi.tough.cf.ec2.AuthorizeSecurityGroupIngressType;
import com.msi.tough.cf.ec2.SecurityGroupType;
import com.msi.tough.core.Appctx;
import com.msi.tough.dasein.DaseinHelper;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheSecurityGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.utils.SecurityGroupUtils;
import com.msi.tough.workflow.core.AbstractWorker;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionRequestMessage;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.EC2SecurityGroup;

public class AuthorizeCacheSecurityGroupIngressActionWorker extends 
		AbstractWorker<AuthorizeCacheSecurityGroupIngressActionRequestMessage, AuthorizeCacheSecurityGroupIngressActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(AuthorizeCacheSecurityGroupIngressActionWorker.class
					.getName());
	
    /**
    * We need a local copy of this doWork to provide the transactional
    * annotation.  Transaction management is handled by the annotation, which
    * can only be on a concrete class.
    * @param req
    * @return
    * @throws Exception
    */
   @Transactional
   public AuthorizeCacheSecurityGroupIngressActionResultMessage doWork(
           AuthorizeCacheSecurityGroupIngressActionRequestMessage req) throws Exception {
       logger.debug("Performing work for AuthorizeCacheSecurityGroupIngressAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected AuthorizeCacheSecurityGroupIngressActionResultMessage doWork0(AuthorizeCacheSecurityGroupIngressActionRequestMessage request,
			ServiceRequestContext context) throws Exception {
		logger.debug("into process0");

		// call the service
		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		final String secGrpName = request.getCacheSecurityGroupName();
		final String ec2SecGrpName = request.getEc2SecurityGroupName();
		final String ec2SecGrpOwnerId = request.getEc2SecurityGroupOwnerId();
		logger.debug("SecurityGroupName = " + secGrpName);

		final CacheSecurityGroupBean secGrp = EcacheUtil
				.getCacheSecurityGroupBean(session, account.getId(), secGrpName);
		if (secGrp == null) {
			throw ElasticacheFaults.CacheSecurityGroupNotFound();
		}

		logger.debug("AuthorizeCacheSecurityGroupIngress");
		logger.debug("SecurityGroupName = " + secGrpName);
		logger.debug("Ec2SecurityGroupName = " + ec2SecGrpName);
		logger.debug("Ec2SecurityGroupOwnerId = " + ec2SecGrpOwnerId);

		final CloudProvider cloudProvider = DaseinHelper.getProvider(
				account.getDefZone(), account.getTenant(),
				account.getAccessKey(), account.getSecretKey());
		final NetworkServices network = cloudProvider.getNetworkServices();
		final FirewallSupport fs = network.getFirewallSupport();
		String groupId = "";

		for (final Firewall f : fs.list()) {
			if (f.getName().equals(ec2SecGrpName)) {
				groupId = f.getProviderFirewallId();
				break;
			}
		}

		SecurityGroupUtils.authorizeSecurityGroupIngress(
				AccountUtil.toAccount(account), secGrp.getProviderId(),
				groupId, secGrp.getStackId(), ec2SecGrpOwnerId, 11211, null);

		// final SecurityGroupType st =
		// SecurityGroupUtils.describeSecurityGroup(
		// AccountUtil.toAccount(account), account.getDefZone(),
		// EcacheUtil.getSecurityGroupName(account.getId(), secGrpName));

		final SecurityGroupType st = SecurityGroupUtils.describeSecurityGroup(
				AccountUtil.toAccount(account), account.getDefZone(),
				secGrp.getProviderId());

		final AuthorizeCacheSecurityGroupIngressActionResultMessage.Builder result = AuthorizeCacheSecurityGroupIngressActionResultMessage.newBuilder();
		CacheSecurityGroup.Builder secGroup = CacheSecurityGroup.newBuilder();
		secGroup.setCacheSecurityGroupName(request.getCacheSecurityGroupName());
		secGroup.setDescription(secGrp.getDescription());
		final Collection<EC2SecurityGroup> grps = new ArrayList<EC2SecurityGroup>();
		for (final AuthorizeSecurityGroupIngressType t : st
				.getSecurityGroupIngress()) {
			if (t.getSourceSecurityGroupName() != null) {
				final EC2SecurityGroup.Builder g = EC2SecurityGroup.newBuilder();
				g.setEc2SecurityGroupName(t.getSourceSecurityGroupName());
				g.setEc2SecurityGroupOwnerId(t.getSourceSecurityGroupOwnerId());
				g.setStatus("authorizing");
				g.setEc2SecurityGroupOwnerId(ec2SecGrpOwnerId);
				grps.add(g.buildPartial());
			}
		}
		secGroup.addAllEc2SecurityGroups(grps);
		secGroup.setOwnerId(secGrp.getAcid() + "");
		result.setCacheSecurityGroup(secGroup);
		return result.buildPartial();
	}
}

