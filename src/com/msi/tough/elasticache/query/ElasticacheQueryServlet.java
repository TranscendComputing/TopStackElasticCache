package com.msi.tough.elasticache.query;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.msi.tough.core.Appctx;

public class ElasticacheQueryServlet extends HttpServlet
{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	
	/**
	 * Process the GET request
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletRespinse
	 */
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException, IOException {
 
		try {
			
			logger.debug("Into doGet");
			final ElasticacheQueryImpl impl = Appctx.getBean("elastiCacheQuery");
			impl.process(req, resp);
			
		} catch (final Exception e){
			logger.debug("doGet Error Class = " + e.getClass() 
				+ " Msg = " + e.getMessage());
			e.printStackTrace();
		}
	}
 
	/**
	 * Process doPost request
	 * 	 
	 * @param HttpServletRequest
	 * @param HttpServletRespinse
	 */
	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException, IOException {
 
		try {
			
			logger.debug("Into doPost");
			final ElasticacheQueryImpl impl = Appctx.getBean("elastiCacheQuery");
			impl.process(req, resp);
		
		} catch (final Exception e){
			
			logger.debug("doPost Error Class = " + e.getClass() 
					+ " Msg = " + e.getMessage());
			e.printStackTrace();
		} 
	}
}
