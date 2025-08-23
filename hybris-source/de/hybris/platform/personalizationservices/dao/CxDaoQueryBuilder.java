package de.hybris.platform.personalizationservices.dao;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collection;
import java.util.Map;

public interface CxDaoQueryBuilder
{
    FlexibleSearchQuery buildQuery(String paramString, Map<String, Object> paramMap);


    FlexibleSearchQuery buildQuery(String paramString, Map<String, Object> paramMap, PaginationData paramPaginationData);


    FlexibleSearchQuery buildQueryFromStrategy(FlexibleSearchQuery paramFlexibleSearchQuery, Collection<? extends CxDaoStrategy> paramCollection, Map<String, String> paramMap);
}
