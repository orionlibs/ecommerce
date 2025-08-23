package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrClientType;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceRuntimeException;
import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.SolrResponseBase;

public class SolrCloudSearchProvider extends AbstractSolrSearchProvider
{
    private static final Logger LOG = Logger.getLogger(SolrCloudSearchProvider.class);
    protected static final int CREATE_INDEX_MAX_RETRIES = 3;
    protected static final String NUMBER_OF_SHARDS_PARAM = "solr.collection.numShards";
    protected static final int DEFAULT_NUMBER_OF_SHARDS = 1;
    protected static final String REPLICATION_FACTOR_PARAM = "solr.collection.replicationFactor";
    protected static final int DEFAULT_REPLICATION_FACTOR = 1;
    protected static final String AUTO_ADD_REPLICAS_PARAM = "solr.collection.autoAddReplicas";


    public CachedSolrClient getClient(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        return getSolrClientPool().getOrCreate(index, SolrClientType.SEARCH, solrConfig -> createClient(solrConfig, solrConfig.getClientConfig()), this::closeClient);
    }


    public CachedSolrClient getClientForIndexing(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        return getSolrClientPool().getOrCreate(index, SolrClientType.INDEXING, solrConfig -> createClient(solrConfig, solrConfig.getIndexingClientConfig()), this::closeClient);
    }


    protected CloudSolrClient createClient(SolrConfig solrConfig, SolrClientConfig solrClientConfig)
    {
        validateConfiguration(solrConfig);
        List<String> zkHosts = (List<String>)solrConfig.getEndpointURLs().stream().map(EndpointURL::getUrl).collect(Collectors.toList());
        CloudSolrClient.Builder cloudClientBuilder = ((CloudSolrClient.Builder)((CloudSolrClient.Builder)((CloudSolrClient.Builder)(new CloudSolrClient.Builder(zkHosts, Optional.empty())).withHttpClient(createHttpClient(solrClientConfig))).withConnectionTimeout(
                        getIntegerValue(solrClientConfig.getConnectionTimeout(), 5000))).withSocketTimeout(getIntegerValue(solrClientConfig.getSocketTimeout(), 8000))).sendUpdatesOnlyToShardLeaders();
        CloudSolrClient solrClient = cloudClientBuilder.build();
        LBHttpSolrClient lbHttpSolrClient = solrClient.getLbClient();
        lbHttpSolrClient
                        .setAliveCheckInterval(getIntegerValue(solrClientConfig.getAliveCheckInterval(), 5000));
        solrClient.connect();
        return solrClient;
    }


    protected void closeClient(SolrClient solrClient)
    {
        CloudSolrClient cloudSolrClient = (CloudSolrClient)solrClient;
        closeHttpClient(cloudSolrClient.getHttpClient());
    }


