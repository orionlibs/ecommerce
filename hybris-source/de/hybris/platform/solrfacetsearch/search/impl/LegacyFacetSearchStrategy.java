package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SolrQueryConverter;
import de.hybris.platform.solrfacetsearch.search.SolrResultPostProcessor;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "6.4")
public class LegacyFacetSearchStrategy extends AbstractFacetSearchStrategy
{
    private static final String ENCODING = "UTF-8";
    private static final Logger LOG = Logger.getLogger(LegacyFacetSearchStrategy.class);
    private SolrIndexService solrIndexService;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private I18NService i18NService;
    private CommonI18NService commonI18NService;
    private FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory;
    private Converter<SearchResultConverterData, SolrSearchResult> facetSearchResultConverter;
    private SolrQueryConverter solrQueryConverter;
    private List<SolrResultPostProcessor> resultPostProcessors;


    public SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public FacetSearchContextFactory<FacetSearchContext> getFacetSearchContextFactory()
    {
        return this.facetSearchContextFactory;
    }


    @Required
    public void setFacetSearchContextFactory(FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory)
    {
        this.facetSearchContextFactory = facetSearchContextFactory;
    }


    public Converter<SearchResultConverterData, SolrSearchResult> getFacetSearchResultConverter()
    {
        return this.facetSearchResultConverter;
    }


    @Required
    public void setFacetSearchResultConverter(Converter<SearchResultConverterData, SolrSearchResult> facetSearchResultConverter)
    {
        this.facetSearchResultConverter = facetSearchResultConverter;
    }


    public SolrQueryConverter getSolrQueryConverter()
    {
        return this.solrQueryConverter;
    }


    @Required
    public void setSolrQueryConverter(SolrQueryConverter solrQueryConverter)
    {
        this.solrQueryConverter = solrQueryConverter;
    }


    public List<SolrResultPostProcessor> getResultPostProcessors()
    {
        return this.resultPostProcessors;
    }


    public void setResultPostProcessors(List<SolrResultPostProcessor> resultPostProcessors)
    {
        this.resultPostProcessors = resultPostProcessors;
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
            SearchResult searchResult = doSearch(facetSearchContext, index, solrClient);
            return searchResult;
        }
        catch(FacetSearchException | de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException | SolrServerException | RuntimeException e)
        {
            this.facetSearchContextFactory.destroyContext(e);
            throw new FacetSearchException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    protected SearchResult doSearch(FacetSearchContext searchContext, Index index, SolrClient solrClient) throws FacetSearchException, SolrServerException
    {
        SearchResult searchResult;
        try
        {
            searchResult = queryInternal(searchContext, index, solrClient);
        }
        catch(SolrException e)
        {
            if(!canUseFallbackLanguage(e))
            {
                throw new FacetSearchException(e.getMessage(), e);
            }
            searchResult = queryUsingFallbackLanguage(searchContext, index, solrClient, e);
        }
        this.facetSearchContextFactory.getContext().setSearchResult(searchResult);
        this.facetSearchContextFactory.destroyContext();
        return searchResult;
    }


    protected SearchResult queryInternal(FacetSearchContext searchContext, Index index, SolrClient solrClient) throws FacetSearchException, SolrServerException
    {
        SearchQuery searchQuery = searchContext.getSearchQuery();
        SolrQuery solrSearchQuery = this.solrQueryConverter.convertSolrQuery(searchQuery);
        if(LOG.isDebugEnabled())
        {
            try
            {
                LOG.debug("Solr Query: \n" + URLDecoder.decode(solrSearchQuery.toString(), "UTF-8"));
            }
            catch(UnsupportedEncodingException ex)
            {
                throw new FacetSearchException(ex);
            }
        }
        try
        {
            SolrRequest.METHOD method = resolveQueryMethod(searchContext.getFacetSearchConfig());
            QueryResponse queryResponse = solrClient.query(index.getName(), (SolrParams)solrSearchQuery, method);
            SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
            searchResultConverterData.setFacetSearchContext(searchContext);
            searchResultConverterData.setQueryResponse(queryResponse);
            SolrSearchResult searchResult = (SolrSearchResult)this.facetSearchResultConverter.convert(searchResultConverterData);
            return applySearchResultsPostProcessors((SearchResult)searchResult);
        }
        catch(IOException e)
        {
            throw new FacetSearchException(e);
        }
    }


    protected boolean canUseFallbackLanguage(SolrException exception)
    {
        return (exception.getMessage().contains("undefined field") && this.i18NService.isLocalizationFallbackEnabled());
    }


    protected SearchResult queryUsingFallbackLanguage(FacetSearchContext searchContext, Index index, SolrClient solrClient, SolrException exception) throws FacetSearchException
    {
        SearchQuery searchQuery = searchContext.getSearchQuery();
        LanguageModel language = this.commonI18NService.getLanguage(searchQuery.getLanguage());
        List<LanguageModel> languages = language.getFallbackLanguages();
        for(LanguageModel lang : languages)
        {
            searchQuery.setLanguage(lang.getIsocode());
            try
            {
                return queryInternal(searchContext, index, solrClient);
            }
            catch(SolrException | SolrServerException ex)
            {
                LOG.warn(ex);
            }
        }
        throw new FacetSearchException("Cannot query using fallback languages: " + languages, exception);
    }


    protected SearchResult applySearchResultsPostProcessors(SearchResult searchResult)
    {
        SearchResult postProcessingResult = searchResult;
        for(SolrResultPostProcessor postProcessor : getResultPostProcessors())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Processing solr search result post-processor : " + postProcessor.getClass());
            }
            postProcessingResult = postProcessor.process(postProcessingResult);
        }
        return postProcessingResult;
    }


    public String convertSearchQueryToString(SearchQuery query) throws FacetSearchException
    {
        checkQuery(query);
        return getSolrQueryConverter().convertSolrQuery(query).toString();
    }
}
