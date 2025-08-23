package de.hybris.platform.property.interceptor.impl;

import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptor;
import java.util.List;
import java.util.Map;

public final class CompositeServerStartupPropertiesInterceptor implements ServerStartupPropertiesInterceptor
{
    private final List<ServerStartupPropertiesInterceptor> interceptors;


    private CompositeServerStartupPropertiesInterceptor(Builder builder)
    {
        this.interceptors = builder.interceptors;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public void actionDuringIteration(Map.Entry<String, String> entry)
    {
        this.interceptors.forEach(i -> i.actionDuringIteration(entry));
    }


    public void actionAfterIteration()
    {
        this.interceptors.forEach(ServerStartupPropertiesInterceptor::actionAfterIteration);
    }
}
