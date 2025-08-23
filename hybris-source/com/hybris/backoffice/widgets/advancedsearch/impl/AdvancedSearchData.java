/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.json.ser.PolymorphicSerialization;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class AdvancedSearchData
{
    public static final String ORPHANED_SEARCH_CONDITIONS_KEY = "_orphanedSearchConditions";
    @JsonSerialize(using = PolymorphicSerialization.Serializer.class)
    @JsonDeserialize(using = PolymorphicSerialization.Deserializer.class)
    private final Map<String, List<SearchConditionData>> conditions;
    @JsonSerialize(using = PolymorphicSerialization.Serializer.class)
    @JsonDeserialize(using = PolymorphicSerialization.Deserializer.class)
    private final Map<String, List<SearchConditionData>> filterQueryRawConditions;
    private SortData sortData = new SortData();
    private String typeCode;
    private Boolean includeSubtypes;
    private ValueComparisonOperator globalOperator;
    private AdvancedSearchMode advancedSearchMode;
    private String searchQueryText;
    private Map<String, Set<String>> selectedFacets;
    private boolean tokenizable = false;
    private Map<String, String> parameters = new HashMap<>();


    public AdvancedSearchData()
    {
        conditions = new LinkedHashMap<>();
        filterQueryRawConditions = new LinkedHashMap<>();
    }


    /**
     * The <code>fieldList</code> preserves the order of fields.
     */
    public AdvancedSearchData(final FieldListType fieldList)
    {
        conditions = new TreeMap<>(new FieldListSequenceComparator(fieldList));
        filterQueryRawConditions = new LinkedHashMap<>();
    }


    public AdvancedSearchData(final AdvancedSearchData advancedSearchData)
    {
        conditions = AdvancedSearchData.this.createConditionsCopy(advancedSearchData.conditions);
        filterQueryRawConditions = AdvancedSearchData.this.createConditionsCopy(advancedSearchData.filterQueryRawConditions);
        selectedFacets = AdvancedSearchData.this.createFacetsCopy(advancedSearchData.getSelectedFacets());
        if(advancedSearchData.sortData != null)
        {
            sortData.setSortAttribute(advancedSearchData.sortData.getSortAttribute());
            sortData.setAscending(advancedSearchData.sortData.isAscending());
        }
        typeCode = advancedSearchData.typeCode;
        includeSubtypes = advancedSearchData.includeSubtypes;
        globalOperator = advancedSearchData.globalOperator;
        tokenizable = advancedSearchData.tokenizable;
        searchQueryText = advancedSearchData.searchQueryText;
        advancedSearchMode = advancedSearchData.advancedSearchMode;
        parameters = ImmutableMap.copyOf(advancedSearchData.parameters);
    }


    public void addCondition(final FieldType field, final ValueComparisonOperator operator, final Object value)
    {
        addConditionToCollection(conditions, field, operator, value);
    }


    public void addFilterQueryRawCondition(final FieldType field, final ValueComparisonOperator operator, final Object value)
    {
        addConditionToCollection(filterQueryRawConditions, field, operator, value);
    }


    private static void addConditionToCollection(final Map<String, List<SearchConditionData>> target, final FieldType field,
                    final ValueComparisonOperator operator, final Object value)
    {
        if(!target.containsKey(field.getName()))
        {
            target.put(field.getName(), new ArrayList<>());
        }
        target.get(field.getName()).add(new SearchConditionData(field, value, operator));
    }


    public void addConditionList(final ValueComparisonOperator operator, final List<SearchConditionData> searchConditions)
    {
        final SearchConditionDataList listCondition = new SearchConditionDataList(operator, searchConditions);
        final List<SearchConditionData> currentConditions = this.conditions.getOrDefault(ORPHANED_SEARCH_CONDITIONS_KEY,
                        new ArrayList<>());
        currentConditions.add(listCondition);
        this.conditions.putIfAbsent(ORPHANED_SEARCH_CONDITIONS_KEY, currentConditions);
    }


    /**
     * Adds conditions to {@link #filterQueryRawConditions} map using the {@link #ORPHANED_SEARCH_CONDITIONS_KEY} key.
     *
     * @param operator
     *           list operator
     * @param searchConditions
     *           conditions to be added to {@link #filterQueryRawConditions} map
     */
    public void addFilterQueryRawConditionsList(final ValueComparisonOperator operator,
                    final List<SearchConditionData> searchConditions)
    {
        final SearchConditionDataList listCondition = new SearchConditionDataList(operator, searchConditions);
        final List<SearchConditionData> currentConditions = filterQueryRawConditions.getOrDefault(ORPHANED_SEARCH_CONDITIONS_KEY,
                        new ArrayList<>());
        currentConditions.add(listCondition);
        filterQueryRawConditions.put(ORPHANED_SEARCH_CONDITIONS_KEY, currentConditions);
    }


    public ValueComparisonOperator getGlobalOperator()
    {
        return globalOperator;
    }


    public void setGlobalOperator(final ValueComparisonOperator globalOperator)
    {
        this.globalOperator = globalOperator;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    public SortData getSortData()
    {
        return sortData;
    }


    public void setSortData(final SortData sortData)
    {
        this.sortData = sortData;
    }


    public Map<String, Set<String>> getSelectedFacets()
    {
        return selectedFacets;
    }


    public void setSelectedFacets(final Map<String, Set<String>> selectedFacets)
    {
        this.selectedFacets = selectedFacets;
    }


    public Boolean getIncludeSubtypes()
    {
        return includeSubtypes;
    }


    public void setIncludeSubtypes(final Boolean includeSubtypes)
    {
        this.includeSubtypes = includeSubtypes;
    }


    public boolean isTokenizable()
    {
        return tokenizable;
    }


    public void setTokenizable(final boolean tokenizable)
    {
        this.tokenizable = tokenizable;
    }


    public Map<String, String> getParameters()
    {
        return ImmutableMap.copyOf(parameters);
    }


    public void setParameters(final Map<String, String> parameters)
    {
        this.parameters = parameters;
    }


    public List<SearchConditionData> getConditions(final String name)
    {
        return conditions.get(name);
    }


    public List<SearchConditionData> getFilterQueryRawConditions(final String name)
    {
        return filterQueryRawConditions.get(name);
    }


    public boolean conditionsExist(final String name)
    {
        return getConditions(name) != null;
    }


    public AdvancedSearchMode getAdvancedSearchMode()
    {
        return advancedSearchMode;
    }


    public void setAdvancedSearchMode(final AdvancedSearchMode advancedSearchMode)
    {
        this.advancedSearchMode = advancedSearchMode;
    }


    public String getSearchQueryText()
    {
        return searchQueryText;
    }


    public void setSearchQueryText(final String searchQueryText)
    {
        this.searchQueryText = searchQueryText;
    }


    /**
     * @param index
     *           (0-based)
     * @return SearchConditionData with given index
     */
    public SearchConditionData getCondition(final int index)
    {
        if(index < 0)
        {
            throw new IllegalArgumentException();
        }
        int currPosition = index;
        for(final Map.Entry<String, List<SearchConditionData>> entry : conditions.entrySet())
        {
            final List<SearchConditionData> conditionsWithGivenName = entry.getValue();
            if(currPosition < conditionsWithGivenName.size())
            {
                return conditionsWithGivenName.get(currPosition);
            }
            else
            {
                currPosition -= conditionsWithGivenName.size();
            }
        }
        throw new IllegalStateException("Cannot find condition with index= " + index);
    }


    public void removeCondition(final int index)
    {
        if(index < 0)
        {
            throw new IllegalArgumentException();
        }
        int currPosition = index;
        for(final Map.Entry<String, List<SearchConditionData>> entry : conditions.entrySet())
        {
            final String fieldName = entry.getKey();
            final List<SearchConditionData> conditionsWithGivenName = entry.getValue();
            if(currPosition < conditionsWithGivenName.size())
            {
                conditionsWithGivenName.remove(currPosition);
                if(conditionsWithGivenName.isEmpty())
                {
                    conditions.remove(fieldName);
                }
                return;
            }
            else
            {
                currPosition -= conditionsWithGivenName.size();
            }
        }
    }


    /**
     * Clears simple conditions {@link #getFilterQueryRawConditions(String)} and {@link #getSelectedFacets()} stay
     * untouched.
     */
    public void clearConditions()
    {
        if(conditions != null)
        {
            conditions.clear();
        }
    }


    public Set<String> getSearchFields()
    {
        return Collections.unmodifiableSet(conditions.keySet());
    }


    public Set<String> getFilterQueryFields()
    {
        return Collections.unmodifiableSet(filterQueryRawConditions.keySet());
    }


    /**
     * Creates deep copy of passed conditions map
     *
     * @param conditionsToCopy
     *           a map with conditions to co copy
     * @return deep copy of passed map
     */
    protected Map<String, List<SearchConditionData>> createConditionsCopy(
                    final Map<String, List<SearchConditionData>> conditionsToCopy)
    {
        final Map<String, List<SearchConditionData>> copyOfConditions;
        if(conditionsToCopy instanceof TreeMap)
        {
            copyOfConditions = new TreeMap(((TreeMap)conditionsToCopy).comparator());
        }
        else
        {
            copyOfConditions = new LinkedHashMap<>();
        }
        conditionsToCopy.entrySet().stream().filter(mapEntry -> mapEntry.getValue() != null).forEach(mapEntry -> {
            final List<SearchConditionData> copyOfSearchConditionsData = mapEntry.getValue().stream()
                            .map(AdvancedSearchData::createCopy).collect(Collectors.toList());
            copyOfConditions.put(mapEntry.getKey(), copyOfSearchConditionsData);
        });
        return copyOfConditions;
    }


    private static SearchConditionData createCopy(final SearchConditionData original)
    {
        if(original instanceof SearchConditionDataList)
        {
            return new SearchConditionDataList((SearchConditionDataList)original);
        }
        else
        {
            return new SearchConditionData(original);
        }
    }


    protected Map<String, Set<String>> createFacetsCopy(final Map<String, Set<String>> selectedFacets)
    {
        if(MapUtils.isNotEmpty(selectedFacets))
        {
            return selectedFacets.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
        }
        return Collections.emptyMap();
    }


    /**
     * Provides fields order in which they occur in the <code>fieldList</code>
     */
    protected static class FieldListSequenceComparator implements Comparator<String>
    {
        private final FieldListType fieldList;


        /**
         * @param fieldList
         *           - list of all fields
         */
        public FieldListSequenceComparator(final FieldListType fieldList)
        {
            this.fieldList = fieldList;
        }


        @Override
        public int compare(final String f1, final String f2)
        {
            final int f1Index = getIndex(f1);
            final int f2Index = getIndex(f2);
            if(bothIndexesNotFound(f1Index, f2Index))
            {
                return f1.compareTo(f2);
            }
            return f1Index - f2Index;
        }


        /**
         * Returns the absolute index of a <code>field</code> in the <code>fieldList</code>. When field is not found then
         * an index after the last element in <code>fieldList</code> is returned.
         *
         * @param field
         *           - field name
         * @return absolute index of a <code>field</code> in the <code>fieldList</code>
         */
        protected int getIndex(final String field)
        {
            for(int index = 0; index < fieldList.getField().size(); ++index)
            {
                if(field.equals(fieldList.getField().get(index).getName()))
                {
                    return index;
                }
            }
            return fieldList.getField().size();
        }


        private boolean bothIndexesNotFound(final int index1, final int index2)
        {
            return index1 == fieldList.getField().size() && index2 == fieldList.getField().size();
        }
    }
}
