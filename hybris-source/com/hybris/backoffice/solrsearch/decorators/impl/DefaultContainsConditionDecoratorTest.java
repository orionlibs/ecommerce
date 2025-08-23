package com.hybris.backoffice.solrsearch.decorators.impl;

import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DefaultContainsConditionDecoratorTest
{
    private static final String CONTAINS_ATTR_NAME = "attrCont";
    private static final String EQUALS_ATTR_NAME = "attrEq";
    private static final String CONTAINS_VALUE = "cont";
    private static final String EQUALS_VALUE = "eq";
    private DefaultContainsConditionDecorator decorator;


    @Before
    public void setUp()
    {
        this.decorator = new DefaultContainsConditionDecorator();
    }


    @Test
    public void shouldDecorateContainsCondition()
    {
        SearchConditionData conditionData = prepareSearchConditionDataWithQueryCondition();
        this.decorator.decorate(conditionData, null, null);
        Map<String, SolrSearchCondition> decoratedConditionsByAttributeNames = (Map<String, SolrSearchCondition>)conditionData.getQueryConditions().stream().collect(Collectors.toMap(SolrSearchCondition::getAttributeName, Function.identity()));
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues()).hasSize(2);
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues())
                        .allMatch(conditionValue -> "cont".equals(conditionValue.getValue()));
        Assertions.assertThat((List)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues().stream()
                                        .map(SolrSearchCondition.ConditionValue::getComparisonOperator).collect(Collectors.toList()))
                        .containsExactlyInAnyOrder((Object[])new ValueComparisonOperator[] {ValueComparisonOperator.EQUALS, ValueComparisonOperator.CONTAINS});
    }


    @Test
    public void shouldDecorateContainsFQCondition()
    {
        SearchConditionData conditionData = prepareSearchConditionDataWithFilterQueryCondition();
        this.decorator.decorate(conditionData, null, null);
        Map<String, SolrSearchCondition> decoratedConditionsByAttributeNames = (Map<String, SolrSearchCondition>)conditionData.getFilterQueryConditions().stream().collect(Collectors.toMap(SolrSearchCondition::getAttributeName, Function.identity()));
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues()).hasSize(2);
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues())
                        .allMatch(conditionValue -> "cont".equals(conditionValue.getValue()));
        Assertions.assertThat((List)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrCont")).getConditionValues().stream()
                                        .map(SolrSearchCondition.ConditionValue::getComparisonOperator).collect(Collectors.toList()))
                        .containsExactlyInAnyOrder((Object[])new ValueComparisonOperator[] {ValueComparisonOperator.EQUALS, ValueComparisonOperator.CONTAINS});
    }


    @Test
    public void shouldNotDecorateEqualsCondition()
    {
        SearchConditionData conditionData = prepareSearchConditionDataWithQueryCondition();
        this.decorator.decorate(conditionData, null, null);
        Map<String, SolrSearchCondition> decoratedConditionsByAttributeNames = (Map<String, SolrSearchCondition>)conditionData.getQueryConditions().stream().collect(Collectors.toMap(SolrSearchCondition::getAttributeName, Function.identity()));
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues()).hasSize(1);
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues().get(0)).getComparisonOperator())
                        .isEqualTo(ValueComparisonOperator.EQUALS);
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues().get(0)).getValue())
                        .isEqualTo("eq");
    }


    @Test
    public void shouldNotDecorateEqualsFQCondition()
    {
        SearchConditionData conditionData = prepareSearchConditionDataWithFilterQueryCondition();
        this.decorator.decorate(conditionData, null, null);
        Map<String, SolrSearchCondition> decoratedConditionsByAttributeNames = (Map<String, SolrSearchCondition>)conditionData.getFilterQueryConditions().stream().collect(Collectors.toMap(SolrSearchCondition::getAttributeName, Function.identity()));
        Assertions.assertThat(((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues()).hasSize(1);
        Assertions.assertThat((Comparable)((SolrSearchCondition.ConditionValue)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues().get(0)).getComparisonOperator())
                        .isEqualTo(ValueComparisonOperator.EQUALS);
        Assertions.assertThat(((SolrSearchCondition.ConditionValue)((SolrSearchCondition)decoratedConditionsByAttributeNames.get("attrEq")).getConditionValues().get(0)).getValue())
                        .isEqualTo("eq");
    }


    private SolrSearchCondition createContainsCondition(String attributeName, String value)
    {
        SolrSearchCondition result = new SolrSearchCondition(attributeName, "type", SearchQuery.Operator.AND);
        result.addConditionValue(value, ValueComparisonOperator.CONTAINS);
        return result;
    }


    private SolrSearchCondition createEqualsCondition(String attributeName, String value)
    {
        SolrSearchCondition result = new SolrSearchCondition(attributeName, "type", SearchQuery.Operator.AND);
        result.addConditionValue(value, ValueComparisonOperator.EQUALS);
        return result;
    }


    private SearchConditionData prepareSearchConditionDataWithQueryCondition()
    {
        SearchConditionData conditionData = new SearchConditionData();
        conditionData.addQueryCondition(createContainsCondition("attrCont", "cont"));
        conditionData.addQueryCondition(createEqualsCondition("attrEq", "eq"));
        return conditionData;
    }


    private SearchConditionData prepareSearchConditionDataWithFilterQueryCondition()
    {
        SearchConditionData conditionData = new SearchConditionData();
        conditionData.addFilterQueryCondition(createContainsCondition("attrCont", "cont"));
        conditionData.addFilterQueryCondition(createEqualsCondition("attrEq", "eq"));
        return conditionData;
    }
}
