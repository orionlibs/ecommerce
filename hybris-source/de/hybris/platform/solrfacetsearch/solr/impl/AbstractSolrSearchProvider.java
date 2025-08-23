package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrStopWordModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.IndexNameResolver;
import de.hybris.platform.solrfacetsearch.solr.SolrClientPool;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSolrSearchProvider implements SolrSearchProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSolrSearchProvider.class);
    protected static final String INDEX_PARAM = "index";
    protected static final int DEFAULT_ALIVE_CHECK_INTERVAL = 5000;
    protected static final int DEFAULT_MAX_CONNECTIONS = 100;
    protected static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 50;
    protected static final int DEFAULT_SOCKET_TIMEOUT = 8000;
    protected static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    protected static final String DEFAULT_CONFIGSET_PROPERTY = "solrfacetsearch.configsets.default";
    protected static final String DEFAULT_CONFIGSET_VALUE = "default";
    protected static final String SYNONYM_SPLIT_CHAR = ",";
    protected static final String PERCENT_CHAR = "%";
    protected static final String MANAGED_INIT_ARGS_FIELD = "initArgs";
    protected static final String MANAGED_IGNORE_CASE_FIELD = "ignoreCase";
    protected static final String MANAGED_LIST_FIELD = "managedList";
    protected static final String MANAGED_MAP_FIELD = "managedMap";
    protected static final String MANAGED_RESOURCES_PATH = "/schema/managed";
    protected static final String MANAGED_RESOURCES_ROOT_FIELD = "managedResources";
    protected static final String MANAGED_SYNONYMS_IGNORE_CASE_KEY = "solrfacetsearch.synonyms.filter.ignoreCase";
    protected static final String MANAGED_SYNONYMS_TYPE = "org.apache.solr.rest.schema.analysis.ManagedSynonymFilterFactory$SynonymManager";
    protected static final String MANAGED_SYNONYMS_PATH = "/schema/analysis/synonyms/{0}";
    protected static final String MANAGED_SYNONYMS_ROOT_FIELD = "synonymMappings";
    protected static final String MANAGED_STOP_WORDS_IGNORE_CASE_KEY = "solrfacetsearch.stopwords.filter.ignoreCase";
    protected static final String MANAGED_STOP_WORDS_TYPE = "org.apache.solr.rest.schema.analysis.ManagedWordSetResource";
    protected static final String MANAGED_STOP_WORDS_PATH = "/schema/analysis/stopwords/{0}";
    protected static final String MANAGED_STOP_WORDS_ROOT_FIELD = "wordSet";
    protected static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    protected static final String UTF8_ENCODING = "UTF-8";
    protected static final String SOLR_QUERY_SELECT_ALL = "*:*";
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private IndexNameResolver indexNameResolver;
    private ConfigurationService configurationService;
    private SolrClientPool solrClientPool;


    public Index resolveIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String qualifier)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", indexedType);
        String indexName = this.indexNameResolver.resolve(facetSearchConfig, indexedType, qualifier);
        DefaultIndex index = new DefaultIndex();
        index.setName(indexName);
        index.setFacetSearchConfig(facetSearchConfig);
        index.setIndexedType(indexedType);
        index.setQualifier(qualifier);
        return (Index)index;
    }


    public void deleteAllDocuments(Index index) throws SolrServiceException
    {
        SolrClient solrClient = getClientForIndexing(index);
        try
        {
            solrClient.deleteByQuery(index.getName(), "*:*");
        }
        catch(SolrServerException | IOException exception)
        {
            throw new SolrServiceException(exception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void deleteOldDocuments(Index index, long indexOperationId) throws SolrServiceException
    {
        SolrClient solrClient = getClientForIndexing(index);
        try
        {
            String query = "-indexOperationId:[" + indexOperationId + " TO *]";
            solrClient.deleteByQuery(index.getName(), query);
        }
        catch(SolrServerException | IOException exception)
        {
            throw new SolrServiceException(exception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void deleteDocumentsByPk(Index index, Collection<PK> pks) throws SolrServiceException
    {
        SolrClient solrClient = getClientForIndexing(index);
        try
        {
            String query = "pk:(" + (String)pks.stream().map(PK::getLongValueAsString).collect(Collectors.joining(" OR ")) + ")";
            solrClient.deleteByQuery(index.getName(), query);
        }
        catch(SolrServerException | IOException exception)
        {
            throw new SolrServiceException(exception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void commit(Index index, SolrSearchProvider.CommitType commitType) throws SolrServiceException
    {
        SolrClient solrClient = getClientForIndexing(index);
        try
        {
            switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$solr$SolrSearchProvider$CommitType[commitType.ordinal()])
            {
                case 1:
                    solrClient.commit(index.getName(), true, true, false);
                    break;
                case 2:
                    solrClient.commit(index.getName(), false, false, true);
                    break;
            }
        }
        catch(SolrServerException | IOException e)
        {
            throw new SolrServiceException(e);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void optimize(Index index) throws SolrServiceException
    {
        SolrClient solrClient = getClientForIndexing(index);
        try
        {
            solrClient.optimize(index.getName(), false, false);
        }
        catch(SolrServerException | IOException e)
        {
            throw new SolrServiceException(e);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    protected String resolveConfigSet(Index index)
    {
        String configSet = index.getIndexedType().getConfigSet();
        if(StringUtils.isBlank(configSet))
        {
            configSet = getConfigurationService().getConfiguration().getString("solrfacetsearch.configsets.default", "default");
        }
        return configSet;
    }


    protected int getIntegerValue(Integer bigInt, int defaultValue)
    {
        return (bigInt == null) ? defaultValue : bigInt.intValue();
    }


    protected HttpClient createHttpClient(SolrClientConfig solrClientConfig)
    {
        ModifiableSolrParams clientParams = new ModifiableSolrParams();
        clientParams.set("maxConnectionsPerHost",
                        getIntegerValue(solrClientConfig.getMaxConnectionsPerHost(), 50));
        clientParams.set("maxConnections",
                        getIntegerValue(solrClientConfig.getMaxConnections(), 100));
        return (HttpClient)HttpClientUtil.createClient((SolrParams)clientParams);
    }


    protected void closeHttpClient(HttpClient httpClient)
    {
        HttpClientUtil.close(httpClient);
    }


    protected void exportConfig(Index index, SolrClient solrClient) throws SolrServiceException, SolrServerException, IOException
    {
        Map<String, ManagedResource> managedResources = loadManagedResourcesFromServer(index, solrClient);
        List<String> languages = collectLanguages(index.getFacetSearchConfig());
        exportSynonyms(index, solrClient, managedResources, languages);
        exportStopWords(index, solrClient, managedResources, languages);
    }


    protected Map<String, ManagedResource> loadManagedResourcesFromServer(Index index, SolrClient solrClient) throws SolrServerException, IOException
    {
        NamedList<Object> response = executeGet(solrClient, index.getName(), "/schema/managed");
        Map<String, ManagedResource> managedResources = new HashMap<>();
        List<Map<String, String>> respManagedResources = (List<Map<String, String>>)response.get("managedResources");
        if(respManagedResources != null)
        {
            for(Map<String, String> respManagedResource : respManagedResources)
            {
                ManagedResource managedResource = new ManagedResource();
                managedResource.setResourceId(respManagedResource.get("resourceId"));
                managedResource.setType(respManagedResource.get("class"));
                managedResources.put(managedResource.getResourceId(), managedResource);
            }
        }
        return managedResources;
    }


    protected List<String> collectLanguages(FacetSearchConfig configuration)
    {
        return (List<String>)configuration.getIndexConfig().getLanguages().stream().map(C2LItemModel::getIsocode).collect(Collectors.toList());
    }


    protected void exportSynonyms(Index index, SolrClient solrClient, Map<String, ManagedResource> managedResources, List<String> languages) throws IOException, SolrServiceException
    {
        try
        {
            Map<String, Map<String, Set<String>>> configurationSynonyms = loadSynonymsFromConfiguration(index
                            .getFacetSearchConfig(), languages);
            for(String language : languages)
            {
                String managedResourcePath = MessageFormat.format("/schema/analysis/synonyms/{0}", new Object[] {encode(language.toLowerCase(Locale.ROOT))});
                if(!managedResources.containsKey(managedResourcePath))
                {
                    LOG.info("Skipping sysnonyms export (no mapped field): index={}, language={}", index.getName(), language);
                    continue;
                }
                Map<String, Set<String>> synonyms = MapUtils.emptyIfNull(configurationSynonyms.get(language));
                Map<String, Set<String>> serverSynonyms = MapUtils.emptyIfNull(loadSynonymsFromServer(solrClient, index.getName(), managedResourcePath));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Synonyms: index={}, language={}", index.getName(), language);
                    LOG.debug("- from configuration: {}", synonyms);
                    LOG.debug("- from server: {}", serverSynonyms);
                }
                updateSynonymsOnServer(solrClient, index.getName(), managedResourcePath, synonyms, serverSynonyms);
            }
        }
        catch(Exception e)
        {
            throw new SolrServiceException(e);
        }
    }


    protected Map<String, Map<String, Set<String>>> loadSynonymsFromConfiguration(FacetSearchConfig facetSearchConfig, Collection<String> languages)
    {
        Map<String, Map<String, Set<String>>> synonyms = new HashMap<>();
        for(String language : languages)
        {
            synonyms.put(language, new HashMap<>());
        }
        SolrFacetSearchConfigModel solrFacetSearchConfigModel = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(facetSearchConfig.getName());
        for(SolrSynonymConfigModel solrSynonymConfigModel : solrFacetSearchConfigModel.getSynonyms())
        {
            String key = solrSynonymConfigModel.getLanguage().getIsocode();
            Map<String, Set<String>> values = synonyms.get(key);
            if(values != null)
            {
                buildSynonyms(values, solrSynonymConfigModel.getSynonymFrom(), solrSynonymConfigModel.getSynonymTo());
            }
        }
        return synonyms;
    }


    protected void buildSynonyms(Map<String, Set<String>> synonyms, String from, String to)
    {
        List<String> synonymsFrom = null;
        List<String> synonymsTo = null;
        if(StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to))
        {
            synonymsFrom = expandSynonyms(from);
            synonymsTo = expandSynonyms(to);
        }
        else if(StringUtils.isNotBlank(from))
        {
            synonymsFrom = expandSynonyms(from);
            synonymsTo = synonymsFrom;
        }
        if(CollectionUtils.isNotEmpty(synonymsFrom) && CollectionUtils.isNotEmpty(synonymsTo))
        {
            for(String synonymFrom : synonymsFrom)
            {
                Set<String> values = synonyms.get(synonymFrom);
                if(values == null)
                {
                    values = new HashSet<>();
                    synonyms.put(synonymFrom, values);
                }
                values.addAll(synonymsTo);
            }
        }
    }


    protected List<String> expandSynonyms(String value)
    {
        if(value == null)
        {
            return Collections.emptyList();
        }
        return (List<String>)Arrays.<String>stream(value.split(",")).map(String::trim).filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
    }


    protected Map<String, Set<String>> loadSynonymsFromServer(SolrClient solrClient, String indexName, String managedResourcePath) throws SolrServerException, IOException
    {
        NamedList<Object> response = executeGet(solrClient, indexName, managedResourcePath);
        Map<String, Set<String>> respSynonyms = (Map<String, Set<String>>)Utils.getObjectByPath(response, false,
                        Arrays.asList(new String[] {"synonymMappings", "managedMap"}));
        if(MapUtils.isEmpty(respSynonyms))
        {
            return Collections.emptyMap();
        }
        return new HashMap<>(respSynonyms);
    }


    protected void updateSynonymsOnServer(SolrClient solrClient, String indexName, String managedResourcePath, Map<String, Set<String>> synonyms, Map<String, Set<String>> serverSynonyms) throws SolrServiceException, SolrServerException, IOException
    {
        boolean ignoreCase = this.configurationService.getConfiguration().getBoolean("solrfacetsearch.synonyms.filter.ignoreCase", true);
        Set<String> synonymsToRemove = new HashSet<>(serverSynonyms.keySet());
        synonymsToRemove.removeAll(synonyms.keySet());
        if(ignoreCase)
        {
            synonymsToRemove = (Set<String>)synonymsToRemove.stream().map(synonym -> synonym.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
        }
        for(String synonym : synonymsToRemove)
        {
            executeDelete(solrClient, indexName, managedResourcePath + "/" + managedResourcePath);
        }
        Map<String, Object> requestData = new LinkedHashMap<>();
        requestData.put("initArgs", Collections.singletonMap("ignoreCase", Boolean.toString(ignoreCase)));
        requestData.put("managedMap", synonyms);
        executePost(solrClient, indexName, managedResourcePath, requestData);
    }


    protected void exportStopWords(Index index, SolrClient solrClient, Map<String, ManagedResource> managedResources, List<String> languages) throws IOException, SolrServiceException
    {
        try
        {
            Map<String, Set<String>> configurationStopWords = loadStopWordsFromConfiguration(index.getFacetSearchConfig(), languages);
            for(String language : languages)
            {
                String managedResourcePath = MessageFormat.format("/schema/analysis/stopwords/{0}", new Object[] {encode(language.toLowerCase(Locale.ROOT))});
                if(!managedResources.containsKey(managedResourcePath))
                {
                    LOG.info("Skipping stopwords export (no mapped field): index={}, language={}", index.getName(), language);
                    continue;
                }
                Set<String> stopWords = SetUtils.emptyIfNull(configurationStopWords.get(language));
                Set<String> serverStopWords = SetUtils.emptyIfNull(loadStopWordsFromServer(solrClient, index.getName(), managedResourcePath));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Stop words: index={}, language={}", index.getName(), language);
                    LOG.debug("- from configuration: {}", stopWords);
                    LOG.debug("- from server: {}", serverStopWords);
                }
                updateStopWordsOnServer(solrClient, index.getName(), managedResourcePath, stopWords, serverStopWords);
            }
        }
        catch(Exception e)
        {
            throw new SolrServiceException(e);
        }
    }


    protected Map<String, Set<String>> loadStopWordsFromConfiguration(FacetSearchConfig facetSearchConfig, Collection<String> languages)
    {
        Map<String, Set<String>> stopWords = new HashMap<>();
        for(String language : languages)
        {
            stopWords.put(language, new HashSet<>());
        }
        SolrFacetSearchConfigModel solrFacetSearchConfigModel = getSolrFacetSearchConfigDao().findFacetSearchConfigByName(facetSearchConfig.getName());
        for(SolrStopWordModel solrStopWordModel : solrFacetSearchConfigModel.getStopWords())
        {
            String key = solrStopWordModel.getLanguage().getIsocode();
            Set<String> values = stopWords.get(key);
            if(values != null)
            {
                values.add(solrStopWordModel.getStopWord());
            }
        }
        return stopWords;
    }


    protected Set<String> loadStopWordsFromServer(SolrClient solrClient, String indexName, String managedResourcePath) throws SolrServerException, IOException
    {
        NamedList<Object> response = executeGet(solrClient, indexName, managedResourcePath);
        List<String> respStopWords = (List<String>)Utils.getObjectByPath(response, false,
                        Arrays.asList(new String[] {"wordSet", "managedList"}));
        if(CollectionUtils.isEmpty(respStopWords))
        {
            return Collections.emptySet();
        }
        return new HashSet<>(respStopWords);
    }


    protected void updateStopWordsOnServer(SolrClient solrClient, String indexName, String managedResourcePath, Set<String> stopWords, Set<String> serverStopWords) throws SolrServiceException, SolrServerException, IOException
    {
        Set<String> stopWordsToRemove = new HashSet<>(serverStopWords);
        stopWordsToRemove.removeAll(stopWords);
        for(String stopWord : stopWordsToRemove)
        {
            executeDelete(solrClient, indexName, managedResourcePath + "/" + managedResourcePath);
        }
        boolean ignoreCase = this.configurationService.getConfiguration().getBoolean("solrfacetsearch.stopwords.filter.ignoreCase", true);
        Map<String, Object> requestData = new LinkedHashMap<>();
        requestData.put("initArgs", Collections.singletonMap("ignoreCase", Boolean.toString(ignoreCase)));
        requestData.put("managedList", stopWords);
        executePost(solrClient, indexName, managedResourcePath, requestData);
    }


    protected String encodePath(String string) throws SolrServiceException
    {
        String encodedPath = encode(string);
        String encodedPercent = encode("%");
        return encodedPath.replace("%", encodedPercent);
    }


    protected String encode(String string) throws SolrServiceException
    {
        try
        {
            return URLEncoder.encode(string, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new SolrServiceException("error in encoding", e);
        }
    }


    protected NamedList<Object> executeGet(SolrClient solrClient, String indexName, String path) throws SolrServerException, IOException
    {
        GenericSolrRequest request = new GenericSolrRequest(SolrRequest.METHOD.GET, path, null);
        return solrClient.request((SolrRequest)request, indexName);
    }


    protected void executePost(SolrClient solrClient, String indexName, String path, Object payload) throws SolrServerException, IOException
    {
        GenericSolrRequest request = new GenericSolrRequest(SolrRequest.METHOD.POST, path, null);
        if(payload != null)
        {
            RequestWriter.StringPayloadContentWriter contentWriter = new RequestWriter.StringPayloadContentWriter(Utils.toJSONString(payload), "application/json;charset=UTF-8");
            request.setContentWriter((RequestWriter.ContentWriter)contentWriter);
        }
        solrClient.request((SolrRequest)request, indexName);
    }


    protected void executeDelete(SolrClient solrClient, String indexName, String path) throws SolrServerException, IOException
    {
        GenericSolrRequest request = new GenericSolrRequest(SolrRequest.METHOD.DELETE, path, null);
        solrClient.request((SolrRequest)request, indexName);
    }


    public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
    {
        return this.solrFacetSearchConfigDao;
    }


    @Required
    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public IndexNameResolver getIndexNameResolver()
    {
        return this.indexNameResolver;
    }


    @Required
    public void setIndexNameResolver(IndexNameResolver indexNameResolver)
    {
        this.indexNameResolver = indexNameResolver;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public SolrClientPool getSolrClientPool()
    {
        return this.solrClientPool;
    }


    @Required
    public void setSolrClientPool(SolrClientPool solrClientPool)
    {
        this.solrClientPool = solrClientPool;
    }
}
