/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.json.ser.PolymorphicSerialization;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchConditionDataList extends SearchConditionData
{
    @JsonSerialize(using = PolymorphicSerialization.Serializer.class)
    @JsonDeserialize(using = PolymorphicSerialization.Deserializer.class)
    private List<SearchConditionData> conditions = new LinkedList<>();


    private SearchConditionDataList(final SearchConditionDataList searchConditionDataList, final int depth)
    {
        super(searchConditionDataList.getFieldType(), null, searchConditionDataList.getOperator());
        SearchConditionDataList.this.conditions = copyConditions(searchConditionDataList.getConditions(), depth);
    }


    public SearchConditionDataList(final SearchConditionDataList searchConditionDataList)
    {
        this(searchConditionDataList, 10);
    }


    private List<SearchConditionData> copyConditions(final List<SearchConditionData> conditions, final int depth)
    {
        final List<SearchConditionData> conditionList = Lists.newArrayList();
        if(depth > 0)
        {
            for(final SearchConditionData aCondition : conditions)
            {
                SearchConditionData conditionCopy = null;
                if(aCondition instanceof SearchConditionDataList)
                {
                    conditionCopy = new SearchConditionDataList((SearchConditionDataList)aCondition, depth - 1);
                }
                else
                {
                    conditionCopy = new SearchConditionData(aCondition);
                }
                conditionList.add(conditionCopy);
            }
        }
        return conditionList;
    }


    public SearchConditionDataList(final FieldType fieldType, final ValueComparisonOperator operator,
                    final List<SearchConditionData> conditions)
    {
        super(fieldType, null, operator);
        this.conditions.addAll(conditions);
    }


    @JsonCreator
    public SearchConditionDataList(@JsonProperty("operator") final ValueComparisonOperator operator,
                    @JsonProperty("conditions") final List<SearchConditionData> conditions)
    {
        super(null, null, operator);
        this.conditions.addAll(conditions);
    }


    public static SearchConditionDataList or(final List<SearchConditionData> conditions)
    {
        return new SearchConditionDataList(ValueComparisonOperator.OR, conditions);
    }


    public static SearchConditionDataList and(final List<SearchConditionData> conditions)
    {
        return new SearchConditionDataList(ValueComparisonOperator.AND, conditions);
    }


    @JsonIgnore
    public List<SearchConditionData> getConditions()
    {
        return Collections.unmodifiableList(conditions);
    }


    public void setConditions(final List<SearchConditionData> conditions)
    {
        this.conditions = conditions;
    }
}
