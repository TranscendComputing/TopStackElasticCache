package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.RebootCacheClusterActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.RebootCacheClusterActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.RebootCacheClusterActionMessage.RebootCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.RebootCacheClusterActionMessage.RebootCacheClusterActionResultMessage;
import com.yammer.metrics.core.Meter;

public class RebootCacheClusterAction
        extends
        AbstractQueuedAction<RebootCacheClusterActionRequestMessage, RebootCacheClusterActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "RebootCacheClusterAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		RebootCacheClusterActionResultMessage message)
            throws ErrorResponse {
        return new RebootCacheClusterActionResultMarshaller()
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
    public RebootCacheClusterActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final RebootCacheClusterActionRequestMessage requestMessage =
                RebootCacheClusterActionRequestUnmarshaller
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
    		RebootCacheClusterActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


