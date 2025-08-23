package com.hybris.backoffice.solrsearch.decorators;

import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import org.springframework.core.Ordered;

public interface SearchConditionDecorator extends Ordered
{
    void decorate(SearchConditionData paramSearchConditionData, SearchQueryData paramSearchQueryData, IndexedType paramIndexedType);
}