    public void createIndex(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        CachedSolrClient solrClient = getClientForIndexing(index);
        try
        {
            CloudResponse<CollectionAdminResponse> listResponse = doListIndexes(solrClient);
            if(!listResponse.isSuccess())
            {
                throw new SolrServiceException("Could not check index status: name=" + index.getName(), listResponse.getException());
            }
            if(indexExists(index, (CollectionAdminResponse)listResponse.getResponse()))
            {
                return;
            }
            for(int i = 0; i <= 3; i++)
            {
                CloudResponse<CollectionAdminResponse> createResponse = doCreateIndex(index, solrClient);
                if(createResponse.isSuccess())
                {
                    return;
                }
                if(i == 3)
                {
                    throw new SolrServiceException("Could not create index: name=" + index.getName(), createResponse.getException());
                }
                LOG.error("Create index failed, retry: name=" + index.getName() + ", retry=" + i + 1);
            }
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void deleteIndex(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        CachedSolrClient solrClient = getClientForIndexing(index);
        try
        {
            CloudResponse<CollectionAdminResponse> listResponse = doListIndexes(solrClient);
            if(!listResponse.isSuccess())
            {
                throw new SolrServiceException("Could not check index status: name=" + index.getName(), listResponse.getException());
            }
            if(!indexExists(index, (CollectionAdminResponse)listResponse.getResponse()))
            {
                return;
            }
            CloudResponse<CollectionAdminResponse> deleteResponse = doDeleteIndex(index, solrClient);
            if(!deleteResponse.isSuccess())
            {
                throw new SolrServiceException("Could not create index: name=" + index.getName(), deleteResponse.getException());
            }
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void exportConfig(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        try
        {
            CachedSolrClient solrClient = getClientForIndexing(index);
            try
            {
                exportConfig(index, (SolrClient)solrClient);
                CloudResponse<CollectionAdminResponse> reloadResponse = doReloadIndex(index, solrClient);
                if(!reloadResponse.isSuccess())
                {
                    throw new SolrServiceException("Could not reload configuration for index: name=" + index.getName(), reloadResponse
                                    .getException());
                }
                if(solrClient != null)
                {
                    solrClient.close();
                }
            }
            catch(Throwable throwable)
            {
                if(solrClient != null)
                {
                    try
                    {
                        solrClient.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            throw new SolrServiceException("Could not export configuration for index: name=" + index.getName(), e);
        }
    }


    protected void validateConfiguration(SolrConfig solrConfig)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configModel", solrConfig);
        List<EndpointURL> endpointURLs = solrConfig.getEndpointURLs();
        if(CollectionUtils.isEmpty(endpointURLs))
        {
            throw new SolrServiceRuntimeException("No endpoint URL's defined in solr configuration.");
        }
    }


    protected boolean indexExists(Index index, CollectionAdminResponse response)
    {
        List<String> collections = (List<String>)response.getResponse().get("collections");
        return collections.contains(index.getName());
    }


    protected CloudResponse<CollectionAdminResponse> doListIndexes(CachedSolrClient solrClient)
    {
        CollectionAdminRequest.List request = new CollectionAdminRequest.List();
        return cloudRequest((SolrRequest<CollectionAdminResponse>)request, solrClient, null);
    }


    protected CloudResponse<CollectionAdminResponse> doCreateIndex(Index index, CachedSolrClient solrClient)
    {
        String configSet = resolveConfigSet(index);
        Integer numShards = resolveNumShards(index);
        Integer replicationFactor = resolveReplicationFactor(index);
        Boolean autoAddReplicas = resolveAutoAddReplicas(index);
        CollectionAdminRequest.Create request = CollectionAdminRequest.createCollection(index.getName(), configSet, numShards.intValue(), replicationFactor
                        .intValue());
        request.setAutoAddReplicas(autoAddReplicas.booleanValue());
        request.setWaitForFinalState(true);
        return cloudRequest((SolrRequest<CollectionAdminResponse>)request, solrClient, null);
    }


    protected CloudResponse<CollectionAdminResponse> doDeleteIndex(Index index, CachedSolrClient solrClient)
    {
        CollectionAdminRequest.Delete request = CollectionAdminRequest.deleteCollection(index.getName());
        return cloudRequest((SolrRequest<CollectionAdminResponse>)request, solrClient, null);
    }


    protected CloudResponse<CollectionAdminResponse> doReloadIndex(Index index, CachedSolrClient solrClient)
    {
        CollectionAdminRequest.Reload request = CollectionAdminRequest.reloadCollection(index.getName());
        return cloudRequest((SolrRequest<CollectionAdminResponse>)request, solrClient, null);
    }


    protected <T extends SolrResponseBase> CloudResponse<T> cloudRequest(SolrRequest<T> request, CachedSolrClient solrClient, String collection)
    {
        CloudResponse<T> cloudResponse = new CloudResponse();
        try
        {
            SolrResponseBase solrResponseBase = (SolrResponseBase)request.process((SolrClient)solrClient, collection);
            cloudResponse.setResponse((SolrResponse)solrResponseBase);
            if(solrResponseBase.getStatus() == 0)
            {
                cloudResponse.setSuccess(true);
            }
            else
            {
                cloudResponse.setSuccess(false);
            }
        }
        catch(Exception e)
        {
            LOG.error(e);
            cloudResponse.setSuccess(false);
            cloudResponse.setException(e);
        }
        return cloudResponse;
    }


    protected Integer resolveNumShards(Index index)
    {
        Map<String, String> additionalParameters = index.getIndexedType().getAdditionalParameters();
        String numShardsParam = additionalParameters.get("solr.collection.numShards");
        if(StringUtils.isNotBlank(numShardsParam))
        {
            try
            {
                return Integer.valueOf(Integer.parseInt(numShardsParam));
            }
            catch(NumberFormatException e)
            {
                LOG.error("Additional parameter 'number of shards' is not a number");
            }
        }
        SolrConfig config = index.getFacetSearchConfig().getSolrConfig();
        Integer numShards = Integer.valueOf((config.getNumShards() == null) ? 1 : config.getNumShards().intValue());
        LOG.debug("Number of shards " + numShards);
        return numShards;
    }


    protected Integer resolveReplicationFactor(Index index)
    {
        Map<String, String> additionalParameters = index.getIndexedType().getAdditionalParameters();
        String replicationFactorParam = additionalParameters.get("solr.collection.replicationFactor");
        if(StringUtils.isNotBlank(replicationFactorParam))
        {
            try
            {
                return Integer.valueOf(Integer.parseInt(replicationFactorParam));
            }
            catch(NumberFormatException e)
            {
                LOG.error("Additional parameter 'replication factor' is not a number");
            }
        }
        SolrConfig config = index.getFacetSearchConfig().getSolrConfig();
        Integer replicationFactor = Integer.valueOf((config.getNumShards() == null) ? 1 :
                        config.getReplicationFactor().intValue());
        LOG.debug("Replication factor " + replicationFactor);
        return replicationFactor;
    }


    protected Boolean resolveAutoAddReplicas(Index index)
    {
        Map<String, String> additionalParameters = index.getIndexedType().getAdditionalParameters();
        String autoAddReplicasParam = additionalParameters.get("solr.collection.autoAddReplicas");
        if(StringUtils.isNotBlank(autoAddReplicasParam))
        {
            return Boolean.valueOf(autoAddReplicasParam);
        }
        SolrConfig config = index.getFacetSearchConfig().getSolrConfig();
        Boolean autoAddReplicas = Boolean.valueOf(config.isAutoAddReplicas());
        LOG.debug("Automatically add replicas: " + autoAddReplicas);
        return autoAddReplicas;
    }
}
