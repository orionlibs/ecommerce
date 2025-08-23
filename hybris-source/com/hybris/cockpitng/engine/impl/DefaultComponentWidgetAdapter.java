/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultComponentWidgetAdapter implements ComponentWidgetAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultComponentWidgetAdapter.class);
    private CockpitWidgetEngine cockpitWidgetEngine;
    private WidgetUtils widgetUtils;
    private WidgetInstanceFacade widgetInstanceFacade;
    private Map<String, EventListener<SocketEvent>> listeners = new LinkedHashMap<String, EventListener<SocketEvent>>();
    private WidgetInstance stubWidgetInstance;


    @Override
    public void unregisterStubInstance()
    {
        if(stubWidgetInstance != null && stubWidgetInstance.getCreator() instanceof ComponentWidgetAdapterAware)
        {
            widgetInstanceFacade.removeWidgetInstance(stubWidgetInstance);
        }
    }


    @Override
    public void registerStubInstance(final String widgetId, final ComponentWidgetAdapterAware parent)
    {
        final String widgetStubId = WIDGET_STUB_PREFIX + widgetId;
        final Widgetslot widgetslot = widgetUtils.getRegisteredWidgetslot(widgetStubId);
        if(widgetslot != null)
        {
            final Widget widget = widgetslot.getWidgetInstance().getWidget();
            if(widgetslot.getParentWidgetInstance() != null)
            {
                stubWidgetInstance = widgetInstanceFacade.createWidgetInstance(widget, widgetslot.getParentWidgetInstance(), parent);
                final Widgetchildren widgetchildren = WidgetTreeUIUtils.getParentWidgetchildren(widgetslot);
                if(widgetchildren != null)
                {
                    widgetchildren.updateChildren();
                }
            }
        }
    }


    @Override
    public void sendOutput(final String socketId, final Object data, final ComponentWidgetAdapterAware parent)
    {
        if(this.stubWidgetInstance == null)
        {
            registerStubInstance(parent.getComponentID(), parent);
        }
        final Widgetslot stubWidgetSlot = widgetUtils.getRegisteredWidgetslot(this.stubWidgetInstance);
        if(stubWidgetSlot != null)
        {
            cockpitWidgetEngine.sendOutput(stubWidgetSlot, socketId, data, true);
        }
        else
        {
            LOG.error("Stub widget slot for socketId: {} could not be found", socketId);
        }
    }


    @Override
    public void handleSocketInputEvent(final SocketEvent socketInputEvent)
    {
        if(listeners.containsKey(socketInputEvent.getName()))
        {
            final EventListener<SocketEvent> callbackListener = listeners.get(socketInputEvent.getName());
            if(callbackListener != null)
            {
                try
                {
                    callbackListener.onEvent(socketInputEvent);
                }
                catch(final Exception e)
                {
                    LOG.error("Cannot proccess socket input event ( " + socketInputEvent.getName()
                                    + " ) for component widget adapter!", e);
                }
            }
        }
    }


    @Override
    public void addSocketInputEventListener(final String socketId, final EventListener<SocketEvent> eventListener)
    {
        if(listeners == null)
        {
            listeners = new HashMap<String, EventListener<SocketEvent>>();
        }
        listeners.put(socketId, eventListener);
    }


    public void setCockpitWidgetEngine(final CockpitWidgetEngine cockpitWidgetEngine)
    {
        this.cockpitWidgetEngine = cockpitWidgetEngine;
    }


    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    public void setWidgetInstanceFacade(final WidgetInstanceFacade widgetInstanceFacade)
    {
        this.widgetInstanceFacade = widgetInstanceFacade;
    }
}
