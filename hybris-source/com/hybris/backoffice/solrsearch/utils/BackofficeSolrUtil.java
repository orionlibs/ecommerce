package com.hybris.backoffice.solrsearch.utils;

import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public final class BackofficeSolrUtil
{
    public static SearchQuery.Operator convertToSolrOperator(ValueComparisonOperator operator)
    {
        return ValueComparisonOperator.AND.equals(operator) ? SearchQuery.Operator.AND : SearchQuery.Operator.OR;
    }
}
