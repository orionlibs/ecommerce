package com.hybris.backoffice.excel.template.filter;

import org.springframework.beans.factory.annotation.Required;

public class NegateFilter<T> implements ExcelFilter<T>
{
    private ExcelFilter<T> excelFilter;


    public boolean test(T t)
    {
        return this.excelFilter.negate().test(t);
    }


    @Required
    public void setExcelFilter(ExcelFilter<T> excelFilter)
    {
        this.excelFilter = excelFilter;
    }
}
