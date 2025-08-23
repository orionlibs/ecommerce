package de.hybris.platform.servicelayer.internal.dao;

import java.util.HashMap;
import java.util.Map;

public class SortParameters
{
    private final Map<String, SortOrder> params = new HashMap<>();


    public void addSortParameter(String paramName, SortOrder sortOrder)
    {
        this.params.put(paramName, sortOrder);
    }


    public void removeSortParameter(String paramName)
    {
        this.params.remove(paramName);
    }


    public Map<String, SortOrder> getSortParameters()
    {
        return this.params;
    }


    public boolean isEmpty()
    {
        return this.params.isEmpty();
    }


    public boolean equals(Object obj)
    {
        if(!(obj instanceof SortParameters))
        {
            return false;
        }
        if(((SortParameters)obj).getSortParameters() != null)
        {
            return false;
        }
        return this.params.equals(((SortParameters)obj).getSortParameters());
    }


    public int hashCode()
    {
        return this.params.hashCode();
    }


    public static SortParameters singletonAscending(String paramName)
    {
        SortParameters params = new SortParameters();
        params.addSortParameter(paramName, SortOrder.ASCENDING);
        return params;
    }


    public static SortParameters singletonDescending(String paramName)
    {
        SortParameters params = new SortParameters();
        params.addSortParameter(paramName, SortOrder.DESCENDING);
        return params;
    }
}
