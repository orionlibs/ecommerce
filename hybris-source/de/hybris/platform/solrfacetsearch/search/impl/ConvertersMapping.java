package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.enums.ConverterType;
import java.io.Serializable;
import java.util.Map;

public class ConvertersMapping implements Serializable
{
    private transient Map<ConverterType, Converter> converters;


    public void setConverters(Map<ConverterType, Converter> converters)
    {
        this.converters = converters;
    }


    public Converter getConverterForType(ConverterType converterType)
    {
        return this.converters.get(converterType);
    }
}
