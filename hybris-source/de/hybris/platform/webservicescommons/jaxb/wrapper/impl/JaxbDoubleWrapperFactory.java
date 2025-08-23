package de.hybris.platform.webservicescommons.jaxb.wrapper.impl;

import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapper;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapperFactory;

public class JaxbDoubleWrapperFactory implements JaxbObjectWrapperFactory
{
    public JaxbObjectWrapper createWrapper(Object o)
    {
        return (JaxbObjectWrapper)new JaxbDoubleWrapper((Double)o);
    }


    public boolean supports(Class<?> clazz)
    {
        return (Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz));
    }


    public Class<?> getWrapperClass()
    {
        return JaxbDoubleWrapper.class;
    }
}
