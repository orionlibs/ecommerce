package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class SolrClientConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer aliveCheckInterval;
    private Integer connectionTimeout;
    private Integer socketTimeout;
    private Integer maxConnections;
    private Integer maxConnectionsPerHost;
    private boolean tcpNoDelay;
    private String username;
    private String password;


    public void setAliveCheckInterval(Integer aliveCheckInterval)
    {
        this.aliveCheckInterval = aliveCheckInterval;
    }


    public Integer getAliveCheckInterval()
    {
        return this.aliveCheckInterval;
    }


    public void setConnectionTimeout(Integer connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }


    public Integer getConnectionTimeout()
    {
        return this.connectionTimeout;
    }


    public void setSocketTimeout(Integer socketTimeout)
    {
        this.socketTimeout = socketTimeout;
    }


    public Integer getSocketTimeout()
    {
        return this.socketTimeout;
    }


    public void setMaxConnections(Integer maxConnections)
    {
        this.maxConnections = maxConnections;
    }


    public Integer getMaxConnections()
    {
        return this.maxConnections;
    }


    public void setMaxConnectionsPerHost(Integer maxConnectionsPerHost)
    {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }


    public Integer getMaxConnectionsPerHost()
    {
        return this.maxConnectionsPerHost;
    }


    public void setTcpNoDelay(boolean tcpNoDelay)
    {
        this.tcpNoDelay = tcpNoDelay;
    }


    public boolean isTcpNoDelay()
    {
        return this.tcpNoDelay;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String getUsername()
    {
        return this.username;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getPassword()
    {
        return this.password;
    }
}
