package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DeleteCacheClusterActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DeleteCacheClusterActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.DeleteCacheClusterActionMessage.DeleteCacheClusterActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DeleteCacheClusterAction
        extends
        AbstractQueuedAction<DeleteCacheClusterActionRequestMessage, DeleteCacheClusterActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DeleteCacheClusterAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DeleteCacheClusterActionResultMessage message)
            throws ErrorResponse {
        return new DeleteCacheClusterActionResultMarshaller()
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
    public DeleteCacheClusterActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DeleteCacheClusterActionRequestMessage requestMessage =
                DeleteCacheClusterActionRequestUnmarshaller
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
    		DeleteCacheClusterActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


