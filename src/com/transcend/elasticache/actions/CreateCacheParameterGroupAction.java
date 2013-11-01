package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.CreateCacheParameterGroupActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.CreateCacheParameterGroupActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheParameterGroupActionMessage.CreateCacheParameterGroupActionResultMessage;
import com.yammer.metrics.core.Meter;

public class CreateCacheParameterGroupAction
        extends
        AbstractQueuedAction<CreateCacheParameterGroupActionRequestMessage, CreateCacheParameterGroupActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "CreateCacheParameterGroupAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		CreateCacheParameterGroupActionResultMessage message)
            throws ErrorResponse {
        return new CreateCacheParameterGroupActionResultMarshaller()
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
    public CreateCacheParameterGroupActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final CreateCacheParameterGroupActionRequestMessage requestMessage =
                CreateCacheParameterGroupActionRequestUnmarshaller
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
    		CreateCacheParameterGroupActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


