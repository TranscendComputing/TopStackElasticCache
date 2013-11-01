package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.RevokeCacheSecurityGroupIngressActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.RevokeCacheSecurityGroupIngressActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.RevokeCacheSecurityGroupIngressActionMessage.RevokeCacheSecurityGroupIngressActionRequestMessage;
import com.transcend.elasticache.message.RevokeCacheSecurityGroupIngressActionMessage.RevokeCacheSecurityGroupIngressActionResultMessage;
import com.yammer.metrics.core.Meter;

public class RevokeCacheSecurityGroupIngressAction
        extends
        AbstractQueuedAction<RevokeCacheSecurityGroupIngressActionRequestMessage, RevokeCacheSecurityGroupIngressActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "RevokeCacheSecurityGroupIngressAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		RevokeCacheSecurityGroupIngressActionResultMessage message)
            throws ErrorResponse {
        return new RevokeCacheSecurityGroupIngressActionResultMarshaller()
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
    public RevokeCacheSecurityGroupIngressActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final RevokeCacheSecurityGroupIngressActionRequestMessage requestMessage =
                RevokeCacheSecurityGroupIngressActionRequestUnmarshaller
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
    		RevokeCacheSecurityGroupIngressActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


