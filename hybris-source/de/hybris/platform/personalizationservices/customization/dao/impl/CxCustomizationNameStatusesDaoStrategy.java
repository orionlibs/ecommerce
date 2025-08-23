package de.hybris.platform.personalizationservices.customization.dao.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CxCustomizationNameStatusesDaoStrategy extends AbstractCxCustomizationDaoStrategy implements CxCustomizationDaoStrategy
{
    private static final Set<String> BASE_PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"statuses"}));
    private static final Set<String> EX_PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"name", "statuses"}));
    private boolean nameRequired = false;


    public Set<String> getRequiredParameters()
    {
        return this.nameRequired ? EX_PARAMS : BASE_PARAMS;
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params)
    {
        return getQuery(params, null);
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params, Map<String, Object> queryParams)
    {
        String name = getName(params);
        String statuses = params.get("statuses");
        String query = "SELECT {pk} FROM {CxCustomization } WHERE {catalogVersion} " + getMulticountryWhereOperator(queryParams) + "AND LOWER({name}) LIKE LOWER(?name) AND {status} in (?statuses) ORDER BY " + buildOrderByForMulticountry(queryParams) + " {groupPOS} ASC ";
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("name", name);
        extraParams.put("statuses", getStatusesForCodesStr(statuses));
        return getCxDaoQueryBuilder().buildQuery(query, extraParams);
    }


    protected String getName(Map<String, String> params)
    {
        if(this.nameRequired)
        {
            return "%" + (String)StringUtils.defaultIfEmpty(params.get("name"), "") + "%";
        }
        return "%";
    }


    public void setNameRequired(boolean nameRequired)
    {
        this.nameRequired = nameRequired;
    }
}
