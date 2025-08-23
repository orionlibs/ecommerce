/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents the search data that is used by search logic controller to build query.
 */
public interface SearchQueryData extends IdentifiableSearchQuery
{
    /**
     * Returns set of current search attributes - {@link SearchAttributeDescriptor}
     *
     * 	/**
     * 	 * Returns set of current search attributes - {@link SearchAttributeDescriptor}
     * 	 *
     * @return current search attributes
     */
    Set<SearchAttributeDescriptor> getAttributes();


    /**
     * Returns current search type
     *
     * @return search type
     */
    String getSearchType();


    /**
     * Whether subtypes should be taken into account in search resulsts
     *
     * @return if true all subtypes should be taken into accoutn in search results
     */
    boolean isIncludeSubtypes();


    /**
     * Returns current page size
     *
     * @return current page size
     */
    int getPageSize();


    /**
     * Returns sort information
     *
     * @return sort information
     */
    SortData getSortData();


    /**
     * Sets given sort data information
     *
     * @param sortData sort data information
     */
    void setSortData(final SortData sortData);


    /**
     * Returns search operator for given attribute i.e. {@link SearchAttributeDescriptor}
     *
     * @param attribute given search attribute
     * @return appropriate search operator
     */
    ValueComparisonOperator getValueComparisonOperator(final SearchAttributeDescriptor attribute);


    /**
     * Returns value for a given attribute i.e. {@link SearchAttributeDescriptor}
     *
     * @param attribute given search attribute
     * @return appropriate search operator
     */
    Object getAttributeValue(final SearchAttributeDescriptor attribute);


    /**
     * Returns global operator i.e. how conditions are connected are ther OR-ed or AND-ed
     *
     * @return global operator
     */
    ValueComparisonOperator getGlobalComparisonOperator();


    /**
     * @return whether condition value should be tokenized based on special characters and converted to multiple
     *         conditions
     */
    boolean isTokenizable();


    /**
     * @return condition collection
     */
    default List<? extends SearchQueryCondition> getConditions()
    {
        return Collections.emptyList();
    }


    /**
     * @return simple search user query
     */
    default String getSearchQueryText()
    {
        return StringUtils.EMPTY;
    }


    /**
     * Returns selected facets.
     *
     * @return map of facets names with selected values.
     */
    default Map<String, Set<String>> getSelectedFacets()
    {
        return Collections.emptyMap();
    }


    /**
     * @return true in case the query should be case-sensitive. If unset, system-defaults will be applied.
     */
    default Optional<Boolean> isCaseSensitive()
    {
        return Optional.empty();
    }


    /**
     * @param flatRepresentation if true SearchQueryConditionList is falter
     * @return Set of condition
     */
    default Set<SearchQueryCondition> getConditions(final boolean flatRepresentation)
    {
        final Set<SearchQueryCondition> ret = Sets.newHashSet();
        for(final SearchQueryCondition aCondition : getConditions())
        {
            if(flatRepresentation)
            {
                ret.addAll(getConditionsInternal(aCondition));
            }
            else
            {
                ret.add(aCondition);
            }
        }
        return ret;
    }


    private Set<SearchQueryCondition> getConditionsInternal(final SearchQueryCondition searchQueryCondition)
    {
        final Set<SearchQueryCondition> ret = Sets.newHashSet();
        if(searchQueryCondition instanceof SearchQueryConditionList)
        {
            for(final SearchQueryCondition aCondition : ((SearchQueryConditionList)searchQueryCondition).getConditions())
            {
                ret.addAll(getConditionsInternal(aCondition));
            }
        }
        else
        {
            ret.add(searchQueryCondition);
        }
        return ret;
    }
}
