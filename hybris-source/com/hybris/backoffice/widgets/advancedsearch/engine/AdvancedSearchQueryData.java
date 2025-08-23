/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.engine;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents search query data within advanced search widget
 *
 */
public class AdvancedSearchQueryData implements SearchQueryData
{
    private final String typeCode;
    private final List<SearchQueryCondition> conditions;
    private final ValueComparisonOperator globalOperator;
    private final int pageSize;
    private final boolean includeSubtypes;
    private final boolean tokenizable;
    private final AdvancedSearchMode advancedSearchMode;
    private final String searchQueryText;
    private final Map<String, Set<String>> selectedFacets;
    private final Optional<Boolean> caseSensitive;
    private SortData sortData;


    protected AdvancedSearchQueryData(final Builder builder)
    {
        this.typeCode = builder.typeCode;
        this.pageSize = builder.pageSize;
        this.includeSubtypes = builder.includeSubtypes;
        this.sortData = builder.sortData;
        this.globalOperator = builder.globalOperator;
        this.tokenizable = builder.tokenizable;
        this.conditions = builder.conditions;
        this.searchQueryText = builder.searchQueryText;
        this.selectedFacets = builder.selectedFacets;
        this.advancedSearchMode = builder.advancedSearchMode;
        this.caseSensitive = builder.caseSensitive;
    }


    @Override
    public Set<SearchAttributeDescriptor> getAttributes()
    {
        return getConditions(true).stream().map(SearchQueryCondition::getDescriptor).collect(Collectors.toSet());
    }


    @Override
    public String getSearchType()
    {
        return typeCode;
    }


    @Override
    public boolean isIncludeSubtypes()
    {
        return includeSubtypes;
    }


    @Override
    public int getPageSize()
    {
        return pageSize;
    }


    @Override
    public SortData getSortData()
    {
        return sortData;
    }


    @Override
    public void setSortData(final SortData sortData)
    {
        this.sortData = sortData;
    }


    @Override
    public ValueComparisonOperator getValueComparisonOperator(final SearchAttributeDescriptor attribute)
    {
        return getConditions(true).stream().filter(entry -> entry.getDescriptor().equals(attribute)).findFirst()
                        .map(SearchQueryCondition::getOperator).orElse(null);
    }


    @Override
    public Object getAttributeValue(final SearchAttributeDescriptor attribute)
    {
        return getConditions(true).stream().filter(entry -> entry.getDescriptor().equals(attribute)).findFirst()
                        .map(SearchQueryCondition::getValue).orElse(null);
    }


    @Override
    public ValueComparisonOperator getGlobalComparisonOperator()
    {
        return this.globalOperator;
    }


    @Override
    public boolean isTokenizable()
    {
        return tokenizable;
    }


    @Override
    public List<? extends SearchQueryCondition> getConditions()
    {
        return conditions;
    }


    @Override
    public String getSearchQueryText()
    {
        return this.searchQueryText;
    }


    @Override
    public Optional<Boolean> isCaseSensitive()
    {
        return this.caseSensitive;
    }


    @Override
    public String getQueryId()
    {
        String queryId = SearchQueryData.super.getQueryId();
        final AdvancedSearchMode mode = getAdvancedSearchMode();
        final AdvancedSearchMode searchMode = mode != null ? mode : AdvancedSearchMode.ADVANCED;
        if(searchMode == AdvancedSearchMode.SIMPLE)
        {
            queryId = searchMode + getSearchType() + getSearchQueryText().hashCode()
                            + getSelectedFacetsOrEmptyMap().entrySet().toString();
        }
        else if(searchMode == AdvancedSearchMode.ADVANCED)
        {
            queryId = searchMode + getSearchType() + getConditionsHash() + getGlobalComparisonOperator().getOperatorCode()
                            + includeSubtypes;
        }
        return queryId;
    }


