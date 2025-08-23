package com.hybris.backoffice.solrsearch.dataaccess.facades;

import com.hybris.backoffice.search.dataaccess.facades.DefaultFieldSearchFacadeStrategy;
import com.hybris.backoffice.solrsearch.converters.FullTextSearchDataConverter;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchService;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficeSolrFieldSearchFacadeStrategy<T extends ItemModel> extends DefaultFieldSearchFacadeStrategy<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSolrFieldSearchFacadeStrategy.class);
    private BackofficeFacetSearchService facetSearchService;
    private SolrAutoSuggestService solrAutoSuggestService;
    private FullTextSearchDataConverter fullTextSearchDataConverter;


    public Pageable<T> search(SearchQueryData queryData)
    {
        return (queryData != null) ? (Pageable<T>)new BackofficeSolrSearchPageable(this, queryData) : (Pageable<T>)new PageableList(Collections.emptyList(), 1);
    }


    public String getStrategyName()
    {
        return "solr";
    }


    public boolean useOrForGlobalOperator()
    {
        return true;
    }


    public Map<String, Collection<String>> getAutosuggestionsForQuery(AutosuggestionQueryData queryData, Context context)
    {
        SolrIndexedTypeModel indexedType = (SolrIndexedTypeModel)getFacetSearchConfigService().getIndexedTypeModel(queryData.getSearchType());
        if(indexedType != null)
        {
            try
            {
                SolrSuggestion solrSuggestion = this.solrAutoSuggestService.getAutoSuggestionsForQuery(getCommonI18NService().getCurrentLanguage(), indexedType, queryData.getQueryText());
                return solrSuggestion.getSuggestions();
            }
            catch(SolrAutoSuggestException e)
            {
                LOG.warn("Couldn't retrieve auto suggestions for query: {}, and type: {}", new Object[] {queryData.getQueryText(), queryData
                                .getSearchType(), e});
            }
        }
        return Collections.emptyMap();
    }


    public void setFacetSearchService(BackofficeFacetSearchService facetSearchService)
    {
        this.facetSearchService = facetSearchService;
    }


    public void setSolrAutoSuggestService(SolrAutoSuggestService solrAutoSuggestService)
    {
        this.solrAutoSuggestService = solrAutoSuggestService;
    }


    public void setFullTextSearchDataConverter(FullTextSearchDataConverter fullTextSearchDataConverter)
    {
        this.fullTextSearchDataConverter = fullTextSearchDataConverter;
    }
}
