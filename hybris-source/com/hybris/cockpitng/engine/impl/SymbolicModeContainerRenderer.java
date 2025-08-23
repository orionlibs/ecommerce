/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.List;
import java.util.Map;
import org.zkoss.zul.Div;

/**
 * Renderer for children slot in symbolic mode.
 */
public class SymbolicModeContainerRenderer extends AbstractChildrenContainerRenderer
{
    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final Div container = new Div();
        childrenComponent.appendChild(container);
        container.setSclass("widget_children_list");
        for(final WidgetInstance child : children)
        {
            final Widgetslot widgetContainer = new Widgetslot();
            widgetContainer.setWidgetInstance(child);
            widgetContainer.setParentChildrenContainer(childrenComponent);
            widgetContainer.setSlotID(childrenComponent.getSlotID());
            container.appendChild(widgetContainer);
            widgetContainer.afterCompose();
        }
    }
}
