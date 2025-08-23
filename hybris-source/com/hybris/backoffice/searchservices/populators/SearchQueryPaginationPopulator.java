package com.hybris.backoffice.searchservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;

public class SearchQueryPaginationPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
    public void populate(SnSearchQueryConverterData snSearchQueryConverterData, SnSearchQuery snSearchQuery)
    {
        snSearchQuery.setLimit(Integer.valueOf(snSearchQueryConverterData.getPageSize()));
        snSearchQuery.setOffset(Integer.valueOf(snSearchQueryConverterData.getOffset()));
    }
}
