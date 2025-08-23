/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.draganddrop.DragAndDrop;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetDragAndDropStrategyRepository implements WidgetDragAndDropStrategyRepository
{
    public static final String DRAG_AND_DROP_CONFIG_CTX = "dragAndDropConfigCtx";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetDragAndDropStrategyRepository.class);
    private static final DragAndDropStrategy FALLBACK_DND_STRATEGY = new FallbackDragAndDropStrategy();
    private final List<WidgetDragAndDropStrategyResolver> widgetDragAndDropStrategyResolvers;


    public DefaultWidgetDragAndDropStrategyRepository(
                    final List<WidgetDragAndDropStrategyResolver> widgetDragAndDropStrategyResolvers)
    {
        this.widgetDragAndDropStrategyResolvers = widgetDragAndDropStrategyResolvers;
    }


    @Override
    public synchronized DragAndDropStrategy getDragAndDropStrategy(final WidgetInstanceManager widgetInstanceManager,
                    final Widget widget)
    {
        final String strategyName = resolveStrategyNameFromWidgetConfigCtxSettingInWidgetHierarchy(widget, widgetInstanceManager);
        if(StringUtils.isNotBlank(strategyName))
        {
            final DragAndDropStrategy dragAndDropStrategy = findDragAndDropStrategyThroughResolvers(strategyName);
            if(dragAndDropStrategy != null)
            {
                return dragAndDropStrategy;
            }
            else
            {
                LOG.warn("Strategy named [{}] not found. Fallback Drag&Drop strategy is returned.", strategyName);
                return FALLBACK_DND_STRATEGY;
            }
        }
        else
        {
            if(LOG.isDebugEnabled() && widget != null)
            {
                LOG.debug("No strategy for widget [{}] was declared. Fallback Drag&Drop strategy is returned.", widget.getId());
            }
            return FALLBACK_DND_STRATEGY;
        }
    }


    private String resolveStrategyNameFromWidgetConfigCtxSettingInWidgetHierarchy(final Widget widget,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(widget == null)
        {
            return null;
        }
        final String dragAndDropConfigCtx = widget.getWidgetSettings().getString(DRAG_AND_DROP_CONFIG_CTX);
        if(dragAndDropConfigCtx == null)
        {
            return resolveStrategyNameFromWidgetConfigCtxSettingInWidgetHierarchy(widget.getParent(), widgetInstanceManager);
        }
        else
        {
            final DragAndDrop dragAndDropConfiguration = loadDragAndDropConfiguration(widgetInstanceManager, dragAndDropConfigCtx);
            return dragAndDropConfiguration != null ? dragAndDropConfiguration.getStrategy() : null;
        }
    }


    private DragAndDrop loadDragAndDropConfiguration(final WidgetInstanceManager widgetInstanceManager,
                    final String dragAndDropConfigCtx)
    {
        try
        {
            final ConfigContext configContext = new DefaultConfigContext(dragAndDropConfigCtx);
            return widgetInstanceManager.loadConfiguration(configContext, DragAndDrop.class);
        }
        catch(final CockpitConfigurationException cockpitConfigurationException)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("DragAndDrop configuration not found for configContext=" + dragAndDropConfigCtx,
                                cockpitConfigurationException);
            }
        }
        return null;
    }


    private DragAndDropStrategy findDragAndDropStrategyThroughResolvers(final String strategyName)
    {
        for(final WidgetDragAndDropStrategyResolver resolver : widgetDragAndDropStrategyResolvers)
        {
            final DragAndDropStrategy resolvedStrategy = resolver.resolve(strategyName);
            if(resolvedStrategy != null)
            {
                return resolvedStrategy;
            }
        }
        return null;
    }


    public List<WidgetDragAndDropStrategyResolver> getWidgetDragAndDropStrategyResolvers()
    {
        return Collections.unmodifiableList(widgetDragAndDropStrategyResolvers);
    }
}
