/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.events.SocketEvent;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Represents an adapter that is responsible for communication with widgets. Allows you to send data to output sockets,
 * as well reacts on registered input sockets. Should be used in 'non-widget' aware components (e.g. actions, editors)
 * to provide a widget related functionality.
 *
 */
public interface ComponentWidgetAdapter
{
    String WIDGET_STUB_PREFIX = "STUB_";


    /**
     * Sends data to the output socked with the given ID.
     *
     * @param socketId output socket ID to sent the data to
     * @param data the data to be sent
     */
    void sendOutput(String socketId, Object data, ComponentWidgetAdapterAware parent);


    /**
     * Registers an eventListener for input socket with the given ID.
     *
     * @param socketId output socket ID to sent the data to
     * @param eventListener event listener that is invoked when socket input event comes
     */
    void addSocketInputEventListener(String socketId, EventListener<SocketEvent> eventListener);


    /**
     * Registers a particular stub widget instance for given 'non-widget' aware component
     *
     * @param componentId given component id e.g. editor id, action id
     * @param parent the parent for registered stub widget instance
     */
    void registerStubInstance(String componentId, ComponentWidgetAdapterAware parent);


    /**
     * Unregisters a particular stub widget instance associated with current instance of {@link ComponentWidgetAdapter}
     */
    void unregisterStubInstance();


    /**
     * Handles a socket input event sent to this {@link ComponentWidgetAdapterAware} instance from outside.
     *
     * @param socketInputEvent a socket input event
     */
    void handleSocketInputEvent(SocketEvent socketInputEvent);
}
