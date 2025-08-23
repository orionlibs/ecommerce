package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.CacheStatisticsEntry;
import de.hybris.platform.cache.relation.RelationCacheKey;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.logging.Logs;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;

public class RegionCacheAdapter extends StaticCache implements Cache, CacheLifecycleCallback
{
    private static final Logger LOGGER = Logger.getLogger(RegionCacheAdapter.class);
    private static final String MSG_KEY_AND_LOADER_NOT_NULLABLE = "Key and loader must NOT be NULL!";
    private final Tenant tenant;
    private final String tenantId;
    private final boolean forceExclusiveComputation;
    private volatile boolean isEnabled = true;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile CacheController controller;


    public RegionCacheAdapter(Tenant tenant, CacheController controller)
    {
        this.tenant = tenant;
        this.tenantId = (tenant == null) ? "master" : tenant.getTenantID();
        this.controller = controller;
        this.forceExclusiveComputation = (tenant == null || tenant.getConfig().getBoolean("cache.unit.exclusive.computation", false));
        if(this.controller != null)
        {
            controller.addLifecycleCallback(this);
        }
    }


    private CacheController getController()
    {
        if(this.controller == null)
        {
            synchronized(this)
            {
                if(this.controller == null)
                {
                    CacheController tmpController = (CacheController)Registry.getSingletonGlobalApplicationContext().getBean("cacheController", CacheController.class);
                    tmpController.addLifecycleCallback(this);
                    this.controller = tmpController;
                    checkLicence();
                }
            }
        }
        return this.controller;
    }


    private void checkLicence()
    {
        try
        {
            Cache cache = Registry.getCurrentTenant().getCache();
            int cacheSizeTotal = cache.getMaxAllowedSize();
            int licenceCacheLimit = Licence.getDefaultLicence().getCacheLimit();
            if(cacheSizeTotal > licenceCacheLimit)
            {
                LOGGER.error("**************");
                LOGGER.error("Configuration is not valid with the given licence - cache size limit exceeded ");
                LOGGER.error("Total size for all regions(currently:" + cacheSizeTotal + ") must be lower then " + licenceCacheLimit);
                LOGGER.error("**************");
                System.exit(-1);
            }
        }
        catch(IllegalStateException e)
        {
            if(RedeployUtilities.isShutdownInProgress())
            {
                LOGGER.warn("Check licence skipped since system is already shutting down");
            }
            else
            {
                throw e;
            }
        }
    }


    public int getMaxAllowedSize()
    {
        if(this.controller == null)
        {
            LOGGER.warn("Size not available because CacheController is not available yet");
            return 0;
        }
        Collection<CacheRegion> regions = getController().getRegions();
        int size = 0;
        if(regions != null)
        {
            for(CacheRegion cacheRegion : regions)
            {
                if(isNotUnlimitedCache(cacheRegion))
                {
                    size = (int)(size + cacheRegion.getCacheMaxEntries());
                }
            }
        }
        return size;
    }


    private boolean isNotUnlimitedCache(CacheRegion cacheRegion)
    {
        return !(cacheRegion instanceof de.hybris.platform.regioncache.region.UnlimitedCacheRegion);
    }


