package com.amazonaws.services.elasticache.model.common;

import java.util.Date;
import java.util.List;

import com.generationjava.io.xml.XMLNode;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNode;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificParameter;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificValue;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroupStatus;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroupMembership;
import com.transcend.elasticache.message.ElastiCacheMessage.EC2SecurityGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.Endpoint;
import com.transcend.elasticache.message.ElastiCacheMessage.NotificationConfiguration;
import com.transcend.elasticache.message.ElastiCacheMessage.Parameter;
import com.transcend.elasticache.message.ElastiCacheMessage.PendingModifiedValues;

public class MarshallingUtils {

	/**
	 * @param nodeParent
	 * @param nodeName
	 * @param bool
	 */
	public static void marshallBoolean(final XMLNode nodeParent,
			final String nodeName, final Boolean bool) {

		MarshallingUtils.marshallString(nodeParent, nodeName, bool.toString());
	}

	/**
	 * Copies each of the CacheCluster records from the
	 * DescribeCacheClustersResponse java bean to the nodeCacheClusters node in
	 * the XML document
	 * 
	 * @param showNode
	 * 
	 * @param in
	 *            DescribeCacheClustersResponse java bean
	 * @param nodeCacheClusters
	 *            node
	 */
	public static XMLNode marshallCacheCluster(final CacheCluster cc,
			final Boolean showNode) {

		// add new CacheCluster Node
		final XMLNode nodeCacheCluster = new XMLNode("CacheCluster");

		QueryUtil.addNode(nodeCacheCluster, "CacheClusterId",
				cc.getCacheClusterId());

		QueryUtil.addNode(nodeCacheCluster, "CacheNodeType",
				cc.getCacheNodeType());

		QueryUtil.addNode(nodeCacheCluster, "NumCacheNodes",
				cc.getNumCacheNodes());

		QueryUtil.addNode(nodeCacheCluster, "Engine", cc.getEngine());
		QueryUtil.addNode(nodeCacheCluster, "EngineVersion",
				cc.getEngineVersion());

		QueryUtil.addNode(nodeCacheCluster, "CacheClusterCreateTime",
				cc.getCacheClusterCreateTime());


		QueryUtil.addNode(nodeCacheCluster, "CacheClusterStatus",
				cc.getCacheClusterStatus());

		QueryUtil.addNode(nodeCacheCluster, "PreferredAvailabilityZone",
				cc.getPreferredAvailabilityZone());

		QueryUtil.addNode(
				nodeCacheCluster,
				"PreferredMaintenanceWindow",
				cc.getPreferredMaintenanceWindow() == null ? "default" : cc
						.getPreferredMaintenanceWindow());

		QueryUtil.addNode(nodeCacheCluster, "AutoMinorVersionUpgrade",
				cc.getAutoMinorVersionUpgrade());

		// Process ParameterGroup Status
		final CacheParameterGroupStatus pgs = cc.getCacheParameterGroup();
		if (pgs != null) {

			final XMLNode nodePGrpStatus = QueryUtil.addNode(nodeCacheCluster,
					"CacheParameterGroup");

			QueryUtil.addNode(nodePGrpStatus, "CacheParameterGroupName",
					pgs.getCacheParameterGroupName());
			QueryUtil.addNode(
					nodePGrpStatus,
					"ParameterApplyStatus",
					pgs.getParameterApplyStatus() == null ? "in-sync" : pgs
							.getParameterApplyStatus());

			final List<String> idList = pgs.getCacheNodeIdsToRebootList();
			if (idList != null) {
				MarshallingUtils.marshallStringList(nodePGrpStatus,
						"CacheNodeIdsToReboot", idList, "CacheNodeId");
			}
		}

		// Process SecurityGroups
		final List<CacheSecurityGroupMembership> sgmList = cc
				.getCacheSecurityGroupsList();
		if (sgmList != null) {

			final XMLNode nodeSGrps = QueryUtil.addNode(nodeCacheCluster,
					"CacheSecurityGroups");
			for (final CacheSecurityGroupMembership sgm : sgmList) {
				final XMLNode nodeSGrpMem = QueryUtil.addNode(nodeSGrps,
						"CacheSecurityGroup");

				QueryUtil.addNode(nodeSGrpMem, "CacheSecurityGroupName",
						sgm.getCacheSecurityGroupName());
				QueryUtil.addNode(nodeSGrpMem, "Status", "active");
			}
		}

		// Process NotificationConfiguration
		final NotificationConfiguration notc = cc
				.getNotificationConfiguration();
		if (notc != null) {
			final XMLNode nodeNotConfig = new XMLNode(
					"NotificationConfiguration");

			QueryUtil.addNode(nodeNotConfig, "TopicArn", notc.getTopicArn());
			QueryUtil.addNode(nodeNotConfig, "TopicStatus",
					notc.getTopicStatus());
			nodeCacheCluster.addNode(nodeNotConfig);
		}

		// Process PendingValues
		final XMLNode nodePendMod = QueryUtil.addNode(nodeCacheCluster,
				"PendingModifiedValues");
		final PendingModifiedValues pm = cc.getPendingModifiedValues();
		if (pm != null) {
			QueryUtil.addNode(nodePendMod, "NumCacheNodes",
					pm.getNumCacheNodes());
			QueryUtil.addNode(nodePendMod, "EngineVersion",
					pm.getEngineVersion());
			final XMLNode remove = QueryUtil.addNode(nodePendMod,
					"CacheNodeIdsToRemove");
			final List<String> cnList = pm.getCacheNodeIdsToRemoveList();
			for (final String r : cnList) {
				QueryUtil.addNode(remove, "CacheNodeId", r);
			}
		}

		if (showNode) {
			// Process CacheNode List
			final List<CacheNode> nlist = cc.getCacheNodesList();
			if (nlist != null) {

				final XMLNode nodeCacheNodes = new XMLNode("CacheNodes");
				nodeCacheCluster.addNode(nodeCacheNodes);

				for (final CacheNode cn : nlist) {
					final XMLNode nodeCacheNode = new XMLNode("CacheNode");

					QueryUtil.addNode(nodeCacheNode, "CacheNodeCreateTime",
							cn.getCacheNodeCreateTime());

					QueryUtil.addNode(nodeCacheNode, "CacheNodeId",
							cn.getCacheNodeId());
					QueryUtil.addNode(nodeCacheNode, "CacheNodeStatus",
							cn.getCacheNodeStatus());
					QueryUtil.addNode(nodeCacheNode, "ParameterGroupStatus",
							cn.getParameterGroupStatus());

					// add end point information
					final XMLNode nodeEndpoint = new XMLNode("Endpoint");
					final Endpoint ep = cn.getEndpoint();
					QueryUtil.addNode(nodeEndpoint, "Address", ep.getAddress());
					QueryUtil.addNode(nodeEndpoint, "Port", ep.getPort());
					nodeCacheNode.addNode(nodeEndpoint);

					nodeCacheNodes.addNode(nodeCacheNode);
				}
			}
		}
		return nodeCacheCluster;
	}

