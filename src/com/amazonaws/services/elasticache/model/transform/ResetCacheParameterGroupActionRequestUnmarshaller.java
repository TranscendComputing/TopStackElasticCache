/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for ResetCacheParameterGroupAction from XML document to 
 * the ResetCacheParameterGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import com.amazonaws.services.elasticache.model.common.UnMarshallingUtils;
import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionRequestMessage;

public class ResetCacheParameterGroupActionRequestUnmarshaller implements
		Unmarshaller<ResetCacheParameterGroupActionRequestMessage, Map<String, String[]>> {

	private static ResetCacheParameterGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static ResetCacheParameterGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new ResetCacheParameterGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * ResetCacheParameterGroupAction java bean.
	 * 
	 * CacheParameterGroupName String required ResetAllParameters Boolean
	 * optional ParameterNameValues List<ParameterNameValue>
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated ResetCacheParameterGroupAction java bean
	 */
	@Override
	public ResetCacheParameterGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final ResetCacheParameterGroupActionRequestMessage.Builder request = ResetCacheParameterGroupActionRequestMessage.newBuilder();

		request.setCacheParameterGroupName(QueryUtil.requiredString(mapIn,
				Constants.CACHEPARAMETERGROUPNAME));
		request.setResetAllParameters(QueryUtil.getBoolean(mapIn,
				Constants.RESETALLPARAMETERS, false));

		request.addAllParameterNameValues(UnMarshallingUtils
				.requiredNameValueArray(mapIn,
						"ParameterNameValues.ParameterNameValue.", 20, true));

		return request.buildPartial();
	}
}
