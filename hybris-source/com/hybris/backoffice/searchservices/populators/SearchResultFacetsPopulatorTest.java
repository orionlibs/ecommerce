package com.hybris.backoffice.searchservices.populators;

import com.hybris.backoffice.searchservices.providers.impl.CategoryFacetValueDisplayNameProvider;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse;
import de.hybris.platform.searchservices.search.data.SnFacetFilterMode;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnTermBucketResponse;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchResultFacetsPopulatorTest
{
    @Mock
    private CockpitLocaleService cockpitLocaleService;
    @Mock
    private CategoryFacetValueDisplayNameProvider categoryFacetValueDisplayNameProvider;
    @InjectMocks
    private SearchResultFacetsPopulator searchResultFacetsPopulator;
    private final SnSearchResultSourceData snSearchResultSourceData = (SnSearchResultSourceData)Mockito.mock(SnSearchResultSourceData.class);
    private final SnSearchResultConverterData snSearchResultConverterData = (SnSearchResultConverterData)Mockito.mock(SnSearchResultConverterData.class);
    private static final String RES_NAME = "response_name";
    private static final String RES_ID = "1";


    @Test
    public void shouldNotSetFullTextDataWhenGotNullResponse()
    {
        SnSearchResult snSearchResult = (SnSearchResult)Mockito.mock(SnSearchResult.class);
        SnTermBucketResponse snTermBucketResponse = (SnTermBucketResponse)Mockito.mock(SnTermBucketResponse.class);
        SnTermBucketsFacetResponse facetResponse = (SnTermBucketsFacetResponse)Mockito.mock(SnTermBucketsFacetResponse.class);
        List<AbstractSnFacetResponse> facets = Arrays.asList(new AbstractSnFacetResponse[] {(AbstractSnFacetResponse)facetResponse});
        Mockito.when(facetResponse.getFilterMode()).thenReturn(SnFacetFilterMode.REFINE);
        Mockito.when(facetResponse.getId()).thenReturn("1");
        Mockito.when(facetResponse.getName()).thenReturn("response_name");
        Mockito.when(facetResponse.getSelectedBuckets()).thenReturn(Arrays.asList(new SnTermBucketResponse[] {snTermBucketResponse}));
        Mockito.when(this.snSearchResultSourceData.getSnSearchResult()).thenReturn(snSearchResult);
        Mockito.when(snSearchResult.getFacets()).thenReturn(facets);
        this.searchResultFacetsPopulator.populate(this.snSearchResultSourceData, this.snSearchResultConverterData);
        ((SnSearchResultConverterData)Mockito.verify(this.snSearchResultConverterData)).setFullTextSearchData((FullTextSearchData)Matchers.any());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGotUnexpectedResponse()
    {
        SnSearchResult snSearchResult = (SnSearchResult)Mockito.mock(SnSearchResult.class);
        AbstractSnBucketsFacetResponse facetResponse = (AbstractSnBucketsFacetResponse)Mockito.mock(AbstractSnBucketsFacetResponse.class);
        List<AbstractSnFacetResponse> facets = Arrays.asList(new AbstractSnFacetResponse[] {(AbstractSnFacetResponse)facetResponse});
        Mockito.when(facetResponse.getFilterMode()).thenReturn(SnFacetFilterMode.REFINE);
        Mockito.when(facetResponse.getId()).thenReturn("1");
        Mockito.when(facetResponse.getName()).thenReturn("response_name");
        Mockito.when(this.snSearchResultSourceData.getSnSearchResult()).thenReturn(snSearchResult);
        Mockito.when(snSearchResult.getFacets()).thenReturn(facets);
        this.searchResultFacetsPopulator.populate(this.snSearchResultSourceData, this.snSearchResultConverterData);
    }
}
