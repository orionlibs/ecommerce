package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import java.util.Date;
import java.util.Objects;

public class DateDataToModelConverter implements Converter<VersionPayloadDescriptor, Date>
{
    public Date convert(VersionPayloadDescriptor source)
    {
        if(Objects.isNull(source))
        {
            return null;
        }
        return new Date(Long.parseLong(source.getValue()));
    }
}