    private Map<String, Set<String>> getSelectedFacetsOrEmptyMap()
    {
        if(getSelectedFacets() == null || getSelectedFacets().values().stream()
                        .allMatch(facetValues -> facetValues == null || facetValues.isEmpty()))
        {
            return Collections.emptyMap();
        }
        else
        {
            return getSelectedFacets();
        }
    }


    protected int getConditionsHash()
    {
        final Set<SearchQueryCondition> flatConditions = getConditions(true);
        return flatConditions != null
                        ? flatConditions.stream()
                        .sorted(Comparator.comparing(comparedObject -> comparedObject.getDescriptor().getAttributeName()))
                        .map(SearchQueryCondition::getPartialHash).reduce((currentResult, addition) -> currentResult + addition)
                        .orElse(StringUtils.EMPTY).hashCode()
                        : 0;
    }


    @Override
    public Map<String, Set<String>> getSelectedFacets()
    {
        return selectedFacets;
    }


    public AdvancedSearchMode getAdvancedSearchMode()
    {
        return advancedSearchMode;
    }


    public static class Builder
    {
        private final String typeCode;
        private final List<SearchQueryCondition> conditions = Lists.newArrayList();
        private int pageSize;
        private boolean includeSubtypes;
        private SortData sortData;
        private ValueComparisonOperator globalOperator;
        private boolean tokenizable;
        private String searchQueryText;
        private AdvancedSearchMode advancedSearchMode;
        private Map<String, Set<String>> selectedFacets;
        private Optional<Boolean> caseSensitive = Optional.empty();


        public Builder(final String typeCode)
        {
            this.typeCode = typeCode;
        }


        public Builder(final SearchQueryData queryData)
        {
            this(queryData.getSearchType());
            sortData = queryData.getSortData();
            globalOperator = queryData.getGlobalComparisonOperator();
            pageSize = queryData.getPageSize();
            searchQueryText = queryData.getSearchQueryText();
            tokenizable = queryData.isTokenizable();
            includeSubtypes = queryData.isIncludeSubtypes();
            selectedFacets = queryData.getSelectedFacets();
            caseSensitive = queryData.isCaseSensitive();
            if(queryData instanceof AdvancedSearchQueryData)
            {
                advancedSearchMode = ((AdvancedSearchQueryData)queryData).getAdvancedSearchMode();
            }
        }


        public Builder conditions(final List<? extends SearchQueryCondition> conditions)
        {
            this.conditions.addAll(conditions);
            return this;
        }


        public Builder conditions(final SearchQueryCondition... conditions)
        {
            Collections.addAll(this.conditions, conditions);
            return this;
        }


        public Builder pageSize(final int pageSize)
        {
            this.pageSize = pageSize;
            return this;
        }


        public Builder includeSubtypes(final boolean includeSubtypes)
        {
            this.includeSubtypes = includeSubtypes;
            return this;
        }


        public Builder sortData(final SortData sortData)
        {
            this.sortData = sortData;
            return this;
        }


        public Builder globalOperator(final ValueComparisonOperator globalOperator)
        {
            this.globalOperator = globalOperator;
            return this;
        }


        public Builder tokenizable(final boolean tokenizable)
        {
            this.tokenizable = tokenizable;
            return this;
        }


        public Builder searchQueryText(final String searchQueryText)
        {
            this.searchQueryText = StringUtils.trim(searchQueryText);
            return this;
        }


        public Builder selectedFacets(final Map<String, Set<String>> selectedFacets)
        {
            if(selectedFacets != null)
            {
                this.selectedFacets = new HashMap<>();
            }
            this.selectedFacets = selectedFacets;
            return this;
        }


        public Builder advancedSearchMode(final AdvancedSearchMode advancedSearchMode)
        {
            this.advancedSearchMode = advancedSearchMode;
            return this;
        }


        public Builder caseSensitive(final Boolean sensitive)
        {
            this.caseSensitive = sensitive == null ? Optional.empty() : Optional.of(sensitive);
            return this;
        }


        public AdvancedSearchQueryData build()
        {
            return new AdvancedSearchQueryData(this);
        }
    }
}
