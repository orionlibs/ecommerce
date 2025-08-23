package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;

public class FacetSearchQueryFilterQueriesPopulator extends AbstractFacetSearchQueryPopulator
{
    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        List<String> filterQueries = new ArrayList<>();
        addQueryFieldQueries(searchQuery, filterQueries);
        addRawQueries(searchQuery, filterQueries);
        for(String filterQuery : filterQueries)
        {
            target.addFilterQuery(new String[] {filterQuery});
        }
    }


    protected void addQueryFieldQueries(SearchQuery searchQuery, List<String> queries)
    {
        for(QueryField filterQuery : searchQuery.getFilterQueries())
        {
            String query = convertQueryField(searchQuery, filterQuery);
            queries.add(query);
        }
    }


    protected void addRawQueries(SearchQuery searchQuery, List<String> queries)
    {
        for(RawQuery filterRawQuery : searchQuery.getFilterRawQueries())
        {
            String query = convertRawQuery(searchQuery, filterRawQuery);
            queries.add(query);
        }
    }
}
