package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.io.Closeable;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.IOUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchStrategy extends AbstractFacetSearchStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSearchStrategy.class);
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory;
    private Converter<SearchQueryConverterData, SolrQuery> facetSearchQueryConverter;
    private Converter<SearchResultConverterData, SearchResult> facetSearchResultConverter;
    private SolrIndexService solrIndexService;


    public FacetSearchContextFactory<FacetSearchContext> getFacetSearchContextFactory()
    {
        return this.facetSearchContextFactory;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    @Required
    public void setFacetSearchContextFactory(FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory)
    {
        this.facetSearchContextFactory = facetSearchContextFactory;
    }


    public Converter<SearchQueryConverterData, SolrQuery> getFacetSearchQueryConverter()
    {
        return this.facetSearchQueryConverter;
    }


    @Required
    public void setFacetSearchQueryConverter(Converter<SearchQueryConverterData, SolrQuery> facetSearchQueryConverter)
    {
        this.facetSearchQueryConverter = facetSearchQueryConverter;
    }


    public Converter<SearchResultConverterData, SearchResult> getFacetSearchResultConverter()
    {
        return this.facetSearchResultConverter;
    }


    @Required
    public void setFacetSearchResultConverter(Converter<SearchResultConverterData, SearchResult> facetSearchResultConverter)
    {
        this.facetSearchResultConverter = facetSearchResultConverter;
    }


    public SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    public SearchResult search(SearchQuery searchQuery, Map<String, String> searchHints) throws FacetSearchException
    {
        checkQuery(searchQuery);
        SolrClient solrClient = null;
        try
        {
            FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
            IndexedType indexedType = searchQuery.getIndexedType();
            FacetSearchContext facetSearchContext = this.facetSearchContextFactory.createContext(facetSearchConfig, indexedType, searchQuery);
            facetSearchContext.getSearchHints().putAll(searchHints);
            this.facetSearchContextFactory.initializeContext();
            checkContext(facetSearchContext);
            if(MapUtils.isNotEmpty(searchHints))
            {
                boolean execute = Boolean.parseBoolean(searchHints.get("execute"));
                if(!execute)
                {
                    return null;
                }
            }
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            SolrIndexModel activeIndex = this.solrIndexService.getActiveIndex(facetSearchConfig.getName(), indexedType
                            .getIdentifier());
            Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, activeIndex.getQualifier());
            solrClient = solrSearchProvider.getClient(index);
            SearchQueryConverterData searchQueryConverterData = new SearchQueryConverterData();
            searchQueryConverterData.setFacetSearchContext(facetSearchContext);
            searchQueryConverterData.setSearchQuery(searchQuery);
            SolrQuery solrQuery = (SolrQuery)this.facetSearchQueryConverter.convert(searchQueryConverterData);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(solrQuery);
            }
            SolrRequest.METHOD method = resolveQueryMethod(facetSearchConfig);
            QueryResponse queryResponse = solrClient.query(index.getName(), (SolrParams)solrQuery, method);
            SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
            searchResultConverterData.setFacetSearchContext(facetSearchContext);
            searchResultConverterData.setQueryResponse(queryResponse);
            SearchResult searchResult = (SearchResult)this.facetSearchResultConverter.convert(searchResultConverterData);
            this.facetSearchContextFactory.getContext().setSearchResult(searchResult);
            this.facetSearchContextFactory.destroyContext();
            return searchResult;
        }
        catch(SolrServiceException | org.apache.solr.client.solrj.SolrServerException | java.io.IOException | RuntimeException e)
        {
            this.facetSearchContextFactory.destroyContext(e);
            throw new FacetSearchException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }
}
