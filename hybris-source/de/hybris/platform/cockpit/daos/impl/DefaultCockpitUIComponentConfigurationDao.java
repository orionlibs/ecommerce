package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.daos.CockpitUIComponentConfigurationDao;
import de.hybris.platform.cockpit.jalo.CockpitUIComponentConfiguration;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DefaultCockpitUIComponentConfigurationDao implements CockpitUIComponentConfigurationDao
{
    public CockpitUIComponentConfiguration findCockpitUIComponentConfiguration(String role, String objectTemplateCode, String code)
    {
        String query;
        if(role != null)
        {
            query = "SELECT {c.pk} FROM {CockpitUIComponentConfiguration AS c JOIN Principal AS p ON {c.principal} = {p.pk}} WHERE {p.uid} = ?role AND {c.objectTemplateCode} = ?objectTemplateCode AND {c.code} = ?code";
        }
        else
        {
            query = "SELECT {c.pk} FROM {CockpitUIComponentConfiguration AS c} WHERE {c.principal} IS NULL AND {c.objectTemplateCode} = ?objectTemplateCode AND {c.code} = ?code";
        }
        Map<String, Object> params = new HashMap<>();
        if(role != null)
        {
            params.put("role", role);
        }
        params.put("objectTemplateCode", objectTemplateCode);
        params.put("code", code);
        FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
        SearchResult result = flexibleSearch.search(query, params, CockpitUIComponentConfiguration.class);
        if(result.getCount() == 0)
        {
            return null;
        }
        return result.getResult().get(0);
    }


    public List<CockpitUIComponentConfiguration> findCockpitUIComponentConfigurationsByPrincipal(Principal principal)
    {
        Collection<Principal> principals = new HashSet<>();
        principals.add(principal);
        principals.addAll(principal.getAllGroups());
        String query = String.format("SELECT {PK} FROM {%s} WHERE {principal} IN (?principals)", new Object[] {GeneratedCockpitConstants.TC.COCKPITUICOMPONENTCONFIGURATION});
        Map<String, Object> params = new HashMap<>();
        params.put("principals", principals);
        FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
        SearchResult result = flexibleSearch.search(query, params, CockpitUIComponentConfiguration.class);
        return result.getResult();
    }


    public List<String> findRoleNamesByPrincipal(Principal principal)
    {
        Collection<Principal> principals = new HashSet<>();
        principals.add(principal);
        principals.addAll(principal.getAllGroups());
        String query = String.format("SELECT DISTINCT {principal} FROM {%s} WHERE {principal} IN (?principals)", new Object[] {GeneratedCockpitConstants.TC.COCKPITUICOMPONENTCONFIGURATION});
        Map<String, Object> params = new HashMap<>();
        params.put("principals", principals);
        FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
        SearchResult result = flexibleSearch.search(query, params, Principal.class);
        List<Principal> resultPrincipals = result.getResult();
        List<String> roleNames = new ArrayList<>();
        for(Principal p : resultPrincipals)
        {
            roleNames.add(p.getUID());
        }
        return roleNames;
    }
}
