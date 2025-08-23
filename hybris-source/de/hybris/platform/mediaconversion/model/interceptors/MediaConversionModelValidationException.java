package de.hybris.platform.mediaconversion.model.interceptors;

import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public class MediaConversionModelValidationException extends InterceptorException
{
    private static final long serialVersionUID = -7809334451197577185L;


    MediaConversionModelValidationException(String message, Interceptor inter)
    {
        super(message, null, inter);
    }


    MediaConversionModelValidationException(String message, Throwable cause, Interceptor inter)
    {
        super(message, cause, inter);
    }
}
