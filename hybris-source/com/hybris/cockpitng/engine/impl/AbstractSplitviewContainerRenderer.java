/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.List;
import java.util.Map;
import org.zkoss.zul.Box;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Splitter;

/**
 * Renders a {@link Widgetchildren} component as {@link Box} and its children as {@link Panel}s. {@link Splitter}s are
 * added between the {@link Panel}s. The orientation depends on the return value of {@link #createBoxComponent()}.
 */
public abstract class AbstractSplitviewContainerRenderer extends PortalContainerRenderer
{
    protected abstract Box createBoxComponent();


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final Box container = createBoxComponent();
        container.setSclass("splitterChildrenContainer");
        childrenComponent.appendChild(container);
        Splitter splitter = null;
        int index = 0;
        for(final WidgetInstance child : children)
        {
            renderPanel(child, container, index, childrenComponent);
            splitter = new Splitter();
            splitter.setSclass("splitterChildrenContainer_split");
            container.appendChild(splitter);
            index++;
        }
        // remove last splitter
        if(splitter != null)
        {
            splitter.detach();
        }
    }
}
