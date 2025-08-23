package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.List;

public class ClusterConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer aliveCheckInterval;
    private Integer connectionTimeout;
    private Integer socketTimeout;
    private Integer maxConnections;
    private Integer maxConnectionsPerHost;
    private boolean tcpNoDelay;
    private List<EndpointURL> endpointURLs;
    private boolean useMasterNodeExclusivelyForIndexing;
    private Integer numShards;
    private Integer replicationFactor;


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


    public void setEndpointURLs(List<EndpointURL> endpointURLs)
    {
        this.endpointURLs = endpointURLs;
    }


    public List<EndpointURL> getEndpointURLs()
    {
        return this.endpointURLs;
    }


    public void setUseMasterNodeExclusivelyForIndexing(boolean useMasterNodeExclusivelyForIndexing)
    {
        this.useMasterNodeExclusivelyForIndexing = useMasterNodeExclusivelyForIndexing;
    }


    public boolean isUseMasterNodeExclusivelyForIndexing()
    {
        return this.useMasterNodeExclusivelyForIndexing;
    }


    public void setNumShards(Integer numShards)
    {
        this.numShards = numShards;
    }


    public Integer getNumShards()
    {
        return this.numShards;
    }


    public void setReplicationFactor(Integer replicationFactor)
    {
        this.replicationFactor = replicationFactor;
    }


    public Integer getReplicationFactor()
    {
        return this.replicationFactor;
    }
}
