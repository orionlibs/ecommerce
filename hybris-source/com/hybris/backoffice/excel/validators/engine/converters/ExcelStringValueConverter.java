package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Objects;

public class ExcelStringValueConverter implements ExcelValueConverter<String>
{
    private int order;


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return Objects.equals(excelAttribute.getType(), String.class.getName());
    }


    public String convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return importParameters.getCellValue().toString();
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }
}
