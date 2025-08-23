package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;

public class FacetSearchResultSortsPopulator implements Populator<SearchResultConverterData, SolrSearchResult>
{
    public void populate(SearchResultConverterData source, SolrSearchResult target)
    {
        FacetSearchContext facetSearchContext = source.getFacetSearchContext();
        IndexedTypeSort namedSort = facetSearchContext.getNamedSort();
        target.setCurrentNamedSort(namedSort);
        target.setAvailableNamedSorts(facetSearchContext.getAvailableNamedSorts());
    }
}
