package de.hybris.platform.webservicescommons.jaxb.wrapper.impl;

import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapper;
import de.hybris.platform.webservicescommons.jaxb.wrapper.JaxbObjectWrapperFactory;
import java.util.List;

public class JaxbListWrapperFactory implements JaxbObjectWrapperFactory
{
    public JaxbObjectWrapper createWrapper(Object o)
    {
        return (JaxbObjectWrapper)new JaxbListWrapper((List)o);
    }


    public boolean supports(Class<?> clazz)
    {
        return (List.class.isAssignableFrom(clazz) && !clazz.isMemberClass());
    }


    public Class<?> getWrapperClass()
    {
        return JaxbListWrapper.class;
    }
}
