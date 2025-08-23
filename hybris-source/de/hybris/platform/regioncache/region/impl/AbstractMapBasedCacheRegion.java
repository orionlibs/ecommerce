package de.hybris.platform.regioncache.region.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractMapBasedCacheRegion implements CacheRegion
{
    private static final Logger LOG = Logger.getLogger(AbstractMapBasedCacheRegion.class);
    protected Map<CacheKey, Object> cacheMap;
    protected String[] handledTypes;
    protected CacheLifecycleCallback lifecycleCallback;
    protected final String name;
    protected final CacheStatistics stats;
    protected volatile boolean statsEnabled;


    public AbstractMapBasedCacheRegion(String name, boolean statsEnabled)
    {
        this(name, new CacheStatistics(), statsEnabled);
    }


    public AbstractMapBasedCacheRegion(String name, CacheStatistics stats, boolean statsEnabled)
    {
        this.name = name;
        this.stats = stats;
        this.statsEnabled = statsEnabled;
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = handledTypes;
    }


    public Object getWithLoader(CacheKey key, CacheValueLoader loader) throws CacheValueLoadException
    {
        Object value = null;
        boolean hit = true;
        value = this.cacheMap.get(key);
        if(value == null)
        {
            hit = false;
            value = loader.load(key);
            if(this.statsEnabled)
            {
                this.stats.fetched(key.getTypeCode());
            }
            Object previous = putIfAbsent(key, value);
            if(previous != null)
            {
                hit = true;
                if(this.lifecycleCallback != null)
                {
                    this.lifecycleCallback.onMissLoad(key, value, this);
                }
                value = previous;
            }
            if(!hit && this.lifecycleCallback != null)
            {
                this.lifecycleCallback.onAfterAdd(key, value, this);
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


    protected abstract Object putIfAbsent(CacheKey paramCacheKey, Object paramObject);


    public Object get(CacheKey key)
    {
        return this.cacheMap.get(key);
    }


    public Object invalidate(CacheKey key, boolean fireEventsEvenIfNotRemoved)
    {
        Object value = this.cacheMap.remove(key);
        if((value != null || fireEventsEvenIfNotRemoved) && this.lifecycleCallback != null)
        {
            this.lifecycleCallback.onAfterRemove(key, value, this);
        }
        if(this.statsEnabled && value != null)
        {
            this.stats.invalidated(key.getTypeCode());
        }
        return value;
    }


    public String getName()
    {
        return this.name;
    }


    public String[] getHandledTypes()
    {
        return this.handledTypes;
    }


    public CacheStatistics getCacheRegionStatistics()
    {
        return this.stats;
    }


    public void clearCache()
    {
        this.cacheMap.clear();
        this.stats.clear();
    }


    public void registerLifecycleCallback(CacheLifecycleCallback callback)
    {
        this.lifecycleCallback = callback;
    }


    public long getMaxReachedSize()
    {
        return this.cacheMap.size();
    }


    public Collection<CacheKey> getAllKeys()
    {
        return (Collection<CacheKey>)ImmutableSet.copyOf(this.cacheMap.keySet());
    }


    public Object remove(CacheKey key, boolean fireEvents)
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


    public boolean containsKey(CacheKey key)
    {
        return this.cacheMap.containsKey(key);
    }


    public void setStatsEnabled(boolean enabled)
    {
        this.statsEnabled = enabled;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("statistics for region \"" + this.name + "\" are " + (enabled ? "enabled" : "disabled"));
        }
    }


    public boolean isStatsEnabled()
    {
        return this.statsEnabled;
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
        AbstractMapBasedCacheRegion other = (AbstractMapBasedCacheRegion)obj;
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


    public String toString()
    {
        return "Name: " + this.name + ", size: " + this.cacheMap.size() + ", types: " + Arrays.deepToString((Object[])this.handledTypes) + ", statistics: " + this.statsEnabled + ", class: " +
                        getClass();
    }
}
