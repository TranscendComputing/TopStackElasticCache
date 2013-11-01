package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.ModifyCacheParameterGroupActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.ModifyCacheParameterGroupActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.ModifyCacheParameterGroupActionMessage.ModifyCacheParameterGroupActionRequestMessage;
import com.transcend.elasticache.message.ModifyCacheParameterGroupActionMessage.ModifyCacheParameterGroupActionResultMessage;
import com.yammer.metrics.core.Meter;

public class ModifyCacheParameterGroupAction
        extends
        AbstractQueuedAction<ModifyCacheParameterGroupActionRequestMessage, ModifyCacheParameterGroupActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "ModifyCacheParameterGroupAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		ModifyCacheParameterGroupActionResultMessage message)
            throws ErrorResponse {
        return new ModifyCacheParameterGroupActionResultMarshaller()
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
    public ModifyCacheParameterGroupActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final ModifyCacheParameterGroupActionRequestMessage requestMessage =
                ModifyCacheParameterGroupActionRequestUnmarshaller
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
    		ModifyCacheParameterGroupActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


