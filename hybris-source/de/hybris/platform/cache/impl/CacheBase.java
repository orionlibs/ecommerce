package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.collections.CacheMap;
import de.hybris.platform.util.collections.YMap;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;

class CacheBase
{
    private static final Logger LOG = Logger.getLogger(CacheBase.class.getName());
    private final Map unitMapTree;
    private final CacheMap<AbstractCacheUnit, AbstractCacheUnit> cacheMap;
    private final boolean isYMap;
    private final ReadWriteLock nonYMapRWLock;
    private final Object treeLock;
    private long gets = 0L;
    private long misses = 0L;
    private long removes = 0L;
    private long adds = 0L;


    CacheBase(Tenant tenant, int max)
    {
        this.unitMapTree = new HashMap<>();
        this.treeLock = this.unitMapTree;
        this.cacheMap = internalCreateMapInstance(tenant, max);
        this.isYMap = this.cacheMap instanceof YMap;
        this.nonYMapRWLock = this.isYMap ? null : new ReentrantReadWriteLock();
    }


    protected CacheMap internalCreateMapInstance(Tenant tenant, int max)
    {
        Class[] argumentsClass = {CacheBase.class, int.class};
        Object[] argumentValues = {this, Integer.valueOf(max)};
        String mapName = null;
        try
        {
            mapName = tenant.getConfig().getParameter("cache.main.map");
            Class<?> cacheMapDefinition = Class.forName(mapName);
            Constructor<?> constructor = cacheMapDefinition.getConstructor(argumentsClass);
            CacheMap cacheMap = (CacheMap)constructor.newInstance(argumentValues);
            return cacheMap;
        }
        catch(Exception e)
        {
            throw new IllegalStateException("can't initialize cache: " + mapName, e);
        }
    }


    int getMaxReachedSize()
    {
        return this.cacheMap.getMaxReachedSize();
    }


    int getMaxAllowedSize()
    {
        return this.cacheMap.maxSize();
    }


    long getAddCount()
    {
        return this.adds;
    }


    long getRemoveCount()
    {
        return this.removes;
    }


    long getGetCount()
    {
        return this.gets;
    }


    long getMissCount()
    {
        return this.misses;
    }


    protected void putIntoCacheMap(AbstractCacheUnit unit)
    {
        if(this.isYMap)
        {
            this.cacheMap.put(unit, unit);
        }
        else
        {
            this.nonYMapRWLock.readLock().lock();
            try
            {
                this.cacheMap.put(unit, unit);
            }
            finally
            {
                this.nonYMapRWLock.readLock().unlock();
            }
        }
    }


    protected void removeFromCacheMap(AbstractCacheUnit unit)
    {
        if(this.isYMap)
        {
            this.cacheMap.remove(unit);
        }
        else
        {
            this.nonYMapRWLock.readLock().lock();
            try
            {
                this.cacheMap.remove(unit);
            }
            finally
            {
                this.nonYMapRWLock.readLock().unlock();
            }
        }
    }


    protected void clearCacheMap()
    {
        if(this.isYMap)
        {
            ((YMap)this.cacheMap).clear((YMap.ClearHandler)new Object(this));
        }
        else
        {
            this.nonYMapRWLock.writeLock().lock();
            try
            {
                for(Map.Entry<AbstractCacheUnit, AbstractCacheUnit> entry : (Iterable<Map.Entry<AbstractCacheUnit, AbstractCacheUnit>>)this.cacheMap.entrySet())
                {
                    ((AbstractCacheUnit)entry.getKey()).removedFromCache();
                }
                this.cacheMap.clear();
            }
            finally
            {
                this.nonYMapRWLock.writeLock().unlock();
            }
        }
    }


    synchronized void clear()
    {
        synchronized(this.treeLock)
        {
            this.unitMapTree.clear();
        }
        clearCacheMap();
        this.gets = 0L;
        this.removes = 0L;
        this.adds = 0L;
        this.misses = 0L;
    }


    int getSize()
    {
        return this.cacheMap.size();
    }


    public void removeUnit(AbstractCacheUnit unit)
    {
        removeUnitFromNestedMapOnly(unit);
        removeFromCacheMap(unit);
    }


    AbstractCacheUnit getUnit(Cache caller, String firstKeyElement, String secondKeyElement, String thirdKeyElement, PK fourthKeyElement)
    {
        Object[] key = {firstKeyElement, secondKeyElement, thirdKeyElement, fourthKeyElement};
        return getUnit((AbstractCacheUnit)new AnonymousCacheUnit(caller, key));
    }


    AbstractCacheUnit getUnit(AbstractCacheUnit unit)
    {
        if(unit.isCachingSupported())
        {
            this.gets++;
            AbstractCacheUnit ret = (AbstractCacheUnit)this.cacheMap.get(unit);
            if(ret == null || !ret.isValueKnown())
            {
                this.misses++;
            }
            return ret;
        }
        return null;
    }


