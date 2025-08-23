package de.hybris.platform.servicelayer.interceptor;

public interface InitDefaultsInterceptor<MODEL> extends Interceptor
{
    void onInitDefaults(MODEL paramMODEL, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
