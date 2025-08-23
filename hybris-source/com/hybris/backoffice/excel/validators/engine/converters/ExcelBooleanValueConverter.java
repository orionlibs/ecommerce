package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class ExcelBooleanValueConverter implements ExcelValueConverter<Boolean>
{
    private int order;


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        return (StringUtils.isNotBlank(cellValue) && Objects.equals(excelAttribute.getType(), Boolean.class.getName()));
    }


    public Boolean convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return Boolean.valueOf("true".equalsIgnoreCase(importParameters.getCellValue().toString()));
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
