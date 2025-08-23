package com.hybris.backoffice.searchservices.dataaccess.facades;

import com.hybris.backoffice.search.dataaccess.facades.AbstractBackofficeSearchPageable;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.populators.SnSearchQueryConverterData;
import com.hybris.backoffice.searchservices.populators.SnSearchResultConverterData;
import com.hybris.backoffice.searchservices.populators.SnSearchResultSourceData;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.searchservices.suggest.SnSuggestException;
import de.hybris.platform.searchservices.suggest.data.SnSuggestHit;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.suggest.service.SnSuggestService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSearchServicesFieldSearchFacadeStrategyTest
{
    @Mock
    private SnSearchService snSearchService;
    @Mock
    private SnSuggestService snSuggestService;
    @Mock
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    @Mock
    private Converter<SnSearchQueryConverterData, SnSearchQuery> searchQueryConverter;
    @Mock
    private Converter<SnSearchResultSourceData, SnSearchResultConverterData> searchResultConverter;
    @InjectMocks
    private DefaultSearchServicesFieldSearchFacadeStrategy defaultSearchServicesFieldSearchFacadeStrategy;
    private static final String TYPE_CODE = "testTypeCode";
    private static final String QUERY_TEXT = "testQueryText";
    private static final String INDEX_TYPE_ID = "testIndexTypeId";
    private static final int PAGE_SIZE = 10;
    private static final String MODEL_ID = "1";


    @Before
    public void setup()
    {
        this.defaultSearchServicesFieldSearchFacadeStrategy.setSearchQueryConverter(this.searchQueryConverter);
        this.defaultSearchServicesFieldSearchFacadeStrategy.setSearchResultConverter(this.searchResultConverter);
    }


    @Test
    public void shouldGetEmptyPageableWhenQueryDataIsNull()
    {
        Pageable<PageableList> result = this.defaultSearchServicesFieldSearchFacadeStrategy.search(null);
        Assertions.assertThat(result.getAllResults()).isEmpty();
    }


    @Test
    public void shouldGetPageableWhenQueryDataIsNotNull()
    {
        SearchQueryData queryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(queryData.getSearchType()).thenReturn("testTypeCode");
        Mockito.when(Integer.valueOf(queryData.getPageSize())).thenReturn(Integer.valueOf(10));
        Pageable<?> result = this.defaultSearchServicesFieldSearchFacadeStrategy.search(queryData);
        Assertions.assertThat(result.getTypeCode()).isEqualTo("testTypeCode");
        Assertions.assertThat(result.getPageSize()).isEqualTo(10);
    }


    @Test
    public void shouldGetPageableItemListAfterSearch() throws SnSearchException
    {
        SearchQueryData queryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(queryData.getSearchType()).thenReturn("testTypeCode");
        Mockito.when(Integer.valueOf(queryData.getPageSize())).thenReturn(Integer.valueOf(10));
        SnIndexTypeModel indexTypeModel = (SnIndexTypeModel)Mockito.mock(SnIndexTypeModel.class);
        Mockito.when(indexTypeModel.getId()).thenReturn("testIndexTypeId");
        Mockito.when(this.facetSearchConfigService.getIndexedTypeModel("testTypeCode")).thenReturn(indexTypeModel);
        SnSearchRequest snSearchRequest = (SnSearchRequest)Mockito.mock(SnSearchRequest.class);
        Mockito.when(this.snSearchService.createSearchRequest((String)Matchers.eq("testIndexTypeId"), (SnSearchQuery)Matchers.any())).thenReturn(snSearchRequest);
        SnSearchResponse snSearchResponse = (SnSearchResponse)Mockito.mock(SnSearchResponse.class);
        Mockito.when(this.snSearchService.search(snSearchRequest)).thenReturn(snSearchResponse);
        SnSearchResult snSearchResult = (SnSearchResult)Mockito.mock(SnSearchResult.class);
        Mockito.when(snSearchResponse.getSearchResult()).thenReturn(snSearchResult);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        Mockito.when(this.searchQueryConverter.convert(Matchers.any())).thenReturn(snSearchQuery);
        SnSearchResultConverterData snResultData = new SnSearchResultConverterData();
        FullTextSearchData fullTextSearchData = (FullTextSearchData)Mockito.mock(FullTextSearchData.class);
        snResultData.setFullTextSearchData(fullTextSearchData);
        ItemModel item = (ItemModel)Mockito.mock(ItemModel.class);
        snResultData.setItemModels(Arrays.asList(new ItemModel[] {item}));
        Mockito.when(this.searchResultConverter.convert(Matchers.any())).thenReturn(snResultData);
        AbstractBackofficeSearchPageable result = (AbstractBackofficeSearchPageable)this.defaultSearchServicesFieldSearchFacadeStrategy.search(queryData);
        List currentPage = result.getCurrentPage();
        Assertions.assertThat(currentPage).hasSize(1);
        Assertions.assertThat(currentPage).contains(new Object[] {item});
    }


    @Test
    public void shouldGetAutoSuggestionsWhenGetQueryData() throws SnSuggestException
    {
        AutosuggestionQueryData queryData = (AutosuggestionQueryData)Mockito.mock(AutosuggestionQueryData.class);
        Mockito.when(queryData.getQueryText()).thenReturn("testQueryText");
        Mockito.when(queryData.getSearchType()).thenReturn("testTypeCode");
        SnIndexTypeModel indexTypeModel = (SnIndexTypeModel)Mockito.mock(SnIndexTypeModel.class);
        Mockito.when(indexTypeModel.getId()).thenReturn("1");
        Mockito.when(this.facetSearchConfigService.getIndexedTypeModel("testTypeCode")).thenReturn(indexTypeModel);
        SnSuggestRequest snSuggestRequest = (SnSuggestRequest)Mockito.mock(SnSuggestRequest.class);
        SnSuggestResponse snSuggestResponse = (SnSuggestResponse)Mockito.mock(SnSuggestResponse.class);
        Mockito.when(this.snSuggestService.suggest(snSuggestRequest)).thenReturn(snSuggestResponse);
        Mockito.when(this.snSuggestService.createSuggestRequest((String)Matchers.eq("1"), (SnSuggestQuery)Matchers.any())).thenReturn(snSuggestRequest);
        SnSuggestHit hit1 = new SnSuggestHit();
        hit1.setQuery("Hit1");
        SnSuggestHit hit2 = new SnSuggestHit();
        hit2.setQuery("Hit2");
        List<SnSuggestHit> snSuggestHitList = Arrays.asList(new SnSuggestHit[] {hit1, hit2});
        SnSuggestResult snSuggestResult = (SnSuggestResult)Mockito.mock(SnSuggestResult.class);
        Mockito.when(snSuggestResult.getSuggestHits()).thenReturn(snSuggestHitList);
        Mockito.when(snSuggestResponse.getSuggestResult()).thenReturn(snSuggestResult);
        this.defaultSearchServicesFieldSearchFacadeStrategy.setFacetSearchConfigService(this.facetSearchConfigService);
        Map<String, Collection<String>> autosuggestionsForQuery = this.defaultSearchServicesFieldSearchFacadeStrategy.getAutosuggestionsForQuery(queryData, (Context)Mockito.mock(Context.class));
        Assertions.assertThat(autosuggestionsForQuery).hasSize(1);
        Assertions.assertThat(autosuggestionsForQuery.get("testQueryText".toLowerCase().trim())).isEqualTo(Arrays.asList(new String[] {"Hit1", "Hit2"}));
    }
}
