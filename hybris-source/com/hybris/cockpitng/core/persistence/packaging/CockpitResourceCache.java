/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.util.Resettable;
import java.util.Collection;

/**
 * Cache for the WidgetResourceLoader
 */
public interface CockpitResourceCache extends Resettable
{
    /**
     * Gets the resource for the given key, if present in cache.
     *
     * @return The cached byte array, or null, if not in cache.
     */
    byte[] getResourceAsBytes(String uuid);


    /**
     * Adds a chunk of bytes to the cache.
     *
     * @param uuid The unique key for the resource, usually an uri.
     *
     * @param resource The resource to add as byte array.
     * @return The passed resource, for convenience reasons.
     */
    byte[] addResourceToCache(String uuid, byte[] resource);


    /**
     * Removes all elements from the cache.
     */
    @Override
    void reset();


    /**
     * Returns the size (in bytes) of all entries in the cache.
     */
    long getSize();


    /**
     * Returns the average size (in bytes) of all entries.
     */
    long getAverageEntrySize();


    /**
     * Returns the keys for the current entries.
     */
    Collection<String> getKeys();
}
