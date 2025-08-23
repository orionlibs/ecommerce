package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.ImpexRow;
import java.util.HashMap;
import java.util.Map;

public class ExcelImportContext
{
    private ImpexRow impexRow;
    private final Map<String, Object> ctx = new HashMap<>();


    public ImpexRow getImpexRow()
    {
        return this.impexRow;
    }


    public void setImpexRow(ImpexRow impexRow)
    {
        this.impexRow = impexRow;
    }


    public Map<String, Object> getCtx()
    {
        return this.ctx;
    }


    public Object getValue(String key)
    {
        return this.ctx.get(key);
    }


    public void setValue(String key, Object value)
    {
        this.ctx.put(key, value);
    }
}
