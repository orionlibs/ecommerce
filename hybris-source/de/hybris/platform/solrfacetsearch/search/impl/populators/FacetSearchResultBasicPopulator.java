package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverters;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchResultBasicPopulator implements Populator<SearchResultConverterData, SolrSearchResult>
{
    private SearchResultConverters searchResultConverters;


    public SearchResultConverters getSearchResultConverters()
    {
        return this.searchResultConverters;
    }


    @Required
    public void setSearchResultConverters(SearchResultConverters searchResultConverters)
    {
        this.searchResultConverters = searchResultConverters;
    }


    public void populate(SearchResultConverterData source, SolrSearchResult target)
    {
        SearchQuery searchQuery = source.getFacetSearchContext().getSearchQuery();
        if(this.searchResultConverters != null)
        {
            IndexedType indexedType = searchQuery.getIndexedType();
            target.setConvertersMapping(this.searchResultConverters.getConverterMapping(indexedType.getCode()));
        }
        target.setSearchQuery(searchQuery);
        target.setQueryResponse(source.getQueryResponse());
        target.setBreadcrumbs(searchQuery.getBreadcrumbs());
    }
}
