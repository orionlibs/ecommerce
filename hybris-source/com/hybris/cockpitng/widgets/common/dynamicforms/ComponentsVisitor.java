/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.zkoss.zk.ui.Component;

/**
 * An interface for dynamic forms visitor which will apply configured behavior on all registered components and theirs
 * children.
 */
public interface ComponentsVisitor
{
    String COMPONENT_CTX = "componentCtx";
    String MODEL_ROOT = "*";
    ComponentsVisitor NULL = component -> {
    };


    /**
     * Registers a component and it's child components recursively using {@link Component#getChildren()}
     *
     * @param component component to register
     */
    void register(final Component component);


    /**
     * Unregisters a component and it's child components recursively using {@link Component#getChildren()}
     *
     * @param component component to unregister
     */
    default void unRegister(final Component component)
    {
        // NOOP
    }


    /**
     * Removes all registered components and observers.
     */
    default void cleanUp()
    {
    }


    /**
     * Initializes a visitor. The method should be called before the first usage of the visitor (calling method
     * {@link #register(Component)}).
     */
    default void initialize(final String typeCode, final WidgetInstanceManager wim, final DynamicForms dynamicForms)
    {
        // NOOP
    }
}
