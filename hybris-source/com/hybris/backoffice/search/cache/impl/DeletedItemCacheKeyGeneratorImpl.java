package com.hybris.backoffice.search.cache.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.key.CacheKey;

public class DeletedItemCacheKeyGeneratorImpl
{
    private static final String NO_ACTIVE_TENANT = "NO_ACTIVE_TENANT";


    public String computeKey(Object object)
    {
        String cacheKey = "";
        if(object instanceof ItemModel)
        {
            ItemModel itemModel = (ItemModel)object;
            cacheKey = itemModel.getItemtype() + "_" + itemModel.getItemtype();
        }
        return cacheKey;
    }


    public CacheKey createCacheKey(String typeCode, String cacheKey)
    {
        return (CacheKey)new DefaultDeletedItemCacheKey(typeCode, cacheKey, getTenantId());
    }


    private String getTenantId()
    {
        if(Registry.hasCurrentTenant())
        {
            return Registry.getCurrentTenant().getTenantID();
        }
        return "NO_ACTIVE_TENANT";
    }
}
