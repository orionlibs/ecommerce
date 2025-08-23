package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.core.GenericCondition;
import java.util.Collections;
import java.util.Map;

public class GenericSearchSubQuery extends GenericCondition
{
    private String subQuery;


    public static GenericCondition createSearchSubQuery(String query)
    {
        return new GenericSearchSubQuery(query);
    }


    protected GenericSearchSubQuery(String query)
    {
        super(null);
        setSubQuery(query);
    }


    public void setSubQuery(String query)
    {
        if(query == null)
        {
            throw new IllegalArgumentException("String object must not be null.");
        }
        this.subQuery = query;
    }


    public String getSubQuery()
    {
        return this.subQuery;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(getSubQuery());
    }


    public Map getResettableValues()
    {
        return Collections.EMPTY_MAP;
    }


    public void setResettableValue(String key, Object value)
    {
    }


    protected void checkOperator()
    {
    }
}
