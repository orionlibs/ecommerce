package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.invalidation.InvalidationFilter;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.impl.GenerationalCacheDelegate;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.regioncache.region.CacheRegionProvider;
import de.hybris.platform.regioncache.region.CacheRegionResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

public class DefaultCacheController implements CacheController
{
    private static final Logger LOGGER = Logger.getLogger(DefaultCacheController.class);
    private CacheRegionProvider cacheRegionProvider;
    private GenerationalCacheDelegate generationalCacheDelegate;
    private final List<InvalidationFilter> filters;
    private final CacheRegionResolver[] resolvers;
    private final List<CacheLifecycleCallback> lifecycleCallbacks = new CopyOnWriteArrayList<>();
    private CacheRegion singleRegion = null;


    public DefaultCacheController(@Qualifier("cacheConfiguration") CacheConfiguration configuration)
    {
        if(configuration == null)
        {
            this.resolvers = new CacheRegionResolver[0];
            this.filters = null;
        }
        else
        {
            List<InvalidationFilter> filtersTmp = configuration.getFilters();
            if(filtersTmp != null && filtersTmp.size() == 1 && filtersTmp.get(0) == null)
            {
                this.filters = null;
            }
            else
            {
                this.filters = configuration.getFilters();
            }
            if(configuration.getResolvers() == null || configuration.getResolvers().isEmpty())
            {
                this.resolvers = new CacheRegionResolver[0];
            }
            else
            {
                this.resolvers = (CacheRegionResolver[])configuration.getResolvers().toArray((Object[])new CacheRegionResolver[0]);
            }
            if(this.resolvers != null && this.resolvers.length == 1 && this.resolvers[0] instanceof de.hybris.platform.regioncache.region.SingleCacheRegionResolver)
            {
                this.singleRegion = this.resolvers[0].resolveForGet(null);
            }
        }
    }


    @PostConstruct
    public void init()
    {
        Collection<CacheRegion> regions = getRegions();
        CacheRegionResolver[] resolvers = getResolvers();
        if(resolvers == null || resolvers.length < 1)
        {
            throw new IllegalArgumentException("No region resolvers found.");
        }
        LOGGER.info("Regioncache initialized with region count " + regions.size());
        for(CacheRegion region : regions)
        {
            LOGGER.info("Cache region: " + region.toString());
            Object object = new Object(this);
            region.registerLifecycleCallback((CacheLifecycleCallback)object);
        }
    }


