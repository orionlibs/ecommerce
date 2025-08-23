package com.hybris.backoffice.solrsearch.converters;

import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;

public interface SearchConditionDataConverter
{
    SearchConditionData convertConditions(List<SolrSearchCondition> paramList, SearchQuery.Operator paramOperator);
}
