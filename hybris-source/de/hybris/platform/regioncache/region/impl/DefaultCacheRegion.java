package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheMap;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.EvictionCallback;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCacheRegion implements CacheRegion
{
    private static final Logger LOG = Logger.getLogger(DefaultCacheRegion.class);
    protected CacheMap cacheMap;
    protected String[] handledTypes;
    protected CacheLifecycleCallback lifecycleCallback;
    protected final String name;
    protected final CacheStatistics stats;
    protected final ReentrantReadWriteLock sharedLock;
    protected volatile boolean statsEnabled;
    protected final boolean exclusiveComputation;


    public DefaultCacheRegion(String name)
    {
        this(name, null, new CacheStatistics(), null);
    }


    public DefaultCacheRegion(String name, CacheMap cacheMap, String[] handledTypes)
    {
        this(name, cacheMap, new CacheStatistics(), handledTypes, false, true);
    }


    public DefaultCacheRegion(String name, boolean exclusiveComputation, boolean statsEnabled)
    {
        this(name, null, new CacheStatistics(), null, exclusiveComputation, statsEnabled);
    }


    public DefaultCacheRegion(String name, CacheMap cacheMap, CacheStatistics stats, String[] handledTypes)
    {
        this(name, cacheMap, stats, handledTypes, false, true);
    }


    public DefaultCacheRegion(String name, CacheMap cacheMap, CacheStatistics stats, String[] handledTypes, boolean exclusiveComputation, boolean statsEnabled)
    {
        this.cacheMap = cacheMap;
        this.stats = stats;
        this.name = name;
        this.handledTypes = handledTypes;
        this.sharedLock = new ReentrantReadWriteLock();
        this.exclusiveComputation = exclusiveComputation;
        this.statsEnabled = statsEnabled;
    }


    public DefaultCacheRegion(String name, int maxEntries, boolean exclusiveComputation, boolean statsEnabled)
    {
        this(name, (CacheMap)new DefaultCacheMap(maxEntries), new CacheStatistics(), null, exclusiveComputation, statsEnabled);
    }


    @Deprecated(since = "1811", forRemoval = true)
    public DefaultCacheRegion(String name, int maxEntries, boolean exclusiveComputation, String evictionPolicy, boolean statsEnabled)
    {
        this(name, (CacheMap)new DefaultCacheMap(maxEntries), new CacheStatistics(), null, exclusiveComputation, statsEnabled);
    }


    public DefaultCacheRegion(String name, int maxEntries)
    {
        this(name, (CacheMap)new DefaultCacheMap(maxEntries), new CacheStatistics(), null, false, true);
    }


    @PostConstruct
    public void init()
    {
        this.cacheMap.registerEvictionCallback((EvictionCallback)new Object(this));
    }


    public Object get(CacheKey key)
    {
        getLock().readLock().lock();
        try
        {
            return this.cacheMap.get(key);
        }
        finally
        {
            getLock().readLock().unlock();
        }
    }


    public long getMaxReachedSize()
    {
        return this.cacheMap.size();
    }


    public Object getWithLoader(CacheKey key, CacheValueLoader loader) throws CacheValueLoadException
    {
        Object value = null;
        boolean hit = true;
        getLock().readLock().lock();
        try
        {
            value = this.cacheMap.get(key);
        }
        finally
        {
            getLock().readLock().unlock();
        }
        if(value == null)
        {
            Object loaded = null;
            if(!this.exclusiveComputation)
            {
                if(this.statsEnabled)
                {
                    this.stats.fetched(key.getTypeCode());
                }
                loaded = loader.load(key);
            }
            getLock().writeLock().lock();
            try
            {
                value = this.cacheMap.get(key);
                if(value == null)
                {
                    hit = false;
                    if(this.exclusiveComputation)
                    {
                        if(this.statsEnabled)
                        {
                            this.stats.fetched(key.getTypeCode());
                        }
                        value = loader.load(key);
                    }
                    else
                    {
                        value = loaded;
                    }
                    this.cacheMap.put(key, value);
                    if(this.lifecycleCallback != null)
                    {
                        this.lifecycleCallback.onAfterAdd(key, value, this);
                    }
                }
            }
            finally
            {
                getLock().writeLock().unlock();
            }
        }
        if(this.statsEnabled)
        {
            if(hit)
            {
                this.stats.hit(key.getTypeCode());
            }
            else
            {
                this.stats.missed(key.getTypeCode());
            }
        }
        return value;
    }


    public Object invalidate(CacheKey key, boolean fireEventsEvenIfNotRemoved)
    {
        getLock().writeLock().lock();
        Object obj = null;
        try
        {
            obj = this.cacheMap.remove(key);
            if((obj != null || fireEventsEvenIfNotRemoved) && this.lifecycleCallback != null)
            {
                this.lifecycleCallback.onAfterRemove(key, obj, this);
            }
        }
        finally
        {
            getLock().writeLock().unlock();
        }
        if(this.statsEnabled && obj != null)
        {
            this.stats.invalidated(key.getTypeCode());
        }
        return obj;
    }


    public String getName()
    {
        return this.name;
    }


    public String[] getHandledTypes()
    {
        return this.handledTypes;
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = handledTypes;
    }


    public CacheMap getCacheMap()
    {
        return this.cacheMap;
    }


    public void setCacheMap(CacheMap cacheMap)
    {
        this.cacheMap = cacheMap;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        DefaultCacheRegion other = (DefaultCacheRegion)obj;
        if(this.name == null)
        {
            if(other.name != null)
            {
                return false;
            }
        }
        else if(!this.name.equals(other.name))
        {
            return false;
        }
        return true;
    }


    public CacheStatistics getCacheRegionStatistics()
    {
        return this.stats;
    }


    public long getCacheMaxEntries()
    {
        return this.cacheMap.getCacheMaxEntries();
    }


    public void clearCache()
    {
        getLock().writeLock().lock();
        try
        {
            this.cacheMap.clear();
        }
        finally
        {
            getLock().writeLock().unlock();
        }
        this.stats.clear();
    }


    private void notifyEvictionListener(CacheKey key, Object value)
    {
        if(this.lifecycleCallback != null)
        {
            this.lifecycleCallback.onAfterEviction(key, value, this);
        }
    }


    public void registerLifecycleCallback(CacheLifecycleCallback callback)
    {
        this.lifecycleCallback = callback;
    }


    private ReadWriteLock getLock()
    {
        return this.sharedLock;
    }


    public String toString()
    {
        return "Name: " + this.name + ", size: " + this.cacheMap.getCacheMaxEntries() + ", types: " + Arrays.deepToString((Object[])this.handledTypes) + ", exclusive computation: " + this.exclusiveComputation + ", statistics: " + this.statsEnabled + ", class: " +
                        getClass();
    }


    public Collection<CacheKey> getAllKeys()
    {
        Lock readLock = getLock().readLock();
        readLock.lock();
        try
        {
            return this.cacheMap.getAllKeys();
        }
        finally
        {
            readLock.unlock();
        }
    }


    public Object remove(CacheKey key, boolean fireEvents)
    {
        Lock writeLock = getLock().writeLock();
        writeLock.lock();
        try
        {
            Object value = this.cacheMap.remove(key);
            if(value != null && fireEvents && this.lifecycleCallback != null)
            {
                this.lifecycleCallback.onAfterRemove(key, value, this);
            }
            if(this.statsEnabled && value != null)
            {
                this.stats.invalidated(key.getTypeCode());
            }
            return value;
        }
        finally
        {
            writeLock.unlock();
        }
    }


    public boolean containsKey(CacheKey key)
    {
        Lock readLock = getLock().readLock();
        readLock.lock();
        try
        {
            return this.cacheMap.contains(key);
        }
        finally
        {
            readLock.unlock();
        }
    }


    public void setStatsEnabled(boolean enabled)
    {
        this.statsEnabled = enabled;
        LOG.debug("statistics for region \"" + this.name + "\" are " + (enabled ? "enabled" : "disabled"));
    }


    public boolean isStatsEnabled()
    {
        return this.statsEnabled;
    }
}
