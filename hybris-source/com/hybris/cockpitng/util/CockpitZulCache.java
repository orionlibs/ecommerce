/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.util.Resettable;
import java.util.Set;
import org.zkoss.zk.ui.metainfo.PageDefinition;

/**
 * Cache for {@link PageDefinition}s. Used by {@link CockpitUIFactory} to cache e.g. widget view definitions.
 */
public interface CockpitZulCache extends Resettable
{
    /**
     * Returns the {@link PageDefinition} for a given path.
     *
     * @param path An unique identifier for the page.
     * @return A {@link PageDefinition}, if present in the cache, or null otherwise.
     */
    PageDefinition getPageDefinition(String path);


    /**
     * Adds a {@link PageDefinition} to the cache.
     *
     * @param path The path to the zul file.
     * @param def The {@link PageDefinition} to add to the cache.
     * @return The {@link PageDefinition} that was added (for convenience).
     */
    PageDefinition addPageDefinitionToCache(String path, PageDefinition def);


    /**
     * Returns all paths currently stored in the cache.
     */
    Set<String> getDefinitionPaths();


    /**
     * Clears all entries in the cache.
     */
    @Override
    void reset();
}