	/**
	 * 
	 * @param cntParams
	 * @return
	 */
	public static XMLNode marshallCacheNodeTypeSpecificParameters(
			final List<CacheNodeTypeSpecificParameter> cntParams) {

		final XMLNode nodeSpecificParams = new XMLNode(
				Constants.CACHENODETYPESPECIFICPARAMETERS);

		for (final CacheNodeTypeSpecificParameter cns : cntParams) {

			final XMLNode nodeSpecificParam = new XMLNode(
					Constants.CACHENODETYPESPECIFICPARAMETER);
			nodeSpecificParams.addNode(nodeSpecificParam);

			QueryUtil.addNode(nodeSpecificParam, "AllowedValues",
					cns.getAllowedValues());

			final XMLNode nodeSpecificValues = new XMLNode(
					Constants.CACHENODETYPESPECIFICVALUES);
			nodeSpecificParam.addNode(nodeSpecificValues);

			final List<CacheNodeTypeSpecificValue> cnsValueList = cns
					.getCacheNodeTypeSpecificValuesList();
			if (cnsValueList != null) {

				for (final CacheNodeTypeSpecificValue cnsValue : cnsValueList) {

					final XMLNode nodeSpecificValue = new XMLNode(
							Constants.CACHENODETYPESPECIFICVALUE);
					nodeSpecificValues.addNode(nodeSpecificValue);

					QueryUtil.addNode(nodeSpecificValue, "CacheNodeType",
							cnsValue.getCacheNodeType());
					QueryUtil.addNode(nodeSpecificValue, "Value",
							cnsValue.getValue());
				}
			}

			QueryUtil.addNode(nodeSpecificParam, "DataType", cns.getDataType());
			QueryUtil.addNode(nodeSpecificParam, "Description",
					cns.getDescription());
			QueryUtil.addNode(nodeSpecificParam, "IsModifiable",
					cns.getIsModifiable());

			QueryUtil.addNode(nodeSpecificParam, "MinimumEngineVersion",
					cns.getMinimumEngineVersion());
			QueryUtil.addNode(nodeSpecificParam, "ParameterName",
					cns.getParameterName());
			QueryUtil.addNode(nodeSpecificParam, "Source", cns.getSource());
		}

		return nodeSpecificParams;
	}

	/**
	 * 
	 * @param cpg
	 * @return
	 */
	public static XMLNode marshallCacheParameterGroup(
			final CacheParameterGroup cpg) {

		// add new CacheCluster Node
		final XMLNode nodeCacheParameterGroup = new XMLNode(
				"CacheParameterGroup");

		QueryUtil.addNode(nodeCacheParameterGroup, "CacheParameterGroupName",
				cpg.getCacheParameterGroupName());
		QueryUtil.addNode(nodeCacheParameterGroup, "CacheParameterGroupFamily",
				cpg.getCacheParameterGroupFamily());
		QueryUtil.addNode(nodeCacheParameterGroup, "Description",
				cpg.getDescription());

		return nodeCacheParameterGroup;
	}

