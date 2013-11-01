/**
 * Transcend Computing, Inc.
 * Confidential and Proprietary
 * Copyright (c) Transcend Computing, Inc. 2013
 * All Rights Reserved.
 */
package com.transcend.elasticache.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;

import com.msi.tough.core.Appctx;
import com.msi.tough.query.AsyncServiceImpl;
import com.msi.tough.query.AsyncServiceImpl.ServiceResponseListener;
import com.msi.tough.query.ErrorResponse;
import com.msi.tough.query.ServiceRequest;
import com.msi.tough.query.ServiceResponse;
import com.msi.tough.servlet.BaseAsyncServlet;

/**
 * Servlet to receive ElastiCache requests.
 */
@WebServlet(
// servlet name
name = "elastiCache-async", displayName = "ElastiCache request servlet",
loadOnStartup = 1,
// servlet url pattern
urlPatterns = { "/*" },
// async support needed
asyncSupported = true)
public class ElastiCacheAsyncServlet extends BaseAsyncServlet
    implements ServiceResponseListener {
    private final Logger logger = Appctx
            .getLogger(ElastiCacheAsyncServlet.class.getName());

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ElastiCacheAsyncServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Initializing elastiCache servlet.");
        super.init(config);
    }

    /**
     * Send the request off for processing.
     */
    protected void enqueLongRunningTask(final ServiceRequest request,
            ServiceResponse response, final AsyncContext ctx) {
        try {
            final AsyncServiceImpl impl = Appctx.getBean("elastiCacheAsync");
            register(impl);
            impl.process(request, response);
        } catch (final ErrorResponse errorResponse) {
            errorResponse.setRequestId(request.getRequestId());
            handleError(errorResponse);
        } catch (final Exception e) {
            e.printStackTrace();
            ErrorResponse error = ErrorResponse.InternalFailure();
            error.setRequestId(request.getRequestId());
        }
    }

}
