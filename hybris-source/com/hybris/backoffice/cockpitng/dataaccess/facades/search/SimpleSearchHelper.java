/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.search;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SimpleSearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.util.Config;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleSearchHelper
{
    public static final String FLEXIBLE_SEARCH_COMPARISON_OPERATOR_TYPES = "backoffice.flexible.search.comparison.operator.%s.types";
    private static final Logger LOG = LoggerFactory.getLogger(SimpleSearchHelper.class);


    private SimpleSearchHelper()
    {
        // Do nothing.
    }


    public static boolean isSimpleSearch(final SearchQueryData searchQueryData)
    {
        if(searchQueryData instanceof AdvancedSearchQueryData)
        {
            return AdvancedSearchMode.SIMPLE == ((AdvancedSearchQueryData)searchQueryData).getAdvancedSearchMode();
        }
        return searchQueryData instanceof SimpleSearchQueryData;
    }


    /**
     * In simple search case, the default value comparison operator is <b>contains</b>, this may cause SQL query slow， so we
     * introduce some configuration to allow user to change default operator for specific types.
     *
     * @param searchQueryData
     * @param condition
     */
    public static void processDefaultComparisonOperator(final SearchQueryData searchQueryData,
                    final SearchQueryCondition condition)
    {
        if(!isSimpleSearch(searchQueryData))
        {
            LOG.debug("Current 'searchQueryData' is not simple search case.");
            return;
        }
        final String searchTypeCode = searchQueryData.getSearchType();
        final boolean isReplaced = replaceConditionOperator(ValueComparisonOperator.STARTS_WITH, searchTypeCode, condition)
                        || replaceConditionOperator(ValueComparisonOperator.ENDS_WITH, searchTypeCode, condition)
                        || replaceConditionOperator(ValueComparisonOperator.EQUALS, searchTypeCode, condition);
        if(isReplaced)
        {
            LOG.debug("Change default comparison operator to '{}' for type '{}'", condition.getOperator(), searchTypeCode);
        }
    }


    private static boolean replaceConditionOperator(final ValueComparisonOperator operator, final String searchTypeCode,
                    final SearchQueryCondition condition)
    {
        final Set<String> typeSet = Stream.of(Config.getString(
                                        String.format(FLEXIBLE_SEARCH_COMPARISON_OPERATOR_TYPES, operator.getOperatorCode().toLowerCase(Locale.ROOT)), "")
                        .trim().split(",")).map(String::trim).collect(Collectors.toSet());
        if(typeSet.contains("*") || typeSet.contains(searchTypeCode))
        {
            condition.setOperator(operator);
            LOG.debug("Matched:[{}], replace condition operator to {}.", searchTypeCode, operator);
            return true;
        }
        LOG.debug("NotMatched:[{}], nothing changed.", searchTypeCode);
        return false;
    }
}
