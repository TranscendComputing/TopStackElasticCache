package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DeleteCacheSecurityGroupActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DeleteCacheSecurityGroupActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionRequestMessage;
import com.transcend.elasticache.message.DeleteCacheSecurityGroupActionMessage.DeleteCacheSecurityGroupActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DeleteCacheSecurityGroupAction
        extends
        AbstractQueuedAction<DeleteCacheSecurityGroupActionRequestMessage, DeleteCacheSecurityGroupActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DeleteCacheSecurityGroupAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DeleteCacheSecurityGroupActionResultMessage message)
            throws ErrorResponse {
        return new DeleteCacheSecurityGroupActionResultMarshaller()
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
    public DeleteCacheSecurityGroupActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DeleteCacheSecurityGroupActionRequestMessage requestMessage =
                DeleteCacheSecurityGroupActionRequestUnmarshaller
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
    		DeleteCacheSecurityGroupActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


