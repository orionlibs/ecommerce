/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Represents the list of search query criteria that is used by search logic controller to build query.
 */
public class SearchQueryConditionList extends SearchQueryCondition
{
    private List<SearchQueryCondition> conditions;


    public SearchQueryConditionList()
    {
        // NOP
    }


    public SearchQueryConditionList(final ValueComparisonOperator operator, final SearchQueryCondition... conditions)
    {
        this(operator, Lists.newArrayList(conditions));
    }


    public SearchQueryConditionList(final ValueComparisonOperator operator, final List<SearchQueryCondition> conditions)
    {
        this.conditions = conditions;
        SearchQueryConditionList.this.setOperator(operator);
    }


    public static SearchQueryConditionList or(final SearchQueryCondition... conditions)
    {
        return new SearchQueryConditionList(ValueComparisonOperator.OR, conditions);
    }


    public static SearchQueryConditionList and(final SearchQueryCondition... conditions)
    {
        return new SearchQueryConditionList(ValueComparisonOperator.AND, conditions);
    }


    public static SearchQueryConditionList or(final List<SearchQueryCondition> conditions)
    {
        return new SearchQueryConditionList(ValueComparisonOperator.OR, conditions);
    }


    public static SearchQueryConditionList and(final List<SearchQueryCondition> conditions)
    {
        return new SearchQueryConditionList(ValueComparisonOperator.AND, conditions);
    }


    @Override
    public boolean isFilteringCondition()
    {
        return conditions.stream().allMatch(SearchQueryCondition::isFilteringCondition);
    }


    @Override
    public void setFilteringCondition(final boolean filteringCondition)
    {
        super.setFilteringCondition(filteringCondition);
        applyFilteringCondition();
    }


    public List<SearchQueryCondition> getConditions()
    {
        return conditions;
    }


    public void setConditions(final List<SearchQueryCondition> conditions)
    {
        this.conditions = conditions;
        applyFilteringCondition();
    }


    public void setConditions(final List<SearchQueryCondition>... conditions)
    {
        this.conditions = Lists.newArrayList((SearchQueryCondition[])conditions);
        applyFilteringCondition();
    }


    protected void applyFilteringCondition()
    {
        if(getConditions() != null)
        {
            getConditions().forEach(condition -> condition.setFilteringCondition(this.isFilteringCondition()));
        }
    }
}
