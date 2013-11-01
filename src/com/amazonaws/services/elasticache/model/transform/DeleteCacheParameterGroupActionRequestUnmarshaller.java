/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for DeleteCacheParameterGroupAction from XML document to 
 * the DeleteCacheParameterGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.services.elasticache.model.common.UnMarshallingUtils;
import com.msi.tough.query.ErrorResponse;
import com.transcend.elasticache.message.DeleteCacheParameterGroupActionMessage.DeleteCacheParameterGroupActionRequestMessage;

public class DeleteCacheParameterGroupActionRequestUnmarshaller implements
		Unmarshaller<DeleteCacheParameterGroupActionRequestMessage, Map<String, String[]>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());
	private static DeleteCacheParameterGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static DeleteCacheParameterGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new DeleteCacheParameterGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * DeleteCacheParameterGroupAction java bean.
	 * 
	 * CacheParameterGroupName String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated DeleteCacheParameterGroupAction java bean
	 */
	@Override
	public DeleteCacheParameterGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final DeleteCacheParameterGroupActionRequestMessage.Builder request = DeleteCacheParameterGroupActionRequestMessage.newBuilder();
		
		request.setCacheParameterGroupName(UnMarshallingUtils.unmarshallString(
				mapIn, "CacheParameterGroupName", true, "", logger));
		

		return request.buildPartial();
	}
}
