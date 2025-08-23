/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.http.SerializableSession;
import org.zkoss.zk.ui.sys.DesktopCache;

public class BackofficeSession extends SerializableSession
{
    private transient DesktopCache desktopCache;
    private transient Map<String, WidgetInstance> widgetInstanceMap;


    public BackofficeSession(final WebApp wapp, final HttpSession hsess, final Object request)
    {
        super(wapp, hsess, request);
    }


    public BackofficeSession(final WebApp wapp, final Object hsess, final Object request)
    {
        super(wapp, hsess, request);
    }


    @Override
    public DesktopCache getDesktopCache()
    {
        return desktopCache;
    }


    @Override
    public void setDesktopCache(final DesktopCache desktopCache)
    {
        this.desktopCache = desktopCache;
    }


    public Map<String, WidgetInstance> getWidgetInstanceMap()
    {
        return widgetInstanceMap;
    }


    public void setWidgetInstanceMap(final Map<String, WidgetInstance> widgetInstanceMap)
    {
        this.widgetInstanceMap = widgetInstanceMap;
    }
}
