package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheSecurityGroupsActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheSecurityGroupsActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheSecurityGroupsActionMessage.DescribeCacheSecurityGroupsActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DescribeCacheSecurityGroupsAction
        extends
        AbstractQueuedAction<DescribeCacheSecurityGroupsActionRequestMessage, DescribeCacheSecurityGroupsActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DescribeCacheSecurityGroupsAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DescribeCacheSecurityGroupsActionResultMessage message)
            throws ErrorResponse {
        return new DescribeCacheSecurityGroupsActionResultMarshaller()
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
    public DescribeCacheSecurityGroupsActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DescribeCacheSecurityGroupsActionRequestMessage requestMessage =
                DescribeCacheSecurityGroupsActionRequestUnmarshaller
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
    		DescribeCacheSecurityGroupsActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


