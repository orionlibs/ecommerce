package de.hybris.platform.servicelayer.interceptor;

public interface RemoveInterceptor<MODEL> extends Interceptor
{
    void onRemove(MODEL paramMODEL, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
