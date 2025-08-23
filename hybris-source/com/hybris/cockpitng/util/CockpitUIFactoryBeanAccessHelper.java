/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.WebApp;

/**
 * Used by {@link CockpitUIFactory} to access needed beans per {@link WebApp}.
 * Only for internal use.
 */
public class CockpitUIFactoryBeanAccessHelper
{
    private CockpitResourceLoader cockpitResourceLoader;
    private CockpitProperties cockpitProperties;
    private CockpitZulCache cockpitZulCache;
    private WidgetLibUtils widgetLibUtils;


    public CockpitResourceLoader getCockpitResourceLoader()
    {
        return cockpitResourceLoader;
    }


    public void setCockpitResourceLoader(final CockpitResourceLoader cockpitResourceLoader)
    {
        this.cockpitResourceLoader = cockpitResourceLoader;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitZulCache getCockpitZulCache()
    {
        return cockpitZulCache;
    }


    @Required
    public void setCockpitZulCache(final CockpitZulCache cockpitZulCache)
    {
        this.cockpitZulCache = cockpitZulCache;
    }


    public WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }
}
