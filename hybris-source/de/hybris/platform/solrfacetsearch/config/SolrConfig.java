package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SolrConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private SolrServerMode mode;
    private List<EndpointURL> endpointURLs;
    private boolean useMasterNodeExclusivelyForIndexing;
    private Integer numShards;
    private Integer replicationFactor;
    private boolean autoAddReplicas;
    private SolrClientConfig clientConfig;
    private SolrClientConfig indexingClientConfig;
    private QueryMethod queryMethod;
    private Date modifiedTime;
    private String version;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setMode(SolrServerMode mode)
    {
        this.mode = mode;
    }


    public SolrServerMode getMode()
    {
        return this.mode;
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


    public void setAutoAddReplicas(boolean autoAddReplicas)
    {
        this.autoAddReplicas = autoAddReplicas;
    }


    public boolean isAutoAddReplicas()
    {
        return this.autoAddReplicas;
    }


    public void setClientConfig(SolrClientConfig clientConfig)
    {
        this.clientConfig = clientConfig;
    }


    public SolrClientConfig getClientConfig()
    {
        return this.clientConfig;
    }


    public void setIndexingClientConfig(SolrClientConfig indexingClientConfig)
    {
        this.indexingClientConfig = indexingClientConfig;
    }


    public SolrClientConfig getIndexingClientConfig()
    {
        return this.indexingClientConfig;
    }


    public void setQueryMethod(QueryMethod queryMethod)
    {
        this.queryMethod = queryMethod;
    }


    public QueryMethod getQueryMethod()
    {
        return this.queryMethod;
    }


    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }


    public Date getModifiedTime()
    {
        return this.modifiedTime;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return this.version;
    }
}
