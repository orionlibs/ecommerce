/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

/**
 * Represents the search query criteria that is used by search logic controller to build query.
 */
public class SearchQueryCondition
{
    private SearchAttributeDescriptor descriptor;
    private Object value;
    private ValueComparisonOperator operator;
    private boolean filteringCondition = false;


    public SearchQueryCondition()
    {
        // NOP
    }


    public SearchQueryCondition(final SearchAttributeDescriptor descriptor, final Object value,
                    final ValueComparisonOperator operator)
    {
        this(descriptor, value, operator, false);
    }


    public SearchQueryCondition(final SearchAttributeDescriptor descriptor, final Object value,
                    final ValueComparisonOperator operator, final boolean filteringCondition)
    {
        this.descriptor = descriptor;
        this.value = value;
        this.operator = operator;
        this.filteringCondition = filteringCondition;
    }


    public SearchAttributeDescriptor getDescriptor()
    {
        return descriptor;
    }


    public void setDescriptor(final SearchAttributeDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }


    public Object getValue()
    {
        return value;
    }


    public void setValue(final Object value)
    {
        this.value = value;
    }


    public ValueComparisonOperator getOperator()
    {
        return operator;
    }


    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }


    /**
     * Tells if this criteria should be used as filtering criteria if feasible. Some search engines are able to narrow the
     * dataset on which the actual search will be executed using such filtering criterias. It also means all filtering
     * criterias will be logically connected with other criterias with AND operator.
     *
     * @return if true this criteria should be used as filtering criteria
     */
    public boolean isFilteringCondition()
    {
        return filteringCondition;
    }


    public void setFilteringCondition(final boolean filteringCondition)
    {
        this.filteringCondition = filteringCondition;
    }


    public String getPartialHash()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDescriptor() != null ? getDescriptor().getNameAndNumberHash() : 0)
                        .append(getValue() != null ? getValue().hashCode() : 0)
                        .append(getOperator() != null ? getOperator().getOperatorCode() : "");
        return String.valueOf(stringBuilder.toString().hashCode());
    }
}
