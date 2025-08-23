/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.util.CockpitZulCache;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.zkoss.zk.ui.metainfo.PageDefinition;

/**
 * Default {@link CockpitZulCache} implementation, using {@link ConcurrentHashMap}.
 */
public class DefaultCockpitZulCache implements CockpitZulCache
{
    private final Map<String, PageDefinition> defCache = new ConcurrentHashMap<String, PageDefinition>();


    @Override
    public PageDefinition getPageDefinition(final String path)
    {
        return path == null ? null : defCache.get(path);
    }


    @Override
    public PageDefinition addPageDefinitionToCache(final String path, final PageDefinition def)
    {
        if(path != null && def != null)
        {
            defCache.put(path, def);
        }
        return def;
    }


    @Override
    public void reset()
    {
        defCache.clear();
    }


    @Override
    public Set<String> getDefinitionPaths()
    {
        return defCache.keySet();
    }
}
