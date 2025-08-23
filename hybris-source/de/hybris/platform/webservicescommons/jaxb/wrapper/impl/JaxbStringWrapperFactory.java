package de.hybris.platform.webservicescommons.jaxb.wrapper.impl;

import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapper;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapperFactory;

public class JaxbStringWrapperFactory implements JaxbObjectWrapperFactory
{
    public JaxbObjectWrapper createWrapper(Object o)
    {
        return (JaxbObjectWrapper)new JaxbStringWrapper((String)o);
    }


    public boolean supports(Class<?> clazz)
    {
        return String.class.isAssignableFrom(clazz);
    }


    public Class<?> getWrapperClass()
    {
        return JaxbStringWrapper.class;
    }
}
