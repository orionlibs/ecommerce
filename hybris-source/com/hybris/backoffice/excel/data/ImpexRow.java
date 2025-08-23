package com.hybris.backoffice.excel.data;

import java.util.Collection;
import java.util.Map;

public class ImpexRow
{
    private final Map<ImpexHeaderValue, Object> row;


    public ImpexRow(Map<ImpexHeaderValue, Object> row)
    {
        this.row = row;
    }


    public Collection<ImpexHeaderValue> getHeaders()
    {
        return this.row.keySet();
    }


    public Collection<Object> getValues()
    {
        return this.row.values();
    }


    public Map<ImpexHeaderValue, Object> getRow()
    {
        return this.row;
    }
}
