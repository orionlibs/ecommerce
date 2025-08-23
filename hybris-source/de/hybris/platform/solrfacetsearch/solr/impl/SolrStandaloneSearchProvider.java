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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.SolrResponseBase;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.util.Utils;

public class SolrStandaloneSearchProvider extends AbstractSolrSearchProvider
{
    private static final Logger LOG = Logger.getLogger(SolrStandaloneSearchProvider.class);


    public CachedSolrClient getClient(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        return getSolrClientPool().getOrCreate(index, SolrClientType.SEARCH, this::createClient, this::closeClient);
    }


    protected SolrClient createClient(SolrConfig solrConfig)
    {
        validateConfiguration(solrConfig);
        String[] urls = (String[])solrConfig.getEndpointURLs().stream().filter(url -> (!solrConfig.isUseMasterNodeExclusivelyForIndexing() || !url.isMaster())).map(EndpointURL::getUrl).toArray(x$0 -> new String[x$0]);
        SolrClientConfig clientConfig = solrConfig.getClientConfig();
        PatchedLBHttpSolrClient patchedLBHttpSolrClient = ((PatchedLBHttpSolrClient.Builder)((PatchedLBHttpSolrClient.Builder)((PatchedLBHttpSolrClient.Builder)(new PatchedLBHttpSolrClient.Builder()).withBaseSolrUrls(urls).withHttpClient(createHttpClient(clientConfig))).withConnectionTimeout(
                        getIntegerValue(clientConfig.getConnectionTimeout(), 5000))).withSocketTimeout(getIntegerValue(clientConfig.getSocketTimeout(), 8000))).build();
        patchedLBHttpSolrClient.setAliveCheckInterval(getIntegerValue(clientConfig.getAliveCheckInterval(), 5000));
        return (SolrClient)patchedLBHttpSolrClient;
    }


    protected void closeClient(SolrClient solrClient)
    {
        PatchedLBHttpSolrClient lbHttpSolrClient = (PatchedLBHttpSolrClient)solrClient;
        closeHttpClient(lbHttpSolrClient.getHttpClient());
    }


    public CachedSolrClient getClientForIndexing(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        try
        {
            return getSolrClientPool().getOrCreate(index, SolrClientType.INDEXING, this::createClientForIndexing, this::closeClientForIndexing);
        }
        catch(RuntimeException e)
        {
            throw new SolrServiceException(new IllegalArgumentException("Could not create client.", e));
        }
    }


    protected SolrClient createClientForIndexing(SolrConfig solrConfig)
    {
        validateConfiguration(solrConfig);
        String[] urls = (String[])solrConfig.getEndpointURLs().stream().map(EndpointURL::getUrl).toArray(x$0 -> new String[x$0]);
        String defaultUrl = (String)solrConfig.getEndpointURLs().stream().filter(EndpointURL::isMaster).map(EndpointURL::getUrl).findFirst().orElseThrow(() -> new IllegalArgumentException("Missing master endpoint URL in Solr configuration."));
        SolrClientConfig clientConfig = solrConfig.getIndexingClientConfig();
        return (SolrClient)((ClusterSolrClient.Builder)((ClusterSolrClient.Builder)((ClusterSolrClient.Builder)(new ClusterSolrClient.Builder()).withBaseSolrUrls(urls).withDefaultBaseSolrUrl(defaultUrl)
                        .withHttpClient(createHttpClient(clientConfig)))
                        .withConnectionTimeout(getIntegerValue(clientConfig.getConnectionTimeout(), 5000)))
                        .withSocketTimeout(getIntegerValue(clientConfig.getSocketTimeout(), 8000))).build();
    }


    protected void closeClientForIndexing(SolrClient solrClient)
    {
        ClusterSolrClient clusterSolrClient = (ClusterSolrClient)solrClient;
        closeHttpClient(clusterSolrClient.getHttpClient());
    }


