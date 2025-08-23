package de.hybris.platform.platformbackoffice.dao.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.dao.BackofficeSavedQueryDAO;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeSavedQueryDAO implements BackofficeSavedQueryDAO
{
    private static final String FIND_BY_USER_AND_GROUPS_QUERY = String.format("SELECT DISTINCT {p:%s},{p:%s} FROM {%s AS p left join BackofficeSavedQuery2UserGroupRelation as rel on {p:%s}={rel:source}} WHERE {p:%s}=?%s or {rel:target} in (?%s) order by {p:%s} desc",
                    new Object[] {"pk", "modifiedtime", "BackofficeSavedQuery", "pk", "queryOwner", "queryOwner", "userGroups", "modifiedtime"});
    private static final String FIND_BY_USER_QUERY = String.format("SELECT DISTINCT {p:%s},{p:%s} FROM {%s AS p} WHERE {p:%s}=?%s order by {p:%s} desc", new Object[] {"pk", "modifiedtime", "BackofficeSavedQuery", "queryOwner", "queryOwner", "modifiedtime"});
    private FlexibleSearchService flexibleSearchService;


    public List<BackofficeSavedQueryModel> findSavedQueries(UserModel userModel)
    {
        FlexibleSearchQuery query;
        if(CollectionUtils.isNotEmpty(userModel.getAllGroups()))
        {
            Map<String, Object> queryParams = Maps.newHashMap();
            queryParams.put("queryOwner", userModel);
            queryParams.put("userGroups", userModel.getAllGroups());
            query = new FlexibleSearchQuery(FIND_BY_USER_AND_GROUPS_QUERY, queryParams);
        }
        else
        {
            Map<String, Object> queryParams = Maps.newHashMap();
            queryParams.put("queryOwner", userModel);
            query = new FlexibleSearchQuery(FIND_BY_USER_QUERY, queryParams);
        }
        return this.flexibleSearchService.search(query).getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
