package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelDateValueConverter implements ExcelValueConverter<Date>
{
    private int order;
    private ExcelDateUtils excelDateUtils;


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        return (StringUtils.isNotBlank(cellValue) && Objects.equals(excelAttribute.getType(), Date.class.getName()));
    }


    public Date convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        return getExcelDateUtils().convertToImportedDate(cellValue);
    }


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
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
