package de.hybris.platform.personalizationwebservices.queries.impl;

import de.hybris.platform.personalizationwebservices.queries.RestQueryExecutor;
import de.hybris.platform.personalizationwebservices.queries.RestQueryService;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultRestQueryService implements RestQueryService
{
    private Map<String, RestQueryExecutor> queries = Collections.emptyMap();


    @Autowired(required = false)
    public void setQueries(Map<String, RestQueryExecutor> queries)
    {
        this.queries = MapUtils.isNotEmpty(queries) ? queries : Collections.<String, RestQueryExecutor>emptyMap();
    }


    public Object executeQuery(String name, Map<String, String> params)
    {
        RestQueryExecutor query = this.queries.get(name);
        if(query == null)
        {
            throw new NotFoundException("No query with name " + name);
        }
        return query.execute(params);
    }
}
