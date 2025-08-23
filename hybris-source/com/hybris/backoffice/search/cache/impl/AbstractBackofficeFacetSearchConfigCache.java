package com.hybris.backoffice.search.cache.impl;

import com.hybris.backoffice.search.cache.BackofficeFacetSearchConfigCache;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBackofficeFacetSearchConfigCache<T> implements BackofficeFacetSearchConfigCache, InvalidationListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBackofficeFacetSearchConfigCache.class);
    private static final int NUM_INDEX_TYPECODE = 2;
    private Set<String> invalidatingTypecodes = new HashSet<>();
    private ModelService modelService;
    protected final Map<String, PK> cache = new HashMap<>();
    protected final ReadWriteLock cacheLock = new ReentrantReadWriteLock();


    public void initialize()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this);
    }


    public boolean containsSearchConfigForTypeCode(String typeCode)
    {
        Lock readLock = this.cacheLock.readLock();
        readLock.lock();
        try
        {
            return this.cache.containsKey(typeCode);
        }
        finally
        {
            readLock.unlock();
        }
    }


    public T getSearchConfigForTypeCode(String typeCode)
    {
        Lock readLock = this.cacheLock.readLock();
        readLock.lock();
        T facetSearchConfig = null;
        try
        {
            PK facetSearchConfigPk = this.cache.get(typeCode);
            if(null != facetSearchConfigPk)
            {
                facetSearchConfig = (T)this.modelService.get(facetSearchConfigPk);
            }
        }
        catch(IllegalArgumentException ex)
        {
            LOGGER.error("Could not load config for typecode {}.", typeCode, ex);
        }
        finally
        {
            readLock.unlock();
        }
        return facetSearchConfig;
    }


    public void invalidateCache()
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Invalidating cache");
        }
        Lock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try
        {
            this.cache.clear();
        }
        finally
        {
            writeLock.unlock();
        }
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSource)
    {
        if(target instanceof Cache && isOperationInvalidating(invalidationType) && isTypeInvalidating(key))
        {
            invalidateCache();
        }
    }


    public void setInvalidatingTypecodes(Set<String> invalidatingTypecodes)
    {
        this.invalidatingTypecodes = invalidatingTypecodes;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    private boolean isOperationInvalidating(int invalidationType)
    {
        return (4 == invalidationType || 2 == invalidationType || 1 == invalidationType);
    }


    private boolean isTypeInvalidating(Object[] key)
    {
        return this.invalidatingTypecodes.contains(key[2]);
    }
}
