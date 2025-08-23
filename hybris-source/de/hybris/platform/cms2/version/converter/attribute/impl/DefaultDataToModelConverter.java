package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;

public class DefaultDataToModelConverter implements Converter<VersionPayloadDescriptor, Object>
{
    private static Logger LOG = Logger.getLogger(DefaultDataToModelConverter.class);


    public Object convert(VersionPayloadDescriptor payloadDescriptor)
    {
        try
        {
            Class<?> clazz = Class.forName(payloadDescriptor.getType());
            return ConvertUtils.convert(payloadDescriptor.getValue(), clazz);
        }
        catch(ClassNotFoundException e)
        {
            LOG.debug("Unable to find a Class called: " + payloadDescriptor.getType(), e);
            return null;
        }
    }
}
