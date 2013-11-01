/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for CreateCacheClusterAction from XML document to 
 * the CreateCacheClusterActionRequest java bean supplied by the AWS SDK
 * 
 */

package com.amazonaws.services.elasticache.model.transform;

import java.util.Collection;
import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.google.common.base.Strings;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.msi.tough.utils.ValidateAWS;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionRequestMessage;

public class CreateCacheClusterActionRequestUnmarshaller implements
		Unmarshaller<CreateCacheClusterActionRequestMessage, Map<String, String[]>> {

	private static CreateCacheClusterActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static CreateCacheClusterActionRequestUnmarshaller getInstance() {

		if (instance == null) {
			instance = new CreateCacheClusterActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * CreateCacheClusterActionsrequest java bean.
	 * 
	 * CacheClisterID String required AvailabilityZone String optional defaults
	 * random NumCacheNodes Int required I CacheNodeType String required Engine
	 * String required EngineVersion String optional Port Int optional
	 * MinorVersionUpdate boolean optional default = true ParameterGroupName
	 * String optional SecurityGroupName List<String> required
	 * NorificationTopicArn String optional PreferreMaintenanceWindow String
	 * optional
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated CreateCacheClusterActionRequest java bean
	 */
	@Override
	public CreateCacheClusterActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) {
		final CreateCacheClusterActionRequestMessage.Builder req = CreateCacheClusterActionRequestMessage.newBuilder();

		// TODO: Use QueryUtil
		final String cacheClusterId = QueryUtil.requiredString(mapIn,
				Constants.CACHECLUSTERID);
		if (!ValidateAWS.isAWSHyphenatedId(cacheClusterId, 20)) {
			throw new ErrorResponse("Sender", "cacheClusterId not valid", "",
					400);
		}

		req.setCacheClusterId(cacheClusterId);

		req.setPreferredAvailabilityZone(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				Constants.PREFERREDAVAILABILITYZONE)));

		req.setNumCacheNodes(QueryUtil.requiredInt(mapIn,
				Constants.NUMCACHENODES));

		req.setCacheNodeType(QueryUtil.requiredString(mapIn,
				Constants.CACHENODETYPE));
		req.setEngine(QueryUtil.requiredString(mapIn, Constants.ENGINE));
		req.setEngineVersion(Strings.nullToEmpty(QueryUtil
				.getString(mapIn, Constants.ENGINEVERSION)));
		req.setPort(QueryUtil.getInt(mapIn, Constants.PORT));
		req.setAutoMinorVersionUpgrade(QueryUtil.getBoolean(mapIn,
				Constants.AUTOMINORVERSIONUPGRADE));
		req.setCacheParameterGroupName(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				Constants.CACHEPARAMETERGROUPNAME)));

		Collection<String> securityGroupNames = QueryUtil.requiredStringArray(
				mapIn, "CacheSecurityGroupNames.CacheSecurityGroupName", 10);
		if (securityGroupNames == null || securityGroupNames.size() == 0) {
			securityGroupNames = QueryUtil.requiredStringArray(mapIn,
					"CacheSecurityGroupNames.member", 10);
		}
		req.addAllCacheSecurityGroupNames(securityGroupNames);
		req.setNotificationTopicArn(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				Constants.NOTIFICATIONTOPICARN)));
		req.setPreferredMaintenanceWindow(Strings.nullToEmpty(QueryUtil.getString(mapIn,
				Constants.PREFERREDMAINENANCEWINDOW)));

		return req.buildPartial();
	}
}
