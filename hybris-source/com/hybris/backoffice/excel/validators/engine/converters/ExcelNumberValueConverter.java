package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class ExcelNumberValueConverter implements ExcelValueConverter<Number>
{
    private int order;
    private final Map<String, Function<String, Number>> supportedTypes;


    public ExcelNumberValueConverter()
    {
        this.supportedTypes = new HashMap<>();
        this.supportedTypes.put(Byte.class.getName(), Byte::valueOf);
        this.supportedTypes.put(Short.class.getName(), Short::valueOf);
        this.supportedTypes.put(Integer.class.getName(), Integer::valueOf);
        this.supportedTypes.put(Long.class.getName(), Long::valueOf);
        this.supportedTypes.put(Float.class.getName(), Float::valueOf);
        this.supportedTypes.put(Double.class.getName(), Double::valueOf);
        this.supportedTypes.put(BigDecimal.class.getName(), BigDecimal::new);
        this.supportedTypes.put(BigInteger.class.getName(), BigInteger::new);
    }


    public boolean canConvert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        return (StringUtils.isNotBlank(cellValue) && this.supportedTypes.containsKey(excelAttribute.getType()));
    }


    public Number convert(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        String cellValue = Objects.toString(importParameters.getCellValue(), "");
        return ((Function<String, Number>)this.supportedTypes.get(excelAttribute.getType())).apply(cellValue);
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
