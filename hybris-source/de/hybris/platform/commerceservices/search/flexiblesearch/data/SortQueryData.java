package de.hybris.platform.commerceservices.search.flexiblesearch.data;

import java.io.Serializable;

public class SortQueryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String sortCode;
    private String sortName;
    private String query;


    public void setSortCode(String sortCode)
    {
        this.sortCode = sortCode;
    }


    public String getSortCode()
    {
        return this.sortCode;
    }


    public void setSortName(String sortName)
    {
        this.sortName = sortName;
    }


    public String getSortName()
    {
        return this.sortName;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public String getQuery()
    {
        return this.query;
    }
}
