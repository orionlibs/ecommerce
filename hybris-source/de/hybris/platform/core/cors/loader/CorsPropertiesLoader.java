package de.hybris.platform.core.cors.loader;

import com.google.common.collect.ImmutableMap;

public interface CorsPropertiesLoader
{
    ImmutableMap<String, String> loadProperties(String paramString);
}
