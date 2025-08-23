package de.hybris.platform.servicelayer.interceptor;

import java.util.Collection;

public interface InterceptorRegistry
{
    Collection<PrepareInterceptor> getPrepareInterceptors(String paramString);


    Collection<LoadInterceptor> getLoadInterceptors(String paramString);


    Collection<RemoveInterceptor> getRemoveInterceptors(String paramString);


    Collection<ValidateInterceptor> getValidateInterceptors(String paramString);


    Collection<InitDefaultsInterceptor> getInitDefaultsInterceptors(String paramString);
}
