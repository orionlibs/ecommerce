package de.hybris.platform.servicelayer.interceptor;

public interface LoadInterceptor<MODEL> extends Interceptor
{
    void onLoad(MODEL paramMODEL, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
