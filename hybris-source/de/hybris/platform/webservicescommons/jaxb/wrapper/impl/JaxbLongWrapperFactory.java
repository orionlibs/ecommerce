package de.hybris.platform.webservicescommons.jaxb.wrapper.impl;

import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapper;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapperFactory;

public class JaxbLongWrapperFactory implements JaxbObjectWrapperFactory
{
    public JaxbObjectWrapper createWrapper(Object o)
    {
        return (JaxbObjectWrapper)new JaxbLongWrapper((Long)o);
    }


    public boolean supports(Class<?> clazz)
    {
        return (Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz));
    }


    public Class<?> getWrapperClass()
    {
        return JaxbLongWrapper.class;
    }
}
