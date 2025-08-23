package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class IndexedTypeSortField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private boolean ascending;


    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }


    public String getFieldName()
    {
        return this.fieldName;
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
