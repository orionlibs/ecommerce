/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.impl.DefaultSessionWidgetInstanceRegistry;
import com.hybris.cockpitng.util.BackofficeSession;
import com.hybris.cockpitng.util.CockpitSessionService;
import java.io.Serializable;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

/**
 * Provides access to the current cockpit session lifecycle and attributes.
 */
public class DefaultCockpitSessionService implements CockpitSessionService, Serializable
{
    private String logoutURI = "j_spring_security_logout";


    @Override
    public void setWidgetInstanceMap(final Map<String, WidgetInstance> ret)
    {
        final Session session = Sessions.getCurrent();
        if(session instanceof BackofficeSession)
        {
            ((BackofficeSession)session).setWidgetInstanceMap(ret);
        }
        else
        {
            setAttribute(DefaultSessionWidgetInstanceRegistry.CNG_WIDGET_SESSION_INSTANCE_MAP, ret);
        }
    }


    @Override
    public Map<String, WidgetInstance> getWidgetInstanceMap()
    {
        final Session session = Sessions.getCurrent();
        if(session instanceof BackofficeSession)
        {
            return ((BackofficeSession)session).getWidgetInstanceMap();
        }
        return (Map<String, WidgetInstance>)getAttribute(DefaultSessionWidgetInstanceRegistry.CNG_WIDGET_SESSION_INSTANCE_MAP);
    }


    @Override
    public Object getAttribute(final String string)
    {
        final Session session = Sessions.getCurrent();
        if(session != null)
        {
            return Sessions.getCurrent().getAttribute(string);
        }
        return null;
    }


    @Override
    public void removeAttribute(final String string)
    {
        final Session session = Sessions.getCurrent();
        if(session != null)
        {
            Sessions.getCurrent().removeAttribute(string);
        }
    }


    @Override
    public void setAttribute(final String string, final Object value)
    {
        final Session session = Sessions.getCurrent();
        if(session != null)
        {
            Sessions.getCurrent().setAttribute(string, value);
        }
    }


    @Override
    public void logout()
    {
        Executions.getCurrent().sendRedirect(logoutURI);
    }


    public void setLogoutURI(final String logoutURI)
    {
        this.logoutURI = logoutURI;
    }
}
