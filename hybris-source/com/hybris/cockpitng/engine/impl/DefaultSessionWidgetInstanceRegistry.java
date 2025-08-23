/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.util.CockpitSessionService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

/**
 * Handles widget instances for the current session.
 */
public class DefaultSessionWidgetInstanceRegistry implements SessionWidgetInstanceRegistry, Resettable
{
    public static final String CNG_WIDGET_SESSION_INSTANCE_MAP = "cng_widget_instance_map";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSessionWidgetInstanceRegistry.class);
    private static final Boolean ACTIVATED_BY_DEFAULT = Boolean.FALSE;
    private CockpitSessionService sessionService;
    private CockpitProperties cockpitProperties;
    private Boolean useDesktopScope;


    @Override
    public WidgetInstance getRegisteredWidgetInstance(final String slotId)
    {
        return slotId == null ? null : getWidgetInstanceMap().get(slotId);
    }


    @Override
    public void registerWidgetInstance(final WidgetInstance instance, final String slotId)
    {
        if(instance == null || slotId == null)
        {
            LOG.error("Could not register widget instance {} for slotId {}, both must not be null!", instance, slotId);
        }
        else
        {
            getWidgetInstanceMap().put(slotId, instance);
        }
    }


    @Override
    public void unregisterWidgetInstance(final String slotId)
    {
        if(slotId != null)
        {
            getWidgetInstanceMap().remove(slotId);
        }
    }


    @Override
    public void clear()
    {
        getWidgetInstanceMap().clear();
    }


    protected Map<String, WidgetInstance> getWidgetInstanceMap()
    {
        if(isUsingDesktopScope())
        {
            return getDesktopInstanceMapSessionFallback();
        }
        else
        {
            return getSessionInstanceMap();
        }
    }


    private boolean isUsingDesktopScope()
    {
        if(useDesktopScope == null)
        {
            if(cockpitProperties != null)
            {
                final String property = cockpitProperties.getProperty("cockpitng.widgetInstanceMap.enableDesktopScope");
                useDesktopScope = property == null ? ACTIVATED_BY_DEFAULT : Boolean.valueOf(property);
            }
            else
            {
                useDesktopScope = ACTIVATED_BY_DEFAULT;
            }
        }
        return Boolean.TRUE.equals(useDesktopScope);
    }


    protected Map<String, WidgetInstance> getSessionInstanceMap()
    {
        Map<String, WidgetInstance> ret = sessionService.getWidgetInstanceMap();
        if(ret == null)
        {
            ret = new ConcurrentHashMap<>();
            sessionService.setWidgetInstanceMap(ret);
        }
        return ret;
    }


    protected Map<String, WidgetInstance> getDesktopInstanceMapSessionFallback()
    {
        final Desktop desktop = getDesktop();
        if(desktop == null)
        {
            LOG.warn("Desktop scope not available for getting widget instances, using session scope instead.");
            useDesktopScope = Boolean.FALSE;
            return getSessionInstanceMap();
        }
        else
        {
            Map<String, WidgetInstance> ret = (Map<String, WidgetInstance>)desktop.getAttribute(CNG_WIDGET_SESSION_INSTANCE_MAP);
            if(ret == null)
            {
                ret = new ConcurrentHashMap<>();
                desktop.setAttribute(CNG_WIDGET_SESSION_INSTANCE_MAP, ret);
            }
            return ret;
        }
    }


    protected Desktop getDesktop()
    {
        final Execution execution = Executions.getCurrent();
        if(execution != null)
        {
            return execution.getDesktop();
        }
        return null;
    }


    @Override
    public void reset()
    {
        clear();
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
