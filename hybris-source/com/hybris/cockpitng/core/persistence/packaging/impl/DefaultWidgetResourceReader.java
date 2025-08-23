/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceCache;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default cockpit widgets resources loader - asks all defined loader whether any of them is able to load a resource.
 */
public class DefaultWidgetResourceReader implements CockpitResourceLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetResourceReader.class);
    public static final String COCKPITNG_RESOURCELOADER_RESOURCECACHE_ENABLED = "cockpitng.resourceloader.resourcecache.enabled";
    private List<CockpitResourceLoader> loaders;
    private CockpitResourceCache cockpitResourceCache;
    private CockpitProperties cockpitProperties;
    private Boolean resourceCacheEnabled;


    public List<CockpitResourceLoader> getLoaders()
    {
        return loaders;
    }


    @Required
    public void setLoaders(final List<CockpitResourceLoader> loaders)
    {
        this.loaders = loaders;
    }


    @Override
    public InputStream getViewResourceAsStream(final String path)
    {
        InputStream is = getResourceAsStreamFromCache(path);
        if(is == null)
        {
            for(final Iterator<CockpitResourceLoader> it = getLoaders().iterator(); it.hasNext() && is == null; )
            {
                is = it.next().getViewResourceAsStream(path);
            }
        }
        return addResourceToCache(path, is);
    }


    @Override
    public String getViewResourceAsString(final WidgetJarLibInfo jarLibInfo, final String widgetFilename)
    {
        String resource = null;
        final String uid = getResourceCacheUid(jarLibInfo, widgetFilename);
        try(final InputStream is = getResourceAsStreamFromCache(uid))
        {
            if(is == null)
            {
                final Iterator<CockpitResourceLoader> it = getLoaders().iterator();
                while(it.hasNext() && resource == null)
                {
                    resource = it.next().getViewResourceAsString(jarLibInfo, widgetFilename);
                }
                if(resource != null && isResourceCacheEnabled())
                {
                    addResourceToCache(uid, resource.getBytes());
                }
            }
            else
            {
                resource = IOUtils.toString(is);
            }
        }
        catch(final IOException e)
        {
            LOG.error(e.getMessage(), e);
        }
        return resource;
    }


    @Override
    public InputStream getResourceAsStream(final String path)
    {
        InputStream stream = getResourceAsStreamFromCache(path);
        if(stream != null)
        {
            return stream;
        }
        final Iterator<CockpitResourceLoader> it = getLoaders().iterator();
        while(it.hasNext() && stream == null)
        {
            final CockpitResourceLoader resourceLoader = it.next();
            stream = resourceLoader.getResourceAsStream(path);
        }
        return addResourceToCache(path, stream);
    }


    @Override
    public boolean hasResource(final String path)
    {
        boolean hasResource = false;
        final Iterator<CockpitResourceLoader> it = getLoaders().iterator();
        while(it.hasNext() && !hasResource)
        {
            hasResource = it.next().hasResource(path);
        }
        return hasResource;
    }


    @Override
    public InputStream getResourceAsStream(final WidgetJarLibInfo jarLibInfo, final String widgetFilename)
    {
        final String uid = getResourceCacheUid(jarLibInfo, widgetFilename);
        InputStream stream = getResourceAsStreamFromCache(uid);
        if(stream != null)
        {
            return stream;
        }
        final Iterator<CockpitResourceLoader> it = getLoaders().iterator();
        while(it.hasNext() && stream == null)
        {
            final CockpitResourceLoader resourceLoader = it.next();
            stream = resourceLoader.getResourceAsStream(jarLibInfo, widgetFilename);
        }
        return addResourceToCache(uid, stream);
    }


    @Override
    public boolean hasResource(final WidgetJarLibInfo jarLibInfo, final String path)
    {
        return getLoaders().stream().anyMatch(loader -> loader.hasResource(jarLibInfo, path));
    }


    @Override
    public boolean hasResource(final File moduleJar, final String path)
    {
        return getLoaders().stream().anyMatch(loader -> loader.hasResource(moduleJar, path));
    }


    @Override
    public InputStream getResourceAsStream(final File moduleJar, final String path)
    {
        final String uid = getResourceCacheUid(moduleJar, null, path);
        InputStream stream = getResourceAsStreamFromCache(uid);
        if(stream != null)
        {
            return stream;
        }
        final Iterator<CockpitResourceLoader> it = getLoaders().iterator();
        while(it.hasNext() && stream == null)
        {
            final CockpitResourceLoader resourceLoader = it.next();
            stream = resourceLoader.getResourceAsStream(moduleJar, path);
        }
        return addResourceToCache(uid, stream);
    }


    protected String getResourceCacheUid(final WidgetJarLibInfo jarLibInfo, final String requestURI)
    {
        if(jarLibInfo == null)
        {
            return requestURI;
        }
        return getResourceCacheUid(jarLibInfo.getJarPath(), jarLibInfo.getPrefix(), requestURI);
    }


    protected String getResourceCacheUid(final File moduleJar, final String prefix, final String requestURI)
    {
        final String ret;
        if(moduleJar != null)
        {
            ret = String.format("jar://%s!%s", moduleJar.getName(),
                            AbstractCockpitResourceReader.getFullResourceName(prefix, requestURI));
        }
        else
        {
            ret = AbstractCockpitResourceReader.adaptResourceName(requestURI);
        }
        return ret;
    }


    protected InputStream addResourceToCache(final String cacheUid, InputStream is)
    {
        if(is != null && isResourceCacheEnabled())
        {
            try
            {
                final byte[] res = IOUtils.toByteArray(is);
                addResourceToCache(cacheUid, res);
                is = new ByteArrayInputStream(res);
            }
            catch(final IOException e)
            {
                LOG.error(e.getMessage(), e);
                is = null;
            }
            finally
            {
                IOUtils.closeQuietly(is);
            }
        }
        return is;
    }


    protected void addResourceToCache(final String cacheUid, final byte[] writeString)
    {
        if(isResourceCacheEnabled())
        {
            getCockpitResourceCache().addResourceToCache(cacheUid, writeString);
        }
    }


    protected InputStream getResourceAsStreamFromCache(final String cacheUid)
    {
        if(isResourceCacheEnabled())
        {
            final byte[] resourceAsBytes = getCockpitResourceCache().getResourceAsBytes(cacheUid);
            if(resourceAsBytes != null && resourceAsBytes.length != 0)
            {
                return new ByteArrayInputStream(resourceAsBytes);
            }
        }
        return null;
    }


    protected boolean isResourceCacheEnabled()
    {
        if(resourceCacheEnabled == null)
        {
            resourceCacheEnabled = Boolean
                            .valueOf(Boolean.parseBoolean(getCockpitProperties().getProperty(COCKPITNG_RESOURCELOADER_RESOURCECACHE_ENABLED))
                                            && getCockpitResourceCache() != null);
        }
        return resourceCacheEnabled.booleanValue();
    }


    protected CockpitResourceCache getCockpitResourceCache()
    {
        return cockpitResourceCache;
    }


    @Required
    public void setCockpitResourceCache(final CockpitResourceCache cockpitResourceCache)
    {
        this.cockpitResourceCache = cockpitResourceCache;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
