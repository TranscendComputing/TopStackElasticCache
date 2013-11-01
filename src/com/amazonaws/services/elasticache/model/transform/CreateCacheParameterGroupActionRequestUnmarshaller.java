/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for CreateCacheParameterGroupAction from XML document to 
 * the CreateCacheParameterGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionRequestMessage;

public class CreateCacheParameterGroupActionRequestUnmarshaller implements
		Unmarshaller<CreateCacheParameterGroupActionRequestMessage, Map<String, String[]>> {

	private static CreateCacheParameterGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static CreateCacheParameterGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new CreateCacheParameterGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * CreateCacheParameterGroupAction java bean.
	 * 
	 * CacheParameterGroupName String required Description String required
	 * CacheParameterGroupFamily String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated CreateCacheParameterGroupAction java bean
	 */
	@Override
	public CreateCacheParameterGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final CreateCacheParameterGroupActionRequestMessage.Builder request = CreateCacheParameterGroupActionRequestMessage.newBuilder();
		request.setCacheParameterGroupName(QueryUtil.requiredString(mapIn,
				"CacheParameterGroupName"));
		request.setDescription(QueryUtil.requiredString(mapIn, "Description"));
		request.setCacheParameterGroupFamily(QueryUtil.requiredString(mapIn,
				"CacheParameterGroupFamily"));

		return request.buildPartial();
	}
}
