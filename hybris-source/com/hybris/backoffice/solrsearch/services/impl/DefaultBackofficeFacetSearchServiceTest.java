package com.hybris.backoffice.solrsearch.services.impl;

import com.google.common.collect.Sets;
import com.hybris.backoffice.solrsearch.converters.SearchConditionDataConverter;
import com.hybris.backoffice.solrsearch.converters.impl.DefaultSearchQueryConditionsConverter;
import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class DefaultBackofficeFacetSearchServiceTest
{
    public static final String CUSTOM_CV_QUALIFIER = "_custom_cv_qualifier";
    public static final String BACKOFFICE_PRODUCT = "BackofficeProduct";
    public static final long STAGED_PK_LONG = 1L;
    public static final long ONLINE_PK_LONG = 2L;
    public static final int IMPOSSIBLE_PK = -1;
    private static final String CATALOG_VERSION_PROPERTY = "catalogVersionPk";
    public static final String FACET_1 = "facet1";
    public static final String FACET_2 = "facet2";
    public static final String FACET_3 = "facet3";
    @Spy
    @InjectMocks
    private DefaultBackofficeFacetSearchService service;
    @Mock
    private IndexedType indexedType;
    @Mock
    private ComposedTypeModel catalogAwareType;
    @Mock
    private ComposedTypeModel catalogUnawareType;
    @Mock
    private CatalogTypeService catalogTypeService;
    @Mock
    private SearchConditionData searchConditionData;
    @Mock
    private SearchQueryData queryData;
    @Mock
    private SearchQuery searchQuery;
    @Mock
    private CatalogVersionModel staged;
    @Mock
    private CatalogVersionModel online;
    @Mock
    private UserService userService;
    @Mock
    private DefaultSearchQueryConditionsConverter searchQueryConditionsConverter;
    @Mock
    private SearchConditionDataConverter searchConditionDataConverter;
    @Mock
    private TypeService typeService;
    @Mock
    private CatalogVersionService catalogVersionService;
    private Collection<CatalogVersionModel> readableCatalogVersions;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.indexedType.getIdentifier()).thenReturn("BackofficeProduct");
        Mockito.when(Boolean.valueOf(this.catalogTypeService.isCatalogVersionAwareType(this.catalogAwareType))).thenReturn(Boolean.TRUE);
        Mockito.when(Boolean.valueOf(this.catalogTypeService.isCatalogVersionAwareType(this.catalogUnawareType))).thenReturn(Boolean.FALSE);
        this.service.setIndexedTypeToCatalogVersionPropertyMapping(new HashMap<>());
        this.readableCatalogVersions = new ArrayList<>();
        this.readableCatalogVersions.add(this.staged);
        this.readableCatalogVersions.add(this.online);
        PK stagedPK = PK.fromLong(1L);
        Mockito.when(this.staged.getPk()).thenReturn(stagedPK);
        PK onlinePK = PK.fromLong(2L);
        Mockito.when(this.online.getPk()).thenReturn(onlinePK);
    }


    @Test
    public void isCatalogVersionAwareCatalogUnawareType()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogUnawareType);
        Mockito.when(Boolean.valueOf(this.catalogTypeService.isCatalogVersionAwareType(this.catalogUnawareType))).thenReturn(Boolean.FALSE);
        Assertions.assertThat(this.service.isCatalogVersionAware(this.indexedType)).isFalse();
    }


    @Test
    public void isCatalogVersionAwareCustomCVQualifier()
    {
        HashMap<String, IndexedProperty> indexedProperties = new HashMap<>();
        indexedProperties.put("_custom_cv_qualifier", null);
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        HashMap<String, String> typeToCatalogVersionPropertyMapping = new HashMap<>();
        typeToCatalogVersionPropertyMapping.put("BackofficeProduct", "_custom_cv_qualifier");
        this.service.setIndexedTypeToCatalogVersionPropertyMapping(typeToCatalogVersionPropertyMapping);
        Assertions.assertThat(this.service.isCatalogVersionAware(this.indexedType)).isTrue();
    }


    @Test
    public void isCatalogVersionAwareDefaultCVQualifier()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        HashMap<String, IndexedProperty> indexedProperties = new HashMap<>();
        indexedProperties.put("catalogVersionPk", null);
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        Assertions.assertThat(this.service.isCatalogVersionAware(this.indexedType)).isTrue();
    }


    @Test
    public void isCatalogVersionAwareNoIndexedProperties()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        HashMap<String, IndexedProperty> indexedProperties = new HashMap<>();
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        Assertions.assertThat(this.service.isCatalogVersionAware(this.indexedType)).isFalse();
    }


    @Test
    public void prepareCatalogVersionConditionNoReadableCVs()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        this.service.prepareCatalogVersionCondition(this.indexedType, this.searchConditionData, this.queryData, new ArrayList());
        ArgumentCaptor<SolrSearchCondition> captor = ArgumentCaptor.forClass(SolrSearchCondition.class);
        ((SearchConditionData)Mockito.verify(this.searchConditionData)).addFilterQueryCondition((SolrSearchCondition)captor.capture());
        SolrSearchCondition effectiveCondition = (SolrSearchCondition)captor.getValue();
        List<SolrSearchCondition.ConditionValue> conditionValues = effectiveCondition.getConditionValues();
        Assertions.assertThat(conditionValues).hasSize(1);
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)conditionValues.get(0)).getValue()).isEqualTo(Integer.valueOf(-1));
    }


    @Test
    public void prepareCatalogVersionConditionTwoReadableCVs()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        ArgumentCaptor<SolrSearchCondition> captor = ArgumentCaptor.forClass(SolrSearchCondition.class);
        this.service.prepareCatalogVersionCondition(this.indexedType, this.searchConditionData, this.queryData, this.readableCatalogVersions);
        ((SearchConditionData)Mockito.verify(this.searchConditionData)).addFilterQueryCondition((SolrSearchCondition)captor.capture());
        SolrSearchCondition effectiveCondition = (SolrSearchCondition)captor.getValue();
        List<SolrSearchCondition.ConditionValue> conditionValues = effectiveCondition.getConditionValues();
        Assertions.assertThat(conditionValues).hasSize(2);
        Assertions.assertThat((List)conditionValues.stream().map(SolrSearchCondition.ConditionValue::getValue).collect(Collectors.toList()))
                        .contains((Object[])new Serializable[] {Long.valueOf(1L), Long.valueOf(2L)});
    }


    @Test
    public void shouldAddCatalogVersionConditionWhenUserIsNotAdmin()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        Mockito.when(this.queryData.getConditions()).thenReturn(Lists.emptyList());
        Mockito.when(this.queryData.getSearchType()).thenReturn("");
        Mockito.when(this.searchConditionDataConverter.convertConditions((List)Matchers.any(), (SearchQuery.Operator)Matchers.any())).thenReturn(this.searchConditionData);
        Map<String, IndexedProperty> indexedProperties = new HashMap<>();
        indexedProperties.put("catalogVersionPk", (IndexedProperty)Mockito.mock(IndexedProperty.class));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        List<CatalogVersionModel> readableCV = Lists.newArrayList((Object[])new CatalogVersionModel[] {this.staged, this.online});
        Mockito.when(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)Matchers.any())).thenReturn(readableCV);
        ArgumentCaptor<SolrSearchCondition> captor = ArgumentCaptor.forClass(SolrSearchCondition.class);
        this.service.prepareSearchConditionData(this.queryData, this.indexedType);
        ((SearchConditionData)Mockito.verify(this.searchConditionData)).addFilterQueryCondition((SolrSearchCondition)captor.capture());
        SolrSearchCondition effectiveCondition = (SolrSearchCondition)captor.getValue();
        List<SolrSearchCondition.ConditionValue> conditionValues = effectiveCondition.getConditionValues();
        Assertions.assertThat(conditionValues).hasSize(2);
        Assertions.assertThat((List)conditionValues.stream().map(SolrSearchCondition.ConditionValue::getValue).collect(Collectors.toList()))
                        .contains((Object[])new Serializable[] {Long.valueOf(1L), Long.valueOf(2L)});
    }


    @Test
    public void shouldNotAddCatalogVersionConditionWhenUserIsAdmin()
    {
        Mockito.when(this.indexedType.getComposedType()).thenReturn(this.catalogAwareType);
        Mockito.when(Boolean.valueOf(this.userService.isAdmin((UserModel)Matchers.any()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.queryData.getConditions()).thenReturn(Lists.emptyList());
        Mockito.when(this.queryData.getSearchType()).thenReturn("");
        Mockito.when(this.searchConditionDataConverter.convertConditions((List)Matchers.any(), (SearchQuery.Operator)Matchers.any())).thenReturn(this.searchConditionData);
        Map<String, IndexedProperty> indexedProperties = new HashMap<>();
        indexedProperties.put("catalogVersionPk", (IndexedProperty)Mockito.mock(IndexedProperty.class));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        List<CatalogVersionModel> readableCV = Collections.singletonList(this.staged);
        Mockito.when(this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)Matchers.any())).thenReturn(readableCV);
        SearchConditionData searchCondition = this.service.prepareSearchConditionData(this.queryData, this.indexedType);
        Assertions.assertThat(searchCondition.getFilterQueryConditions()).isEmpty();
    }


    @Test
    public void shouldAddTypeConditionWhenItemtypeIsIndexed()
    {
        Map<String, IndexedProperty> indexedProperties = Collections.singletonMap("itemtype", null);
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        Mockito.when(this.queryData.getSearchType()).thenReturn("");
        Mockito.when(this.typeService.getComposedTypeForCode("")).thenReturn(null);
        Mockito.when(Boolean.valueOf(this.queryData.isIncludeSubtypes())).thenReturn(Boolean.FALSE);
        Optional<SolrSearchCondition> condition = this.service.prepareTypeCondition(this.indexedType, this.queryData);
        Assertions.assertThat(condition).isPresent();
        Assertions.assertThat(((SolrSearchCondition)condition.get()).getAttributeName()).isEqualTo("itemtype");
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)condition.get()).getConditionValues().get(0)).getComparisonOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
    }


    @Test
    public void shouldNotAddTypeConditionWhenItemtypeIsNotIndexed()
    {
        Map<String, IndexedProperty> indexedProperties = Collections.singletonMap("_custom_cv_qualifier", null);
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedProperties);
        Optional<SolrSearchCondition> condition = this.service.prepareTypeCondition(this.indexedType, this.queryData);
        Assertions.assertThat(condition).isNotPresent();
    }


    @Test
    public void shouldNotAddSelectedFacetWhichIsNotInAvailableFacets()
    {
        Map<String, Set<String>> selectedFacets = new HashMap<>();
        selectedFacets.put("facet1", Collections.emptySet());
        selectedFacets.put("facet2", Collections.emptySet());
        selectedFacets.put("facet3", Collections.emptySet());
        Set<String> availableFacets = Sets.newHashSet((Object[])new String[] {"facet1", "facet3"});
        this.service.populateSelectedFacets(selectedFacets, availableFacets, this.searchQuery);
        ((SearchQuery)Mockito.verify(this.searchQuery)).addFacetValue((String)Matchers.eq("facet1"), (Set)Matchers.any(Set.class));
        ((SearchQuery)Mockito.verify(this.searchQuery, Mockito.never())).addFacetValue((String)Matchers.eq("facet2"), (Set)Matchers.any(Set.class));
        ((SearchQuery)Mockito.verify(this.searchQuery)).addFacetValue((String)Matchers.eq("facet3"), (Set)Matchers.any(Set.class));
    }
}
