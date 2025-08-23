package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class SearchQuerySort implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private boolean ascending;


    public void setField(String field)
    {
        this.field = field;
    }


    public String getField()
    {
        return this.field;
    }


    public void setAscending(boolean ascending)
    {
        this.ascending = ascending;
    }


    public boolean isAscending()
    {
        return this.ascending;
    }
}
