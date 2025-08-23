package de.hybris.platform.cms2.services.impl;

import de.hybris.platform.cms2.services.SortQueryData;
import de.hybris.platform.servicelayer.internal.dao.Dao;

public class DefaultSortQueryData implements SortQueryData
{
    private Class<? extends Dao> typeClass;
    private String sortCode;
    private String query;
    private boolean isDefault;


    public Class<? extends Dao> getTypeClass()
    {
        return this.typeClass;
    }


    public String getSortCode()
    {
        return this.sortCode;
    }


    public String getQuery()
    {
        return this.query;
    }


    public boolean isDefault()
    {
        return this.isDefault;
    }


    public void setTypeClass(Class<? extends Dao> typeClass)
    {
        this.typeClass = typeClass;
    }


    public void setSortCode(String sortCode)
    {
        this.sortCode = sortCode;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public void setDefault(boolean isDefault)
    {
        this.isDefault = isDefault;
    }
}
