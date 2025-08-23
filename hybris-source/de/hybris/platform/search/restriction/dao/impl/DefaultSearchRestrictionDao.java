package de.hybris.platform.search.restriction.dao.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.search.restriction.dao.SearchRestrictionDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSearchRestrictionDao extends DefaultGenericDao<SearchRestrictionModel> implements SearchRestrictionDao
{
    public DefaultSearchRestrictionDao()
    {
        super("SearchRestriction");
    }


    public List<SearchRestrictionModel> findActiveSearchRestrictionsByPrincipalAndType(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("active", Boolean.TRUE);
        return search(principal, includePrincipalGroups, types, values);
    }


    public List<SearchRestrictionModel> findInactiveSearchRestrictionsByPrincipalAndType(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("active", Boolean.FALSE);
        return search(principal, includePrincipalGroups, types, values);
    }


    public List<SearchRestrictionModel> findSearchRestrictionsByPrincipalAndType(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        Map<String, Object> values = new HashMap<>();
        return search(principal, includePrincipalGroups, types, values);
    }


    public List<SearchRestrictionModel> findSearchRestrictionsByType(ComposedTypeModel type)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("SearchRestriction").append("}");
        query.append(" WHERE {").append("restrictedType").append("}=?type");
        Map<String, Object> values = new HashMap<>();
        values.put("type", type);
        SearchResult<SearchRestrictionModel> search = getFlexibleSearchService().search(query.toString(), values);
        return search.getResult();
    }


    private String buildOracleStatement(String expression, String parameter, String expressionOperator, Collection originalParameters, Map<String, Object> paramsMap)
    {
        return FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(expression, parameter, expressionOperator, originalParameters, paramsMap);
    }


    private String getFlexibleSearchQuery(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types, Map<String, Object> values)
    {
        Collection<PrincipalModel> principals = new ArrayList<>();
        principals.add(principal);
        if(includePrincipalGroups)
        {
            principals.addAll(principal.getAllGroups());
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("SearchRestriction").append("}");
        query.append(" WHERE ");
        query.append(buildOracleStatement("{principal} IN (?principals)", "principals", "OR", principals, values));
        query.append(" AND ");
        query.append(buildOracleStatement("{restrictedType} IN (?types)", "types", "OR", types, values));
        if(values.get("active") != null)
        {
            query.append(" AND {").append("active").append("} = ?active");
        }
        return query.toString();
    }


    private List<SearchRestrictionModel> search(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types, Map<String, Object> values)
    {
        String query = getFlexibleSearchQuery(principal, includePrincipalGroups, types, values);
        SearchResult<SearchRestrictionModel> search = getFlexibleSearchService().search(query, values);
        return search.getResult();
    }
}
