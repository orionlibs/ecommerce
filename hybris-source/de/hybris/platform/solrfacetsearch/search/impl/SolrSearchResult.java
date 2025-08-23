package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.enums.ConverterType;
import de.hybris.platform.solrfacetsearch.loader.ModelLoader;
import de.hybris.platform.solrfacetsearch.loader.ModelLoadingException;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroupCommand;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;

public class SolrSearchResult implements SearchResult, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SolrSearchResult.class);
    private ConvertersMapping convertersMapping;
    private SearchQuery searchQuery;
    private transient QueryResponse queryResponse;
    private long numberOfResults = 0L;
    private List<Document> documents = new ArrayList<>();
    private List<SolrDocument> solrDocuments;
    private List<SearchResultGroupCommand> groupCommands = new ArrayList<>();
    private Map<String, Facet> facets = new LinkedHashMap<>();
    private List<Breadcrumb> breadcrumbs = new ArrayList<>();
    private List<KeywordRedirectValue> keywordRedirects = new ArrayList<>();
    private List<IndexedTypeSort> availableNamedSorts = new ArrayList<>();
    private IndexedTypeSort currentNamedSort;
    private final transient Map<String, Object> attributes = new LinkedHashMap<>();


    public ConvertersMapping getConvertersMapping()
    {
        return this.convertersMapping;
    }


    public void setConvertersMapping(ConvertersMapping convertersMapping)
    {
        this.convertersMapping = convertersMapping;
    }


    public SearchQuery getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setSearchQuery(SearchQuery searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public QueryResponse getQueryResponse()
    {
        return this.queryResponse;
    }


    public void setQueryResponse(QueryResponse queryResponse)
    {
        this.queryResponse = queryResponse;
    }


    public long getNumberOfResults()
    {
        return this.numberOfResults;
    }


    public void setNumberOfResults(long numberOfResults)
    {
        this.numberOfResults = numberOfResults;
    }


    public List<Document> getDocuments()
    {
        return this.documents;
    }


    public void setDocuments(List<Document> documents)
    {
        this.documents = documents;
    }


    public List<SolrDocument> getSolrDocuments()
    {
        return this.solrDocuments;
    }


    public void setSolrDocuments(List<SolrDocument> solrDocuments)
    {
        this.solrDocuments = solrDocuments;
    }


    public void setGroupCommandResult(SearchResultGroupCommand groupCommandResult)
    {
        this.groupCommands.clear();
        this.groupCommands.add(groupCommandResult);
    }


    public SearchResultGroupCommand getGroupCommandResult()
    {
        if(CollectionUtils.isEmpty(this.groupCommands))
        {
            return null;
        }
        return this.groupCommands.get(0);
    }


    public List<SearchResultGroupCommand> getGroupCommands()
    {
        return this.groupCommands;
    }


    public void setGroupCommands(List<SearchResultGroupCommand> groupCommands)
    {
        this.groupCommands = groupCommands;
    }


    public Map<String, Facet> getFacetsMap()
    {
        return this.facets;
    }


    public void setFacetsMap(Map<String, Facet> facets)
    {
        this.facets = facets;
    }


    public List<Breadcrumb> getBreadcrumbs()
    {
        return this.breadcrumbs;
    }


    public void setBreadcrumbs(List<Breadcrumb> breadcrumbs)
    {
        this.breadcrumbs = breadcrumbs;
    }


    public List<KeywordRedirectValue> getKeywordRedirects()
    {
        return this.keywordRedirects;
    }


    public List<IndexedTypeSort> getAvailableNamedSorts()
    {
        return this.availableNamedSorts;
    }


    public void setAvailableNamedSorts(List<IndexedTypeSort> availableNamedSorts)
    {
        this.availableNamedSorts = availableNamedSorts;
    }


    public IndexedTypeSort getCurrentNamedSort()
    {
        return this.currentNamedSort;
    }


    public void setCurrentNamedSort(IndexedTypeSort currentNamedSort)
    {
        this.currentNamedSort = currentNamedSort;
    }


    public void setKeywordRedirects(List<KeywordRedirectValue> keywordRedirects)
    {
        this.keywordRedirects = keywordRedirects;
    }


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.searchQuery.getFacetSearchConfig();
    }


    public IndexedType getIndexedType()
    {
        return this.searchQuery.getIndexedType();
    }


    public int getOffset()
    {
        return this.searchQuery.getOffset();
    }


    public int getPageSize()
    {
        return this.searchQuery.getPageSize();
    }


    public boolean hasNext()
    {
        return (((getOffset() + 1) * getPageSize()) < getNumberOfResults());
    }


    public boolean hasPrevious()
    {
        return (getOffset() > 0);
    }


    public long getNumberOfPages()
    {
        int pageSize = getPageSize();
        return (getNumberOfResults() + pageSize - 1L) / pageSize;
    }


    public List<String> getIdentifiers()
    {
        List<String> identifiers = new ArrayList<>();
        for(SolrDocument solrDocument : this.solrDocuments)
        {
            identifiers.add((String)solrDocument.getFieldValue("id"));
        }
        return identifiers;
    }


    public List<PK> getResultPKs() throws FacetSearchException
    {
        if(this.solrDocuments == null)
        {
            throw new IllegalStateException("Collection of Solr Documents must not be null");
        }
        if(this.solrDocuments.isEmpty())
        {
            return Collections.emptyList();
        }
        List<PK> result = new ArrayList<>(this.solrDocuments.size());
        for(SolrDocument solrDocument : this.solrDocuments)
        {
            Long pk = (Long)solrDocument.getFirstValue("pk");
            if(pk == null)
            {
                throw new FacetSearchException("SolrDocument does not contain field 'pk'");
            }
            result.add(PK.fromLong(pk.longValue()));
        }
        return result;
    }


    public List<String> getResultCodes() throws FacetSearchException
    {
        try
        {
            ModelLoader<?> modelLoader = getModelLoader(this.searchQuery.getIndexedType());
            return modelLoader.loadCodes(this.solrDocuments);
        }
        catch(ModelLoadingException e)
        {
            throw new FacetSearchException(e.getMessage(), e);
        }
    }


    public List<? extends ItemModel> getResults() throws FacetSearchException
    {
        try
        {
            ModelLoader<ItemModel> modelLoader = getModelLoader(this.searchQuery.getIndexedType());
            return modelLoader.loadModels(this.solrDocuments);
        }
        catch(ModelLoadingException e)
        {
            throw new FacetSearchException(e.getMessage(), e);
        }
    }


    public <T> List<T> getResultData(ConverterType converterType)
    {
        if(getSearchQuery() == null)
        {
            throw new IllegalStateException("Query must be set to use result converters");
        }
        Converter<SolrResult, T> solrResultConverter = getConverter(converterType);
        if(solrResultConverter == null)
        {
            throw new IllegalStateException("Result converter must be registered.");
        }
        if(this.solrDocuments == null || this.solrDocuments.isEmpty())
        {
            return Collections.emptyList();
        }
        List<T> resultData = new ArrayList<>(this.solrDocuments.size());
        for(SolrDocument document : this.solrDocuments)
        {
            resultData.add((T)solrResultConverter.convert(new SolrResult(document, getSearchQuery())));
        }
        return resultData;
    }


    public void addFacet(Facet facet)
    {
        this.facets.put(facet.getName(), facet);
    }


    public Set<String> getFacetNames()
    {
        return this.facets.keySet();
    }


    public boolean containsFacet(String name)
    {
        return this.facets.containsKey(name);
    }


    public Facet getFacet(String name)
    {
        Facet facet = this.facets.get(name);
        if(facet == null)
        {
            LOG.debug("No facet with name [" + name + "] found. Return null.");
        }
        return facet;
    }


    public List<Facet> getFacets()
    {
        return new ArrayList<>(this.facets.values());
    }


    public String getSpellingSuggestion()
    {
        if(getQueryResponse() != null && getQueryResponse().getSpellCheckResponse() != null)
        {
            return getQueryResponse().getSpellCheckResponse().getCollatedResult();
        }
        return null;
    }


    public SearchQueryInfo getQueryInfo()
    {
        NamedList object = (NamedList)this.queryResponse.getResponseHeader().get("params");
        String query = (String)object.get("q");
        return new SearchQueryInfo(query, getNumberOfResults(), this.searchQuery.getFacetSearchConfig().getName(), this.searchQuery
                        .getLanguage(), Calendar.getInstance().getTime());
    }


    public QueryResponse getSolrObject()
    {
        return this.queryResponse;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    protected Converter getConverter(ConverterType converterType)
    {
        if(converterType == null || this.convertersMapping == null || this.convertersMapping.getConverterForType(converterType) == null)
        {
            String name = this.searchQuery.getIndexedType().getSolrResultConverter();
            return (name == null) ? null : (Converter)Registry.getApplicationContext().getBean(name, Converter.class);
        }
        return this.convertersMapping.getConverterForType(converterType);
    }


    protected ModelLoader getModelLoader(IndexedType indexType)
    {
        String name = indexType.getModelLoader();
        return (name == null) ? null : (ModelLoader)Registry.getApplicationContext().getBean(name, ModelLoader.class);
    }
}
