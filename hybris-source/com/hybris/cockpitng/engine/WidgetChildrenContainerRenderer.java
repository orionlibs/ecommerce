/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.List;
import java.util.Map;

/**
 * Creates the child view components for a {@link Widgetchildren} component. Implement this interface and register it in
 * spring to add your own children renderer to the engine.
 */
public interface WidgetChildrenContainerRenderer
{
    /**
     * Renders the {@link Widgetchildren} component and its child widgets.
     *
     * @param childrenComponent
     *           The {@link Widgetchildren} component (usually defined as &lt;widgetchildren&gt; tag within zul).
     * @param children
     *           The corresponding child widgets for this container.
     * @param ctx
     *           Additional context information
     */
    void render(Widgetchildren childrenComponent, List<WidgetInstance> children, Map<String, Object> ctx);
}
