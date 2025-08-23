/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.events.SocketEvent;
import org.zkoss.zk.ui.event.EventListener;

public abstract class AbstractComponentWidgetAdapterAware implements ComponentWidgetAdapterAware
{
    private ComponentWidgetAdapter componentWidgetAdapter;
    private String componentID;


    @Override
    public void initialize(final ComponentWidgetAdapter componentWidgetAdapter, final String componentID)
    {
        this.componentID = componentID;
        if(this.componentWidgetAdapter == null)
        {
            this.componentWidgetAdapter = componentWidgetAdapter;
        }
        initializeDefaultEventListeners();
    }


    @Override
    public void registerStubInstance(final AbstractCockpitComponentDefinition definition)
    {
        this.componentWidgetAdapter.registerStubInstance(definition.getCode(), this);
    }


    @Override
    public void unregisterStubInstance()
    {
        this.componentWidgetAdapter.unregisterStubInstance();
    }


    @Override
    public void handleSocketInputEvent(final SocketEvent event)
    {
        this.componentWidgetAdapter.handleSocketInputEvent(event);
    }


    public void sendOutput(final String socketId, final Object data)
    {
        this.componentWidgetAdapter.sendOutput(socketId, data, this);
    }


    public void addSocketInputEventListener(final String socketId, final EventListener<SocketEvent> eventListener)
    {
        this.componentWidgetAdapter.addSocketInputEventListener(socketId, eventListener);
    }


    @Override
    public String getComponentID()
    {
        return componentID;
    }


    public void setComponentID(final String componentID)
    {
        this.componentID = componentID;
    }


    @Override
    public void initializeDefaultEventListeners()
    {
        // NOP
    }
}
