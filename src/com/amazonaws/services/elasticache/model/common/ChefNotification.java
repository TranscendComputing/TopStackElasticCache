package com.amazonaws.services.elasticache.model.common;

public class ChefNotification {

	private String nodeName ;
	private String hostName ;
	private String cacheClusterId ;
	
	public ChefNotification() {}
	
	public ChefNotification( String nodeName, String hostName, String cacheClusterId ){
		this() ;
		this.nodeName = nodeName ;
		this.hostName = hostName ;
		this.cacheClusterId = cacheClusterId ;
	}

	public String getNodeName(){ return nodeName ; }

	public String getHostName(){ return hostName ; }

	public String getCacheClusterId(){ return cacheClusterId; }

	public void setNodeName( String nodeName ) { this.nodeName = nodeName; }

	public void setHostName( String hostName ) { this.hostName = hostName ; }

	public void setCacheClusterId( String cacheClusterId ) { this.cacheClusterId = cacheClusterId ; }
}
