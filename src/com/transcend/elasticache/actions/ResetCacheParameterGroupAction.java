package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.ResetCacheParameterGroupActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.ResetCacheParameterGroupActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.ResetCacheParameterGroupActionMessage.ResetCacheParameterGroupActionResultMessage;
import com.yammer.metrics.core.Meter;

public class ResetCacheParameterGroupAction
        extends
        AbstractQueuedAction<ResetCacheParameterGroupActionRequestMessage, ResetCacheParameterGroupActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "ResetCacheParameterGroupAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		ResetCacheParameterGroupActionResultMessage message)
            throws ErrorResponse {
        return new ResetCacheParameterGroupActionResultMarshaller()
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
    public ResetCacheParameterGroupActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final ResetCacheParameterGroupActionRequestMessage requestMessage =
                ResetCacheParameterGroupActionRequestUnmarshaller
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
    		ResetCacheParameterGroupActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


