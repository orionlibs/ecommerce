package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.Map;

public class IndexedTypeFlexibleSearchQuery implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String query;
    private String userId;
    private Map<String, Object> parameters;
    private boolean injectLastIndexTime;
    private boolean injectCurrentTime;
    private boolean injectCurrentDate;
    private IndexOperation type;
    private String parameterProviderId;


    public void setQuery(String query)
    {
        this.query = query;
    }


    public String getQuery()
    {
        return this.query;
    }


    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    public String getUserId()
    {
        return this.userId;
    }


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public void setInjectLastIndexTime(boolean injectLastIndexTime)
    {
        this.injectLastIndexTime = injectLastIndexTime;
    }


    public boolean isInjectLastIndexTime()
    {
        return this.injectLastIndexTime;
    }


    public void setInjectCurrentTime(boolean injectCurrentTime)
    {
        this.injectCurrentTime = injectCurrentTime;
    }


    public boolean isInjectCurrentTime()
    {
        return this.injectCurrentTime;
    }


    public void setInjectCurrentDate(boolean injectCurrentDate)
    {
        this.injectCurrentDate = injectCurrentDate;
    }


    public boolean isInjectCurrentDate()
    {
        return this.injectCurrentDate;
    }


    public void setType(IndexOperation type)
    {
        this.type = type;
    }


    public IndexOperation getType()
    {
        return this.type;
    }


    public void setParameterProviderId(String parameterProviderId)
    {
        this.parameterProviderId = parameterProviderId;
    }


    public String getParameterProviderId()
    {
        return this.parameterProviderId;
    }
}