    private boolean isAllowInvalidation(CacheKey key, CacheRegion region)
    {
        List<InvalidationFilter> filters = getFilters();
        if(filters != null)
        {
            for(int i = 0; i < filters.size(); i++)
            {
                if(!((InvalidationFilter)filters.get(i)).allowInvalidation(key, region))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public void invalidate(CacheKey key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Unable to invalidate NULL key");
        }
        List<CacheRegion> regions = resolveRegionsForInvalidation(key);
        if(regions == null || regions.size() < 1)
        {
            throw new CacheRegionNotSpecifiedException("Unable to get region for invalidation");
        }
        this.generationalCacheDelegate.incrementGeneration(key);
        boolean fireEventsEvenIfNotRemoved = !(key instanceof de.hybris.platform.regioncache.key.RegistrableCacheKey);
        for(int i = 0; i < regions.size(); i++)
        {
            CacheRegion region = regions.get(i);
            if(isAllowInvalidation(key, region))
            {
                region.invalidate(key, fireEventsEvenIfNotRemoved);
            }
        }
    }


    public void remove(CacheKey key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Unable to remove NULL key");
        }
        CacheRegion region = resolveCacheRegionForAdd(key);
        boolean fireEvents = key instanceof de.hybris.platform.regioncache.key.RegistrableCacheKey;
        Object value = region.remove(key, fireEvents);
        if(value != null && !fireEvents)
        {
            executeOnAfterRemoveCallbacks(key, value, region);
        }
    }


    public <T> T getWithLoader(CacheKey key, CacheValueLoader<T> loader) throws CacheValueLoadException
    {
        CacheRegion region = resolveCacheRegionForAdd(key);
        return (T)region.getWithLoader(this.generationalCacheDelegate.getGenerationalCacheKey(key), loader);
    }


    public <T> T get(CacheKey key)
    {
        if(this.singleRegion != null)
        {
            return (T)this.singleRegion.get(key);
        }
        return (T)resolveCacheRegionForAdd(key).get(key);
    }


    public Collection<CacheRegion> resolveQueryRegionsForInvalidation(CacheKey key)
    {
        CacheRegionResolver[] resolvers = getResolvers();
        if(resolvers == null)
        {
            throw new CacheRegionNotSpecifiedException("No region resolvers defined.");
        }
        if(resolvers.length == 1)
        {
            return resolvers[0].resolveQueryRegions(key);
        }
        List<CacheRegion> regions = new ArrayList<>();
        for(int i = 0; i < resolvers.length; i++)
        {
            Collection<CacheRegion> tmpRegions = resolvers[i].resolveQueryRegions(key);
            if(tmpRegions != null)
            {
                regions.addAll(tmpRegions);
            }
        }
        return regions;
    }


    public List<CacheRegion> resolveRegionsForInvalidation(CacheKey key)
    {
        CacheRegionResolver[] resolvers = getResolvers();
        if(resolvers == null)
        {
            throw new CacheRegionNotSpecifiedException("No region resolvers defined.");
        }
        List<CacheRegion> regions = null;
        if(resolvers.length == 1)
        {
            regions = resolvers[0].resolveForInvalidation(key);
        }
        else
        {
            regions = new ArrayList<>(2);
            for(int i = 0; i < resolvers.length; i++)
            {
                Collection<CacheRegion> tmpRegions = resolvers[i].resolveForInvalidation(key);
                if(tmpRegions != null)
                {
                    regions.addAll(tmpRegions);
                }
            }
        }
        return (regions != null && regions.size() > 0) ? regions : this.cacheRegionProvider.getRegions();
    }


    public CacheRegion resolveCacheRegionForAdd(CacheKey key) throws CacheRegionNotSpecifiedException
    {
        if(this.singleRegion != null)
        {
            return this.singleRegion;
        }
        CacheRegionResolver[] resolvers = getResolvers();
        for(int i = 0; i < resolvers.length; i++)
        {
            CacheRegion region = resolvers[i].resolveForGet(key);
            if(region != null)
            {
                return region;
            }
        }
        throw new CacheRegionNotSpecifiedException("No region can be resolved for key " + key);
    }


    public CacheRegionResolver[] getResolvers()
    {
        return this.resolvers;
    }


    public List<InvalidationFilter> getFilters()
    {
        return this.filters;
    }


    public List<CacheRegion> getRegions()
    {
        return this.cacheRegionProvider.getAllRegions();
    }


    public void clearCache(CacheRegion region)
    {
        region.clearCache();
    }


    public void addLifecycleCallback(CacheLifecycleCallback callback)
    {
        if(this.lifecycleCallbacks.contains(callback))
        {
            LOGGER.info("Listener already registered: " + callback + " : " + callback.getClass().getName());
        }
        else
        {
            this.lifecycleCallbacks.add(callback);
            LOGGER.info("Registered Listener " + callback + " on cache controller. Currently registered " + this.lifecycleCallbacks
                            .size());
        }
    }


    public void removeLifecycleCallback(CacheLifecycleCallback callback)
    {
        boolean removed = this.lifecycleCallbacks.remove(callback);
        if(!removed)
        {
            LOGGER.info("Listener not un-registered (not found): " + callback + " : " + callback.getClass().getName());
        }
        else
        {
            LOGGER.info("Removed listener from cache controller. Currently registered " + this.lifecycleCallbacks.size());
        }
    }


    public int getLifecycleCallbackCnt()
    {
        return this.lifecycleCallbacks.size();
    }


    protected void executeOnAfterAddCallbacks(CacheKey key, Object value, CacheRegion region)
    {
        for(int i = 0; i < this.lifecycleCallbacks.size(); i++)
        {
            ((CacheLifecycleCallback)this.lifecycleCallbacks.get(i)).onAfterAdd(key, value, region);
        }
    }


    protected void executeOnAfterRemoveCallbacks(CacheKey key, Object value, CacheRegion region)
    {
        for(int i = 0; i < this.lifecycleCallbacks.size(); i++)
        {
            ((CacheLifecycleCallback)this.lifecycleCallbacks.get(i)).onAfterRemove(key, value, region);
        }
    }


    protected void executeOnEvictionCallbacks(CacheKey key, Object value, CacheRegion region)
    {
        for(int i = 0; i < this.lifecycleCallbacks.size(); i++)
        {
            ((CacheLifecycleCallback)this.lifecycleCallbacks.get(i)).onAfterEviction(key, value, region);
        }
    }


    protected void executeOnMissLoadCallbacks(CacheKey key, Object value, CacheRegion region)
    {
        for(int i = 0; i < this.lifecycleCallbacks.size(); i++)
        {
            ((CacheLifecycleCallback)this.lifecycleCallbacks.get(i)).onMissLoad(key, value, region);
        }
    }


    public void setStatsEnabled(boolean enabled)
    {
        for(CacheRegion region : getRegions())
        {
            region.setStatsEnabled(enabled);
        }
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public void registerTenant(String tenantId)
    {
        throw new UnsupportedOperationException("no CacheRegistry related logic use needed , using generational cache");
    }


    @Resource(name = "cacheRegionProvider")
    public void setCacheRegionProvider(CacheRegionProvider cacheRegionProvider)
    {
        this.cacheRegionProvider = cacheRegionProvider;
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public void setRegionRegistryAllocationStrategy(RegionRegistryAllocationStrategy regionRegistryAllocationStrategy)
    {
        throw new UnsupportedOperationException("no CacheRegistry related logic use needed , using generational cache");
    }


    @Resource(name = "generationalCacheDelegate")
    public void setGenerationalCacheDelegate(GenerationalCacheDelegate generationalCacheDelegate)
    {
        this.generationalCacheDelegate = generationalCacheDelegate;
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public CacheController unwrap()
    {
        return this;
    }
}
