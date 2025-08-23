package de.hybris.platform.webservicescommons.jaxb.wrapper.impl;

import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapper;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapperFactory;
import java.util.Map;

public class JaxbMapWrapperFactory implements JaxbObjectWrapperFactory
{
    public JaxbObjectWrapper createWrapper(Object o)
    {
        return (JaxbObjectWrapper)new JaxbMapWrapper((Map)o);
    }


    public boolean supports(Class<?> clazz)
    {
        return (Map.class.isAssignableFrom(clazz) && !clazz.isMemberClass());
    }


    public Class<?> getWrapperClass()
    {
        return JaxbMapWrapper.class;
    }
}
