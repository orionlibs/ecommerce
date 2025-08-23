package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.enums.ConverterType;
import java.util.Map;

public class SearchResultConverters
{
    private Map<String, ConvertersMapping> converterMapping;


    public void setConverterMapping(Map<String, ConvertersMapping> converterMapping)
    {
        this.converterMapping = converterMapping;
    }


    public Converter getConverter(String indexedTypeCode, ConverterType converterType)
    {
        ConvertersMapping convertersMapping = this.converterMapping.get(indexedTypeCode);
        if(convertersMapping == null)
        {
            return null;
        }
        return convertersMapping.getConverterForType(converterType);
    }


    public ConvertersMapping getConverterMapping(String indexedTypeCode)
    {
        return this.converterMapping.get(indexedTypeCode);
    }
}
