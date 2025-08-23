package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class EnumDataToModelConverter implements Converter<VersionPayloadDescriptor, HybrisEnumValue>
{
    private static Logger LOG = Logger.getLogger(EnumDataToModelConverter.class);
    private EnumerationService enumerationService;


    public HybrisEnumValue convert(VersionPayloadDescriptor payloadDescriptor)
    {
        try
        {
            Class<?> typeClass = Class.forName(payloadDescriptor.getType());
            return getEnumerationService().getEnumerationValue(typeClass, payloadDescriptor.getValue().toLowerCase());
        }
        catch(ClassNotFoundException e)
        {
            LOG.debug("Unable to find a Class called: " + payloadDescriptor.getType(), e);
            return null;
        }
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
