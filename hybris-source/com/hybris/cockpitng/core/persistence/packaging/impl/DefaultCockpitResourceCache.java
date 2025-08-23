/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceCache;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default CockpitResourceCache implementation. Uses a {@link ConcurrentHashMap} for caching and
 * {@link ReentrantReadWriteLock} for handling concurrent access.
 */
public class DefaultCockpitResourceCache implements CockpitResourceCache
{
    public static final String COCKPITNG_RESOURCELOADER_RESOURCECACHE_MAXENTRYSIZE = "cockpitng.resourceloader.resourcecache.maxentrysize";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitResourceCache.class);
    private CockpitProperties cockpitProperties;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReadLock rLock = rwLock.readLock();
    private final WriteLock wLock = rwLock.writeLock();
    private long size;
    private Long maxEntrySize;
    private final Map<String, byte[]> cacheMap = new ConcurrentHashMap<String, byte[]>();


    @Override
    public byte[] getResourceAsBytes(final String uuid)
    {
        if(uuid != null)
        {
            rLock.lock();
            try
            {
                return cacheMap.get(uuid);
            }
            finally
            {
                rLock.unlock();
            }
        }
        return new byte[0];
    }


    @Override
    public byte[] addResourceToCache(final String uuid, final byte[] resource)
    {
        if(resource != null && uuid != null)
        {
            wLock.lock();
            try
            {
                if(getMaxEntrySize() > 0 && resource.length > getMaxEntrySize())
                {
                    LOG.debug("Could not add '{}' to cache, maxEntrySize exceeded [size: {},max: {}].", uuid, resource.length,
                                    getMaxEntrySize());
                }
                else
                {
                    cacheMap.put(uuid, resource);
                    size += resource.length;
                }
            }
            finally
            {
                wLock.unlock();
            }
        }
        return resource;
    }


    @Override
    public void reset()
    {
        wLock.lock();
        try
        {
            size = 0;
            cacheMap.clear();
        }
        finally
        {
            wLock.unlock();
        }
    }


    @Override
    public long getSize()
    {
        rLock.lock();
        try
        {
            return size;
        }
        finally
        {
            rLock.unlock();
        }
    }


    @Override
    public long getAverageEntrySize()
    {
        rLock.lock();
        try
        {
            long realSize = 0;
            int numberOfEntries = 0;
            for(final byte[] entry : cacheMap.values())
            {
                if(entry != null)
                {
                    realSize += entry.length;
                    numberOfEntries++;
                }
            }
            return numberOfEntries == 0 ? 0 : (realSize / numberOfEntries);
        }
        finally
        {
            rLock.unlock();
        }
    }


    @Override
    public Collection<String> getKeys()
    {
        return cacheMap.keySet();
    }


    public long getMaxEntrySize()
    {
        if(maxEntrySize == null)
        {
            maxEntrySize = Long.valueOf(initMaxEntrySize());
        }
        return maxEntrySize.longValue();
    }


    protected long initMaxEntrySize()
    {
        long ret = 0;
        if(getCockpitProperties() != null)
        {
            final String property = getCockpitProperties().getProperty(COCKPITNG_RESOURCELOADER_RESOURCECACHE_MAXENTRYSIZE);
            if(property != null)
            {
                try
                {
                    ret = Long.parseLong(property);
                }
                catch(final NumberFormatException e)
                {
                    LOG.error("Could not parse '{}', expected 'long' but got {}", COCKPITNG_RESOURCELOADER_RESOURCECACHE_MAXENTRYSIZE,
                                    property);
                }
            }
        }
        return ret;
    }


    public void setMaxEntrySize(final long maxEntrySize)
    {
        this.maxEntrySize = Long.valueOf(maxEntrySize);
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
