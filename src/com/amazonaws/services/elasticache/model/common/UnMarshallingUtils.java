package com.amazonaws.services.elasticache.model.common;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.QueryUtil;
import com.transcend.elasticache.message.ElastiCacheMessage.ParameterNameValue;

public class UnMarshallingUtils {

	public static Collection<ParameterNameValue> requiredNameValueArray(
			final Map<String, String[]> in, final String key, final int max,
			final boolean nameOnly) {
		final Collection<ParameterNameValue> parameters = new ArrayList<ParameterNameValue>();

		for (int i = 1; i <= max; i++) {
			final String nameKey = key + i + ".ParameterName";
			if (in.get(nameKey) == null) {
				break;
			}

			final String valueKey = key + i + ".ParameterValue";
			if (nameOnly == false && in.get(valueKey) == null) {
				break;
			}

			final String value = nameOnly ? "" : QueryUtil.getString(in,
					valueKey);

			final ParameterNameValue.Builder nv = ParameterNameValue.newBuilder();
				nv.setParameterName(QueryUtil.requiredString(in, nameKey));
				nv.setParameterValue(value);
			parameters.add(nv.buildPartial());
		}

		if (in.size() == 0) {
			throw ErrorResponse.invlidData(key + " is required");
		}

		return parameters;
	}

	// TODO: Try to remove everything below here. QueryUtil should
	// be the sole source of this kind of code

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static BigInteger unmarshallBigInt(
			final Map<String, String[]> mapIn, final String key,
			final boolean required, final String defaultValue,
			final Logger logger) throws Exception {

		final String value[] = mapIn.get(key);
		BigInteger val = null;

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw new Exception(msg);
			} else {
				if (defaultValue == null) {
					logger.info("No key \"" + key + "\" exists in the request.");
				} else {
					logger.info("No key \"" + key
							+ "\" exists in the request. "
							+ "Setting to default " + defaultValue);
					val = new BigInteger(defaultValue);
				}
			}

		} else {
			val = new BigInteger(value[0]);
			logger.info("Setting" + key + "\" to value" + val);
		}
		return val;
	}

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static boolean unmarshallBoolean(final Map<String, String[]> mapIn,
			final String key, final boolean required,
			final String defaultValue, final Logger logger) throws Exception {

		final String value[] = mapIn.get(key);
		boolean val = false;

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw new Exception(msg);
			} else {
				if (defaultValue == null) {
					logger.info("No key \"" + key + "\" exists in the request.");
				} else {
					logger.info("No key \"" + key
							+ "\" exists in the request. "
							+ "Setting to default " + defaultValue);
					val = Boolean.parseBoolean(defaultValue);
				}
			}

		} else {
			val = Boolean.parseBoolean(value[0]);
			logger.info("Setting" + key + "\" to value" + val);
		}
		return val;
	}

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static Date unmarshallDate(final Map<String, String[]> mapIn,
			final String key, final boolean required,
			final String defaultValue, final Logger logger) throws Exception {

		final String value[] = mapIn.get(key);
		final DateFormat dFormatter = new SimpleDateFormat(
				com.msi.tough.utils.Constants.DATEFORMAT);
		Date val = null;

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw new Exception(msg);
			} else {
				if (defaultValue == null) {
					logger.info("No key \"" + key + "\" exists in the request.");
				} else {
					logger.info("No key \"" + key
							+ "\" exists in the request. "
							+ "Setting to default " + defaultValue);
					val = dFormatter.parse(defaultValue);
				}
			}

		} else {

			val = dFormatter.parse(value[0]);
			logger.info("Setting" + key + "\" to value" + val);
		}
		return val;
	}

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static int unmarshallInt(final Map<String, String[]> mapIn,
			final String key, final boolean required,
			final String defaultValue, final Logger logger) throws ErrorResponse {

		final String value[] = mapIn.get(key);
		int val = 0;

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw ErrorResponse.missingParameter();
			} else {
				if (defaultValue == null) {
					logger.info("No key \"" + key + "\" exists in the request.");
				} else {
					logger.info("No key \"" + key
							+ "\" exists in the request. "
							+ "Setting to default " + defaultValue);
					val = Integer.valueOf(defaultValue);
				}
			}

		} else {
			val = Integer.valueOf(value[0]);
			logger.info("Setting" + key + "\" to value" + val);
		}
		return val;
	}

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static String unmarshallString(final Map<String, String[]> mapIn,
			final String key, final Boolean required,
			final String defaultValue, final Logger logger) throws ErrorResponse{

		final String[] value = mapIn.get(key);
		String val = "";

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw ErrorResponse.missingParameter();
			} else {
				logger.info("No key \"" + key + "\" exists in the request. "
						+ "Setting to default " + defaultValue);
				val = defaultValue;
			}
		} else {
			val = value[0];
			logger.info("Setting" + key + "\" to value" + val);
		}
		return val;
	}

	/**
	 * 
	 * @param mapIn
	 * @param key
	 * @param required
	 * @param defaultValue
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static List<String> unmarshallStringList(
			final Map<String, String[]> mapIn, final String key,
			final Boolean required, final List<String> defaultValue,
			final Logger logger) throws Exception {

		final String value[] = mapIn.get(key);
		List<String> strList = new ArrayList<String>();

		if (value == null) {

			if (required == true) {
				final String msg = "request value " + key + " is required";
				logger.error(msg);
				throw new Exception(msg);
			} else {
				if (defaultValue == null) {
					logger.info("No key \"" + key + "\" exists in the request.");
				} else {
					logger.info("No key \"" + key
							+ "\" exists in the request. "
							+ "Setting to default " + defaultValue);
					strList = defaultValue;
				}
			}

		} else {
			for (int i = 0; i < strList.size(); i++) {
				strList.add(value[i]);
				logger.info("Setting" + key + "\" to value" + value[i]);
			}
		}
		return strList;
	}

}
