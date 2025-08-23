/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.WidgetChildrenContainerRenderer;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;

public class ConditionalChildrenContainerRenderer extends AbstractChildrenContainerRenderer
{
    private final static String ATTRIBUTE_RENDERER = "renderer";
    private final static String RENDERER_SPECIFIC = "specific";
    private final static String RENDERER_FALLBACK = "fallback";
    private BiPredicate<Widgetchildren, Map<String, Object>> condition;
    private WidgetChildrenContainerRenderer specificRenderer;
    private WidgetChildrenContainerRenderer fallbackRenderer;


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final Object current = isSpecificRendererValid(childrenComponent, ctx) ? RENDERER_SPECIFIC : RENDERER_FALLBACK;
        final Object previous = childrenComponent.getAttribute(ATTRIBUTE_RENDERER);
        if(ObjectUtils.notEqual(current, previous))
        {
            childrenComponent.getChildren().clear();
        }
        if(RENDERER_SPECIFIC.equals(current))
        {
            getSpecificRenderer().render(childrenComponent, children, ctx);
        }
        else
        {
            getFallbackRenderer().render(childrenComponent, children, ctx);
        }
        childrenComponent.setAttribute(ATTRIBUTE_RENDERER, current);
    }


    protected boolean isSpecificRendererValid(final Widgetchildren childrenComponent, final Map<String, Object> ctx)
    {
        return getCondition().test(childrenComponent, ctx);
    }


    protected BiPredicate<Widgetchildren, Map<String, Object>> getCondition()
    {
        return condition;
    }


    @Required
    public void setCondition(final BiPredicate<Widgetchildren, Map<String, Object>> condition)
    {
        this.condition = condition;
    }


    protected WidgetChildrenContainerRenderer getSpecificRenderer()
    {
        return specificRenderer;
    }


    @Required
    public void setSpecificRenderer(final WidgetChildrenContainerRenderer specificRenderer)
    {
        this.specificRenderer = specificRenderer;
    }


    protected WidgetChildrenContainerRenderer getFallbackRenderer()
    {
        return fallbackRenderer;
    }


    @Required
    public void setFallbackRenderer(final WidgetChildrenContainerRenderer fallbackRenderer)
    {
        this.fallbackRenderer = fallbackRenderer;
    }
}