    public void invalidate(Object[] key, int invalidationType)
    {
        this.lock.readLock().lock();
        try
        {
            LegacyCacheKey legacyCacheKey;
            if(RelationCacheKey.isRelationKey(key))
            {
                RelationCacheKey relationCacheKey = RelationCacheKey.createKey(key);
            }
            else
            {
                legacyCacheKey = new LegacyCacheKey(key, this.tenantId);
            }
            getController().invalidate((CacheKey)legacyCacheKey);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }


    public AbstractCacheUnit getOrAddUnit(AbstractCacheUnit unit)
    {
        if(this.isEnabled)
        {
            this.lock.readLock().lock();
            try
            {
                CacheKey key = unit.getKey();
                CacheValueLoader<? extends AbstractCacheUnit> loader = unit.getLoader();
                if(key == null || loader == null)
                {
                    throw new IllegalArgumentException("Key and loader must NOT be NULL!");
                }
                if(getController() == null || !unit.isCachingSupported())
                {
                    return (AbstractCacheUnit)unit.load(key);
                }
                AbstractCacheUnit obj = (AbstractCacheUnit)getController().getWithLoader(key, loader);
                if(obj == null)
                {
                    this.controller.invalidate(key);
                    obj = (AbstractCacheUnit)getController().getWithLoader(key, loader);
                }
                if(obj == null)
                {
                    LOGGER.fatal("Cache Load failed", new Exception("stack trace"));
                }
                return obj;
            }
            finally
            {
                this.lock.readLock().unlock();
            }
        }
        return unit;
    }


    public void removeUnit(AbstractCacheUnit unit)
    {
        this.lock.readLock().lock();
        try
        {
            CacheKey key = unit.getKey();
            if(key == null)
            {
                throw new IllegalArgumentException("Key and loader must NOT be NULL!");
            }
            ((DefaultCacheController)getController()).remove(key);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }


    public boolean isForceExclusiveComputation()
    {
        return this.forceExclusiveComputation;
    }


    public void clear()
    {
        String name = null;
        CacheKey key = null;
        this.lock.writeLock().lock();
        try
        {
            clearRequestCache();
            clearStaticCache();
            Collection<CacheRegion> regions = getController().getRegions();
            for(CacheRegion region : regions)
            {
                name = region.getName();
                Collection<CacheKey> allKeys = region.getAllKeys();
                for(CacheKey cacheKey : allKeys)
                {
                    key = cacheKey;
                    onAfterRemove(cacheKey, region.get(cacheKey), region);
                }
                getController().clearCache(region);
            }
        }
        catch(RuntimeException e)
        {
            LOGGER.error("Unable to clear cache. Failed on region " + name + ". Last key " + key + " " + (
                            (key == null) ? null : key.getClass()), e);
            throw e;
        }
        finally
        {
            this.lock.writeLock().unlock();
        }
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
        if(!enabled)
        {
            clear();
        }
    }


    public long getAddCount()
    {
        return getMissCount();
    }


    public long getRemoveCount()
    {
        long cnt = 0L;
        Collection<CacheRegion> regions = getController().getRegions();
        for(CacheRegion region : regions)
        {
            cnt += region.getCacheRegionStatistics().getInvalidations();
        }
        return cnt;
    }


    public long getGetCount()
    {
        long cnt = 0L;
        Collection<CacheRegion> regions = getController().getRegions();
        for(CacheRegion region : regions)
        {
            cnt += region.getCacheRegionStatistics().getHits();
        }
        return cnt;
    }


    public long getMissCount()
    {
        long cnt = 0L;
        Collection<CacheRegion> regions = getController().getRegions();
        for(CacheRegion region : regions)
        {
            cnt += region.getCacheRegionStatistics().getMisses();
        }
        return cnt;
    }


    public boolean statsEnabled()
    {
        return true;
    }


    public void setStatsEnabled(boolean on)
    {
        getController().setStatsEnabled(on);
    }


    public void clearStats()
    {
        Collection<CacheRegion> regions = getController().getRegions();
        for(CacheRegion region : regions)
        {
            region.getCacheRegionStatistics().clear();
        }
    }


    public boolean isStatsEmpty()
    {
        return (getAddCount() == 0L);
    }


    public int getMaxReachedSize()
    {
        long cnt = 0L;
        Collection<CacheRegion> regions = getController().getRegions();
        for(CacheRegion region : regions)
        {
            cnt += region.getMaxReachedSize();
        }
        return (int)cnt;
    }


    public AbstractCacheUnit getUnit(AbstractCacheUnit unit) throws CacheRegionNotSpecifiedException
    {
        if(this.isEnabled)
        {
            this.lock.readLock().lock();
            try
            {
                CacheKey key = unit.getKey();
                if(key == null)
                {
                    throw new IllegalArgumentException("Key and loader must NOT be NULL!");
                }
                return (AbstractCacheUnit)getController().get(key);
            }
            finally
            {
                this.lock.readLock().unlock();
            }
        }
        return null;
    }


    public Set<? extends CacheStatisticsEntry> getStatistics(int upperBound, int lowerBound)
    {
        Set<CacheStatistics> stats = new HashSet<>();
        Collection<CacheRegion> regions = this.controller.getRegions();
        for(CacheRegion region : regions)
        {
            stats.add(region.getCacheRegionStatistics());
        }
        return (Set)stats;
    }


    public AbstractCacheUnit getAbstractCacheUnit(String firstKeyElement, String secondKeyElement, String thirdKeyElement, PK fourthKeyElement)
    {
        if(this.isEnabled)
        {
            Object[] key = {firstKeyElement, secondKeyElement, thirdKeyElement, fourthKeyElement};
            try
            {
                return getUnit((AbstractCacheUnit)new AnonymousCacheUnit(this, key));
            }
            catch(Exception e)
            {
                if(RedeployUtilities.isShutdownInProgress())
                {
                    Logs.debug(LOGGER, () -> "Error during data loading", e);
                }
                else
                {
                    LOGGER.error("Error during data loading", e);
                }
                throw e;
            }
        }
        return null;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public void enableStats(boolean enabled)
    {
        setStatsEnabled(enabled);
    }


    public int getSize()
    {
        return getMaxReachedSize();
    }


    public void destroy()
    {
        clear();
        clearStats();
        getController().removeLifecycleCallback(this);
    }


    public void onAfterAdd(CacheKey key, Object value, CacheRegion region)
    {
    }


    public void onAfterRemove(CacheKey key, Object value, CacheRegion region)
    {
        if(value instanceof AbstractCacheUnit)
        {
            AbstractCacheUnit acu = (AbstractCacheUnit)value;
            acu.removedFromCache();
        }
    }


    public void onAfterEviction(CacheKey key, Object value, CacheRegion region)
    {
        onAfterRemove(key, value, region);
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + ((this.controller == null) ? 0 : this.controller.hashCode());
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
        RegionCacheAdapter other = (RegionCacheAdapter)obj;
        if(this.controller == null)
        {
            if(other.controller != null)
            {
                return false;
            }
        }
        else if(!this.controller.equals(other.controller))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "RegionCacheAdapter(" + hashCode() + ") - <<" + this.tenantId + ">>";
    }


    public void onMissLoad(CacheKey key, Object value, CacheRegion lruCacheRegion)
    {
    }
}
