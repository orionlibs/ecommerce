package com.hybris.backoffice.searchservices.populators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetFilter;
import de.hybris.platform.searchservices.search.data.SnAndQuery;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnMatchQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnMatchType;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchQueryFiltersPopulatorTest
{
    @Mock
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    @Mock
    private TypeService typeService;
    @Mock
    private CatalogTypeService catalogTypeService;
    @Mock
    private UserService userService;
    @Mock
    private CatalogVersionService catalogVersionService;
    @InjectMocks
    private SearchQueryFiltersPopulator searchQueryFiltersPopulator;
    private static final String SEARCH_TYPE = "searchType";
    private static final String ATTR_NAME = "attribute_name";
    private static final String QUERY_VALUE = "condition_value";
    private static final String QUERY_TEXT = "query_text";
    private static final String FACET_A = "facet_a";
    private static final String FACET_B = "facet_b";
    private static final int PAGE_SIZE = 10;
    private static final int OFFSET = 1;
    private final SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
    private final SearchQueryConditionList searchQueryConditionList = (SearchQueryConditionList)Mockito.mock(SearchQueryConditionList.class);
    private final SnIndexTypeModel snIndexTypeModel = (SnIndexTypeModel)Mockito.mock(SnIndexTypeModel.class);
    private final SnFieldModel snFieldModel = (SnFieldModel)Mockito.mock(SnFieldModel.class);
    private Set<SearchQueryCondition> searchQueryConditions = new HashSet<>();
    private SnSearchQuery snSearchQuery = new SnSearchQuery();


    @Before
    public void setup()
    {
        HashSet<String> item1Set = Sets.newHashSet((Object[])new String[] {"a1", "a2"});
        HashSet<String> item2Set = Sets.newHashSet((Object[])new String[] {"b1", "b2"});
        Map<String, Set<String>> selectedFacets = new HashMap<>();
        selectedFacets.put("facet_a", item1Set);
        selectedFacets.put("facet_b", item2Set);
        this.snSearchQuery.setQuery("query_text");
        this.snSearchQuery.setLimit(Integer.valueOf(10));
        this.snSearchQuery.setOffset(Integer.valueOf(1));
        Mockito.when(this.snFieldModel.getId()).thenReturn("attribute_name");
        Mockito.when(this.snFieldModel.getFieldType()).thenReturn(SnFieldType.TEXT);
        Mockito.when(this.snIndexTypeModel.getFields()).thenReturn(Lists.newArrayList((Object[])new SnFieldModel[] {this.snFieldModel}));
        Mockito.when(this.facetSearchConfigService.getIndexedTypeModel("searchType")).thenReturn(this.snIndexTypeModel);
        Mockito.when(this.searchQueryData.getSelectedFacets()).thenReturn(selectedFacets);
        Mockito.when(this.searchQueryData.getSearchType()).thenReturn("searchType");
    }


    @Test
    public void shouldDoNothingWhenSearchQueryDataIsNull()
    {
        SnSearchQueryConverterData snSearchQueryConverterData = (SnSearchQueryConverterData)Mockito.mock(SnSearchQueryConverterData.class);
        SnSearchQuery snSearchQuery = new SnSearchQuery();
        Mockito.when(snSearchQueryConverterData.getSearchQueryData()).thenReturn(null);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, snSearchQuery);
        Assertions.assertThat(snSearchQuery.getQuery()).isNull();
        Assertions.assertThat(snSearchQuery.getFilters()).isEmpty();
        Assertions.assertThat(snSearchQuery.getFacetFilters()).isEmpty();
    }


    @Test
    public void shouldNotAddFiltersWhenGotSearchQueryConditionsWithoutMatchedFieldModel()
    {
        List conditionsList = Lists.newArrayList(this.searchQueryConditions);
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        Mockito.when(this.searchQueryData.getConditions()).thenReturn(conditionsList);
        Mockito.when(this.snFieldModel.getId()).thenReturn("attribute_name");
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<SnFilter> snFilters = this.snSearchQuery.getFilters();
        Assert.assertTrue(snFilters.isEmpty());
    }


    @Test
    public void shouldAddFacetFiltersWhenGotSearchQueryConditionsWithMatchedFieldModel()
    {
        this.searchQueryConditions = Sets.newHashSet((Object[])new SearchQueryCondition[] {prepareCondition("attribute_name", "condition_value", ValueComparisonOperator.EQUALS)});
        Mockito.when(this.searchQueryData.getConditions()).thenReturn(Lists.newArrayList(this.searchQueryConditions));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<AbstractSnFacetFilter> facetFilters = this.snSearchQuery.getFacetFilters();
        Assertions.assertThat(facetFilters).hasSize(2);
        Assertions.assertThat(((AbstractSnFacetFilter)facetFilters.get(0)).getFacetId()).isEqualTo("facet_a");
        Assertions.assertThat(((AbstractSnFacetFilter)facetFilters.get(1)).getFacetId()).isEqualTo("facet_b");
    }


    @Test
    public void shouldAddFiltersWhenGotSearchQueryConditionsWithMatchedFieldModel()
    {
        this.searchQueryConditions = Sets.newHashSet((Object[])new SearchQueryCondition[] {prepareCondition("attribute_name", "condition_value", ValueComparisonOperator.EQUALS)});
        Mockito.when(this.searchQueryData.getConditions()).thenReturn(Lists.newArrayList(this.searchQueryConditions));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<SnFilter> snFilters = this.snSearchQuery.getFilters();
        Assertions.assertThat(snFilters).hasSize(1);
        SnFilter snFilter = snFilters.get(0);
        Assert.assertTrue(snFilter.getQuery() instanceof SnMatchQuery);
        SnMatchQuery snMatchQuery = (SnMatchQuery)snFilter.getQuery();
        Assertions.assertThat(snMatchQuery.getValue()).isEqualTo("condition_value");
        Assertions.assertThat(snMatchQuery.getExpression()).isEqualTo("attribute_name");
    }


    @Test
    public void shouldNotAddFiltersWhenGotSearchQueryConditionsWithPk()
    {
        this.searchQueryConditions = Sets.newHashSet((Object[])new SearchQueryCondition[] {prepareCondition("pk", "condition_value", ValueComparisonOperator.CONTAINS)});
        Mockito.when(this.searchQueryData.getConditions()).thenReturn(Lists.newArrayList(this.searchQueryConditions));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<SnFilter> snFilters = this.snSearchQuery.getFilters();
        Assert.assertTrue(snFilters.isEmpty());
    }


    @Test
    public void shouldAddFiltersWhenGotSearchQueryConditionListWhichContainsMatchedSearchQueryCondition()
    {
        this.searchQueryConditions = Sets.newHashSet((Object[])new SearchQueryCondition[] {prepareCondition("attribute_name", "condition_value", ValueComparisonOperator.EQUALS)});
        Mockito.when(this.searchQueryConditionList.getConditions()).thenReturn(Lists.newArrayList(this.searchQueryConditions));
        Mockito.when(this.searchQueryConditionList.getOperator()).thenReturn(ValueComparisonOperator.AND);
        Mockito.when(this.searchQueryData.getConditions()).thenReturn(Lists.newArrayList((Object[])new SearchQueryConditionList[] {this.searchQueryConditionList}));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<SnFilter> snFilters = this.snSearchQuery.getFilters();
        Assertions.assertThat(snFilters).hasSize(1);
        SnFilter snFilter = snFilters.get(0);
        Assert.assertTrue(snFilter.getQuery() instanceof SnAndQuery);
        SnAndQuery andQuery = (SnAndQuery)snFilter.getQuery();
        List<SnMatchQuery> queries = andQuery.getQueries();
        Assertions.assertThat(queries).hasSize(1);
        SnMatchQuery snMatchQuery = queries.get(0);
        Assertions.assertThat(snMatchQuery.getValue()).isEqualTo("condition_value");
        Assertions.assertThat(snMatchQuery.getExpression()).isEqualTo("attribute_name");
    }


    @Test
    public void shouldNotAddCatalogVersionFiltersWhenLoginUserIsAdmin()
    {
        SnFieldModel snCVField = (SnFieldModel)Mockito.mock(SnFieldModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        UserModel currentUser = (UserModel)Mockito.mock(UserModel.class);
        Mockito.when(this.snIndexTypeModel.getFields()).thenReturn(Lists.newArrayList((Object[])new SnFieldModel[] {snCVField}));
        Mockito.when(snCVField.getId()).thenReturn("catalogVersionPk");
        Mockito.when(this.typeService.getComposedTypeForCode("searchType")).thenReturn(composedType);
        Mockito.when(this.userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.lenient().when(Boolean.valueOf(this.userService.isAdmin(currentUser))).thenReturn(Boolean.valueOf(true));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        Assertions.assertThat(this.snSearchQuery.getFilters()).isEmpty();
    }


    @Test
    public void shouldNotAddCatalogVersionFiltersWhenNoCatalogVersionEixsted()
    {
        SnFieldModel snCVField = (SnFieldModel)Mockito.mock(SnFieldModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        UserModel currentUser = (UserModel)Mockito.mock(UserModel.class);
        Mockito.when(this.snIndexTypeModel.getFields()).thenReturn(Lists.newArrayList((Object[])new SnFieldModel[] {snCVField}));
        Mockito.when(snCVField.getId()).thenReturn("catalogVersionPk");
        Mockito.when(this.typeService.getComposedTypeForCode("searchType")).thenReturn(composedType);
        Mockito.when(this.userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.lenient().when(Boolean.valueOf(this.userService.isAdmin(currentUser))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)currentUser)).thenReturn(CollectionUtils.EMPTY_COLLECTION);
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        Assertions.assertThat(this.snSearchQuery.getFilters()).isEmpty();
    }


    @Test
    public void shouldNotAddCatalogVersionFiltersWhenSearchTypeIsNotCatalogVersionAwareType()
    {
        SnFieldModel snCVField = (SnFieldModel)Mockito.mock(SnFieldModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        UserModel currentUser = (UserModel)Mockito.mock(UserModel.class);
        Mockito.when(this.snIndexTypeModel.getFields()).thenReturn(Lists.newArrayList((Object[])new SnFieldModel[] {snCVField}));
        Mockito.when(snCVField.getId()).thenReturn("catalogVersionPk");
        Mockito.when(this.typeService.getComposedTypeForCode("searchType")).thenReturn(composedType);
        Mockito.when(this.userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.lenient().when(Boolean.valueOf(this.userService.isAdmin(currentUser))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.catalogTypeService.isCatalogVersionAwareType(composedType))).thenReturn(Boolean.valueOf(false));
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        Assertions.assertThat(this.snSearchQuery.getFilters()).isEmpty();
    }


    @Test
    public void shouldAddCatalogVersionFilters()
    {
        SnFieldModel snCVField = (SnFieldModel)Mockito.mock(SnFieldModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        UserModel currentUser = (UserModel)Mockito.mock(UserModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        PK pk = PK.BIG_PK;
        Mockito.when(this.snIndexTypeModel.getFields()).thenReturn(Lists.newArrayList((Object[])new SnFieldModel[] {snCVField}));
        Mockito.when(snCVField.getId()).thenReturn("catalogVersionPk");
        Mockito.when(this.typeService.getComposedTypeForCode("searchType")).thenReturn(composedType);
        Mockito.when(this.userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(currentUser))).thenReturn(Boolean.valueOf(false));
        Mockito.when(Boolean.valueOf(this.catalogTypeService.isCatalogVersionAwareType(composedType))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)currentUser)).thenReturn(Lists.newArrayList((Object[])new CatalogVersionModel[] {catalogVersion}));
        Mockito.when(catalogVersion.getPk()).thenReturn(pk);
        SnSearchQueryConverterData snSearchQueryConverterData = new SnSearchQueryConverterData(this.searchQueryData, 10, 1);
        this.searchQueryFiltersPopulator.populate(snSearchQueryConverterData, this.snSearchQuery);
        List<SnFilter> snFilters = this.snSearchQuery.getFilters();
        Assertions.assertThat(snFilters).hasSize(1);
        SnFilter snFilter = snFilters.get(0);
        Assertions.assertThat(snFilter.getQuery()).isInstanceOf(SnMatchTermsQuery.class);
        SnMatchTermsQuery snMatchTermsQuery = (SnMatchTermsQuery)snFilter.getQuery();
        Assertions.assertThat((Comparable)snMatchTermsQuery.getMatchType()).isEqualTo(SnMatchType.ANY);
        Assertions.assertThat(snMatchTermsQuery.getExpression()).isEqualTo("catalogVersionPk");
        List<Object> catalogVersionPKs = snMatchTermsQuery.getValues();
        Assertions.assertThat(catalogVersionPKs.size()).isEqualTo(1);
        Assertions.assertThat(catalogVersionPKs.get(0)).isEqualTo(pk.toString());
    }


    protected SearchQueryCondition prepareCondition(String attrName, Object value, ValueComparisonOperator operator)
    {
        return new SearchQueryCondition(new SearchAttributeDescriptor(attrName), value, operator);
    }
}
