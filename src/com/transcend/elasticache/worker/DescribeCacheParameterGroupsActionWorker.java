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
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.mysql.jdbc.StringUtils;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroup;

public class DescribeCacheParameterGroupsActionWorker extends 
		AbstractWorker<DescribeCacheParameterGroupsActionRequestMessage, DescribeCacheParameterGroupsActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeCacheParameterGroupsActionWorker.class
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
   public DescribeCacheParameterGroupsActionResultMessage doWork(
           DescribeCacheParameterGroupsActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeCacheParameterGroupsAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeCacheParameterGroupsActionResultMessage doWork0(DescribeCacheParameterGroupsActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0 - DescribeCacheParameterGroup");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();

		final DescribeCacheParameterGroupsActionResultMessage.Builder result = DescribeCacheParameterGroupsActionResultMessage.newBuilder();

		final String paramGrpName = request.getCacheParameterGroupName();
		final String marker = request.getMarker();
		final int maxRecords = request.getMaxRecords();
		logger.debug("DescribeCacheParameterGroups");
		logger.debug("ParameterGroupName = " + paramGrpName);
		logger.debug("Marker = " + marker);
		logger.debug("MaxRecord = " + maxRecords);

		final boolean hasParameterGroupId = !StringUtils
				.isNullOrEmpty(paramGrpName);

		EcacheUtil.ensureDefaultParameterGroup(session, account.getId());

		final Collection<CacheParameterGroup> awsParamGroups = new ArrayList<CacheParameterGroup>();
		String sql = "from CacheParameterGroupBean where acid="
				+ account.getId();
		if (hasParameterGroupId) {
			sql += " and name='" + paramGrpName + "'";
		} else if (!StringUtils.isNullOrEmpty(marker)) {
			sql += " and name>'" + paramGrpName + "'";
		}
		sql += " order by name";
		final Query q = session.createQuery(sql);
		final List<CacheParameterGroupBean> l = q.list();
		//
		// if (maxRecords != 0 && hasParameterGroupId == false) {
		// paramGroupCriteria.setMaxResults(maxRecords);
		// }

		if (paramGrpName != null && !"".equals(paramGrpName) && l.size() == 0) {
			throw ElasticacheFaults.CacheParameterGroupNotFound();
		}

		String lastParameterGroupName = null;
		for (final CacheParameterGroupBean parameterGroup : l) {
			awsParamGroups.add(EcacheUtilV2.toAwsCacheParameterGroup(session,
					parameterGroup));
			lastParameterGroupName = parameterGroup.getName();
		}
		result.addAllCacheParameterGroups(awsParamGroups);
		result.setMarker(lastParameterGroupName);
		return result.buildPartial();
	}
}

