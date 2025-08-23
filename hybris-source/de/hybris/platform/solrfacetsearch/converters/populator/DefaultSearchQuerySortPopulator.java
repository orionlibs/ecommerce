package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.SearchQuerySort;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQuerySortModel;

public class DefaultSearchQuerySortPopulator implements Populator<SolrSearchQuerySortModel, SearchQuerySort>
{
    public void populate(SolrSearchQuerySortModel source, SearchQuerySort target)
    {
        target.setField(source.getField());
        target.setAscending(source.isAscending());
    }
}
