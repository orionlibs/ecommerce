package de.hybris.platform.property.interceptor;

import java.util.Map;

public interface ServerStartupPropertiesInterceptor
{
    default void actionDuringIteration(Map.Entry<String, String> entry)
    {
    }


    default void actionAfterIteration()
    {
    }
}
