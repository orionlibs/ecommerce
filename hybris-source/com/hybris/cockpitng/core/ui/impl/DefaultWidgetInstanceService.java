/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.ui.impl;

import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of service for managing widget instances.
 *
 * @see WidgetInstance
 */
public class DefaultWidgetInstanceService implements WidgetInstanceService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetInstanceService.class);
    private CockpitComponentDefinitionService componentDefinitionService;


    @Override
    public WidgetInstance createRootWidgetInstance(final Widget widget)
    {
        return createWidgetInstanceInternal(widget);
    }


    @Override
    public WidgetInstance createWidgetInstance(final Widget widget, final WidgetInstance parent, final Object creator)
    {
        final WidgetInstance widgetInstance = createWidgetInstance(widget, parent);
        if(widgetInstance instanceof DefaultWidgetInstance)
        {
            ((DefaultWidgetInstance)widgetInstance).setCreator(creator);
        }
        return widgetInstance;
    }


    @Override
    public WidgetInstance createWidgetInstance(final Widget widget, final WidgetInstance parentInstance)
    {
        if(parentInstance == null)
        {
            if(widget.getParent() == null)
            {
                return createRootWidgetInstance(widget);
            }
            throw new IllegalArgumentException(
                            "given widget's parentInstance instance is null but the widget parentInstance is not null: '" + widget.getParent()
                                            + "'");
        }
        final Widget parent = parentInstance.getWidget();
        if(parent.getComposedRootInstance() == null)
        {
            if(!parent.equals(widget.getParent()))
            {
                throw new IllegalArgumentException("given widget's parentInstance '" + widget.getParent()
                                + "' is not the parentInstance of the given parentInstance widget instance '" + parentInstance + "'");
            }
        }
        else if(widget.getParent() == null)
        {
            LOG.debug("Creating instance with default parent");
        }
        else if(widget.getParent().getComposedRootInstance().equals(parent.getComposedRootInstance()))
        {
            LOG.debug("Creating instance for composed widget");
        }
        else
        {
            throw new IllegalArgumentException("given widget's parentInstance '" + widget.getParent()
                            + "' differs from composed root of the given parentInstance widget instance '" + parentInstance + "'");
        }
        final DefaultWidgetInstance instance = createWidgetInstanceInternal(widget);
        if(instance != null)
        {
            instance.setParent(parentInstance);
            instance.setTemplateRoot(parentInstance.getTemplateRoot());
        }
        return instance;
    }


    @Override
    public List<WidgetInstance> getRootWidgetInstances(final Widget widget)
    {
        return widget.getWidgetInstances();
    }


    @Override
    public List<WidgetInstance> getWidgetInstances(final WidgetInstance parent)
    {
        final Widget parentWidget = parent.getWidget();
        final List<WidgetInstance> result = new ArrayList<>();
        final List<Widget> children;
        if(parentWidget.getComposedRootInstance() == null)
        {
            children = parentWidget.getChildren();
        }
        else
        {
            children = Collections.singletonList(parentWidget.getComposedRootInstance());
        }
        for(final Widget childWidget : children)
        {
            for(final WidgetInstance childInstance : childWidget.getWidgetInstances())
            {
                if(parent.equals(childInstance.getParent()))
                {
                    result.add(childInstance);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }


    @Override
    public List<WidgetInstance> getWidgetInstances(final Widget widget, final WidgetInstance parent)
    {
        final List<WidgetInstance> children = getWidgetInstances(parent);
        if(!children.isEmpty())
        {
            final List<WidgetInstance> result = new ArrayList<>();
            for(final WidgetInstance instance : children)
            {
                if(widget.equals(instance.getWidget()))
                {
                    result.add(instance);
                }
            }
            return Collections.unmodifiableList(result);
        }
        else
        {
            return children;
        }
    }


    @Override
    public void removeWidgetInstance(final WidgetInstance instance)
    {
        final Set<WidgetInstance> removed = new HashSet<>();
        removeWidgetInstanceRecursively(instance, removed);
        removed.clear();
    }


    private void removeWidgetInstanceRecursively(final WidgetInstance instance, final Set<WidgetInstance> removed)
    {
        // remove children
        for(final WidgetInstance child : getWidgetInstances(instance))
        {
            if(!removed.contains(child))
            {
                removeWidgetInstanceRecursively(child, removed);
            }
        }
        // remove itself
        final Widget widget = instance.getWidget();
        if(widget != null)
        {
            widget.removeWidgetInstance(instance);
        }
        ((DefaultWidgetInstance)instance).setParent(null);
        removed.add(instance);
    }


    private DefaultWidgetInstance createWidgetInstanceInternal(final Widget widget)
    {
        DefaultWidgetInstance instance = null;
        final WidgetDefinition definition = (WidgetDefinition)componentDefinitionService.getComponentDefinitionForCode(widget
                        .getWidgetDefinitionId());
        if(definition == null)
        {
            LOG.error("Unable to load widget definition for code " + widget.getWidgetDefinitionId());
        }
        else
        {
            final int index = getNextIndex(widget);
            instance = new DefaultWidgetInstance(widget, createViewModel(), index);
            widget.addWidgetInstance(instance);
        }
        return instance;
    }


    private Object createViewModel()
    {
        return new ConcurrentHashMap<String, Object>(1);
    }


    private int getNextIndex(final Widget widget)
    {
        int index = 0;
        for(final WidgetInstance instance : widget.getWidgetInstances())
        {
            final int instanceIndex = ((DefaultWidgetInstance)instance).getIndex();
            if(instanceIndex >= index)
            {
                index = instanceIndex + 1;
            }
        }
        return index;
    }


    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }
}
