package com.msi.tough.elasticache.query;

/**
 * @author Stepehen Dean
 * 
 * The Elasticache Servlet Implementation
 * 
 */
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.msi.tough.query.Action;

public class ElasticacheQueryImpl
{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Map<String, Action> actionMap;

    /**
     * constructor
     * @param actionMap
     */
    public ElasticacheQueryImpl(final Map<String, Action> actionMap)
    {
        this.actionMap = actionMap;
    }

    /**
     * Process request
     * 
     * @param req
     * @param resp
     * @throws Exception
     */
    public void process(final HttpServletRequest req,
        final HttpServletResponse resp) throws Exception
    {
    	logger.debug("Into process");
        final Action a = this.actionMap.get(req.getParameter("Action"));
        
        logger.debug("calling action " + a);
        a.process(req, resp);
    }
}