	/**
	 * 
	 * @param csg
	 * @return
	 */
	public static XMLNode marshallCacheSecurityGroup(
			final CacheSecurityGroup csg) {

		// add new CacheCluster Node
		final XMLNode nodeCacheSecurityGroup = new XMLNode("CacheSecurityGroup");

		QueryUtil.addNode(nodeCacheSecurityGroup, "CacheSecurityGroupName",
				csg.getCacheSecurityGroupName());
		QueryUtil.addNode(nodeCacheSecurityGroup, "Description",
				csg.getDescription());
		QueryUtil.addNode(nodeCacheSecurityGroup, "OwnerId", csg.getOwnerId());

		// process list of EC2SecurityGroups
		final List<EC2SecurityGroup> ecsgList = csg.getEc2SecurityGroupsList();

		if (ecsgList != null) {

			final XMLNode nodeEC2SecurityGroups = QueryUtil.addNode(
					nodeCacheSecurityGroup, "EC2SecurityGroups");
			for (final EC2SecurityGroup sg : ecsgList) {

				final XMLNode nodeEC2SecurityGroup = QueryUtil.addNode(
						nodeEC2SecurityGroups, "EC2SecurityGroup");

				QueryUtil.addNode(nodeEC2SecurityGroup, "EC2SecurityGroupName",
						sg.getEc2SecurityGroupName());
				QueryUtil.addNode(nodeEC2SecurityGroup,
						"EC2SecurityGroupOwnerId",
						sg.getEc2SecurityGroupOwnerId());
			}
		}
		return nodeCacheSecurityGroup;
	}

	/**
	 * @param nodeParent
	 * @param nodeName
	 * @param dval
	 */
	public static void marshallDouble(final XMLNode nodeParent,
			final String nodeName, final Double dval) {

		MarshallingUtils.marshallString(nodeParent, nodeName, dval.toString());
	}

	/**
	 * @param nodeParent
	 * @param nodeName
	 * @param ival
	 */
	public static void marshallInteger(final XMLNode nodeParent,
			final String nodeName, final Integer ival) {

		MarshallingUtils.marshallString(nodeParent, nodeName, ival.toString());
	}

	/**
	 * 
	 * @param params
	 * @return
	 */
	public static XMLNode marshallParameters(final List<Parameter> params) {

		final XMLNode nodeParams = new XMLNode("Parameters");

		for (final Parameter param : params) {

			final XMLNode nodeParam = new XMLNode("Parameter");
			nodeParams.addNode(nodeParam);

			QueryUtil.addNode(nodeParam, "ParameterName",
					param.getParameterName());
			QueryUtil.addNode(nodeParam, "Source", param.getSource());
			QueryUtil.addNode(nodeParam, "ParameterValue",
					param.getParameterValue());
			QueryUtil.addNode(nodeParam, "Description", param.getDescription());
			QueryUtil.addNode(nodeParam, "DataType", param.getDataType());
			QueryUtil.addNode(nodeParam, "AllowedValues",
					param.getAllowedValues());
			QueryUtil.addNode(nodeParam, "IsModifiable",
					param.getIsModifiable());
			QueryUtil.addNode(nodeParam, "MinimumEngineVersion",
					param.getMinimumEngineVersion());
		}
		return nodeParams;
	}

	/**
	 * @param nodeParent
	 * @param nodeName
	 * @param string
	 */
	public static void marshallString(final XMLNode nodeParent,
			final String nodeName, final String string) {

		final XMLNode nodeString = new XMLNode(nodeName);
		nodeParent.addNode(nodeString);
		final XMLNode nodeString0 = new XMLNode();
		nodeString.addNode(nodeString0);
		nodeString0.setPlaintext(string);
	}

	/**
	 * @param nodeParent
	 * @param nodeName
	 * @param list
	 * @param stringNodeNames
	 */
	public static void marshallStringList(final XMLNode nodeParent,
			final String nodeName, final List<String> list,
			final String stringNodeNames) {

		final XMLNode nodeList = new XMLNode(nodeName);
		nodeParent.addNode(nodeList);

		for (final String string : list) {
			MarshallingUtils.marshallString(nodeList, stringNodeNames, string);
		}
	}

	/**
	 * @param nodeParent
	 * @param nodeAlarmconfigurationupdatedtimestamp
	 * @param alarmConfigurationUpdatedTimestamp
	 */
	public static void marshallTimestamp(final XMLNode nodeParent,
			final String nodeName, final Date timeStamp) {

		final XMLNode nodeTimestamp = new XMLNode(nodeName);
		nodeParent.addNode(nodeTimestamp);
		nodeParent.setPlaintext("" + timeStamp);
	}
}
