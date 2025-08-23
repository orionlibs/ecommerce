package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchQueryPaginationPopulatorTest
{
    private static final int PAGE_SIZE = 10;
    private static final int OFFSET = 1;
    private final SearchQueryPaginationPopulator searchQueryPaginationPopulator = new SearchQueryPaginationPopulator();


    @Test
    public void shouldSetQueryPaginationWhenGetSearchQueryData()
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(searchQueryData, 10, 1);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        this.searchQueryPaginationPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        Assertions.assertThat(snSearchQuery.getLimit()).isEqualTo(10);
        Assertions.assertThat(snSearchQuery.getOffset()).isEqualTo(1);
    }
}
