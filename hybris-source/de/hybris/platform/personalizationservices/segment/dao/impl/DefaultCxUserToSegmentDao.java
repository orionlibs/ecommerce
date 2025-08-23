package de.hybris.platform.personalizationservices.segment.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxUserToSegmentDao;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DefaultCxUserToSegmentDao extends AbstractCxDao<CxUserToSegmentModel> implements CxUserToSegmentDao
{
    public DefaultCxUserToSegmentDao()
    {
        super("CxUserToSegment");
    }


    public SearchPageData<CxUserToSegmentModel> findUserToSegmentRelations(UserModel user, CxSegmentModel segment, BaseSiteModel baseSite, SearchPageData<?> pagination)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {pk} FROM {CxUserToSegment} ");
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("segment", segment);
        params.put("baseSite", baseSite);
        addParameters(query, params);
        query.append(" ORDER BY ").append("pk");
        return queryList(query.toString(), params, pagination);
    }


    private static void addParameters(StringBuilder query, Map<String, Object> parameters)
    {
        parameters.values().removeIf(Objects::isNull);
        if(!parameters.isEmpty())
        {
            query.append(" WHERE ");
            Iterator<Map.Entry<String, Object>> params = parameters.entrySet().iterator();
            while(params.hasNext())
            {
                Map.Entry<String, Object> parameter = params.next();
                addParameter(parameter.getKey(), query);
                if(params.hasNext())
                {
                    query.append(" AND ");
                }
            }
        }
    }


    private static void addParameter(String parameter, StringBuilder query)
    {
        query.append(" {").append(parameter).append("} = ?").append(parameter).append(" ");
    }
}
