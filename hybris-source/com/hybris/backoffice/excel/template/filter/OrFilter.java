package com.hybris.backoffice.excel.template.filter;

import org.springframework.beans.factory.annotation.Required;

public class OrFilter<T> implements ExcelFilter<T>
{
    private ExcelFilter<T> excelFilter1;
    private ExcelFilter<T> excelFilter2;


    public boolean test(T t)
    {
        return this.excelFilter1.or(this.excelFilter2).test(t);
    }


    @Required
    public void setExcelFilter1(ExcelFilter<T> excelFilter1)
    {
        this.excelFilter1 = excelFilter1;
    }


    @Required
    public void setExcelFilter2(ExcelFilter<T> excelFilter2)
    {
        this.excelFilter2 = excelFilter2;
    }
}
