package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class OrderField implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String SCORE = "score";
    private String field;
    private SortOrder sortOrder;


    public OrderField(String field)
    {
        this(field, SortOrder.ASCENDING);
    }


    public OrderField(String field, SortOrder sortOrder)
    {
        this.field = field;
        this.sortOrder = sortOrder;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public SortOrder getSortOrder()
    {
        return this.sortOrder;
    }


    public void setSortOrder(SortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }


    public boolean isAscending()
    {
        return (this.sortOrder == SortOrder.ASCENDING);
    }


    public void setAscending(boolean ascending)
    {
        this.sortOrder = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
    }


    public String toString()
    {
        return getClass().getName() + " [" + getClass().getName() + " (ascending = " + this.field + ")]";
    }
}
