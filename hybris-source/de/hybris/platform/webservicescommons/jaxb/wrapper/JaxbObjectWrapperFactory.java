package de.hybris.platform.webservicescommons.jaxb.wrapper;

public interface JaxbObjectWrapperFactory
{
    JaxbObjectWrapper createWrapper(Object paramObject);


    Class<?> getWrapperClass();


    boolean supports(Class<?> paramClass);
}
