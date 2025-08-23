package com.hybris.backoffice.solrsearch.dataaccess.facades;

import com.hybris.backoffice.search.daos.ItemModelSearchDAO;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.converters.FullTextSearchDataConverter;
import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchService;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeSolrFieldSearchFacadeStrategyTest
{
    @Mock
    private BackofficeFacetSearchService facetSearchService;
    @Mock
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private ItemModelSearchDAO itemModelSearchDAO;
    @Mock
    private SolrAutoSuggestService solrAutoSuggestService;
    @Mock
    private FullTextSearchDataConverter fullTextSearchDataConverter;
    @InjectMocks
    private BackofficeSolrFieldSearchFacadeStrategy solrSearchStrategy;
    private static final String TYPE_CODE = "Product";


    @Test
    public void shouldReturnEmptyPageIfQueryDataIsNull()
    {
        Pageable result = this.solrSearchStrategy.search(null);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result instanceof com.hybris.cockpitng.search.data.pageable.PageableList).isTrue();
        Assertions.assertThat(result.getAllResults()).isEmpty();
    }


    @Test
    public void shouldReturnCorrectPage()
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        Mockito.when(searchQueryData.getSearchType()).thenReturn("Product");
        Mockito.when(Integer.valueOf(searchQueryData.getPageSize())).thenReturn(Integer.valueOf(2));
        Pageable result = this.solrSearchStrategy.search(searchQueryData);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(result.getPageSize()).isEqualTo(2);
    }


    @Test
    public void shouldGetEmptySuggestionIfIndexTypeNotExist()
    {
        AutosuggestionQueryData queryData = (AutosuggestionQueryData)Mockito.mock(AutosuggestionQueryData.class);
        Mockito.when(queryData.getSearchType()).thenReturn("Product");
        Mockito.when(this.facetSearchConfigService.getIndexedTypeModel("Product")).thenReturn(null);
        Map<String, Collection<String>> result = this.solrSearchStrategy.getAutosuggestionsForQuery(queryData, null);
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    public void shouldGetExpectedSuggestions() throws SolrAutoSuggestException
    {
        AutosuggestionQueryData queryData = (AutosuggestionQueryData)Mockito.mock(AutosuggestionQueryData.class);
        SolrSuggestion solrSuggestion = (SolrSuggestion)Mockito.mock(SolrSuggestion.class);
        SolrIndexedTypeModel indexedType = (SolrIndexedTypeModel)Mockito.mock(SolrIndexedTypeModel.class);
        LanguageModel language = (LanguageModel)Mockito.mock(LanguageModel.class);
        String queryText = "test";
        Map<String, Collection<String>> suggestion = new HashMap<>();
        suggestion.put("test", Arrays.asList(new String[] {"test1", "test2"}));
        Mockito.when(solrSuggestion.getSuggestions()).thenReturn(suggestion);
        Mockito.when(this.commonI18NService.getCurrentLanguage()).thenReturn(language);
        Mockito.when(queryData.getSearchType()).thenReturn("Product");
        Mockito.when(this.facetSearchConfigService.getIndexedTypeModel("Product")).thenReturn(indexedType);
        Mockito.when(queryData.getQueryText()).thenReturn("test");
        Mockito.when(this.solrAutoSuggestService.getAutoSuggestionsForQuery(language, indexedType, "test")).thenReturn(solrSuggestion);
        Map<String, Collection<String>> result = this.solrSearchStrategy.getAutosuggestionsForQuery(queryData, null);
        Assertions.assertThat(result).isEqualTo(suggestion);
    }


    @Test
    public void shouldGetEmptyResultIfSolrReturnEmptyPK() throws FacetSearchException
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        IndexedType indexedType = (IndexedType)Mockito.mock(IndexedType.class);
        SearchResult searchResult = (SearchResult)Mockito.mock(SearchResult.class);
        String autocorrection = "autocorrection";
        Breadcrumb breadcrumb = (Breadcrumb)Mockito.mock(Breadcrumb.class);
        Facet facet = (Facet)Mockito.mock(Facet.class);
        List<Facet> facets = Arrays.asList(new Facet[] {facet});
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb[] {breadcrumb});
        FacetData facetData = (FacetData)Mockito.mock(FacetData.class);
        Collection<FacetData> facetDatas = Arrays.asList(new FacetData[] {facetData});
        BackofficeSearchQuery solrSearchQuery = (BackofficeSearchQuery)Mockito.mock(BackofficeSearchQuery.class);
        Mockito.when(searchQueryData.getSearchType()).thenReturn("Product");
        Mockito.when(Integer.valueOf(searchQueryData.getPageSize())).thenReturn(Integer.valueOf(2));
        Mockito.when(this.facetSearchService.createBackofficeSolrSearchQuery(searchQueryData)).thenReturn(solrSearchQuery);
        Mockito.when(solrSearchQuery.getIndexedType()).thenReturn(indexedType);
        Mockito.when(this.facetSearchService.search((SearchQuery)solrSearchQuery)).thenReturn(searchResult);
        Mockito.when(searchResult.getSpellingSuggestion()).thenReturn("autocorrection");
        Mockito.when(searchResult.getBreadcrumbs()).thenReturn(breadcrumbs);
        Mockito.when(searchResult.getFacets()).thenReturn(facets);
        Mockito.when(searchResult.getResultPKs()).thenReturn(Collections.emptyList());
        Mockito.when(this.fullTextSearchDataConverter.convertFacets(facets, breadcrumbs, indexedType)).thenReturn(facetDatas);
        FullTextSearchPageable pageable = (FullTextSearchPageable)this.solrSearchStrategy.search(searchQueryData);
        List searchResults = pageable.getCurrentPage();
        FullTextSearchData fullTextSearchData = pageable.getFullTextSearchData();
        Assertions.assertThat(fullTextSearchData.getFacets()).isEqualTo(facetDatas);
        Assertions.assertThat(fullTextSearchData.getAutocorrection()).isEqualTo("autocorrection");
        Assertions.assertThat(searchResults).isEmpty();
    }


    @Test
    public void shouldGetCorrectResults() throws FacetSearchException
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        IndexedType indexedType = (IndexedType)Mockito.mock(IndexedType.class);
        SearchResult searchResult = (SearchResult)Mockito.mock(SearchResult.class);
        String autocorrection = "autocorrection";
        Breadcrumb breadcrumb = (Breadcrumb)Mockito.mock(Breadcrumb.class);
        Facet facet = (Facet)Mockito.mock(Facet.class);
        List<Facet> facets = Arrays.asList(new Facet[] {facet});
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb[] {breadcrumb});
        FacetData facetData = (FacetData)Mockito.mock(FacetData.class);
        Collection<FacetData> facetDatas = Arrays.asList(new FacetData[] {facetData});
        BackofficeSearchQuery solrSearchQuery = (BackofficeSearchQuery)Mockito.mock(BackofficeSearchQuery.class);
        PK pk = PK.createUUIDPK(10);
        List<PK> pks = Arrays.asList(new PK[] {pk});
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        List<ItemModel> itemModels = Arrays.asList(new ItemModel[] {itemModel});
        Mockito.when(searchQueryData.getSearchType()).thenReturn("Product");
        Mockito.when(Integer.valueOf(searchQueryData.getPageSize())).thenReturn(Integer.valueOf(2));
        Mockito.when(this.facetSearchService.createBackofficeSolrSearchQuery(searchQueryData)).thenReturn(solrSearchQuery);
        Mockito.when(solrSearchQuery.getIndexedType()).thenReturn(indexedType);
        Mockito.when(this.facetSearchService.search((SearchQuery)solrSearchQuery)).thenReturn(searchResult);
        Mockito.when(searchResult.getSpellingSuggestion()).thenReturn("autocorrection");
        Mockito.when(searchResult.getBreadcrumbs()).thenReturn(breadcrumbs);
        Mockito.when(searchResult.getFacets()).thenReturn(facets);
        Mockito.when(searchResult.getResultPKs()).thenReturn(pks);
        Mockito.when(Integer.valueOf(searchResult.getOffset())).thenReturn(Integer.valueOf(1));
        Mockito.when(Long.valueOf(searchResult.getNumberOfResults())).thenReturn(Long.valueOf(123L));
        Mockito.when(this.itemModelSearchDAO.findAll((String)Matchers.eq("Product"), Matchers.anyList())).thenReturn(itemModels);
        Mockito.when(this.fullTextSearchDataConverter.convertFacets(facets, breadcrumbs, indexedType)).thenReturn(facetDatas);
        FullTextSearchPageable pageable = (FullTextSearchPageable)this.solrSearchStrategy.search(searchQueryData);
        List searchResults = pageable.getCurrentPage();
        FullTextSearchData fullTextSearchData = pageable.getFullTextSearchData();
        Assertions.assertThat(fullTextSearchData.getFacets()).isEqualTo(facetDatas);
        Assertions.assertThat(fullTextSearchData.getAutocorrection()).isEqualTo("autocorrection");
        Assertions.assertThat(searchResults).isEqualTo(itemModels);
        Assertions.assertThat(pageable.getTotalCount()).isEqualTo(123);
    }
}
