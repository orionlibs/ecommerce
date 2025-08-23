package de.hybris.platform.webservicescommons.api.restrictions.data;

import java.io.Serializable;
import java.lang.reflect.Method;

public class EndpointContextData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Method method;


    public void setMethod(Method method)
    {
        this.method = method;
    }


    public Method getMethod()
    {
        return this.method;
    }
}
