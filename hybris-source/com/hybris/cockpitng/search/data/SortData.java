/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

/**
 * DTO for sort data
 */
public class SortData
{
    private String sortAttribute;
    private boolean ascending;


    public SortData()
    {
        // NOP
    }


    public SortData(final String sortAttribute, final boolean ascending)
    {
        this.ascending = ascending;
        this.sortAttribute = sortAttribute;
    }


    public String getSortAttribute()
    {
        return this.sortAttribute;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ascending ? 1231 : 1237);
        result = prime * result + ((sortAttribute == null) ? 0 : sortAttribute.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final SortData other = (SortData)obj;
        if(ascending != other.ascending)
        {
            return false;
        }
        if(sortAttribute == null)
        {
            if(other.sortAttribute != null)
            {
                return false;
            }
        }
        else if(!sortAttribute.equals(other.sortAttribute))
        {
            return false;
        }
        return true;
    }


    public void setSortAttribute(final String sortAttribute)
    {
        this.sortAttribute = sortAttribute;
    }


    public boolean isAscending()
    {
        return ascending;
    }


    public void setAscending(final boolean ascending)
    {
        this.ascending = ascending;
    }
}
