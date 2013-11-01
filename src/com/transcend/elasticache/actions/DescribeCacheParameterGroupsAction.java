package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheParameterGroupsActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheParameterGroupsActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheParameterGroupsActionMessage.DescribeCacheParameterGroupsActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DescribeCacheParameterGroupsAction
        extends
        AbstractQueuedAction<DescribeCacheParameterGroupsActionRequestMessage, DescribeCacheParameterGroupsActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DescribeCacheParameterGroupsAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DescribeCacheParameterGroupsActionResultMessage message)
            throws ErrorResponse {
        return new DescribeCacheParameterGroupsActionResultMarshaller()
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
    public DescribeCacheParameterGroupsActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DescribeCacheParameterGroupsActionRequestMessage requestMessage =
                DescribeCacheParameterGroupsActionRequestUnmarshaller
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
    		DescribeCacheParameterGroupsActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


