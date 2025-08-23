package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttribute;

public class MultivalueExcelMarkAppender implements ExcelMarkAppender<ExcelAttribute>
{
    private static final int ORDER_OF_MULTIVALUE_EXCEL_MARK_APPENDER = 30000;


    public String apply(String s, ExcelAttribute excelAttribute)
    {
        return excelAttribute.isMultiValue() ? (s + s) : s;
    }


    public int getOrder()
    {
        return 30000;
    }
}
