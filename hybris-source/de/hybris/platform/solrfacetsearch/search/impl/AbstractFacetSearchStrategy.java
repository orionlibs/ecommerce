package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.QueryMethod;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.Keyword;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQueryCatalogVersionsResolver;
import de.hybris.platform.solrfacetsearch.search.SearchQueryCurrencyResolver;
import de.hybris.platform.solrfacetsearch.search.SearchQueryKeywordsResolver;
import de.hybris.platform.solrfacetsearch.search.SearchQueryLanguageResolver;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractFacetSearchStrategy implements FacetSearchStrategy
{
    protected static final String EXECUTE = "execute";
    private SearchQueryLanguageResolver searchQueryLanguageResolver;
    private SearchQueryCurrencyResolver searchQueryCurrencyResolver;
    private SearchQueryKeywordsResolver searchQueryKeywordsResolver;
    private SearchQueryCatalogVersionsResolver searchQueryCatalogVersionsResolver;


    public SearchQueryLanguageResolver getSearchQueryLanguageResolver()
    {
        return this.searchQueryLanguageResolver;
    }


    @Required
    public void setSearchQueryLanguageResolver(SearchQueryLanguageResolver searchQueryLanguageResolver)
    {
        this.searchQueryLanguageResolver = searchQueryLanguageResolver;
    }


    public SearchQueryCurrencyResolver getSearchQueryCurrencyResolver()
    {
        return this.searchQueryCurrencyResolver;
    }


    @Required
    public void setSearchQueryCurrencyResolver(SearchQueryCurrencyResolver searchQueryCurrencyResolver)
    {
        this.searchQueryCurrencyResolver = searchQueryCurrencyResolver;
    }


    public SearchQueryKeywordsResolver getSearchQueryKeywordsResolver()
    {
        return this.searchQueryKeywordsResolver;
    }


    @Required
    public void setSearchQueryKeywordsResolver(SearchQueryKeywordsResolver searchQueryKeywordsResolver)
    {
        this.searchQueryKeywordsResolver = searchQueryKeywordsResolver;
    }


    public SearchQueryCatalogVersionsResolver getSearchQueryCatalogVersionsResolver()
    {
        return this.searchQueryCatalogVersionsResolver;
    }


    @Required
    public void setSearchQueryCatalogVersionsResolver(SearchQueryCatalogVersionsResolver searchQueryCatalogVersionsResolver)
    {
        this.searchQueryCatalogVersionsResolver = searchQueryCatalogVersionsResolver;
    }


    protected void checkQuery(SearchQuery query)
    {
        ServicesUtil.validateParameterNotNull(query, "Parameter 'query' can not be null!");
        FacetSearchConfig facetSearchConfig = query.getFacetSearchConfig();
        IndexedType indexedType = query.getIndexedType();
        checkLanguage(query, facetSearchConfig, indexedType);
        checkCurrency(query, facetSearchConfig, indexedType);
        checkKeywords(query, facetSearchConfig, indexedType);
        checkCatalogVersions(query, facetSearchConfig, indexedType);
    }


    protected void checkLanguage(SearchQuery query, FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        if(query.getLanguage() == null)
        {
            LanguageModel language = this.searchQueryLanguageResolver.resolveLanguage(facetSearchConfig, indexedType);
            if(language != null)
            {
                String languageCode = language.getIsocode();
                if(StringUtils.isNotEmpty(languageCode))
                {
                    query.setLanguage(languageCode);
                }
            }
        }
    }


    protected void checkCurrency(SearchQuery query, FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        if(query.getCurrency() == null)
        {
            CurrencyModel currency = this.searchQueryCurrencyResolver.resolveCurrency(facetSearchConfig, indexedType);
            if(currency != null)
            {
                String currencyCode = currency.getIsocode();
                if(StringUtils.isNotEmpty(currencyCode))
                {
                    query.setCurrency(currencyCode);
                }
            }
        }
    }


    protected void checkKeywords(SearchQuery query, FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        if(query.getKeywords() == null && StringUtils.isNotBlank(query.getUserQuery()))
        {
            List<Keyword> keywords = this.searchQueryKeywordsResolver.resolveKeywords(facetSearchConfig, indexedType, query
                            .getUserQuery());
            if(CollectionUtils.isNotEmpty(keywords))
            {
                query.setKeywords(keywords);
            }
        }
    }


    protected void checkCatalogVersions(SearchQuery query, FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        if(CollectionUtils.isEmpty(query.getCatalogVersions()))
        {
            query.setCatalogVersions(this.searchQueryCatalogVersionsResolver.resolveCatalogVersions(facetSearchConfig, indexedType));
        }
    }


    protected void checkContext(FacetSearchContext facetSearchContext)
    {
        checkNamedSort(facetSearchContext);
    }


    protected void checkNamedSort(FacetSearchContext facetSearchContext)
    {
        if(facetSearchContext.getNamedSort() == null)
        {
            if(CollectionUtils.isNotEmpty(facetSearchContext.getAvailableNamedSorts()))
            {
                SearchQuery searchQuery = facetSearchContext.getSearchQuery();
                Optional<IndexedTypeSort> first = facetSearchContext.getAvailableNamedSorts().stream().filter(sort -> StringUtils.equals(searchQuery.getNamedSort(), sort.getCode())).findFirst();
                if(first.isPresent())
                {
                    facetSearchContext.setNamedSort(first.get());
                }
                else
                {
                    facetSearchContext.setNamedSort(facetSearchContext.getAvailableNamedSorts().get(0));
                }
            }
            else
            {
                facetSearchContext.setNamedSort(null);
            }
        }
    }


    protected SolrRequest.METHOD resolveQueryMethod(FacetSearchConfig facetSearchConfig)
    {
        QueryMethod queryMethod = facetSearchConfig.getSolrConfig().getQueryMethod();
        return (queryMethod == null || queryMethod == QueryMethod.GET) ? SolrRequest.METHOD.GET : SolrRequest.METHOD.POST;
    }
}
