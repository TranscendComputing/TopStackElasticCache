/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DeleteCacheClusterAction from XML document to 
 * the DeleteCacheClusterActionsRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionRequestMessage;

public class DeleteCacheClusterActionRequestUnmarshaller implements
		Unmarshaller<DeleteCacheClusterActionRequestMessage, Map<String, String[]>> {

	private static DeleteCacheClusterActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DeleteCacheClusterActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DeleteCacheClusterActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DeleteCacheClusterActions java bean.
	 * 
	 * CacheClisterID String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DeleteCacheClusterActions java bean
	 */
	@Override
	public DeleteCacheClusterActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DeleteCacheClusterActionRequestMessage.Builder request = DeleteCacheClusterActionRequestMessage.newBuilder();

		request.setCacheClusterId(QueryUtil.requiredString(mapIn,
				Constants.CACHECLUSTERID));

		return request.buildPartial();
	}
}
