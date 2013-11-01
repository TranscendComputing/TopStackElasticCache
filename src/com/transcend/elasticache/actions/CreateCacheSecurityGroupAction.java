package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.CreateCacheSecurityGroupActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.CreateCacheSecurityGroupActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionRequestMessage;
import com.transcend.elasticache.message.CreateCacheSecurityGroupActionMessage.CreateCacheSecurityGroupActionResultMessage;
import com.yammer.metrics.core.Meter;

public class CreateCacheSecurityGroupAction
        extends
        AbstractQueuedAction<CreateCacheSecurityGroupActionRequestMessage, CreateCacheSecurityGroupActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "CreateCacheSecurityGroupAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		CreateCacheSecurityGroupActionResultMessage message)
            throws ErrorResponse {
        return new CreateCacheSecurityGroupActionResultMarshaller()
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
    public CreateCacheSecurityGroupActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final CreateCacheSecurityGroupActionRequestMessage requestMessage =
                CreateCacheSecurityGroupActionRequestUnmarshaller
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
    		CreateCacheSecurityGroupActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


