package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheParametersActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheParametersActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DescribeCacheParametersAction
        extends
        AbstractQueuedAction<DescribeCacheParametersActionRequestMessage, DescribeCacheParametersActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DescribeCacheParametersAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DescribeCacheParametersActionResultMessage message)
            throws ErrorResponse {
        return new DescribeCacheParametersActionResultMarshaller()
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
    public DescribeCacheParametersActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DescribeCacheParametersActionRequestMessage requestMessage =
                DescribeCacheParametersActionRequestUnmarshaller
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
    		DescribeCacheParametersActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


