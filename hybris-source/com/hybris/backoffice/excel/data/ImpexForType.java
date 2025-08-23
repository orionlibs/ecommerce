package com.hybris.backoffice.excel.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class ImpexForType implements Serializable
{
    private String typeCode;
    private int order = 0;
    private final transient Table<Integer, ImpexHeaderValue, Object> impexTable = (Table<Integer, ImpexHeaderValue, Object>)HashBasedTable.create();


    public ImpexForType(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public void putValue(Integer rowNumber, ImpexHeaderValue key, Object value)
    {
        this.impexTable.put(rowNumber, key, (value != null) ? value : "");
    }


    public void addRow(Map<ImpexHeaderValue, Object> row)
    {
        int nextRow = this.impexTable.rowKeySet().isEmpty() ? 0 : (((Integer)Collections.<Integer>max(this.impexTable.rowKeySet())).intValue() + 1);
        row.forEach((key, value) -> putValue(Integer.valueOf(nextRow), key, value));
    }


    public ImpexRow getRow(Integer rowIndex)
    {
        return new ImpexRow(this.impexTable.row(rowIndex));
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public Table<Integer, ImpexHeaderValue, Object> getImpexTable()
    {
        return this.impexTable;
    }
}
