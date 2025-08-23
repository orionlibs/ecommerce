package de.hybris.platform.personalizationservices.customization.dao.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoStrategy;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDaoStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CxCustomizationCodeDaoStrategy extends AbstractCxDaoStrategy implements CxCustomizationDaoStrategy
{
    private static final String CODE = "code";
    private static final Set<String> PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"code"}));


    public Set<String> getRequiredParameters()
    {
        return PARAMS;
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params)
    {
        return getQuery(params, null);
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params, Map<String, Object> queryParams)
    {
        String code = "%" + (String)params.get("code") + "%";
        String query = "SELECT {pk} FROM {CxCustomization} WHERE {catalogVersion} " + getMulticountryWhereOperator(queryParams) + "AND LOWER({code}) LIKE LOWER(?code) ORDER BY " + buildOrderByForMulticountry(queryParams) + " {groupPOS} ASC ";
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("code", code);
        return getCxDaoQueryBuilder().buildQuery(query, extraParams);
    }
}
