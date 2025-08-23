package com.hybris.backoffice.solrsearch.converters.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class DefaultSearchConditionDataConverterTest
{
    public static final String STRING_CONDITION_TYPE = "string";
    private static final String ATTR_CODE = "code";
    private static final String ATTR_DESC = "desc";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_USER_QUERY = "noFQUserQuery";
    private static final String ATTR_CATALOG_VERSION = "catalogVersion";
    @InjectMocks
    private DefaultSearchConditionDataConverter conditionsConverter;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.conditionsConverter.setFqApplicableOperators(Sets.newHashSet((Object[])new ValueComparisonOperator[] {ValueComparisonOperator.EQUALS}));
        this.conditionsConverter.setFqApplicablePropertiesTypes(Sets.newHashSet((Object[])new SolrPropertiesTypes[] {SolrPropertiesTypes.LONG, SolrPropertiesTypes.STRING}));
    }


    @Test
    public void testConvertConditions()
    {
        List<SolrSearchCondition> conditions = Lists.newArrayList();
        SolrSearchCondition condition = new SolrSearchCondition("catalogVersion", "string", SearchQuery.Operator.AND);
        condition.addConditionValue("fq", ValueComparisonOperator.EQUALS);
        conditions.add(condition);
        condition = new SolrSearchCondition("code", "string", SearchQuery.Operator.AND);
        condition.addConditionValue("noFQWrongOperator", ValueComparisonOperator.CONTAINS);
        conditions.add(condition);
        condition = new SolrSearchCondition("desc", "text", SearchQuery.Operator.AND);
        condition.addConditionValue("noFQNotString", ValueComparisonOperator.AND);
        conditions.add(condition);
        SolrSearchCondition nestedFromSearch = new SolrSearchCondition(conditions, SearchQuery.Operator.AND);
        SolrSearchCondition simpleTextQuery = new SolrSearchCondition("noFQUserQuery", "text", SearchQuery.Operator.AND);
        SolrSearchCondition nestedSimpleWitFromSearch = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {nestedFromSearch, simpleTextQuery}, ), SearchQuery.Operator.AND);
        SolrSearchCondition type = new SolrSearchCondition("type", "string", SearchQuery.Operator.AND);
        type.addConditionValue("product", ValueComparisonOperator.EQUALS);
        SolrSearchCondition topWithType = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {nestedSimpleWitFromSearch, type}, ), SearchQuery.Operator.AND);
        SearchConditionData searchConditionData = this.conditionsConverter.convertConditions(Lists.newArrayList((Object[])new SolrSearchCondition[] {topWithType}, ), SearchQuery.Operator.AND);
        Assertions.assertThat(searchConditionData.getFilterQueryConditions()).hasSize(2);
        Assertions.assertThat(hasConditionForAttribute(searchConditionData.getFilterQueryConditions(), "type")).isTrue();
        Assertions.assertThat(hasConditionForAttribute(searchConditionData.getFilterQueryConditions(), "catalogVersion")).isTrue();
        Assertions.assertThat(searchConditionData.getQueryConditions()).hasSize(3);
        Assertions.assertThat(hasConditionForAttribute(searchConditionData.getQueryConditions(), "code")).isTrue();
        Assertions.assertThat(hasConditionForAttribute(searchConditionData.getQueryConditions(), "desc")).isTrue();
        Assertions.assertThat(hasConditionForAttribute(searchConditionData.getQueryConditions(), "noFQUserQuery")).isTrue();
    }


    @Test
    public void testFlattenedConditions()
    {
        SolrSearchCondition A = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.AND);
        SolrSearchCondition B = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.AND);
        SolrSearchCondition C = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.OR);
        SolrSearchCondition D = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.OR);
        SolrSearchCondition E = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.AND);
        SolrSearchCondition F = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.AND);
        SolrSearchCondition G = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.OR);
        SolrSearchCondition H = new SolrSearchCondition(generateRandomName(), "string", SearchQuery.Operator.OR);
        SolrSearchCondition nestedBCD = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {B, C, D}, ), SearchQuery.Operator.AND);
        SolrSearchCondition nestedEF = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {E, F}, ), SearchQuery.Operator.OR);
        SolrSearchCondition nestedGH = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {G, H}, ), SearchQuery.Operator.AND);
        SolrSearchCondition combinedNested = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {nestedEF, nestedGH}, ), SearchQuery.Operator.AND);
        SolrSearchCondition query = new SolrSearchCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {A, nestedBCD, combinedNested}, ), SearchQuery.Operator.AND);
        List<SolrSearchCondition> flattened = this.conditionsConverter.flattenConditions(Lists.newArrayList((Object[])new SolrSearchCondition[] {query}));
        Assertions.assertThat(flattened).hasSize(8);
    }


    protected String generateRandomName()
    {
        return Long.toString(System.nanoTime(), 24);
    }


    protected boolean hasConditionForAttribute(List<SolrSearchCondition> conditions, String attributeName)
    {
        return conditions.stream().filter(c -> c.getAttributeName().equals(attributeName)).findAny().isPresent();
    }
}
