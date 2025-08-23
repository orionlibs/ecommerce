/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPropertyResolverRegistry implements CockpitProperties
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPropertyResolverRegistry.class);
    private static final int DEFAULT_PROPERTY_CACHE_TIMEOUT = 60;
    private Cache<String, Object> timedCache;
    private Map<String, String> properties;
    private String localPropertyFile;
    private String projectPropertyFile;
    private Integer cacheTimeoutInSeconds;


    @Override
    public String getProperty(final String key)
    {
        return getProperties().get(key);
    }


    @Override
    public boolean getBoolean(final String key)
    {
        return getBoolean(key, false);
    }


    @Override
    public boolean getBoolean(final String key, final boolean defaultValue)
    {
        try
        {
            return (boolean)getTimedCache().get(key,
                            () -> properties != null && properties.containsKey(key) ? Boolean.valueOf(properties.get(key)) : defaultValue);
        }
        catch(final ExecutionException e)
        {
            LOG.warn(e.getMessage(), e);
            return false;
        }
    }


    @Override
    public int getInteger(final String key)
    {
        try
        {
            return (int)getTimedCache().get(key, () -> loadInteger(key));
        }
        catch(final ExecutionException e)
        {
            LOG.warn(e.getMessage(), e);
            return 0;
        }
    }


    @Override
    public double getDouble(final String key)
    {
        try
        {
            return (double)getTimedCache().get(key, () -> loadDouble(key));
        }
        catch(final ExecutionException e)
        {
            LOG.warn(e.getMessage(), e);
            return 0d;
        }
    }


    @Override
    public Map<String, String> getProperties()
    {
        return properties == null ? Collections.emptyMap() : properties;
    }


    /**
     * @deprecated since 6.5 - properties are loaded from project and local files and should not be set manually
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void setProperties(final Map<String, String> properties)
    {
        this.properties = properties;
        if(timedCache != null)
        {
            timedCache.invalidateAll();
        }
    }


    public void initProperties()
    {
        final Map<String, String> originalProperties = this.properties;
        this.properties = new HashMap<>();
        if(MapUtils.isNotEmpty(originalProperties))
        {
            this.properties.putAll(originalProperties);
        }
        initProperties(projectPropertyFile);
        initProperties(localPropertyFile);
    }


    protected void initProperties(final String filename)
    {
        if(StringUtils.isBlank(filename))
        {
            LOG.warn("Could not read property file since it hasn't been specified.");
            return;
        }
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        if(resourceAsStream == null)
        {
            LOG.debug("Property file doesn't exist.");
            return;
        }
        try
        {
            final Properties localProperties = new Properties();
            localProperties.load(resourceAsStream);
            for(final Map.Entry<Object, Object> entry : localProperties.entrySet())
            {
                properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        catch(final IOException e)
        {
            LOG.error("Could not read property file '" + filename + "', error was:", e);
            return;
        }
        finally
        {
            IOUtils.closeQuietly(resourceAsStream);
        }
    }


    private Cache<String, Object> getTimedCache()
    {
        if(timedCache == null)
        {
            timedCache = CacheBuilder.newBuilder().expireAfterWrite(getCacheTimeoutInSeconds(), TimeUnit.SECONDS).build();
        }
        return timedCache;
    }


    private Integer loadInteger(final String key)
    {
        try
        {
            return properties != null ? Integer.parseInt(properties.get(key)) : 0;
        }
        catch(final NumberFormatException nfe)
        {
            LOG.warn(String.format("Could not parse int from property [%s=%s]", key, properties.get(key)));
            return 0;
        }
    }


    private Object loadDouble(final String key)
    {
        try
        {
            return properties != null ? Double.parseDouble(properties.get(key)) : 0d;
        }
        catch(final NumberFormatException nfe)
        {
            LOG.warn(String.format("Could not parse double from property [%s=%s]", key, properties.get(key)));
            return 0d;
        }
    }


    protected String getLocalPropertyFile()
    {
        return localPropertyFile;
    }


    public void setLocalPropertyFile(final String localPropertyFile)
    {
        this.localPropertyFile = localPropertyFile;
    }


    protected String getProjectPropertyFile()
    {
        return projectPropertyFile;
    }


    public void setProjectPropertyFile(final String projectPropertyFile)
    {
        this.projectPropertyFile = projectPropertyFile;
    }


    /**
     * @deprecated since 6.5 - this method will be removed after removal of {@link #setProperties(Map)}.
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public Integer getCacheTimeoutInSeconds()
    {
        return cacheTimeoutInSeconds == null ? DEFAULT_PROPERTY_CACHE_TIMEOUT : cacheTimeoutInSeconds;
    }


    /**
     * @deprecated since 6.5 - this method will be removed after removal of {@link #setProperties(Map)}.
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void setCacheTimeoutInSeconds(final Integer cacheTimeoutInSeconds)
    {
        this.cacheTimeoutInSeconds = cacheTimeoutInSeconds;
    }
}
