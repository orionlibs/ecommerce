package de.hybris.platform.cockpit.model.dynamicquery.impl;

import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import java.util.HashMap;
import java.util.Map;

public class DynamicQueryImpl implements DynamicQuery
{
    public String label;
    private final String flexibleQuery;
    public String description;
    public Map<String, Object> parameters = new HashMap<>();


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getFexibleQuery()
    {
        return this.flexibleQuery;
    }


    public DynamicQueryImpl(String label, String flexibleQuery, Map<String, Object> parameters)
    {
        this.label = label;
        this.flexibleQuery = flexibleQuery;
        this.parameters = parameters;
    }


    public void addQueryParameter(String paramName, Object paramValue)
    {
        this.parameters.put(paramName, paramValue);
    }


    public void removeQueryParameter(String paramName)
    {
        this.parameters.remove(paramName);
    }
}
