/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.util.CockpitProperties;
import javax.servlet.ServletContextEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.http.HttpSessionListener;

/**
 * Sets the zk library property <code>org.zkoss.zk.ui.sys.XMLResourcesLocator.class</code> to
 * {@link FilteringClassLocator}. This
 * is needed to avoid conflicts when having different versions of zk in a system.
 * For the purpose of {@link FilteringClassLocator}, it configures the zk library regexp filer, that may be
 * configured as cockpit property : <code>cockpitng.zk.resourcelocator.urlfilter</code>.
 */
public class CockpitHttpSessionListener extends HttpSessionListener
{
    public static final String COCKPITNG_ZK_RESOURCELOCATOR_URLFILTER = "cockpitng.zk.resourcelocator.urlfilter";
    private static final String COCKPIT_PROPERTIES_BEN_ID = "cockpitProperties";


    @Override
    public void contextInitialized(final ServletContextEvent event)
    {
        final WebApplicationContext context = WebApplicationContextUtils
                        .getRequiredWebApplicationContext(event.getServletContext());
        if(context != null)
        {
            if(context instanceof CockpitApplicationContext)
            {
                Thread.currentThread().setContextClassLoader(context.getClassLoader());
            }
            final CockpitProperties cprops = context.getBean(COCKPIT_PROPERTIES_BEN_ID, CockpitProperties.class);
            final String property = cprops.getProperty(COCKPITNG_ZK_RESOURCELOCATOR_URLFILTER);
            if(StringUtils.isNotBlank(property))
            {
                Library.setProperty(COCKPITNG_ZK_RESOURCELOCATOR_URLFILTER, property);
            }
        }
        Library.setProperty("org.zkoss.zk.ui.sys.XMLResourcesLocator.class", FilteringClassLocator.class.getName());
        super.contextInitialized(event);
    }
}
