package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSort;
import de.hybris.platform.searchservices.search.data.SnSortExpression;
import java.util.Arrays;
import java.util.Objects;

public class SearchQuerySortsPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
    public void populate(SnSearchQueryConverterData snSearchQueryConverterData, SnSearchQuery snSearchQuery)
    {
        SearchQueryData searchQueryData = snSearchQueryConverterData.getSearchQueryData();
        if(Objects.nonNull(searchQueryData))
        {
            SortData sortData = searchQueryData.getSortData();
            if(Objects.nonNull(sortData) && Objects.nonNull(sortData.getSortAttribute()))
            {
                snSearchQuery.setSort(createSearchSortData(sortData.getSortAttribute(), sortData.getSortAttribute(), sortData.isAscending()));
            }
            else
            {
                boolean isAscending = false;
                if(Objects.nonNull(sortData))
                {
                    isAscending = sortData.isAscending();
                }
                snSearchQuery.setSort(createSearchSortData("pk", "pk", isAscending));
            }
        }
    }


    private SnSort createSearchSortData(String id, String name, boolean isAscending)
    {
        SnSortExpression expr = new SnSortExpression();
        expr.setExpression(id);
        expr.setAscending(Boolean.valueOf(isAscending));
        SnSort snSort = new SnSort();
        snSort.setId(id);
        snSort.setName(name);
        snSort.setApplyPromotedHits(Boolean.valueOf(false));
        snSort.setHighlightPromotedHits(Boolean.valueOf(false));
        snSort.setExpressions(Arrays.asList(new SnSortExpression[] {expr}));
        return snSort;
    }
}
