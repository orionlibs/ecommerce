package de.hybris.platform.servicelayer.interceptor;

public interface PrepareInterceptor<MODEL> extends Interceptor
{
    void onPrepare(MODEL paramMODEL, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
