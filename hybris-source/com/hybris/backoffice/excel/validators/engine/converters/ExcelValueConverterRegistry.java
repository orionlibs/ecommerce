package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class ExcelValueConverterRegistry
{
    private List<ExcelValueConverter> converters;


    public <CONVERTER extends ExcelValueConverter> Optional<ExcelValueConverter> getConverter(ExcelAttribute excelAttribute, ImportParameters importParameters, Class<CONVERTER>... exclude)
    {
        Collection<Class<CONVERTER>> excludedConverters = Optional.<Class<CONVERTER>[]>ofNullable(exclude).map(Arrays::asList).orElseGet(Collections::emptyList);
        return getConverters().stream()
                        .filter(converter -> !excludedConverters.contains(converter.getClass()))
                        .filter(converter -> converter.canConvert(excelAttribute, importParameters))
                        .findFirst();
    }


    public List<ExcelValueConverter> getConverters()
    {
        return this.converters;
    }


    @Required
    public void setConverters(List<ExcelValueConverter> converters)
    {
        this.converters = converters;
        OrderComparator.sort(converters);
    }
}
