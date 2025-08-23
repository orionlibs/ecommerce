package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;

public class FacetSearchQueryParamsPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        Map<String, String[]> rawParams = searchQuery.getRawParams();
        for(Map.Entry<String, String[]> entry : rawParams.entrySet())
        {
            target.add(entry.getKey(), entry.getValue());
        }
    }
}
