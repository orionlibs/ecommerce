/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.CockpitComponentInfo;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class WebResourceCockpitComponentLoader extends ResourceCockpitComponentLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(WebResourceCockpitComponentLoader.class);


    @Override
    public Set<CockpitComponentInfo> load()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Loading component infos");
        }
        return super.load();
    }


    @Override
    protected Set<String> getResources(final Collection<URL> urls, final String pkg)
    {
        final Reflections reflections = new Reflections(new ConfigurationBuilder()
                        .filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(pkg))).setUrls(urls)
                        .setScanners(new ResourcesScanner()));
        return reflections.getResources(Pattern.compile(".*" + DEFINITION_XML));
    }
}
