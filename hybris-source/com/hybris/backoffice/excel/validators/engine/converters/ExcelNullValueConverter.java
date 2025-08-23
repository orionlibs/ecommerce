package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class ExcelNullValueConverter implements ExcelValueConverter<Object>
{
    private int order;


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return StringUtils.isBlank(importParameters.getCellValue().toString());
    }


    public Object convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        if(StringUtils.isNotBlank(cellValue) || Objects.equals(excelAttribute.getType(), String.class.getName()))
        {
            return cellValue;
        }
        return null;
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
