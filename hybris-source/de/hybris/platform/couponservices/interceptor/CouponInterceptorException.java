package de.hybris.platform.couponservices.interceptor;

import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public class CouponInterceptorException extends InterceptorException
{
    public CouponInterceptorException(String message)
    {
        super(message, null, null);
    }


    public CouponInterceptorException(String message, Throwable cause)
    {
        super(message, cause, null);
    }


    public CouponInterceptorException(String message, Interceptor inter)
    {
        super(message, null, inter);
    }


    public CouponInterceptorException(String message, Throwable cause, Interceptor inter)
    {
        super(message, cause, inter);
    }
}
