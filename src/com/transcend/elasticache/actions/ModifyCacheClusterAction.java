package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.ModifyCacheClusterActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.ModifyCacheClusterActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionRequestMessage;
import com.transcend.elasticache.message.ModifyCacheClusterActionMessage.ModifyCacheClusterActionResultMessage;
import com.yammer.metrics.core.Meter;

public class ModifyCacheClusterAction
        extends
        AbstractQueuedAction<ModifyCacheClusterActionRequestMessage, ModifyCacheClusterActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "ModifyCacheClusterAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		ModifyCacheClusterActionResultMessage message)
            throws ErrorResponse {
        return new ModifyCacheClusterActionResultMarshaller()
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
    public ModifyCacheClusterActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final ModifyCacheClusterActionRequestMessage requestMessage =
                ModifyCacheClusterActionRequestUnmarshaller
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
    		ModifyCacheClusterActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