    void addUnit(AbstractCacheUnit unit)
    {
        if(unit.isCachingSupported())
        {
            this.adds++;
            Object[] key = unit.getKeyAsArray();
            String tenantID = unit.getTenantID();
            unit.addedToCacheBeforeComputation();
            putIntoCacheMap(unit);
            synchronized(this.treeLock)
            {
                if(DefaultCache.isMultiPathKey(key))
                {
                    int numberOfKeys = key.length;
                    for(int i = 0; i < numberOfKeys; i++)
                    {
                        Object[] singleKey = (Object[])key[i];
                        getNodeForKey(tenantID, singleKey).put(singleKey[singleKey.length - 1], unit);
                    }
                }
                else
                {
                    getNodeForKey(tenantID, key).put(key[key.length - 1], unit);
                }
            }
        }
    }


    void invalidate(Cache caller, Object[] key, int invalidationType)
    {
        if(DefaultCache.isMultiPathKey(key))
        {
            throw new RuntimeException("cannot invalidate multi-path keys ");
        }
        invalidateRecursively(removeKeyFromPath(getFullPathForKey(caller.getTenant().getTenantID(), key), key));
        AnonymousCacheUnit anonymousCacheUnit = new AnonymousCacheUnit(caller, key);
        if(this.cacheMap.containsKey(anonymousCacheUnit))
        {
            removeFromCacheMap((AbstractCacheUnit)anonymousCacheUnit);
        }
        this.removes++;
    }


    protected void removeUnitFromNestedMapOnly(AbstractCacheUnit unit)
    {
        this.removes++;
        Object[] key = unit.getKeyAsArray();
        String tenantID = unit.getTenantID();
        if(DefaultCache.isMultiPathKey(key))
        {
            for(Object inner : key)
            {
                Object[] singleKey = (Object[])inner;
                removeKeyFromPath(getFullPathForKey(tenantID, singleKey), singleKey);
            }
        }
        else
        {
            removeKeyFromPath(getFullPathForKey(tenantID, key), key);
        }
        unit.removedFromCache();
    }


    private Object removeKeyFromPath(Map[] path, Object[] key)
    {
        if(path != null)
        {
            synchronized(this.treeLock)
            {
                if(path.length != key.length)
                {
                    if(LOG.isDebugEnabled())
                    {
                        String message = "path length doesnt match key length (path=" + Arrays.<Map>asList(path) + ", key=" + Arrays.<Object>asList(key) + ")";
                        LOG.debug(message);
                        throw new IllegalStateException(message);
                    }
                    throw new IllegalStateException("path length doesnt match key length (path length = " + path.length + ", key length = " + key.length + "). Switch logger to debug to see the contents.");
                }
                int lastPos = key.length - 1;
                Object ret = path[lastPos].get(key[lastPos]);
                for(int i = lastPos; i >= 0; i--)
                {
                    path[i].remove(key[i]);
                    if(!path[i].isEmpty())
                    {
                        break;
                    }
                }
                return ret;
            }
        }
        return null;
    }


    private Map getNodeForKey(String tenantID, Object[] key)
    {
        Map<Object, Object> map;
        Map<Object, Map<Object, Object>> current = getTenantRootMap(tenantID);
        for(Object k : key)
        {
            Map<Object, Object> newMap = (Map)current.get(k);
            if(newMap == null)
            {
                current.put(k, newMap = new HashMap<>());
            }
            map = newMap;
        }
        return map;
    }


    private Map[] getFullPathForKey(String tenantID, Object[] key)
    {
        synchronized(this.treeLock)
        {
            int depth = key.length;
            Map[] ret = new Map[key.length];
            Map current = getTenantRootMap(tenantID);
            ret[0] = current;
            for(int i = 1; i < depth && current != null; i++)
            {
                current = (Map)current.get(key[i - 1]);
                ret[i] = current;
            }
            return (current != null) ? ret : null;
        }
    }


    private Map getTenantRootMap(String tenantID)
    {
        Map<Object, Object> root = (Map)this.unitMapTree.get(tenantID);
        if(root == null)
        {
            this.unitMapTree.put(tenantID, root = new HashMap<>());
        }
        return root;
    }


    private void invalidateRecursively(Object obj)
    {
        if(obj != null)
        {
            Collection<AbstractCacheUnit> units;
            if(obj instanceof AbstractCacheUnit)
            {
                units = Collections.singleton((AbstractCacheUnit)obj);
            }
            else
            {
                units = new ArrayList<>(30);
                synchronized(this.treeLock)
                {
                    extractUnits((Map<?, ?>)obj, units);
                }
            }
            for(AbstractCacheUnit acu : units)
            {
                acu.executeInvalidation();
                removeUnit(acu);
            }
        }
    }


    private void extractUnits(Map<?, ?> pathElement, Collection<AbstractCacheUnit> toAddTo)
    {
        for(Map.Entry<?, ?> e : pathElement.entrySet())
        {
            Object value = e.getValue();
            if(value instanceof AbstractCacheUnit)
            {
                toAddTo.add((AbstractCacheUnit)value);
                continue;
            }
            extractUnits((Map<?, ?>)value, toAddTo);
        }
        pathElement.clear();
    }
}
