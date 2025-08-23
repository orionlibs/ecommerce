/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.util;

import com.hybris.cockpitng.search.data.SearchQueryCondition;
import java.util.Collection;

/**
 * Utility class for CockpitNG SearchQuery related tasks
 *
 * @deprecated since 6.7, the class will be removed
 */
@Deprecated(since = "6.7", forRemoval = true)
public final class SearchQueryUtil
{
    private SearchQueryUtil()
    {
        //Utility class
    }


    /**
     * Checks if all conditions in the collection have {@linkplain SearchQueryCondition#filteringCondition} flag set to
     * {@code true}
     *
     * @return {@code false} if at least one condition in the collection has {@code filteringCondition} flag set to
     *         {@code false}, {@code true} otherwise
     * @deprecated since 6.7, please use instead inline invocation
     *             <code>conditions.stream().allMatch(SearchQueryCondition::isFilteringCondition)</code>
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static boolean areAllConditionsFiltering(final Collection<SearchQueryCondition> conditions)
    {
        return conditions.stream().allMatch(SearchQueryCondition::isFilteringCondition);
    }
}
