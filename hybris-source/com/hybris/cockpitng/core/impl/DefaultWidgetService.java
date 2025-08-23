/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link WidgetService}. Provides methods dealing with creating and removing widgets,
 * creating composed widgets, creating connections.
 */
public class DefaultWidgetService implements WidgetService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetService.class);


    @Override
    public Widget createWidget(final Widget parent, final String id, final String slotId, final String definitionId)
    {
        final Widget newWidget = new Widget(id == null ? UUID.randomUUID().toString() : id);
        newWidget.setSlotId(slotId);
        newWidget.setWidgetDefinitionId(definitionId);
        newWidget.setParent(parent);
        if(parent != null)
        {
            parent.addChild(newWidget);
        }
        return newWidget;
    }


    @Override
    public Widget createComposedWidget(final Widget parent, final String id, final String slotId, final String definitionId,
                    final Widget rootNode)
    {
        final Widget newWidget = createWidget(parent, id, slotId, definitionId);
        newWidget.setTitle(rootNode.getTitle());
        loadComposedChildren(newWidget, rootNode);
        return newWidget;
    }


    @Override
    public void loadComposedChildren(final Widget composedWidget, final Widget templateNode)
    {
        final Map<Widget, Widget> cloneMapping = new HashMap<>();
        final Widget composedRoot = cloneWidgetTree(templateNode, null, cloneMapping);
        composedRoot.setSlotId(COMPOSED_ROOT_SLOT_ID);
        composedRoot.setGroupContainer(composedWidget);
        cloneConnections(templateNode, cloneMapping, true, false);
        composedWidget.setComposedRootInstance(composedRoot);
    }


    @Override
    public Set<Widget> getAllDescendants(final Widget widget)
    {
        final Set<Widget> ret = new LinkedHashSet<>();
        ret.add(widget);
        for(final Widget child : widget.getChildren())
        {
            ret.addAll(getAllDescendants(child));
        }
        return ret;
    }


    @Override
    public void removeWidget(final Widget widget)
    {
        final Widget parent = widget.getParent();
        if(parent != null)
        {
            parent.removeChild(widget);
        }
        for(final WidgetConnection conn : widget.getInConnections())
        {
            conn.getSource().removeOutConnection(conn);
        }
        for(final WidgetConnection conn : widget.getOutConnections())
        {
            conn.getTarget().removeInConnection(conn);
        }
    }


    @Override
    public List<WidgetConnection> getWidgetConnectionsForOutputWidgetAndSocketID(final Widget widget, final String outputId)
    {
        final List<WidgetConnection> result = new ArrayList<>();
        final List<WidgetConnection> outConnections = widget.getOutConnections();
        for(final WidgetConnection conn : outConnections)
        {
            if(outputId.equals(conn.getOutputId()))
            {
                result.add(conn);
            }
        }
        return result;
    }


    @Override
    public WidgetConnection createWidgetConnection(final Widget sourceWidget, final Widget targetWidget, final String inputId,
                    final String outputId)
    {
        final List<WidgetConnection> existingConns = getWidgetConnectionsForOutputWidgetAndSocketID(sourceWidget, outputId);
        for(final WidgetConnection existingConn : existingConns)
        {
            if(targetWidget.equals(existingConn.getTarget()) && inputId.equals(existingConn.getInputId()))
            {
                return existingConn;
            }
        }
        final WidgetConnection conn = new WidgetConnection(sourceWidget, targetWidget, inputId, outputId);
        sourceWidget.addOutConnection(conn);
        targetWidget.addInConnection(conn);
        return conn;
    }


    private Widget cloneWidgetTree(final Widget originalWidget, final Widget parent, final Map<Widget, Widget> cloneMapping)
    {
        // create the widget
        final Widget clonedWidget = createWidget(parent, null, originalWidget.getSlotId(), originalWidget.getWidgetDefinitionId());
        clonedWidget.setTemplate(originalWidget.isTemplate());
        clonedWidget.setTitle(originalWidget.getTitle());
        // copy settings
        for(final Entry<String, Object> entry : originalWidget.getWidgetSettings().entrySet())
        {
            clonedWidget.getWidgetSettings().putRaw(entry.getKey(), entry.getValue());
        }
        // copy virtual sockets
        for(final WidgetSocket socket : originalWidget.getVirtualInputs())
        {
            clonedWidget.addVirtualInput(socket);
        }
        for(final WidgetSocket socket : originalWidget.getVirtualOutputs())
        {
            clonedWidget.addVirtualOutput(socket);
        }
        // copy access restrictions
        if(originalWidget.getAccessRestrictions() != null)
        {
            clonedWidget.setAccessRestrictions(new ArrayList<>(originalWidget.getAccessRestrictions()));
        }
        // copy instance settings
        clonedWidget.setWidgetInstanceSettings(SerializationUtils.clone(originalWidget.getWidgetInstanceSettings()));
        // set the original ID
        clonedWidget.setOriginalID(originalWidget.getId());
        // clone widget children
        for(final Widget child : originalWidget.getChildren())
        {
            cloneWidgetTree(child, clonedWidget, cloneMapping);
        }
        cloneMapping.put(originalWidget, clonedWidget);
        clonedWidget.setPartOfGroup(originalWidget.isPartOfGroup());
        return clonedWidget;
    }


    private void cloneConnections(final Widget originalWidget, final Map<Widget, Widget> cloneMapping, final boolean recursive,
                    final boolean onlyOutConnections)
    {
        final Widget clonedWidget = cloneMapping.get(originalWidget);
        if(clonedWidget == null)
        {
            LOG.warn("Couldn't find cloned for widget '{}', ignoring connection.", originalWidget);
        }
        else
        {
            for(final WidgetConnection connection : originalWidget.getOutConnections())
            {
                final Widget clonedTarget = cloneMapping.get(connection.getTarget());
                if(clonedTarget == null)
                {
                    LOG.warn("Couldn't find clone for target widget '{}', ignoring connection.", connection.getTarget());
                }
                else
                {
                    createWidgetConnection(clonedWidget, clonedTarget, connection.getInputId(), connection.getOutputId());
                }
            }
            if(!onlyOutConnections)
            {
                for(final WidgetConnection connection : originalWidget.getInConnections())
                {
                    final Widget clonedSource = cloneMapping.get(connection.getSource());
                    if(clonedSource == null)
                    {
                        LOG.warn("Couldn't find clone for source widget '" + connection.getSource() + "', ignoring connection.");
                    }
                    else
                    {
                        createWidgetConnection(clonedSource, clonedWidget, connection.getInputId(), connection.getOutputId());
                    }
                }
            }
            if(recursive)
            {
                for(final Widget child : originalWidget.getChildren())
                {
                    cloneConnections(child, cloneMapping, true, onlyOutConnections);
                }
            }
        }
    }


    @Override
    public void moveWidget(final Widget widget, final Widget newParent)
    {
        final Widget parent = widget.getParent();
        if(parent != null)
        {
            parent.removeChild(widget);
        }
        if(newParent != null)
        {
            newParent.addChild(widget);
        }
        widget.setParent(newParent);
    }
}
