package de.hybris.platform.util;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemCacheUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ItemModificationInvalidationListener implements InvalidationListener
{
    private static final Logger LOG = Logger.getLogger(ItemModificationInvalidationListener.class);
    private final Cache globalCache;
    private String showTCInvalidation = null;


    public ItemModificationInvalidationListener(Cache cache)
    {
        this.globalCache = cache;
        this.showTCInvalidation = cache.getTenant().getConfig().getString("cache.showinvalidationtrace.for.typecode", null);
        if(this.showTCInvalidation != null && StringUtils.isBlank(this.showTCInvalidation))
        {
            this.showTCInvalidation = null;
        }
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        PK pk = (PK)key[3];
        String typeCode = (String)key[2];
        Item cacheBoundItem = getCacheBoundItem(pk);
        boolean updateItemDirect = (cacheBoundItem != null && isRealCacheInvalidation(key, invalidationType, target, remoteSrc));
        logInvalidation(key, invalidationType, target, remoteSrc);
        target.invalidate(new Object[] {Cache.CACHEKEY_JALOITEMDATA, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED_STRING, pk}, invalidationType);
        target.invalidate(new Object[] {Cache.CACHEKEY_JALOTYPE, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED_STRING, typeCode}, invalidationType);
        if(updateItemDirect)
        {
            cacheBoundItem.invalidateLocalCaches();
        }
        if(invalidationType == 2)
        {
            target.invalidate(new Object[] {Cache.CACHEKEY_JALOITEMCACHE, pk}, invalidationType);
            if(updateItemDirect)
            {
                cacheBoundItem.setCacheBound(false);
            }
            target.invalidate(new Object[] {Cache.CACHEKEY_JALOITEMDATA, AbstractCacheUnit.INVALIDATIONTYPE_REMOVED_STRING, pk}, invalidationType);
            target.invalidate(new Object[] {Cache.CACHEKEY_JALOTYPE, AbstractCacheUnit.INVALIDATIONTYPE_REMOVED_STRING, typeCode}, invalidationType);
        }
    }


    private boolean isRealCacheInvalidation(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        return target instanceof Cache;
    }


    private Item getCacheBoundItem(PK pk)
    {
        Object object = new Object(this, this.globalCache, pk);
        JaloItemCacheUnit u2 = (JaloItemCacheUnit)this.globalCache.getUnit((AbstractCacheUnit)object);
        if(u2 != null)
        {
            try
            {
                Item i = (Item)u2.getWithoutCompute();
                return i;
            }
            catch(Exception exception)
            {
            }
        }
        return null;
    }


    private void logInvalidation(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        if(this.showTCInvalidation != null && (this.showTCInvalidation.equalsIgnoreCase("ALL") || key[2].equals(this.showTCInvalidation)))
        {
            LOG.error("-----------> Invalidation received for " + key[2] + ": Invtype=" + invalidationType + ", PK=" + key[3], new Exception());
        }
    }
}
