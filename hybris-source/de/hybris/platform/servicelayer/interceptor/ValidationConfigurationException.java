package de.hybris.platform.servicelayer.interceptor;

public class ValidationConfigurationException extends InterceptorException
{
    public ValidationConfigurationException(String message, Throwable cause, Interceptor inter)
    {
        super(message, cause, inter);
    }
}
