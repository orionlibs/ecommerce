/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cache;

import com.hybris.backoffice.cache.impl.ObjectCacheKey;

/**
 * Generates label related cache keys to be used for hybris cache regions
 */
public interface ObjectCacheKeyGenerator
{
    /**
     * Creates a cache key for the object label cache region.
     *
     * @param typeCode
     *           cache type code
     * @param objectKey
     *           object key to the cache key
     * @return the created cache key
     */
    ObjectCacheKey createCacheKey(String typeCode, Object objectKey);


    /**
     * Compute key for specific object.
     *
     * @param object
     *
     * @return the computed object key
     */
    Object computeKey(Object object);
}
