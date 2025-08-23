package de.hybris.platform.servicelayer.interceptor;

public interface ValidateInterceptor<MODEL> extends Interceptor
{
    void onValidate(MODEL paramMODEL, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
