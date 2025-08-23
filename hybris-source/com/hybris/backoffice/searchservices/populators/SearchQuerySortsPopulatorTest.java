package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSort;
import de.hybris.platform.searchservices.search.data.SnSortExpression;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchQuerySortsPopulatorTest
{
    private static final String SORT_ATTR = "SORT_ATTR";
    private final SearchQuerySortsPopulator searchQuerySortsPopulator = new SearchQuerySortsPopulator();


    @Test
    public void shouldSetQueryTextWhenSearchQueryDataIsNotNull()
    {
        SortData sortData = new SortData("SORT_ATTR", true);
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(searchQueryData.getSortData()).thenReturn(sortData);
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(searchQueryData, 10, 1);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        this.searchQuerySortsPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        SnSort snSort = snSearchQuery.getSort();
        Assertions.assertThat(snSort).isNotNull();
        Assertions.assertThat(snSort.getId()).isEqualTo("SORT_ATTR");
        Assertions.assertThat(snSort.getName()).isEqualTo("SORT_ATTR");
        List<SnSortExpression> expressions = snSort.getExpressions();
        Assertions.assertThat(expressions).hasSize(1);
        Assertions.assertThat(((SnSortExpression)expressions.get(0)).getExpression()).isEqualTo("SORT_ATTR");
        Assertions.assertThat(((SnSortExpression)expressions.get(0)).getAscending()).isTrue();
    }
}
