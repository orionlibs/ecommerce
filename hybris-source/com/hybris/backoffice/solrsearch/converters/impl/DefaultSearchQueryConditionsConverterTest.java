package com.hybris.backoffice.solrsearch.converters.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultSearchQueryConditionsConverterTest
{
    public static final String ATTR_NAME = "name";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_DESC = "desc";
    public static final String VAL_1 = "v1";
    public static final String VAL_2 = "v2";
    public static final String VAL_3 = "v3";
    private DefaultSearchQueryConditionsConverter conditionsConverter;
    @Mock
    private IndexedType indexedType;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.conditionsConverter = new DefaultSearchQueryConditionsConverter();
    }


    @Test
    public void testQueryWithOneAttributeAndMultipleValues()
    {
        List<SearchQueryCondition> conditions = Lists.newArrayList();
        conditions.add(prepareCondition("name", "v1", ValueComparisonOperator.EQUALS));
        conditions.add(prepareCondition("name", "v2", ValueComparisonOperator.CONTAINS));
        conditions.add(prepareCondition("name", "v3", ValueComparisonOperator.IS_NOT_EMPTY));
        Map<String, IndexedProperty> indexedPropertyMap = Maps.newHashMap();
        indexedPropertyMap.put("name", prepareIndexedProperty("name", false));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        List<SolrSearchCondition> converted = this.conditionsConverter.convert(conditions, SearchQuery.Operator.AND, this.indexedType);
        Assertions.assertThat(converted.size()).isEqualTo(1);
        Assertions.assertThat(((SolrSearchCondition)converted.get(0)).getAttributeName()).isEqualTo("name");
        Assertions.assertThat((Comparable)((SolrSearchCondition)converted.get(0)).getOperator()).isEqualTo(SearchQuery.Operator.AND);
        Assertions.assertThat(((SolrSearchCondition)converted.get(0)).getConditionValues().size()).isEqualTo(3);
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(0)).getValue()).isEqualTo("v1");
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(1)).getValue()).isEqualTo("v2");
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(2)).getValue()).isEqualTo("v3");
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(0)).getComparisonOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(1)).getComparisonOperator())
                        .isEqualTo(ValueComparisonOperator.CONTAINS);
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(2)).getComparisonOperator())
                        .isEqualTo(ValueComparisonOperator.IS_NOT_EMPTY);
    }


    @Test
    public void testQueryWithManyAttributes()
    {
        List<SearchQueryCondition> conditions = Lists.newArrayList();
        conditions.add(prepareCondition("name", "v1", ValueComparisonOperator.EQUALS));
        conditions.add(prepareCondition("code", "v2", ValueComparisonOperator.CONTAINS));
        conditions.add(prepareCondition("desc", "v3", ValueComparisonOperator.IS_NOT_EMPTY));
        Map<String, IndexedProperty> indexedPropertyMap = Maps.newHashMap();
        indexedPropertyMap.put("name", prepareIndexedProperty("name", false));
        indexedPropertyMap.put("code", prepareIndexedProperty("code", false));
        indexedPropertyMap.put("desc", prepareIndexedProperty("desc", false));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        List<SolrSearchCondition> converted = this.conditionsConverter.convert(conditions, SearchQuery.Operator.AND, this.indexedType);
        Assertions.assertThat(converted.size()).isEqualTo(3);
        Assertions.assertThat((Comparable)((SolrSearchCondition)converted.get(0)).getOperator()).isEqualTo(SearchQuery.Operator.AND);
        Assertions.assertThat(containsConditionForName(converted, "name")).isTrue();
        Assertions.assertThat(containsConditionForName(converted, "code")).isTrue();
        Assertions.assertThat(containsConditionForName(converted, "desc")).isTrue();
        Assertions.assertThat(((SolrSearchCondition)converted.get(0)).getConditionValues().size()).isEqualTo(1);
        Assertions.assertThat(((SolrSearchCondition)converted.get(1)).getConditionValues().size()).isEqualTo(1);
        Assertions.assertThat(((SolrSearchCondition)converted.get(2)).getConditionValues().size()).isEqualTo(1);
        Assertions.assertThat(containsConditionValue(converted, "v1")).isTrue();
        Assertions.assertThat(containsConditionValue(converted, "v2")).isTrue();
        Assertions.assertThat(containsConditionValue(converted, "v3")).isTrue();
    }


    @Test
    public void testWithLocalizedAttribute()
    {
        Map<String, IndexedProperty> indexedPropertyMap = Maps.newHashMap();
        indexedPropertyMap.put("name", prepareIndexedProperty("name", true));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        Map<Locale, Object> localizedValue = Maps.newHashMap();
        localizedValue.put(Locale.ENGLISH, "v1");
        List<SearchQueryCondition> conditions = Lists.newArrayList();
        conditions.add(prepareCondition("name", localizedValue, ValueComparisonOperator.EQUALS));
        List<SolrSearchCondition> converted = this.conditionsConverter.convert(conditions, SearchQuery.Operator.OR, this.indexedType);
        Assertions.assertThat(converted.size()).isEqualTo(1);
        Assertions.assertThat(((SolrSearchCondition)converted.get(0)).getAttributeName()).isEqualTo("name");
        Assertions.assertThat(((SolrSearchCondition)converted.get(0)).getConditionValues().size()).isEqualTo(1);
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)converted.get(0)).getConditionValues().get(0)).getValue(Locale.ENGLISH)).isEqualTo("v1");
    }


    @Test
    public void testWithInnerConditions()
    {
        List<SearchQueryCondition> conditions = Lists.newArrayList();
        conditions.add(prepareCondition("name", "v1", ValueComparisonOperator.EQUALS));
        SearchQueryConditionList inner = new SearchQueryConditionList();
        inner.setConditions(Lists.newArrayList((Object[])new SearchQueryCondition[] {prepareCondition("code", "v2", ValueComparisonOperator.CONTAINS),
                        prepareCondition("desc", "v3", ValueComparisonOperator.OR)}));
        conditions.add(inner);
        Map<String, IndexedProperty> indexedPropertyMap = Maps.newHashMap();
        indexedPropertyMap.put("name", prepareIndexedProperty("name", false));
        indexedPropertyMap.put("code", prepareIndexedProperty("code", false));
        indexedPropertyMap.put("desc", prepareIndexedProperty("desc", false));
        Mockito.when(this.indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        List<SolrSearchCondition> converted = this.conditionsConverter.convert(conditions, SearchQuery.Operator.AND, this.indexedType);
        Assertions.assertThat(converted.size()).isEqualTo(2);
        SolrSearchCondition innerConverted = extractInnerCondition(converted);
        Assertions.assertThat(innerConverted).isNotNull();
        Assertions.assertThat(innerConverted.isNestedCondition()).isTrue();
        Assertions.assertThat(innerConverted.getNestedConditions().size()).isEqualTo(2);
        Assertions.assertThat((Comparable)innerConverted.getOperator()).isEqualTo(SearchQuery.Operator.OR);
        Assertions.assertThat(containsConditionForName(innerConverted.getNestedConditions(), "code")).isTrue();
        Assertions.assertThat(containsConditionForName(innerConverted.getNestedConditions(), "desc")).isTrue();
        Assertions.assertThat(containsConditionValue(innerConverted.getNestedConditions(), "v2")).isTrue();
        Assertions.assertThat(containsConditionValue(innerConverted.getNestedConditions(), "v3")).isTrue();
    }


    protected SolrSearchCondition extractInnerCondition(List<SolrSearchCondition> conditions)
    {
        Optional<SolrSearchCondition> first = conditions.stream().filter(SolrSearchCondition::isNestedCondition).findFirst();
        return first.isPresent() ? first.get() : null;
    }


    protected boolean containsConditionForName(List<SolrSearchCondition> conditions, String name)
    {
        return conditions.stream().filter(c -> c.getAttributeName().equals(name)).findAny().isPresent();
    }


    protected boolean containsConditionValue(List<SolrSearchCondition> conditions, Object value)
    {
        return conditions.stream()
                        .filter(c -> c.getConditionValues().stream().filter(()).findAny().isPresent())
                        .findAny().isPresent();
    }


    protected IndexedProperty prepareIndexedProperty(String attrName, boolean localized)
    {
        IndexedProperty indexedProperty = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        Mockito.when(indexedProperty.getName()).thenReturn(attrName);
        Mockito.when(Boolean.valueOf(indexedProperty.isLocalized())).thenReturn(Boolean.valueOf(localized));
        return indexedProperty;
    }


    protected SearchQueryCondition prepareCondition(String attrName, Object value, ValueComparisonOperator operator)
    {
        return new SearchQueryCondition(new SearchAttributeDescriptor(attrName), value, operator);
    }
}
