package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class ExcelMultiValueConverter implements ExcelValueConverter<Collection>
{
    private int order;


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return excelAttribute.isMultiValue();
    }


    public Collection convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        return (Collection)importParameters.getMultiValueParameters().stream()
                        .map(params -> (String)params.get("rawValue"))
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
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
