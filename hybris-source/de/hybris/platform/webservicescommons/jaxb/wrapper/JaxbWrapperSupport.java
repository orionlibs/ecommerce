package de.hybris.platform.webservicescommons.jaxb.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JaxbWrapperSupport
{
    private List<JaxbObjectWrapperFactory> wrapperFactories = new ArrayList<>();


    public Object wrap(Object o)
    {
        Optional<JaxbObjectWrapperFactory> findFactory = this.wrapperFactories.stream().filter(f -> f.supports(o.getClass())).findFirst();
        return findFactory.<Object>map(f -> f.createWrapper(o)).orElse(o);
    }


    public Object unwrap(Object o)
    {
        if(o instanceof JaxbObjectWrapper)
        {
            return ((JaxbObjectWrapper)o).getObject();
        }
        return o;
    }


    public boolean supports(Class<?> clazz)
    {
        return (JaxbObjectWrapper.class.isAssignableFrom(clazz) || this.wrapperFactories
                        .stream().anyMatch(f -> f.supports(clazz)));
    }


    public Class getWrapperClass(Class<?> clazz)
    {
        Optional<JaxbObjectWrapperFactory> findFactory = this.wrapperFactories.stream().filter(f -> f.supports(clazz)).findFirst();
        return findFactory.<Class<?>>map(f -> f.getWrapperClass()).orElse(clazz);
    }


    public void setWrapperFactories(List<JaxbObjectWrapperFactory> wrapperFactories)
    {
        this.wrapperFactories = wrapperFactories;
    }
}
