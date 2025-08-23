package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;

public class DefaultAttributeToDataConverter implements Converter<Object, Object>
{
    public Object convert(Object source)
    {
        return source;
    }
}
