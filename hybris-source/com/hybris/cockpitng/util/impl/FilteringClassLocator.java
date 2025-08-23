/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Library;
import org.zkoss.util.resource.ClassLocator;

/**
 * Extends {@link ClassLocator} by filtering out resources that may interfere.
 * Resources are filtered out if their URL matches regexp configured as {@link Library} property
 * <code>cockpitng.zk.resourcelocator.urlfilter</code>.
 * See {@link CockpitHttpSessionListener} for more information.
 */
public class FilteringClassLocator extends ClassLocator
{
    private static final Logger LOG = LoggerFactory.getLogger(FilteringClassLocator.class);


    @Override
    public Enumeration<URL> getResources(final String name) throws IOException
    {
        final Enumeration<URL> resources = super.getResources(name);
        final String filter = Library.getProperty(CockpitHttpSessionListener.COCKPITNG_ZK_RESOURCELOCATOR_URLFILTER);
        if(StringUtils.isBlank(filter))
        {
            return resources;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Locate resources for " + name + ", filtering out by regexp '" + filter + "'");
        }
        final List<URL> tmpList = new ArrayList<URL>();
        while(resources.hasMoreElements())
        {
            final URL url = resources.nextElement();
            if(url == null || url.toString().matches(filter))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Filtered out '" + url + "'.");
                }
            }
            else
            {
                tmpList.add(url);
            }
        }
        return Collections.enumeration(tmpList);
    }
}
