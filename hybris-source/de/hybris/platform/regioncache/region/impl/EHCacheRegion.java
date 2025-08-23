package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class EHCacheRegion implements CacheRegion
{
    protected CacheLifecycleCallback lifecycleCallback = null;
    protected CacheManager manager;
    protected Cache cacheMap;
    private final int maxEntries;
    private final String name;
    private final String evictionPolicyStr;
    private MemoryStoreEvictionPolicy evictionPolicy = MemoryStoreEvictionPolicy.FIFO;
    private String[] handledTypes;
    protected final CacheStatistics stats;
    private final boolean exclusiveComputation;
    private volatile boolean statsEnabled;
    private final Long ttlSeconds;
    private static final Logger LOG = Logger.getLogger(EHCacheRegion.class);


    public String toString()
    {
        return "Name: " + this.name + ", size: " + this.maxEntries + ", types: " + Arrays.deepToString((Object[])this.handledTypes) + ", exclusive computation: " + this.exclusiveComputation + ", statistics: " + this.statsEnabled + ", class: " +
                        getClass();
    }


    public EHCacheRegion(String name, int maxEntries)
    {
        this(name, maxEntries, "LRU", false, true, null);
    }


    public EHCacheRegion(String name, int maxEntries, String evictionPolicy)
    {
        this(name, maxEntries, evictionPolicy, false, true, null);
    }


    public EHCacheRegion(String name, int maxEntries, String evictionPolicy, boolean exclusiveComputation, boolean statsEnabled)
    {
        this(name, maxEntries, evictionPolicy, exclusiveComputation, statsEnabled, null);
    }


    public EHCacheRegion(String name, int maxEntries, String evictionPolicy, boolean exclusiveComputation, boolean statsEnabled, Long ttlSeconds)
    {
        this.name = name;
        this.maxEntries = maxEntries;
        this.evictionPolicyStr = evictionPolicy;
        this.stats = createCacheStatistics();
        this.exclusiveComputation = exclusiveComputation;
        this.statsEnabled = statsEnabled;
        this.ttlSeconds = ttlSeconds;
    }


    protected CacheStatistics createCacheStatistics()
    {
        return new CacheStatistics();
    }


    protected CacheConfiguration createCacheConfiguration()
    {
        CacheConfiguration config = new CacheConfiguration();
        if("LRU".equals(this.evictionPolicyStr))
        {
            this.evictionPolicy = MemoryStoreEvictionPolicy.LRU;
        }
        else if("LFU".equals(this.evictionPolicyStr))
        {
            this.evictionPolicy = MemoryStoreEvictionPolicy.LFU;
        }
        LOG.info("EHCacheRegion " + this.name + ", eviction policy " + this.evictionPolicy + ", size " + this.maxEntries);
        config.setMemoryStoreEvictionPolicy(this.evictionPolicy.toString());
        config.setMaxEntriesLocalHeap(this.maxEntries);
        config.setName(this.name);
        config.addPersistence((new PersistenceConfiguration()).strategy(PersistenceConfiguration.Strategy.NONE));
        if(this.ttlSeconds == null)
        {
            config.setEternal(true);
        }
        else
        {
            config.setEternal(false);
            config.setTimeToLiveSeconds(this.ttlSeconds.longValue());
        }
        return config;
    }


    @PostConstruct
    public void init()
    {
        this.manager = CacheManager.getInstance();
        if(this.manager.cacheExists(this.name))
        {
            this.manager.removeCache(this.name);
        }
        Cache cacheMap = new Cache(createCacheConfiguration());
        cacheMap.getCacheEventNotificationService().registerListener(createCacheEventListener());
        this.manager.addCache(cacheMap);
        this.cacheMap = this.manager.getCache(this.name);
    }


    @PreDestroy
    public void destroy()
    {
        try
        {
            LOG.debug("Unregistering EHCache region name: " + this.name);
            if(this.manager.getStatus() != Status.STATUS_SHUTDOWN && this.manager.cacheExists(this.name))
            {
                this.manager.removeCache(this.name);
            }
        }
        catch(Throwable t)
        {
            LOG.warn("Unable to unregister from EH Cache manager", t);
        }
    }


    protected CacheEventListener createCacheEventListener()
    {
        return (CacheEventListener)new Object(this);
    }


    public Object get(CacheKey key)
    {
        Element element = this.cacheMap.getQuiet(key);
        Object value = (element == null) ? null : element.getObjectValue();
        if(this.statsEnabled)
        {
            if(value != null)
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


    public long getMaxReachedSize()
    {
        return this.cacheMap.getSize();
    }


    public Object getWithLoader(CacheKey key, CacheValueLoader loader) throws CacheValueLoadException
    {
        boolean missed = false;
        Element element = null;
        if(this.exclusiveComputation)
        {
            element = this.cacheMap.get(key);
            if(element == null)
            {
                try
                {
                    this.cacheMap.acquireWriteLockOnKey(key);
                    element = this.cacheMap.getQuiet(key);
                    if(element == null)
                    {
                        if(this.statsEnabled)
                        {
                            this.stats.fetched(key.getTypeCode());
                        }
                        element = createElement(key, loader.load(key));
                        this.cacheMap.put(element);
                        missed = true;
                    }
                }
                finally
                {
                    this.cacheMap.releaseWriteLockOnKey(key);
                }
            }
        }
        else
        {
            element = this.cacheMap.get(key);
            if(element == null)
            {
                if(this.statsEnabled)
                {
                    this.stats.fetched(key.getTypeCode());
                }
                Object value = loader.load(key);
                element = createElement(key, value);
                Element tmp = this.cacheMap.putIfAbsent(element);
                if(tmp == null)
                {
                    missed = true;
                }
                else
                {
                    element = tmp;
                }
            }
        }
        if(missed && this.lifecycleCallback != null)
        {
            this.lifecycleCallback.onAfterAdd((CacheKey)element.getObjectKey(), element.getObjectValue(), this);
        }
        if(this.statsEnabled)
        {
            if(missed)
            {
                this.stats.missed(key.getTypeCode());
            }
            else
            {
                this.stats.hit(key.getTypeCode());
            }
        }
        return element.getObjectValue();
    }


    private Element createElement(CacheKey key, Object value)
    {
        Element elem = new Element(key, value);
        if(this.ttlSeconds == null)
        {
            elem.setTimeToIdle(0);
            elem.setTimeToLive(0);
            elem.setEternal(true);
        }
        return elem;
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


    public long getCacheMaxEntries()
    {
        return this.maxEntries;
    }


    public void registerLifecycleCallback(CacheLifecycleCallback callback)
    {
        this.lifecycleCallback = callback;
    }


    public void clearCache()
    {
        this.cacheMap.removeAll();
        this.stats.clear();
    }


    public Object invalidate(CacheKey key, boolean fireEventsEvenIfNotRemoved)
    {
        Object value = null;
        Element element = null;
        element = this.cacheMap.removeAndReturnElement(key);
        if(element != null)
        {
            value = element.getObjectValue();
        }
        if(element == null && fireEventsEvenIfNotRemoved && this.lifecycleCallback != null)
        {
            this.lifecycleCallback.onAfterRemove(key, value, this);
        }
        if(element != null && this.statsEnabled)
        {
            this.stats.invalidated(key.getTypeCode());
        }
        return value;
    }


    public Collection<CacheKey> getAllKeys()
    {
        return this.cacheMap.getKeys();
    }


    public Object remove(CacheKey key, boolean fireEvents)
    {
        Element elem = null;
        if(fireEvents)
        {
            elem = this.cacheMap.removeAndReturnElement(key);
        }
        else
        {
            this.cacheMap.acquireWriteLockOnKey(key);
            try
            {
                elem = this.cacheMap.getQuiet(key);
                if(elem != null)
                {
                    this.cacheMap.removeQuiet(key);
                }
            }
            finally
            {
                this.cacheMap.releaseWriteLockOnKey(key);
            }
        }
        if(elem != null)
        {
            this.stats.invalidated(key.getTypeCode());
        }
        return (elem == null) ? null : elem.getObjectValue();
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = handledTypes;
    }


    public boolean containsKey(CacheKey key)
    {
        return this.cacheMap.isKeyInCache(key);
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


    public CacheRegion unwrap()
    {
        return this;
    }
}
