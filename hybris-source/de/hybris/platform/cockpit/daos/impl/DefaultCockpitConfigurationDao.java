package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.daos.CockpitConfigurationDao;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DefaultCockpitConfigurationDao extends DefaultGenericDao<CockpitUIComponentConfigurationModel> implements CockpitConfigurationDao
{
    public DefaultCockpitConfigurationDao()
    {
        this("CockpitUIComponentConfiguration");
    }


    public DefaultCockpitConfigurationDao(String typeCode)
    {
        super(typeCode);
    }


    public List<CockpitUIComponentConfigurationModel> findComponentConfigurationsByPrincipal(PrincipalModel principal)
    {
        Collection<PrincipalModel> principals = new HashSet<>();
        principals.add(principal);
        principals.addAll(principal.getAllGroups());
        Map<String, Object> params = new HashMap<>();
        params.put("principals", principals);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append("pk").append("} ");
        queryBuilder.append("FROM {").append("CockpitUIComponentConfiguration").append("} ");
        queryBuilder.append("WHERE {").append("principal").append("} IN (?principals)");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        SearchResult<CockpitUIComponentConfigurationModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public List<CockpitUIComponentConfigurationModel> findComponentConfigurationsByMedia(MediaModel media)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("media", media);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append("pk").append("} ");
        queryBuilder.append("FROM {").append("CockpitUIComponentConfiguration").append("} ");
        queryBuilder.append("WHERE {").append("media").append("} = ?media ");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        SearchResult<CockpitUIComponentConfigurationModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public List<CockpitUIComponentConfigurationModel> findDedicatedComponentConfigurationsByPrincipal(PrincipalModel principal)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("principal", principal);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append("pk").append("} ");
        queryBuilder.append("FROM {").append("CockpitUIComponentConfiguration").append("} ");
        queryBuilder.append("WHERE {").append("principal").append("} = ?principal ");
        queryBuilder.append("ORDER BY {").append("code").append("}");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        SearchResult<CockpitUIComponentConfigurationModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public List<CockpitUIComponentConfigurationModel> findComponentConfigurations(PrincipalModel principal, String objectTemplateCode, String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("objectTemplateCode", objectTemplateCode);
        params.put("code", code);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append("pk").append("} ");
        queryBuilder.append("FROM {").append("CockpitUIComponentConfiguration").append(" AS c} ");
        queryBuilder.append("WHERE {c.").append("objectTemplateCode")
                        .append("} = ?objectTemplateCode ");
        queryBuilder.append("AND {c.").append("code").append("} = ?code ");
        if(principal == null)
        {
            queryBuilder.append("AND {c.").append("principal").append("} IS NULL");
        }
        else
        {
            params.put("principal", principal);
            queryBuilder.append("AND {c.").append("principal").append("} = ?principal");
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        SearchResult<CockpitUIComponentConfigurationModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public List<String> findRoleNamesByPrincipal(PrincipalModel principal)
    {
        Collection<PrincipalModel> principals = new HashSet<>();
        principals.add(principal);
        principals.addAll(principal.getAllGroups());
        Map<String, Object> params = new HashMap<>();
        params.put("principals", principals);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT {").append("principal").append("} ");
        queryBuilder.append("FROM {").append("CockpitUIComponentConfiguration").append("} ");
        queryBuilder.append("WHERE {").append("principal").append("} IN (?principals)");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        query.setResultClassList(Collections.singletonList(PrincipalModel.class));
        SearchResult<PrincipalModel> result = getFlexibleSearchService().search(query);
        List<String> roleNames = new ArrayList<>();
        for(PrincipalModel principalModel : result.getResult())
        {
            roleNames.add(principalModel.getUid());
        }
        return roleNames;
    }
}
