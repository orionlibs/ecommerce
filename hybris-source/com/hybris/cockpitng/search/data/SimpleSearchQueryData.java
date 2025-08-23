/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO for simple search query data
 */
public class SimpleSearchQueryData implements SearchQueryData
{
    private final String typeCode;
    private final Set<SearchAttributeDescriptor> attributes;
    private String searchQueryText;
    private SortData sortData;
    private ValueComparisonOperator valueComparisonOperator;
    private int pageSize;


    public SimpleSearchQueryData(final String typeCode)
    {
        super();
        this.typeCode = typeCode;
        this.attributes = new LinkedHashSet<>();
        this.valueComparisonOperator = ValueComparisonOperator.CONTAINS;
        this.pageSize = 50;
    }


    public SimpleSearchQueryData(final String typeCode, final int pageSize)
    {
        this(typeCode);
        this.pageSize = pageSize;
    }


    public SimpleSearchQueryData(final String typeCode, final Collection<SearchAttributeDescriptor> attributes,
                    final String textQuery)
    {
        this(typeCode);
        this.attributes.addAll(attributes);
        this.searchQueryText = StringUtils.trim(textQuery);
    }


    public SimpleSearchQueryData(final String searchQueryText, final List<SearchAttributeDescriptor> attributes,
                    final String typeCode, final SortData sortData, final int pageSize)
    {
        this(typeCode, attributes, searchQueryText);
        this.sortData = sortData;
        this.pageSize = pageSize;
    }


    /**
     * @return the searchQueryText
     */
    @Override
    public String getSearchQueryText()
    {
        return searchQueryText;
    }


    /**
     * @param searchQueryText
     *           the searchQueryText to set
     */
    public void setSearchQueryText(final String searchQueryText)
    {
        this.searchQueryText = StringUtils.trim(searchQueryText);
    }


    /**
     * @return the attributes
     */
    @Override
    public Set<SearchAttributeDescriptor> getAttributes()
    {
        return attributes;
    }


    /**
     * @param attributes
     *           the attributes to set
     */
    public void setAttributes(final Collection<SearchAttributeDescriptor> attributes)
    {
        this.attributes.clear();
        this.attributes.addAll(attributes);
    }


    @Override
    public String getSearchType()
    {
        return typeCode;
    }


    @Override
    public boolean isIncludeSubtypes()
    {
        return true;
    }


    @Override
    public int getPageSize()
    {
        return pageSize;
    }


    /**
     * @param pageSize
     *           the pageSize to set
     */
    public void setPageSize(final int pageSize)
    {
        this.pageSize = pageSize;
    }


    @Override
    public SortData getSortData()
    {
        return sortData;
    }


    /**
     * @param sortData
     *           the sort data to set
     */
    @Override
    public void setSortData(final SortData sortData)
    {
        this.sortData = sortData;
    }


    @Override
    public ValueComparisonOperator getValueComparisonOperator(final SearchAttributeDescriptor attribute)
    {
        return valueComparisonOperator;
    }


    @Override
    public String getAttributeValue(final SearchAttributeDescriptor attribute)
    {
        return searchQueryText;
    }


    /**
     * @param operator
     *           the operator to set
     */
    public void setValueComparisonOperator(final ValueComparisonOperator operator)
    {
        this.valueComparisonOperator = operator;
    }


    @Override
    public boolean isTokenizable()
    {
        return true;
    }


    @Override
    public List<SearchQueryCondition> getConditions()
    {
        return getAttributes().stream().map(
                                        attribute -> new SearchQueryCondition(attribute, getAttributeValue(attribute), getValueComparisonOperator(attribute)))
                        .collect(Collectors.toList());
    }


    @Override
    public ValueComparisonOperator getGlobalComparisonOperator()
    {
        return ValueComparisonOperator.OR;
    }


    @Override
    public String getQueryId()
    {
        return String.format("%s%s", getSearchType(), getSearchQueryText() != null ? getSearchQueryText().hashCode() : 0);
    }
}
