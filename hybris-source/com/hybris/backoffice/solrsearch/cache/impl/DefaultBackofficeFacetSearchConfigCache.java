package com.hybris.backoffice.solrsearch.cache.impl;

import com.hybris.backoffice.solrsearch.cache.BackofficeFacetSearchConfigCache;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
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
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class DefaultBackofficeFacetSearchConfigCache implements BackofficeFacetSearchConfigCache, InvalidationListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeFacetSearchConfigCache.class);
    protected final Map<String, PK> cache = new HashMap<>();
    protected final ReadWriteLock cacheLock = new ReentrantReadWriteLock();
    protected Set<String> invalidatingTypecodes = new HashSet<>();
    protected ModelService modelService;


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


    public BackofficeIndexedTypeToSolrFacetSearchConfigModel getSearchConfigForTypeCode(String typeCode)
    {
        Lock readLock = this.cacheLock.readLock();
        readLock.lock();
        BackofficeIndexedTypeToSolrFacetSearchConfigModel facetSearchConfig = null;
        try
        {
            PK facetSearchConfigPk = this.cache.get(typeCode);
            if(facetSearchConfigPk != null)
            {
                facetSearchConfig = (BackofficeIndexedTypeToSolrFacetSearchConfigModel)this.modelService.get(facetSearchConfigPk);
            }
        }
        catch(Exception ex)
        {
            LOG.error("Could not load config for typecode {}.", typeCode, ex);
        }
        finally
        {
            readLock.unlock();
        }
        return facetSearchConfig;
    }


    public void putSearchConfigForTypeCode(String typeCode, BackofficeIndexedTypeToSolrFacetSearchConfigModel facetSearchConfig)
    {
        Lock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try
        {
            PK facetSearchConfigPk = (facetSearchConfig != null) ? facetSearchConfig.getPk() : null;
            this.cache.put(typeCode, facetSearchConfigPk);
        }
        finally
        {
            writeLock.unlock();
        }
    }


    public void invalidateCache()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Invalidating cache");
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


    public void setInvalidatingTypecodes(Set<String> invalidatingTypecodes)
    {
        this.invalidatingTypecodes = invalidatingTypecodes;
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSource)
    {
        if(target instanceof Cache && isOperationInvalidating(invalidationType) && isTypeInvalidating(key))
        {
            invalidateCache();
        }
    }


    protected boolean isOperationInvalidating(int invalidationType)
    {
        return (4 == invalidationType || 2 == invalidationType || 1 == invalidationType);
    }


    protected boolean isTypeInvalidating(Object[] key)
    {
        return this.invalidatingTypecodes.contains(key[2]);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
