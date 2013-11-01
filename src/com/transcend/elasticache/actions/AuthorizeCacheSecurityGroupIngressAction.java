package com.transcend.elasticache.actions;
import java.util.Map;

import com.amazonaws.services.elasticache.model.transform.AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.AuthorizeCacheSecurityGroupIngressActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionRequestMessage;
import com.transcend.elasticache.message.AuthorizeCacheSecurityGroupIngressActionMessage.AuthorizeCacheSecurityGroupIngressActionResultMessage;
import com.yammer.metrics.core.Meter;

public class AuthorizeCacheSecurityGroupIngressAction
        extends
        AbstractQueuedAction<AuthorizeCacheSecurityGroupIngressActionRequestMessage, AuthorizeCacheSecurityGroupIngressActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "AuthorizeCacheSecurityGroupIngressAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		AuthorizeCacheSecurityGroupIngressActionResultMessage message)
            throws ErrorResponse {
        return new AuthorizeCacheSecurityGroupIngressActionResultMarshaller()
                .marshall(message);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.msi.tough.query.AbstractQueuedAction#handleRequest(com.msi.tough.
     * query.ServiceRequest, com.msi.tough.query.ServiceRequestContext)
     */
    @Override
    public AuthorizeCacheSecurityGroupIngressActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final AuthorizeCacheSecurityGroupIngressActionRequestMessage requestMessage =
                AuthorizeCacheSecurityGroupIngressActionRequestUnmarshaller
                .getInstance().unmarshall(req.getParameterMap());

        return requestMessage;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.msi.tough.query.AbstractQueuedAction#buildResponse(com.msi.tough.
     * query.ServiceResponse, com.google.protobuf.Message)
     */
    @Override
    public ServiceResponse buildResponse(ServiceResponse resp,
    		AuthorizeCacheSecurityGroupIngressActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


