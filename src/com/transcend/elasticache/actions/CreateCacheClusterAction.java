package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.CreateCacheClusterActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.CreateCacheClusterActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheClusterActionMessage.CreateCacheClusterActionResultMessage;
import com.yammer.metrics.core.Meter;

public class CreateCacheClusterAction
        extends
        AbstractQueuedAction<CreateCacheClusterActionRequestMessage, CreateCacheClusterActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "CreateCacheClusterAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		CreateCacheClusterActionResultMessage message)
            throws ErrorResponse {
        return new CreateCacheClusterActionResultMarshaller()
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
    public CreateCacheClusterActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final CreateCacheClusterActionRequestMessage requestMessage =
                CreateCacheClusterActionRequestUnmarshaller
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
    		CreateCacheClusterActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


