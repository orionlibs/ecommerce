package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class VoidInterceptor implements PrepareInterceptor, ValidateInterceptor, RemoveInterceptor, LoadInterceptor, InitDefaultsInterceptor
{
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
    }


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
    }


    public void onLoad(Object model, InterceptorContext ctx) throws InterceptorException
    {
    }


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
    }
}
