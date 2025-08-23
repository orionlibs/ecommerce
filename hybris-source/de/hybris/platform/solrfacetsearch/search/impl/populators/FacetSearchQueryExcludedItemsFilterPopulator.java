package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;

public class FacetSearchQueryExcludedItemsFilterPopulator extends AbstractFacetSearchQueryPopulator
{
    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        List<PK> excludedItems = searchQuery.getExcludedItems();
        if(CollectionUtils.isNotEmpty(excludedItems))
        {
            String filterQuery = "-pk" + (String)excludedItems.stream().map(PK::getLongValueAsString).collect(Collectors.joining(" OR ", ":(", ")"));
            target.addFilterQuery(new String[] {filterQuery});
        }
    }
}
