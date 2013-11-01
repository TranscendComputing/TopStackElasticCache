/**
 * @author Stephen Dean
 * 
 * Unmarshall's the request for AuthorizeCacheSecurityGroupIngressAction from XML document to 
 * the AuthorizeCacheSecurityGroupIngressActionRequest java bean supplied by the AWS SDK
 * 
 */
package com.amazonaws.services.elasticache.model.transform;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.services.elasticache.model.common.UnMarshallingUtils;
import com.msi.tough.query.ErrorResponse;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionRequestMessage;

public class AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller
		implements
		Unmarshaller<AuthorizeCacheSecurityGroupIngressActionRequestMessage, Map<String, String[]>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());
	private static AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller instance;

	/**
	 * get Instance
	 * 
	 * @return instance of unmarshaller
	 */
	public static AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller getInstance() {
		if (instance == null) {
			instance = new AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller();
		}
		return instance;
	}

	/**
	 * Marshals from the XML document (now represented by a map) into the
	 * AuthorizeCacheSecurityGroupIngressAction java bean.
	 * 
	 * CacheSecurityGroupName String required EC2SecurityGroupName String
	 * required Ec2SecuriryGroupOwnerID String required
	 * 
	 * @param Map
	 *            containing the input values from the XML request documents
	 * @return populated AuthorizeCacheSecurityGroupIngressAction java bean
	 * @throws Exception 
	 */
	@Override
	public AuthorizeCacheSecurityGroupIngressActionRequestMessage unmarshall(
			final Map<String, String[]> mapIn) throws ErrorResponse{

		final AuthorizeCacheSecurityGroupIngressActionRequestMessage.Builder request = 
				AuthorizeCacheSecurityGroupIngressActionRequestMessage.newBuilder();

			request.setCacheSecurityGroupName(UnMarshallingUtils.unmarshallString(
					mapIn, "CacheSecurityGroupName", true, "", logger));
		request.setEc2SecurityGroupName(UnMarshallingUtils.unmarshallString(
				mapIn, "EC2SecurityGroupName", true, "", logger));
		request.setEc2SecurityGroupOwnerId(UnMarshallingUtils.unmarshallString(
				mapIn, "EC2SecurityGroupOwnerId", true, "", logger));

		return request.buildPartial();
	}
}
