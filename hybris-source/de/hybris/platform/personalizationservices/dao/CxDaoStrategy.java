package de.hybris.platform.personalizationservices.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Map;
import java.util.Set;

public interface CxDaoStrategy
{
    Set<String> getRequiredParameters();


    FlexibleSearchQuery getQuery(Map<String, String> paramMap);


    default FlexibleSearchQuery getQuery(Map<String, String> params, Map<String, Object> queryParams)
    {
        return getQuery(params);
    }
}
