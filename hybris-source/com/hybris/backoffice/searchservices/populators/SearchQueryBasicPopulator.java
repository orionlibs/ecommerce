package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import java.util.Objects;

public class SearchQueryBasicPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
    public void populate(SnSearchQueryConverterData snSearchQueryConverterData, SnSearchQuery snSearchQuery)
    {
        SearchQueryData searchQueryData = snSearchQueryConverterData.getSearchQueryData();
        if(Objects.nonNull(searchQueryData))
        {
            snSearchQuery.setQuery(searchQueryData.getSearchQueryText());
        }
    }
}
