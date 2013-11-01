/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for CreateCacheSecurityGroupAction from XML document to 
 * the CreateCacheSecurityGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionRequestMessage;

public class CreateCacheSecurityGroupActionRequestUnmarshaller implements
		Unmarshaller<CreateCacheSecurityGroupActionRequestMessage, Map<String, String[]>> {

	private static CreateCacheSecurityGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static CreateCacheSecurityGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new CreateCacheSecurityGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * CreateCacheSecurityGroupAction java bean.
	 * 
	 * CacheSecurityGroupName String required Description String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated CreateCacheSecurityGroupAction java bean
	 */
	@Override
	public CreateCacheSecurityGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final CreateCacheSecurityGroupActionRequestMessage.Builder request = CreateCacheSecurityGroupActionRequestMessage.newBuilder();

		request.setCacheSecurityGroupName(QueryUtil.requiredString(mapIn,
				"CacheSecurityGroupName"));
		request.setDescription(QueryUtil.requiredString(mapIn, "Description"));

		return request.buildPartial();
	}
}
