package com.transcend.elasticache.worker;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.elasticache.model.common.EcacheUtilV2;
import com.msi.tough.core.Appctx;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.ElasticacheFaults;
import com.msi.tough.workflow.core.AbstractWorker;
import com.mysql.jdbc.StringUtils;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;

public class DescribeCacheClustersActionWorker extends 
		AbstractWorker<DescribeCacheClustersActionRequestMessage, DescribeCacheClustersActionResultMessage> {
	
	private final static Logger logger = Appctx
			.getLogger(DescribeCacheClustersActionWorker.class
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
   public DescribeCacheClustersActionResultMessage doWork(
           DescribeCacheClustersActionRequestMessage req) throws Exception {
       logger.debug("Performing work for DescribeCacheClustersAction.");
       return super.doWork(req, getSession());
   }

	@Override
	protected DescribeCacheClustersActionResultMessage doWork0(DescribeCacheClustersActionRequestMessage request,
			ServiceRequestContext context) throws Exception {

		logger.debug("into process0 - Describe Cache Cluster");

		final AccountBean account = context.getAccountBean();
		final Session 	  session = getSession();
		
		final DescribeCacheClustersActionResultMessage.Builder result = DescribeCacheClustersActionResultMessage.newBuilder();

		final String clusterId = request.getCacheClusterId();
		String marker = request.getMarker();
		final Boolean showNode = request.getShowCacheNodeInfo();
		final int maxRecords = request.getMaxRecords();
		if (maxRecords > 0 && (maxRecords < 20 || maxRecords > 100)) {
			throw QueryFaults.InvalidParameterValue();
		}
		Appctx.setThreadMap("ShowCacheNodeInfo", showNode);
		logger.debug("describeCacheClusters");
		logger.debug("ClusterID = " + clusterId);
		logger.debug("Marker = " + marker);
		logger.debug("MaxRecords = " + maxRecords);

		final boolean hasCacheClusterId = !StringUtils.isNullOrEmpty(clusterId);

		final List<CacheCluster> awsCacheClusters = new ArrayList<CacheCluster>();

		// List cache clusters belonging to account, optionally qualified by
		// name
		final List<CacheClusterBean> clusters = EcacheUtil
				.selectCacheClusterBean(session, account.getId(), clusterId);

		// If a cache cluster ID is specified but not found, return error
		if (hasCacheClusterId && clusters.size() == 0) {
			throw ElasticacheFaults.CacheClusterNotFound();
		}

		for (final CacheClusterBean cluster : clusters) {
			awsCacheClusters
					.add(EcacheUtilV2.toAwsCacheCluster(session, cluster));
			marker = cluster.getName();
		}

		result.addAllCacheClusters(awsCacheClusters);
		result.setMarker(marker);
		return result.buildPartial();
	}
}

