package de.hybris.platform.core.cors.loader.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.cors.loader.CorsPropertiesLoader;

public class RegistryBasedCorsPropertiesLoader implements CorsPropertiesLoader
{
    public ImmutableMap<String, String> loadProperties(String contextName)
    {
        return ImmutableMap.copyOf(Registry.getCurrentTenantNoFallback().getConfig()
                        .getParametersMatching("corsfilter\\." + contextName + "\\.(.*)", false));
    }
}
