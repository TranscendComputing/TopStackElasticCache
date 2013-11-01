package com.transcend.elasticache.actions;
import java.util.Map;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheClustersActionRequestUnmarshaller;
import com.amazonaws.services.elasticache.model.transform.DescribeCacheClustersActionResultMarshaller;
import com.msi.tough.query.AbstractQueuedAction;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceRequestContext;
import com.msi.tough.query.ServiceResponse;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionRequestMessage;
import com.transcend.elasticache.message.DescribeCacheClustersActionMessage.DescribeCacheClustersActionResultMessage;
import com.yammer.metrics.core.Meter;

public class DescribeCacheClustersAction
        extends
        AbstractQueuedAction<DescribeCacheClustersActionRequestMessage, DescribeCacheClustersActionResultMessage> {

    private static Map<String, Meter> meters = initMeter("elasticache",
            "DescribeCacheClustersAction");

    @Override
    protected void mark(Object ret, Exception e) {
        markStandard(meters, e);
    }

    public String marshall(ServiceResponse resp,
    		DescribeCacheClustersActionResultMessage message)
            throws ErrorResponse {
        return new DescribeCacheClustersActionResultMarshaller()
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
    public DescribeCacheClustersActionRequestMessage handleRequest(
            ServiceRequest req, ServiceRequestContext context)
            throws ErrorResponse {
        final DescribeCacheClustersActionRequestMessage requestMessage =
                DescribeCacheClustersActionRequestUnmarshaller
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
    		DescribeCacheClustersActionResultMessage message) {
        resp.setPayload(marshall(resp, message));
        return resp;
    }
}


