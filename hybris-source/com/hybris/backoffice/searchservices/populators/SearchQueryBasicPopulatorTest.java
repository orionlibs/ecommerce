package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchQueryBasicPopulatorTest
{
    private static final String QUERY_TEXT = "queryText";
    private static final String PRE_QUERY_TEXT = "preQueryText";
    private static final int PAGE_SIZE = 10;
    private static final int OFFSET = 1;
    private final SearchQueryBasicPopulator searchQueryBasicPopulator = new SearchQueryBasicPopulator();


    @Test
    public void shouldSetQueryTextWhenSearchQueryDataContainsQueryText()
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(searchQueryData.getSearchQueryText()).thenReturn("queryText");
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(searchQueryData, 10, 1);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        this.searchQueryBasicPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        Assertions.assertThat(snSearchQuery.getQuery()).isEqualTo("queryText");
    }


    @Test
    public void shouldUpdateQueryTextWhenSearchQueryDataContainsQueryText()
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(searchQueryData.getSearchQueryText()).thenReturn("queryText");
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(searchQueryData, 10, 1);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        snSearchQuery.setQuery("preQueryText");
        this.searchQueryBasicPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        Assertions.assertThat(snSearchQuery.getQuery()).isEqualTo("queryText");
    }


    @Test
    public void shouldKeepQueryWhenSearchQueryDataIsNull()
    {
        SnSearchQueryConverterData snSearchQueryConverterData = (SnSearchQueryConverterData)Mockito.mock(SnSearchQueryConverterData.class);
        Mockito.when(snSearchQueryConverterData.getSearchQueryData()).thenReturn(null);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        snSearchQuery.setQuery("preQueryText");
        this.searchQueryBasicPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        Assertions.assertThat(snSearchQuery.getQuery()).isEqualTo("preQueryText");
    }
}
