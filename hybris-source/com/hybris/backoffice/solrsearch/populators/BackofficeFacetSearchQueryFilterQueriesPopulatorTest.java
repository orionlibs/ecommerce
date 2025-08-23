package com.hybris.backoffice.solrsearch.populators;

import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeFacetSearchQueryFilterQueriesPopulatorTest
{
    private static final SearchQuery.Operator CONDITION_VALUE_LINKING_OPERATOR = SearchQuery.Operator.OR;
    private static final String CONDITION_ATTRIBUTE_NAME1 = "someAttribute1";
    private static final String CONDITION_ATTRIBUTE_NAME2 = "someAttribute2";
    private static final Long CONDITION_ATTRIBUTE_VALUE1 = Long.valueOf(1234L);
    private static final Long CONDITION_ATTRIBUTE_VALUE2 = Long.valueOf(5678L);
    private static final String CONDITION_ATTRIBUTE_VALUE_PART1 = "multiple";
    private static final String CONDITION_ATTRIBUTE_VALUE_PART2 = "words";
    private static final String CONDITION_ATTRIBUTE_VALUE_PART3 = "value";
    private static final String CONDITION_ATTRIBUTE_VALUE_MULTIWORD = "multiple words value";
    private static final String FQ_CONDITION_GREATER = " TO *";
    private static final String FQ_CONDITION_LESS = "* TO ";
    private static final String FQ_CONDITION_AND = " AND ";
    private static final String FQ_CONDITION_BETWEEN_INCLUSIVE_PREFIX = "[";
    private static final String FQ_CONDITION_BETWEEN_INCLUSIVE_SUFFIX = "]";
    private static final String FQ_CONDITION_BETWEEN_EXCLUSIVE_PREFIX = "{";
    private static final String FQ_CONDITION_BETWEEN_EXCLUSIVE_SUFFIX = "}";
    private static final String WILDCARD_ANY_STRING = "*";
    private static final String WILDCARD_EMPTY_STRING = "\"\"";
    public static final String FQ_CONDITION_NOT = "!";
    @InjectMocks
    private final BackofficeFacetSearchQueryFilterQueriesPopulator populator = new BackofficeFacetSearchQueryFilterQueriesPopulator();
    private final SearchQuery searchQuery;
    @Mock
    private FieldNameTranslator fieldNameTranslator;
    @Mock
    private FieldNamePostProcessor fieldNamePostProcessor;
    @Mock
    private Map<String, Function<Serializable, String>> conditionValueConverterMap;
    private List<String> queries;


    public BackofficeFacetSearchQueryFilterQueriesPopulatorTest()
    {
        SolrSearchCondition solrSearchCondition1 = new SolrSearchCondition("someAttribute1", "long", CONDITION_VALUE_LINKING_OPERATOR);
        solrSearchCondition1.addConditionValue(CONDITION_ATTRIBUTE_VALUE1, ValueComparisonOperator.EQUALS);
        solrSearchCondition1.addConditionValue(CONDITION_ATTRIBUTE_VALUE2, ValueComparisonOperator.EQUALS);
        SolrSearchCondition solrSearchCondition2 = new SolrSearchCondition("someAttribute2", "long", CONDITION_VALUE_LINKING_OPERATOR);
        solrSearchCondition2.addConditionValue(CONDITION_ATTRIBUTE_VALUE1, ValueComparisonOperator.EQUALS);
        solrSearchCondition2.addConditionValue(CONDITION_ATTRIBUTE_VALUE2, ValueComparisonOperator.EQUALS);
        SearchConditionData searchConditionData = new SearchConditionData();
        searchConditionData.addFilterQueryCondition(solrSearchCondition1);
        searchConditionData.addFilterQueryCondition(solrSearchCondition2);
        this.searchQuery = (SearchQuery)new BackofficeSearchQuery(new FacetSearchConfig(), new IndexedType());
        ((BackofficeSearchQuery)this.searchQuery).setSearchConditionData(searchConditionData);
    }


    private static String getExpectedFQValue(String attributeName)
    {
        return attributeName.concat(":")
                        .concat("(")
                        .concat("\"").concat(CONDITION_ATTRIBUTE_VALUE1.toString())
                        .concat("\"").concat(CONDITION_VALUE_LINKING_OPERATOR.getName())
                        .concat("\"").concat(CONDITION_ATTRIBUTE_VALUE2.toString())
                        .concat("\"")
                        .concat(")");
    }


    @Before
    public void setUp()
    {
        this.queries = new ArrayList<>();
        Mockito.when(this.conditionValueConverterMap.getOrDefault(Matchers.any(String.class), (Function<Serializable, String>)Matchers.any()))
                        .thenReturn(serializable -> Objects.toString(serializable, ""));
    }


    @Test
    public void shouldAddRawQueries()
    {
        Mockito.when(this.fieldNameTranslator.translate(this.searchQuery, "someAttribute1", FieldNameProvider.FieldType.INDEX))
                        .thenReturn("someAttribute1");
        Mockito.when(this.fieldNamePostProcessor.process(this.searchQuery, null, "someAttribute1")).thenReturn("someAttribute1");
        Mockito.when(this.fieldNameTranslator.translate(this.searchQuery, "someAttribute2", FieldNameProvider.FieldType.INDEX))
                        .thenReturn("someAttribute2");
        Mockito.when(this.fieldNamePostProcessor.process(this.searchQuery, null, "someAttribute2")).thenReturn("someAttribute2");
        this.populator.addRawQueries(this.searchQuery, this.queries);
        Assertions.assertThat(this.queries.size()).isEqualTo(2);
        Assertions.assertThat(getExpectedFQValue("someAttribute1")).isEqualTo(this.queries.get(0));
        Assertions.assertThat(getExpectedFQValue("someAttribute2")).isEqualTo(this.queries.get(1));
    }


    @Test
    public void shouldHandleLessOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(CONDITION_ATTRIBUTE_VALUE1);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.LESS);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("[* TO " + CONDITION_ATTRIBUTE_VALUE1 + "}");
    }


    @Test
    public void shouldHandleGreaterOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(CONDITION_ATTRIBUTE_VALUE1);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.GREATER);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("{" + CONDITION_ATTRIBUTE_VALUE1 + " TO *]");
    }


    @Test
    public void shouldHandleLessOrEqualOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(CONDITION_ATTRIBUTE_VALUE1);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.LESS_OR_EQUAL);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("[* TO " + CONDITION_ATTRIBUTE_VALUE1 + "]");
    }


    @Test
    public void shouldHandleGreaterOrEqualOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(CONDITION_ATTRIBUTE_VALUE1);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.GREATER_OR_EQUAL);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("[" + CONDITION_ATTRIBUTE_VALUE1 + " TO *]");
    }


    @Test
    public void shouldHandleContainsOperatorWithMultipleWordsConditionValue()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn("multiple words value");
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("*multiple* AND *words* AND *value*");
    }


    @Test
    public void shouldHandleContainsOperatorWithSingleWordConditionValue()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn("multiple");
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("*multiple*");
    }


    @Test
    public void shouldHandleIsEmptyOperatorWithEmptyValue()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(null);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.IS_EMPTY);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("[\"\" TO *]");
    }


    @Test
    public void shouldHandleIsNotEmptyOperatorWithEmptyValue()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Mockito.when(conditionValue.getValue()).thenReturn(null);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.IS_NOT_EMPTY);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("[\"\" TO *]");
    }


    @Test
    public void shouldHandleInOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Collection<String> collection = new ArrayList<>();
        String value = "PK001";
        collection.add("PK001");
        Mockito.when(conditionValue.getValue()).thenReturn(collection);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.IN);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("*PK001*");
    }


    @Test
    public void shouldHandleNotInOperator()
    {
        SolrSearchCondition.ConditionValue conditionValue = (SolrSearchCondition.ConditionValue)Mockito.mock(SolrSearchCondition.ConditionValue.class);
        Collection<String> collection = new ArrayList<>();
        String value = "PK001";
        collection.add("PK001");
        Mockito.when(conditionValue.getValue()).thenReturn(collection);
        Mockito.when(conditionValue.getComparisonOperator()).thenReturn(ValueComparisonOperator.NOT_IN);
        String convertedValue = this.populator.convertConditionValueToString(conditionValue);
        Assertions.assertThat(convertedValue).isEqualTo("!*PK001*");
    }


    @Test
    public void convertAttributeNameToFieldNameShouldPrefixIsEmptyQuery()
    {
        SolrSearchCondition condition = new SolrSearchCondition("someAttribute1", "long", CONDITION_VALUE_LINKING_OPERATOR);
        condition.addConditionValue(null, ValueComparisonOperator.IS_EMPTY);
        ((FieldNamePostProcessor)Mockito.doReturn("resName").when(this.fieldNamePostProcessor)).process((SearchQuery)Matchers.any(), (Locale)Matchers.any(), (String)Matchers.any());
        String fieldName = this.populator.convertAttributeNameToFieldName((SearchQuery)new BackofficeSearchQuery(new FacetSearchConfig(), new IndexedType()), condition);
        Assertions.assertThat(fieldName).isEqualTo("-resName");
    }
}
