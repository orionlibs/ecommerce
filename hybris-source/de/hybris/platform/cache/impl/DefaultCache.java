package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.collections.CacheMap;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

public final class DefaultCache extends StaticCache implements Cache
{
    private static final Logger LOG = Logger.getLogger(Cache.class.getName());
    private final boolean forceExclusiveComputation;
    protected final String tenantId;
    private final Tenant tenant;
    private boolean isEnabled;
    private boolean statsEnabledVariable;
    private ObjectName objectName;
    private final CacheBase cacheBase;
    private final Map stats = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock[] sharedLockCache;


    private DefaultCache()
    {
        this.tenant = null;
        this.tenantId = null;
        this.cacheBase = null;
        this.forceExclusiveComputation = false;
        this.sharedLockCache = createSharedLocks(8);
    }


    private ReentrantReadWriteLock[] createSharedLocks(int concurrencyLevel)
    {
        if(concurrencyLevel < 0 || concurrencyLevel > 12)
        {
            throw new IllegalArgumentException("invalid cache concurrency level: " + concurrencyLevel);
        }
        int size = 1 << concurrencyLevel;
        ReentrantReadWriteLock[] result = new ReentrantReadWriteLock[size];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = new ReentrantReadWriteLock();
        }
        return result;
    }


    DefaultCache(Tenant tenant, Tenant masterTenant)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("tenant", tenant);
        this.tenant = tenant;
        this.tenantId = tenant.getTenantID();
        this.isEnabled = true;
        this.statsEnabledVariable = false;
        this.forceExclusiveComputation = tenant.getConfig().getBoolean("cache.unit.exclusive.computation", false);
        Cache cache = masterTenant.getCache();
        if(cache instanceof DefaultCache)
        {
            this.cacheBase = ((DefaultCache)cache).cacheBase;
        }
        else
        {
            this.cacheBase = new CacheBase(this.tenant, cache.getMaxAllowedSize());
        }
        this.sharedLockCache = createSharedLocks(tenant.getConfig().getInt("cache.concurrency.level", 8));
    }


    public DefaultCache(Tenant tenant, int maxCacheSize)
    {
        this(tenant, maxCacheSize, tenant.getConfig().getBoolean("cache.unit.exclusive.computation", false));
    }


    public DefaultCache(Tenant tenant, int maxCacheSize, boolean forceExclComp)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("tenant", tenant);
        this.tenant = tenant;
        this.tenantId = tenant.getTenantID();
        this.isEnabled = true;
        this.statsEnabledVariable = false;
        this.forceExclusiveComputation = forceExclComp;
        this.cacheBase = new CacheBase(this.tenant, maxCacheSize);
        this.sharedLockCache = createSharedLocks(tenant.getConfig().getInt("cache.concurrency.level", 8));
    }


    public CacheMap internalCreateMapInstance(int max)
    {
        return this.cacheBase.internalCreateMapInstance(this.tenant, max);
    }


    public static CacheMap internalCreateMapInstanceStatic(Tenant tenant, int max)
    {
        return (new CacheBase(tenant, max)).internalCreateMapInstance(tenant, max);
    }


    public void destroy()
    {
        if(this.objectName != null)
        {
            try
            {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                mbs.unregisterMBean(this.objectName);
            }
            catch(InstanceNotFoundException instanceNotFoundException)
            {
            }
            catch(MBeanRegistrationException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public String toString()
    {
        String id = "-no tenant-";
        if(getTenant() != null)
        {
            id = getTenant().getTenantID();
        }
        return "Cache<<" + id + ">>(max:" + getMaxReachedSize() + ")";
    }


    public synchronized void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
        if(!enabled)
        {
            clear();
        }
    }


    public int getSize()
    {
        return this.cacheBase.getSize();
    }


    public int getMaxAllowedSize()
    {
        return this.cacheBase.getMaxAllowedSize();
    }


    public int getMaxReachedSize()
    {
        return this.cacheBase.getMaxReachedSize();
    }


    public void clear()
    {
        synchronized(this.cacheBase)
        {
            this.cacheBase.clear();
            clearRequestCache();
            clearStaticCache();
            clearStats();
            try
            {
                getTenant().getPersistencePool().clearCache();
            }
            catch(Exception exception)
            {
            }
        }
    }


    public static boolean isMultiPathKey(Object[] key)
    {
        return key[0].getClass().isArray();
    }


    public static List<Object[]> toSingleKey(Object[] key)
    {
        if(isMultiPathKey(key))
        {
            Object[][] keyArray = new Object[key.length][];
            for(int i = 0; i < key.length; i++)
            {
                keyArray[i] = (Object[])key[i];
            }
            return Arrays.asList(keyArray);
        }
        return Collections.singletonList(key);
    }


    public void removeUnit(AbstractCacheUnit unit)
    {
        this.cacheBase.removeUnit(unit);
    }


    public AbstractCacheUnit getUnit(AbstractCacheUnit unit)
    {
        return this.isEnabled ? this.cacheBase.getUnit(unit) : null;
    }


    private ReadWriteLock getLock(AbstractCacheUnit unit)
    {
        int hash = unit.hashCode();
        hash ^= hash >>> 12 ^ hash >>> 20;
        hash ^= hash >>> 4 ^ hash >>> 7;
        int index = hash & this.sharedLockCache.length - 1;
        return this.sharedLockCache[index];
    }


    public AbstractCacheUnit getOrAddUnit(AbstractCacheUnit unit)
    {
        return getOrAddUnitInternal(unit, getLock(unit));
    }


    public AbstractCacheUnit getOrAddUnitInternal(AbstractCacheUnit unit, ReadWriteLock rwLock)
    {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try
        {
            AbstractCacheUnit existingUnit = readUnit(unit);
            if(existingUnit != null)
            {
                return existingUnit;
            }
        }
        finally
        {
            readLock.unlock();
        }
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try
        {
            AbstractCacheUnit currentUnit = readUnit(unit);
            if(currentUnit != null)
            {
                return currentUnit;
            }
            if(this.isEnabled)
            {
                if(this.statsEnabledVariable)
                {
                    addToStatistics(unit.getKeyAsArray(), true);
                }
                this.cacheBase.addUnit(unit);
                return unit;
            }
            return unit;
        }
        finally
        {
            writeLock.unlock();
        }
    }


    private AbstractCacheUnit readUnit(AbstractCacheUnit unit)
    {
        AbstractCacheUnit existingUnit = getUnit(unit);
        if(existingUnit != null)
        {
            if(this.statsEnabledVariable)
            {
                addToStatistics(existingUnit.getKeyAsArray(), !existingUnit.isValueKnown());
            }
        }
        return existingUnit;
    }


    public void invalidate(Object[] key, int invalidationType)
    {
        this.cacheBase.invalidate(this, key, invalidationType);
    }


    public AbstractCacheUnit getAbstractCacheUnit(String firstKeyElement, String secondKeyElement, String thirdKeyElement, PK fourthKeyElement)
    {
        if(this.isEnabled)
        {
            return this.cacheBase.getUnit(this, firstKeyElement, secondKeyElement, thirdKeyElement, fourthKeyElement);
        }
        return null;
    }


    public long getAddCount()
    {
        return this.cacheBase.getAddCount();
    }


    public long getRemoveCount()
    {
        return this.cacheBase.getRemoveCount();
    }


    public long getGetCount()
    {
        return this.cacheBase.getGetCount();
    }


    public long getMissCount()
    {
        return this.cacheBase.getMissCount();
    }


    public boolean statsEnabled()
    {
        return this.statsEnabledVariable;
    }


    public void enableStats(boolean enabled)
    {
        if(this.statsEnabledVariable != enabled)
        {
            this.statsEnabledVariable = enabled;
            if(this.statsEnabledVariable)
            {
                clearStats();
            }
        }
    }


    public void clearStats()
    {
        synchronized(this.stats)
        {
            this.stats.clear();
        }
    }


    public boolean isStatsEmpty()
    {
        return this.stats.isEmpty();
    }


    public boolean isForceExclusiveComputation()
    {
        return this.forceExclusiveComputation;
    }


    void addToStatistics(Object[] key, boolean miss)
    {
        String kstr = keyToString(key);
        synchronized(this.stats)
        {
            DefaultCacheStatisticsEntry defaultCacheStatisticEntry = (DefaultCacheStatisticsEntry)this.stats.get(kstr);
            if(defaultCacheStatisticEntry == null)
            {
                this.stats.put(kstr, defaultCacheStatisticEntry = new DefaultCacheStatisticsEntry(key));
            }
            defaultCacheStatisticEntry.update(miss);
        }
    }


    public Set getStatistics(int upperBound, int lowerBound)
    {
        Set<DefaultCacheStatisticsEntry> ret = new HashSet();
        synchronized(this.stats)
        {
            for(Iterator<Map.Entry> it = this.stats.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                DefaultCacheStatisticsEntry cse = (DefaultCacheStatisticsEntry)entry.getValue();
                int factor = cse.getFactor();
                if(factor <= upperBound && factor >= lowerBound)
                {
                    ret.add(cse);
                }
            }
        }
        return ret;
    }


    public static String keyToString(Object[] key)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(isMultiPathKey(key))
        {
            stringBuilder.append("*[");
            for(int i = 0; i < key.length; i++)
            {
                Object[] singleKey = (Object[])key[i];
                for(int j = 0; j < singleKey.length; j++)
                {
                    stringBuilder.append(singleKey[j]);
                    if(j + 1 < singleKey.length)
                    {
                        stringBuilder.append(".");
                    }
                }
                if(i + 1 < key.length)
                {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]*");
        }
        else
        {
            for(int i = 0; i < key.length; i++)
            {
                stringBuilder.append(key[i]);
                if(i + 1 < key.length)
                {
                    stringBuilder.append(".");
                }
            }
        }
        return stringBuilder.toString();
    }


    public void setStatsEnabled(boolean enabled)
    {
        enableStats(enabled);
    }
}
