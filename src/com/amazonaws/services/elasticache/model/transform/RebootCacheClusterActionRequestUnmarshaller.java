/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for RebootCacheClusterAction from XML document to 
 * the RebootCacheClusterActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.services.elasticache.model.common.UnMarshallingUtils;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.transcend.elasticache.message.RebootCacheClusterActionMessage.RebootCacheClusterActionRequestMessage;

public class RebootCacheClusterActionRequestUnmarshaller implements
		Unmarshaller<RebootCacheClusterActionRequestMessage, Map<String, String[]>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());
	private static RebootCacheClusterActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static RebootCacheClusterActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new RebootCacheClusterActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * RebootCacheClusterActions java bean.
	 * 
	 * CacheClisterID String required CacheNodesIdsToReboot <List>String
	 * required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated RebootCacheClusterActions java bean
	 */
	@Override
	public RebootCacheClusterActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final RebootCacheClusterActionRequestMessage.Builder request = RebootCacheClusterActionRequestMessage.newBuilder();

			request.setCacheClusterId(UnMarshallingUtils.unmarshallString(mapIn,
					"CacheClusterId", true, "", logger));

		final Collection<String> ids = new ArrayList<String>();
		for (int i = 1;; i++) {
			if (!mapIn.containsKey("CacheNodeIdsToReboot.member." + i)) {
				break;
			}
			ids.add(QueryUtil.getString(mapIn, "CacheNodeIdsToReboot.member."
					+ i));
		}
		request.addAllCacheNodeIdsToReboot(ids);

		return request.buildPartial();
	}
}
