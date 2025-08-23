package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;

public class FacetValueData<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private long count;
    private STATE query;
    private boolean selected;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCount(long count)
    {
        this.count = count;
    }


    public long getCount()
    {
        return this.count;
    }


    public void setQuery(STATE query)
    {
        this.query = query;
    }


    public STATE getQuery()
    {
        return this.query;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }
}
