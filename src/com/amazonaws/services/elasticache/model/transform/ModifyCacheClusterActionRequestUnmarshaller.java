/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for ModifyCacheClusterAction from XML document to 
 * the ModifyCacheClusterActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionRequestMessage;

public class ModifyCacheClusterActionRequestUnmarshaller implements
		Unmarshaller<ModifyCacheClusterActionRequestMessage, Map<String, String[]>> {

	private static ModifyCacheClusterActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static ModifyCacheClusterActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new ModifyCacheClusterActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * ModifyCacheClusterActions java bean.
	 * 
	 * CacheClisterID String required NumCacheNodes Int optional EngineVersion
	 * String optional MinorVersionUpdate boolean optional default = true
	 * ParameterGroupName String optional SecurityGroupName List<String>
	 * optional NotificationTopicArn String optional NotificationTopicStatus
	 * String optional PreferreMaintenanceWindow String optional
	 * ApplyImmediately boolean optional CacheNodeIdsToRemove List<Strinhg>
	 * optional
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated ModifyCacheClusterActions java bean
	 */
	@Override
	public ModifyCacheClusterActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final ModifyCacheClusterActionRequestMessage.Builder req = ModifyCacheClusterActionRequestMessage.newBuilder();
		req.setCacheClusterId(QueryUtil.requiredString(mapIn, "CacheClusterId"));

		req.setNumCacheNodes(QueryUtil.getInt(mapIn, "NumCacheNodes"));

		// Default is TRUE
		req.setApplyImmediately(QueryUtil.getBoolean(mapIn, "ApplyImmediately",
				true));
		req.setAutoMinorVersionUpgrade(QueryUtil.getBoolean(mapIn,
				"AutoMinorVersionUpgrade", false));

		req.setCacheParameterGroupName(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				"CacheParameterGroupName")));
		req.setEngineVersion(Strings.nullToEmpty(QueryUtil.getString(mapIn, "EngineVersion")));
		req.setNotificationTopicArn(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				"NotificationTopicArn")));
		req.setNotificationTopicStatus(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				"NotificationTopicStatus")));
		req.setPreferredMaintenanceWindow(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				"PreferredMaintenanceWindow")));
		req.addAllCacheNodeIdsToRemove(QueryUtil.getStringArray(mapIn,
				"CacheNodeIdsToRemove.CacheNodeId", 20));
		req.addAllCacheSecurityGroupNames(QueryUtil.getStringArray(mapIn,
				"CacheSecurityGroupNames.CacheSecurityGroupName", 10));

		return req.buildPartial();
	}
}
