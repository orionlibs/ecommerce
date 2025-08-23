package de.hybris.platform.cms2.namedquery.service.impl;

import de.hybris.platform.cms2.exceptions.InvalidNamedQueryException;
import de.hybris.platform.cms2.namedquery.service.NamedQueryFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultNamedQueryFactory implements NamedQueryFactory
{
    private Map<String, String> namedQueryMap;


    public String getNamedQuery(String queryName) throws InvalidNamedQueryException
    {
        if(!this.namedQueryMap.containsKey(queryName))
        {
            throw new InvalidNamedQueryException(queryName);
        }
        return this.namedQueryMap.get(queryName);
    }


    @Required
    public void setNamedQueryMap(Map<String, String> namedQueryMap)
    {
        this.namedQueryMap = namedQueryMap;
    }


    protected Map<String, String> getNamedQueryMap()
    {
        return this.namedQueryMap;
    }
}
