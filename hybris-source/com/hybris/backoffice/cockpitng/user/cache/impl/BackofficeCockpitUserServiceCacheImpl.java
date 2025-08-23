/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.user.cache.impl;

import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;
import com.hybris.backoffice.cockpitng.user.cache.BackofficeCockpitUserServiceCache;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeCockpitUserServiceCacheImpl implements BackofficeCockpitUserServiceCache
{
    protected static final String CACHED_TYPE_USER = "__BACKOFFICE_USER__";
    private CacheController cacheController;
    private ObjectCacheKeyGenerator objectCacheKeyGenerator;


    @Override
    public boolean isAdmin(final String userId, final Function<String, Boolean> defaultValue)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(CACHED_TYPE_USER, getObjectCacheKeyGenerator().computeKey(userId)),
                        (CacheKey key) -> defaultValue.apply(userId));
    }


    protected CacheController getCacheController()
    {
        return cacheController;
    }


    @Required
    public void setCacheController(final CacheController cacheController)
    {
        this.cacheController = cacheController;
    }


    protected ObjectCacheKeyGenerator getObjectCacheKeyGenerator()
    {
        return objectCacheKeyGenerator;
    }


    @Required
    public void setObjectCacheKeyGenerator(final ObjectCacheKeyGenerator objectCacheKeyGenerator)
    {
        this.objectCacheKeyGenerator = objectCacheKeyGenerator;
    }
}
