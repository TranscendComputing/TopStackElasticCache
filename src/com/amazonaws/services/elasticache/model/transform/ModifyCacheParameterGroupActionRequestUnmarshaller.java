/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for ModifyCacheParameterGroupAction from XML document to 
 * the ModifyCacheParameterGroupActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.amazonaws.transform.Unmarshaller;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.msi.tough.utils.Constants;
import com.transcend.elasticache.message.ElastiCacheMessage.ParameterNameValue;
import com.transcend.elasticache.message.ModifyCacheParameterGroupActionMessage.ModifyCacheParameterGroupActionRequestMessage;

public class ModifyCacheParameterGroupActionRequestUnmarshaller implements
		Unmarshaller<ModifyCacheParameterGroupActionRequestMessage, Map<String, String[]>> {

	private static ModifyCacheParameterGroupActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static ModifyCacheParameterGroupActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new ModifyCacheParameterGroupActionRequestUnmarshaller();
		}
		return instance;
	}

	//
	// public <T> Collection<T> requiredClassArray(final Map<String, String[]>
	// in,
	// final String key, final int max, final T template,
	// final String... props) {
	// final Collection<T> parms = new ArrayList<T>();
	//
	// for (int i = 1; i < max; i++) {
	// for (final String prop : props) {
	//
	// final Class c = template.getClass();
	// final T parm = template;
	//
	// final String cgi = key + i + "." + prop;
	// if (in.get(cgi) == null) {
	// break;
	// }
	//
	// final Method m[] = c.getDeclaredMethods();
	// for (final int mi = 0; i < m.length; i++) {
	// final String s = m[mi].toString();
	// }
	// parms.add(parm);
	// }
	// }
	//
	// return parms;
	// }
	//
	// private Collection<ParameterNameValue> requiredNameValueArray(
	// final Map<String, String[]> in, final String key, final int max) {
	// final Collection<ParameterNameValue> parameters = new
	// ArrayList<ParameterNameValue>();
	//
	// for (int i = 1; i <= max; i++) {
	// final String name = key + i + ".ParameterName";
	// if (in.get(name) == null) {
	// break;
	// }
	//
	// final String value = key + i + ".ParameterValue";
	// if (in.get(value) == null) {
	// break;
	// }
	//
	// final ParameterNameValue nv = new ParameterNameValue(
	// QueryUtil.requiredString(in, name),
	// QueryUtil.requiredString(in, value));
	// parameters.add(nv);
	// }
	//
	// if (in.size() == 0) {
	// throw ErrorResponse.invlidData(key + " is required");
	// }
	//
	// return parameters;
	// }

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * ModifyCacheParameterGroupAction java bean.
	 * 
	 * CacheParameterGroupName String required ParameterNameValues
	 * List<ParameterNameValue>
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated ModifyCacheParameterGroupAction java bean
	 */
	@Override
	public ModifyCacheParameterGroupActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse {

		final ModifyCacheParameterGroupActionRequestMessage.Builder request = ModifyCacheParameterGroupActionRequestMessage.newBuilder();

		request.setCacheParameterGroupName(QueryUtil.requiredString(mapIn,
				Constants.CACHEPARAMETERGROUPNAME));

		// Collect up to 20 parameters
		// AWS doc shows:
		// ParameterNameValues.member.1.ParameterName=chunk_size_growth_factor
		// ParameterNameValues.member.1.ParameterValue=1.02
		// But ModifyCacheParameterGroupActionRequest is creating
		// ParameterNameValues.ParameterNameValue.N.[ParameterName|ParameterValue]
		final Collection<ParameterNameValue> parameterNameValues = new ArrayList<ParameterNameValue>();
		final String s = "ParameterNameValues.ParameterNameValue.";
		for (int i = 1;; i++) {
			if (!mapIn.containsKey(s + i + ".ParameterName")
					|| !mapIn.containsKey(s + i + ".ParameterValue")) {
				break;
			}
			final ParameterNameValue.Builder param = ParameterNameValue.newBuilder();
			param.setParameterName(mapIn.get(s + i + ".ParameterName")[0]);
			param.setParameterValue(mapIn.get(s + i + ".ParameterValue")[0]);
			parameterNameValues.add(param.buildPartial());
		}
		request.addAllParameterNameValues(parameterNameValues);

		return request.buildPartial();
	}

}
