package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class InterceptorException extends BusinessException
{
    private Interceptor interceptor;


    public InterceptorException(String message)
    {
        this(message, null, null);
    }


    public InterceptorException(String message, Throwable cause)
    {
        this(message, cause, null);
    }


    public InterceptorException(String message, Interceptor inter)
    {
        this(message, null, inter);
    }


    public InterceptorException(String message, Throwable cause, Interceptor inter)
    {
        super(message, cause);
        setInterceptor(inter);
    }


    public Interceptor getInterceptor()
    {
        return this.interceptor;
    }


    public void setInterceptor(Interceptor interceptor)
    {
        this.interceptor = interceptor;
    }


    public String getMessage()
    {
        return "[" + this.interceptor + "]:" + super.getMessage();
    }
}
