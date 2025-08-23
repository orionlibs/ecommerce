package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import org.apache.solr.client.solrj.SolrQuery;

public class FacetSearchQueryPagingPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        int start = searchQuery.getOffset() * searchQuery.getPageSize();
        target.setStart(Integer.valueOf(start));
        target.setRows(Integer.valueOf(searchQuery.getPageSize()));
    }
}
