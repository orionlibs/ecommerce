package de.hybris.platform.cockpit.services.query.daos.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.services.query.daos.SavedQueryUserRightsDao;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavedQueryUserRightsDaoImpl extends DefaultSavedQueryDao implements SavedQueryUserRightsDao
{
    public Collection<CockpitSavedQueryModel> findReadableSavedQueriesByUser(UserModel userModel)
    {
        Set<UserModel> userGroupsAndUser = new HashSet(userModel.getAllGroups());
        userGroupsAndUser.add(userModel);
        Map<Object, Object> queryParams = new HashMap<>();
        queryParams.put("users", userGroupsAndUser);
        queryParams.put("user", userModel);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT outertable.PK FROM (");
        builder.append("SELECT DISTINCT uniontable.PK, uniontable.LABEL FROM (");
        builder.append("{{ SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s} \tWHERE {s:");
        builder.append("user");
        builder.append("} = ?user }} ");
        builder.append("UNION  \t{{ ");
        builder.append("SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s JOIN ");
        builder.append(GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION);
        builder.append(" AS readRelation ON {s.");
        builder.append("pk");
        builder.append("} = {readRelation.target}} ");
        builder.append("WHERE {readRelation.source} IN (?users) ");
        builder.append("}}  UNION\t{{ ");
        builder.append("SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s} WHERE {s:user} IS NULL }}");
        builder.append(") uniontable");
        builder.append(") outertable order by outertable.LABEL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitSavedQueryModel> userReadableCollectionsResult = getFlexibleSearchService().search(query);
        List<CockpitSavedQueryModel> resultList = userReadableCollectionsResult.getResult();
        return resultList;
    }


    public Collection<CockpitSavedQueryModel> findSharedQueriesByUser(UserModel userModel)
    {
        Set<UserModel> userGroupsAndUser = new HashSet(userModel.getAllGroups());
        userGroupsAndUser.add(userModel);
        Map<Object, Object> queryParams = new HashMap<>();
        queryParams.put("users", userGroupsAndUser);
        queryParams.put("user", userModel);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT outertable.PK FROM (");
        builder.append("SELECT DISTINCT uniontable.PK, uniontable.LABEL FROM (");
        builder.append("{{ SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s} \tWHERE {s:");
        builder.append("user");
        builder.append("} = ?user AND {s:");
        builder.append("pk");
        builder.append("} IN ({{");
        builder.append("SELECT {target} FROM {");
        builder.append(GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION);
        builder.append("}}})}}");
        builder.append("UNION  \t{{ ");
        builder.append("SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s JOIN ");
        builder.append(GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION);
        builder.append(" AS readRelation ON {s.");
        builder.append("pk");
        builder.append("} = {readRelation.target}} ");
        builder.append("WHERE {readRelation.source} IN (?users) ");
        builder.append("}}");
        builder.append(") uniontable");
        builder.append(") outertable order by outertable.LABEL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitSavedQueryModel> userReadableCollectionsResult = getFlexibleSearchService().search(query);
        List<CockpitSavedQueryModel> resultList = userReadableCollectionsResult.getResult();
        return resultList;
    }


    public Collection<CockpitSavedQueryModel> findNotSharedQueriesByUser(UserModel userModel)
    {
        Map<Object, Object> queryParams = new HashMap<>();
        queryParams.put("user", userModel);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {s:");
        builder.append("pk");
        builder.append("} AS PK, lower({s:");
        builder.append("label");
        builder.append("}) AS LABEL FROM {");
        builder.append("CockpitSavedQuery");
        builder.append(" AS s} \tWHERE {s:");
        builder.append("user");
        builder.append("} = ?user AND {s:");
        builder.append("pk");
        builder.append("} NOT IN ({{");
        builder.append("SELECT {target} FROM {");
        builder.append(GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION);
        builder.append("}}})");
        builder.append(" order by LABEL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitSavedQueryModel> userReadableCollectionsResult = getFlexibleSearchService().search(query);
        List<CockpitSavedQueryModel> resultList = userReadableCollectionsResult.getResult();
        return resultList;
    }
}
