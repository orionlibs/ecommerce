package com.hybris.backoffice.solrsearch.converters;

import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;

public interface SearchQueryConditionsConverter
{
    List<SolrSearchCondition> convert(List<? extends SearchQueryCondition> paramList, SearchQuery.Operator paramOperator, IndexedType paramIndexedType);
}
