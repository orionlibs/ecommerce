package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class GroupCommandField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Integer groupLimit;


    public GroupCommandField(String field)
    {
        this.field = field;
    }


    public GroupCommandField(String field, Integer groupLimit)
    {
        this.field = field;
        this.groupLimit = groupLimit;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public Integer getGroupLimit()
    {
        return this.groupLimit;
    }


    public void setGroupLimit(Integer groupLimit)
    {
        this.groupLimit = groupLimit;
    }
}