    public void createIndex(Index index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        CachedSolrClient solrClient = getClientForIndexing(index);
        try
        {
            List<ClusterNodeResponse<CoreAdminResponse>> statusResponses = doCheckIndexStatus(index, solrClient, null);
            List<String> failedStatusNodes = (List<String>)statusResponses.stream().filter(resp -> !resp.isSuccess()).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(failedStatusNodes))
            {
                throw new SolrServiceException("Could not check index status: index=" + index
                                .getName() + ", nodes=" + failedStatusNodes);
            }
            List<String> createNodes = (List<String>)statusResponses.stream().filter(resp -> !indexExists(index, (CoreAdminResponse)resp.getResponse())).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(createNodes))
            {
                return;
            }
            List<ClusterNodeResponse<CoreAdminResponse>> createResponses = doCreateIndex(index, solrClient, createNodes);
            List<String> failedCreateNodes = (List<String>)createResponses.stream().filter(resp -> !resp.isSuccess()).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(failedCreateNodes))
            {
                doDeleteIndex(index, solrClient, failedCreateNodes);
                throw new SolrServiceException("Could not create index: index=" + index.getName() + ", nodes=" + failedCreateNodes);
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
            List<ClusterNodeResponse<CoreAdminResponse>> statusResponses = doCheckIndexStatus(index, solrClient, null);
            List<String> failedStatusNodes = (List<String>)statusResponses.stream().filter(resp -> !resp.isSuccess()).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(failedStatusNodes))
            {
                throw new SolrServiceException("Could not check index status: index=" + index
                                .getName() + ", nodes=" + failedStatusNodes);
            }
            List<String> deleteNodes = (List<String>)statusResponses.stream().filter(resp -> indexExists(index, (CoreAdminResponse)resp.getResponse())).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(deleteNodes))
            {
                return;
            }
            List<ClusterNodeResponse<CoreAdminResponse>> deleteResponses = doDeleteIndex(index, solrClient, deleteNodes);
            List<String> failedDeleteNodes = (List<String>)deleteResponses.stream().filter(resp -> !resp.isSuccess()).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(failedDeleteNodes))
            {
                throw new SolrServiceException("Could not delete index for nodes: index=" + index
                                .getName() + ", nodes=" + failedDeleteNodes);
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
                ClusterSolrClient clusterSolrClient = (ClusterSolrClient)solrClient.getDelegate();
                for(String node : clusterSolrClient.getNodes())
                {
                    SolrClient clusterNodeSolrClient = clusterSolrClient.getNodeClient((SolrClient)solrClient, node);
                    try
                    {
                        exportConfig(index, clusterNodeSolrClient);
                        if(clusterNodeSolrClient != null)
                        {
                            clusterNodeSolrClient.close();
                        }
                    }
                    catch(Throwable throwable)
                    {
                        if(clusterNodeSolrClient != null)
                        {
                            try
                            {
                                clusterNodeSolrClient.close();
                            }
                            catch(Throwable throwable1)
                            {
                                throwable.addSuppressed(throwable1);
                            }
                        }
                        throw throwable;
                    }
                }
                List<ClusterNodeResponse<CoreAdminResponse>> reloadResponses = doReloadIndex(index, solrClient, null);
                List<String> failedReloadNodes = (List<String>)reloadResponses.stream().filter(resp -> !resp.isSuccess()).map(ClusterNodeResponse::getNode).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(failedReloadNodes))
                {
                    throw new SolrServiceException("Could not reload configuration for index: name=" + index
                                    .getName() + ", nodes=" + failedReloadNodes);
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
        boolean masterSpecified = false;
        for(EndpointURL endpointURL : endpointURLs)
        {
            if(endpointURL.isMaster())
            {
                if(masterSpecified)
                {
                    throw new SolrServiceRuntimeException("Only one endpoint URL can be specified as master.");
                }
                masterSpecified = true;
            }
        }
        if(!masterSpecified)
        {
            throw new SolrServiceRuntimeException("No endpoint URL's specified as master.");
        }
    }


    protected boolean indexExists(Index index, CoreAdminResponse response)
    {
        Object statusIndexName = Utils.getObjectByPath(response.getCoreStatus(), false,
                        Arrays.asList(new String[] {index.getName(), "name"}));
        return Objects.equals(index.getName(), statusIndexName);
    }


    protected List<ClusterNodeResponse<CoreAdminResponse>> doCheckIndexStatus(Index index, CachedSolrClient solrClient, List<String> nodes)
    {
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
        request.setCoreName(index.getName());
        return clusterRequest((SolrRequest<CoreAdminResponse>)request, solrClient, null, nodes);
    }


    protected List<ClusterNodeResponse<CoreAdminResponse>> doCreateIndex(Index index, CachedSolrClient solrClient, List<String> nodes)
    {
        String configSet = resolveConfigSet(index);
        CoreAdminRequest.Create request = new CoreAdminRequest.Create();
        request.setCoreName(index.getName());
        request.setConfigSet(configSet);
        return clusterRequest((SolrRequest<CoreAdminResponse>)request, solrClient, null, nodes);
    }


    protected List<ClusterNodeResponse<CoreAdminResponse>> doDeleteIndex(Index index, CachedSolrClient solrClient, List<String> nodes)
    {
        CoreAdminRequest.Unload request = new CoreAdminRequest.Unload(true);
        request.setDeleteDataDir(true);
        request.setDeleteInstanceDir(true);
        request.setCoreName(index.getName());
        return clusterRequest((SolrRequest<CoreAdminResponse>)request, solrClient, null, nodes);
    }


    protected List<ClusterNodeResponse<CoreAdminResponse>> doReloadIndex(Index index, CachedSolrClient solrClient, List<String> nodes)
    {
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminParams.CoreAdminAction.RELOAD);
        request.setCoreName(index.getName());
        return clusterRequest((SolrRequest<CoreAdminResponse>)request, solrClient, null, nodes);
    }


    protected <T extends SolrResponseBase> List<ClusterNodeResponse<T>> clusterRequest(SolrRequest<T> request, CachedSolrClient solrClient, String collection, List<String> nodes)
    {
        ClusterSolrClient clusterSolrClient = (ClusterSolrClient)solrClient.getDelegate();
        Collection<String> requestNodes = CollectionUtils.isEmpty(nodes) ? clusterSolrClient.getNodes() : nodes;
        List<ClusterNodeResponse<T>> nodeResponses = new ArrayList<>();
        for(String requestNode : requestNodes)
        {
            ClusterNodeResponse<T> nodeResponse = new ClusterNodeResponse();
            nodeResponse.setNode(requestNode);
            try
            {
                SolrClient nodeSolrClient = clusterSolrClient.getNodeClient((SolrClient)solrClient, requestNode);
                try
                {
                    SolrResponseBase solrResponseBase = (SolrResponseBase)request.process(nodeSolrClient, collection);
                    nodeResponse.setResponse((SolrResponse)solrResponseBase);
                    if(solrResponseBase.getStatus() == 0)
                    {
                        nodeResponse.setSuccess(true);
                    }
                    else
                    {
                        nodeResponse.setSuccess(false);
                    }
                    if(nodeSolrClient != null)
                    {
                        nodeSolrClient.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(nodeSolrClient != null)
                    {
                        try
                        {
                            nodeSolrClient.close();
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
                LOG.error(e.getMessage(), e);
                nodeResponse.setSuccess(false);
                nodeResponse.setException(e);
            }
            nodeResponses.add(nodeResponse);
        }
        return nodeResponses;
    }
}
