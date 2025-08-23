/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.engine.impl.ComponentWidgetAdapter;
import com.hybris.cockpitng.events.SocketEvent;

/**
 * Adapter aware marker that supports 'non-widget' components with useful framework features.
 */
public interface ComponentWidgetAdapterAware
{
    /**
     * Widget adapter aware components requires {@link ComponentWidgetAdapter} for incorporate a framework wide
     * features.
     *
     * @param componentWidgetAdapter incorporate a framework wide features
     * @param componentID associated componend ID
     */
    void initialize(ComponentWidgetAdapter componentWidgetAdapter, String componentID);


    /**
     * Registers a particular stub widget instance for given 'non-widget' aware component
     *
     * @param definition given component definition e.g. editor definition, action definition
     */
    void registerStubInstance(final AbstractCockpitComponentDefinition definition);


    /**
     * Unregisters a particular stub widget instance assocated with instance {@link ComponentWidgetAdapterAware}
     */
    void unregisterStubInstance();


    /**
     * Handles a socket input event sent to this {@link ComponentWidgetAdapterAware} instance from outside.
     *
     * @param event a socket input event
     */
    void handleSocketInputEvent(SocketEvent event);


    /**
     * Initializes default socket event listeners for the component.
     */
    void initializeDefaultEventListeners();


    /**
     * Gets associated component ID.
     */
    String getComponentID();
}
