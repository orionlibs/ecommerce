package com.hybris.backoffice.solrsearch.converters.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultFullTextSearchDataConverterTest
{
    public static final String FACET_CATEGORY = "category";
    public static final String FACET_PRICE = "price";
    public static final String FACET_BREADCRUMB = "breadcrumbFacet";
    public static final String VAL_BREADCRUMB = "breadcrumbValue";
    public static final String VAL_SHOES = "shoes";
    public static final String VAL_SHIRTS = "shirts";
    public static final String VAL_PRICE_100 = "100";
    public static final String VAL_PRICE_200 = "200";
    @Mock
    private IndexedType indexedType;
    private DefaultFullTextSearchDataConverter converter;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.converter = new DefaultFullTextSearchDataConverter();
        Map<String, IndexedProperty> indexedPropertyMap = (Map<String, IndexedProperty>)Mockito.mock(Map.class);
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        Mockito.when(indexedPropertyMap.get(Matchers.anyString())).thenAnswer(invocationOnMock -> {
            String name = (String)invocationOnMock.getArguments()[0];
            IndexedProperty indexedProperty = new IndexedProperty();
            indexedProperty.setName(name);
            indexedProperty.setBackofficeDisplayName(name);
            indexedProperty.setFacetType(FacetType.MULTISELECTAND);
            return indexedProperty;
        });
    }


    @Test
    public void convertFacetsWithNotDuplicatedBreadcrumbs()
    {
        List<Facet> facets = new ArrayList<>();
        facets.add(new Facet("category",
                        Lists.newArrayList((Object[])new FacetValue[] {new FacetValue("shoes", 20L, true), new FacetValue("shirts", 10L, false)})));
        facets.add(new Facet("price",
                        Lists.newArrayList((Object[])new FacetValue[] {new FacetValue("100", 20L, true), new FacetValue("200", 10L, false)})));
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(createBreadcrumb("breadcrumbFacet", "breadcrumbValue"));
        Collection<FacetData> convertFacets = this.converter.convertFacets(facets, breadcrumbs, this.indexedType);
        Assertions.assertThat(convertFacets).hasSize(3);
        Map<String, FacetData> facetDataMap = (Map<String, FacetData>)convertFacets.stream().collect(Collectors.toMap(FacetData::getName, fd -> fd));
        Assertions.assertThat((Comparable)facetDataMap.get("category")).isNotNull();
        Assertions.assertThat((Comparable)facetDataMap.get("price")).isNotNull();
        Assertions.assertThat((Comparable)facetDataMap.get("breadcrumbFacet")).isNotNull();
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValues()).hasSize(2);
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("shoes").isSelected()).isTrue();
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("shoes").getCount()).isEqualTo(20L);
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("shirts").isSelected()).isFalse();
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("shirts").getCount()).isEqualTo(10L);
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValues()).hasSize(2);
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("100").isSelected()).isTrue();
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("100").getCount()).isEqualTo(20L);
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("200").isSelected()).isFalse();
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("200").getCount()).isEqualTo(10L);
        Assertions.assertThat(((FacetData)facetDataMap.get("breadcrumbFacet")).getFacetValues()).hasSize(1);
        Assertions.assertThat(((FacetData)facetDataMap.get("breadcrumbFacet")).getFacetValue("breadcrumbValue").isSelected()).isTrue();
        Assertions.assertThat(((FacetData)facetDataMap.get("breadcrumbFacet")).getFacetValue("breadcrumbValue").getCount()).isEqualTo(0L);
    }


    @Test
    public void convertFacetsWithBreadcrumbForExistingFacet()
    {
        List<Facet> facets = new ArrayList<>();
        facets.add(new Facet("category",
                        Lists.newArrayList((Object[])new FacetValue[] {new FacetValue("shoes", 20L, true), new FacetValue("shirts", 10L, false)})));
        facets.add(new Facet("price",
                        Lists.newArrayList((Object[])new FacetValue[] {new FacetValue("100", 20L, true), new FacetValue("200", 10L, false), new FacetValue("breadcrumbValue", 5L, false)})));
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(createBreadcrumb("category", "breadcrumbValue"));
        breadcrumbs.add(createBreadcrumb("price", "breadcrumbValue"));
        Collection<FacetData> convertFacets = this.converter.convertFacets(facets, breadcrumbs, this.indexedType);
        Assertions.assertThat(convertFacets).hasSize(2);
        Map<String, FacetData> facetDataMap = (Map<String, FacetData>)convertFacets.stream().collect(Collectors.toMap(FacetData::getName, fd -> fd));
        Assertions.assertThat((Comparable)facetDataMap.get("category")).isNotNull();
        Assertions.assertThat((Comparable)facetDataMap.get("price")).isNotNull();
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValues()).hasSize(3);
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("breadcrumbValue").isSelected()).isTrue();
        Assertions.assertThat(((FacetData)facetDataMap.get("category")).getFacetValue("breadcrumbValue").getCount())
                        .isEqualTo(-1L);
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValues()).hasSize(3);
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("breadcrumbValue").isSelected()).isTrue();
        Assertions.assertThat(((FacetData)facetDataMap.get("price")).getFacetValue("breadcrumbValue").getCount()).isEqualTo(5L);
    }


    @Test
    public void shouldMergeBreadcrumbWithSelectedFacet()
    {
        FacetValue facetValue = new FacetValue("shirts", "shirtsDisplayName", 10L, false);
        FacetValue facetValueSelected = new FacetValue("shoes", "shoesDisplayName", 20L, true);
        Facet facet = new Facet("category", Collections.singletonList(facetValue));
        facet.setSelectedFacetValues(Collections.singletonList(facetValueSelected));
        List<Facet> facets = Collections.singletonList(facet);
        List<Breadcrumb> breadcrumbs = Collections.singletonList(createBreadcrumb("category", "shoes"));
        Collection<FacetData> facetData = this.converter.convertFacets(facets, breadcrumbs, this.indexedType);
        Assertions.assertThat(facetData).hasSize(1);
        Assertions.assertThat(((FacetData)facetData.stream().findFirst().get()).getFacetValues()).extracting(FacetValueData::getDisplayName)
                        .contains((Object[])new String[] {"shoesDisplayName"});
    }


    @Test
    public void shouldKeepFacetsOrder()
    {
        List<Facet> facets = new ArrayList<>();
        facets.add(new Facet("category", Collections.singletonList(new FacetValue("shoes", 20L, false))));
        facets.add(new Facet("price", Collections.singletonList(new FacetValue("100", 20L, false))));
        facets.add(new Facet("breadcrumbFacet", Collections.singletonList(new FacetValue("100", 20L, false))));
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(createBreadcrumb("category", "breadcrumbValue"));
        breadcrumbs.add(createBreadcrumb("price", "breadcrumbValue"));
        breadcrumbs.add(createBreadcrumb("breadcrumbFacet", "breadcrumbValue"));
        Collection<FacetData> facetData = this.converter.convertFacets(facets, breadcrumbs, this.indexedType);
        Assertions.assertThat(facetData).extracting(FacetData::getName).containsExactly((Object[])new String[] {"category", "price", "breadcrumbFacet"});
    }


    @Test
    public void shouldFacetValuesBeTakenWhenAllFacetValuesCollectionHasTheSameSize()
    {
        FacetValue approved = new FacetValue(ArticleApprovalStatus.APPROVED.getCode(), 1L, false);
        FacetValue checked = new FacetValue(ArticleApprovalStatus.CHECK.getCode(), 1L, false);
        List<FacetValue> facetValues = List.of(approved, checked);
        List<FacetValue> allFacetValues = List.of(checked, approved);
        Facet facet = new Facet("approvalStatus", facetValues);
        facet.setAllFacetValues(allFacetValues);
        IndexedProperty indexedProperty = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        BDDMockito.given(indexedProperty.getBackofficeDisplayName()).willReturn("name");
        FacetData facetData = this.converter.convertFacet(facet, indexedProperty);
        Assertions.assertThat((List)facetData.getFacetValues().stream().map(FacetValueData::getName).collect(Collectors.toList()))
                        .isEqualTo(facetValues.stream().map(FacetValue::getName).collect(Collectors.toList()));
        Assertions.assertThat((List)facetData.getFacetValues().stream().map(FacetValueData::getName).collect(Collectors.toList()))
                        .isNotEqualTo(allFacetValues.stream().map(FacetValue::getName).collect(Collectors.toList()));
    }


    public Breadcrumb createBreadcrumb(String fieldName, String value)
    {
        Breadcrumb breadcrumb = (Breadcrumb)Mockito.mock(Breadcrumb.class);
        Mockito.when(breadcrumb.getFieldName()).thenReturn(fieldName);
        Mockito.when(breadcrumb.getValue()).thenReturn(value);
        Mockito.when(breadcrumb.getDisplayValue()).thenReturn(value);
        return breadcrumb;
    }
}
