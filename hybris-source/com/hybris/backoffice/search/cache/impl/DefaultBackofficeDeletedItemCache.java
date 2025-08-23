package com.hybris.backoffice.search.cache.impl;

import com.hybris.backoffice.search.cache.BackofficeDeletedItemCache;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Objects;

public class DefaultBackofficeDeletedItemCache implements BackofficeDeletedItemCache
{
    private static final String CACHED_TYPE_DELETED_ITEM = "__BACKOFFICE_DELETED_ITEM__";
    private CacheController defaultCacheController;
    private DeletedItemCacheKeyGeneratorImpl objectCacheKeyGenerator;


    public boolean storeDeletedItem(ItemModel itemModel)
    {
        CacheKey objectCacheKey = this.objectCacheKeyGenerator.createCacheKey("__BACKOFFICE_DELETED_ITEM__", this.objectCacheKeyGenerator
                        .computeKey(itemModel));
        String cacheValue = (String)this.defaultCacheController.getWithLoader(objectCacheKey, (CacheValueLoader)new DefaultDeletedItemCacheValueLoader());
        return (Objects.nonNull(cacheValue) && !cacheValue.isEmpty());
    }


    public boolean isExistingInCache(ItemModel itemModel)
    {
        CacheKey objectCacheKey = this.objectCacheKeyGenerator.createCacheKey("__BACKOFFICE_DELETED_ITEM__", this.objectCacheKeyGenerator
                        .computeKey(itemModel));
        String cacheValue = (String)this.defaultCacheController.get(objectCacheKey);
        return (Objects.nonNull(cacheValue) && !cacheValue.isEmpty());
    }


    public CacheController getDefaultCacheController()
    {
        return this.defaultCacheController;
    }


    public void setDefaultCacheController(CacheController defaultCacheController)
    {
        this.defaultCacheController = defaultCacheController;
    }


    public DeletedItemCacheKeyGeneratorImpl getObjectCacheKeyGenerator()
    {
        return this.objectCacheKeyGenerator;
    }


    public void setObjectCacheKeyGenerator(DeletedItemCacheKeyGeneratorImpl objectCacheKeyGenerator)
    {
        this.objectCacheKeyGenerator = objectCacheKeyGenerator;
    }
}
