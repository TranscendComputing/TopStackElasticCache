/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DeleteCacheSecurityGroupAction from XML document to 
 * the DeleteCacheSecurityGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionRequestMessage;

public class DeleteCacheSecurityGroupActionRequestUnmarshaller implements
		Unmarshaller<DeleteCacheSecurityGroupActionRequestMessage, Map<String, String[]>> {

	private static DeleteCacheSecurityGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DeleteCacheSecurityGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DeleteCacheSecurityGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DeleteCacheSecurityGroupAction java bean.
	 * 
	 * CacheSecurityGroupName String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DeleteCacheSecurityGroupAction java bean
	 */
	@Override
	public DeleteCacheSecurityGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DeleteCacheSecurityGroupActionRequestMessage.Builder request = DeleteCacheSecurityGroupActionRequestMessage.newBuilder();

		request.setCacheSecurityGroupName(QueryUtil.requiredString(mapIn,
				Constants.CACHESECURITYGROUPNAME));

		return request.buildPartial();
	}
}
