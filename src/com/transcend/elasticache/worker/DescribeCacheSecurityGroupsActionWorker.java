package com.transcend.elasticache.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheSecurityGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.mysql.jdbc.StringUtils;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;

public class DescribeCacheSecurityGroupsActionWorker extends 
		AbstractWorker<DescribeCacheSecurityGroupsActionRequestMessage, DescribeCacheSecurityGroupsActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeCacheSecurityGroupsActionWorker.class
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
   public DescribeCacheSecurityGroupsActionResultMessage doWork(
           DescribeCacheSecurityGroupsActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeCacheSecurityGroupsAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeCacheSecurityGroupsActionResultMessage doWork0(DescribeCacheSecurityGroupsActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0 - Describe Cache Security Groups");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		EcacheUtil.ensureDefaultSecurityGroup(session, account.getId());

		final String secGrpName = request.getCacheSecurityGroupName();
		final String marker = request.getMarker();
		final int maxRecords = request.getMaxRecords();
		logger.debug("describeSecurityGroup");
		logger.debug("SecurityGroupName = " + secGrpName);
		logger.debug("Marker = " + marker);
		logger.debug("MaxRecord = " + maxRecords);

		final boolean hasSecurityGroupId = !StringUtils
				.isNullOrEmpty(secGrpName);

		final DescribeCacheSecurityGroupsActionResultMessage.Builder result = DescribeCacheSecurityGroupsActionResultMessage.newBuilder();

		final Collection<CacheSecurityGroup> secGrpList = new ArrayList<CacheSecurityGroup>();
		String sql = "from CacheSecurityGroupBean where acid="
				+ account.getId();
		if (!StringUtils.isNullOrEmpty(secGrpName)) {
			sql += " and name='" + secGrpName + "'";
		} else if (!StringUtils.isNullOrEmpty(marker)) {
			sql += " and name>'" + marker + "'";
		}
		sql += " order by name";
		final Query q = session.createQuery(sql);
		final List<CacheSecurityGroupBean> l = q.list();
		if (hasSecurityGroupId && l.isEmpty()) {
			throw ElasticacheFaults.CacheSecurityGroupNotFound();
		}
		//
		// if (maxRecords != 0 && hasParameterGroupId == false) {
		// paramGroupCriteria.setMaxResults(maxRecords);
		// }

		// final String lastParameterGroupName = null;
		for (final CacheSecurityGroupBean grp : l) {
			secGrpList.add(EcacheUtilV2.toAwsCacheSecurityGroup(session, grp));
		}
		result.addAllCacheSecurityGroups(secGrpList);
		// result.setMarker(lastSecurityGroupName);
		return result.buildPartial();
	}
}

