package de.hybris.platform.cms2.cmsitems.service.impl;

import de.hybris.platform.cms2.cmsitems.service.FlexibleSearchAttributeValueConverter;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexibleSearchAttributeValueConverter implements FlexibleSearchAttributeValueConverter
{
    private Map<String, Converter<String, String>> converters;


    public String convert(AttributeDescriptorModel typeModel, String value)
    {
        Optional<Converter<String, String>> converter = this.converters.entrySet().stream().filter(stringConverterEntry -> ((String)stringConverterEntry.getKey()).equals(typeModel.getAttributeType().getCode())).map(Map.Entry::getValue).findFirst();
        return converter.<String>map(stringStringConverter -> (String)stringStringConverter.convert(value)).orElse(value);
    }


    protected Map<String, Converter<String, String>> getConverters()
    {
        return this.converters;
    }


    @Required
    public void setConverters(Map<String, Converter<String, String>> converters)
    {
        this.converters = converters;
    }
}
