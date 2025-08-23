/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import com.hybris.cockpitng.core.persistence.impl.jaxb.Widget;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XMLWidgetPersistenceJAXBLookupHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(XMLWidgetPersistenceJAXBLookupHandler.class);


    public Widget findWidgetParent(final Widgets widgets, final Widget theWidget)
    {
        for(final Widget widget : widgets.getWidget())
        {
            final Widget result = findWidgetParentRecursively(widget, theWidget);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }


    private static Widget findWidgetParentRecursively(final Widget root, final Widget theWidget)
    {
        if(theWidget == null || root == null)
        {
            return null;
        }
        if(theWidget.equals(root))
        {
            return null;
        }
        for(final Widget widget : root.getWidget())
        {
            if(theWidget.equals(widget))
            {
                return root;
            }
            final Widget result = findWidgetParentRecursively(widget, theWidget);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }


    private static Widget findWidgetByIdRecursively(final Widget root, final String widgetId)
    {
        if(widgetId == null || root == null)
        {
            LOG.error("Could not get widget for id {} and root {}", widgetId, root);
            return null;
        }
        if(widgetId.equals(root.getId()))
        {
            return root;
        }
        for(final Widget widget : root.getWidget())
        {
            if(widgetId.equals(widget.getId()))
            {
                return widget;
            }
            final Widget result = findWidgetByIdRecursively(widget, widgetId);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }


    public List<WidgetConnection> findWidgetConnections(final Widgets widgets, final Widget widget, final boolean inConnection)
    {
        final List<WidgetConnection> connections = new ArrayList<>();
        for(final WidgetConnection conn : widgets.getWidgetConnection())
        {
            if(inConnection && (widget.getId().equals(conn.getTargetWidgetId()))
                            || !inConnection && (widget.getId().equals(conn.getSourceWidgetId())))
            {
                connections.add(conn);
            }
        }
        return connections;
    }


    public WidgetConnection findWidgetConnection(final Widgets widgets, final String sourceId, final String targetId,
                    final String inputId, final String outputId)
    {
        for(final WidgetConnection conn : widgets.getWidgetConnection())
        {
            if(inputId.equals(conn.getInputId()) && outputId.equals(conn.getOutputId()) && sourceId.equals(conn.getSourceWidgetId())
                            && targetId.equals(conn.getTargetWidgetId()))
            {
                return conn;
            }
        }
        return null;
    }


    public com.hybris.cockpitng.core.Widget findWidgetInWidgetTree(final com.hybris.cockpitng.core.Widget rootWidget,
                    final String widgetIdToFind)
    {
        if(rootWidget.getId().equals(widgetIdToFind))
        {
            return rootWidget;
        }
        for(final com.hybris.cockpitng.core.Widget child : rootWidget.getChildren())
        {
            final com.hybris.cockpitng.core.Widget foundWidget = findWidgetInWidgetTree(child, widgetIdToFind);
            if(foundWidget != null)
            {
                return foundWidget;
            }
        }
        return null;
    }


    public List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> findWidgetsInSlot(
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget, final String slotId)
    {
        final List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> widgetsInSpecifiedSlot = new ArrayList<>();
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget child : widget.getWidget())
        {
            if(slotId.equals(child.getSlotId()))
            {
                widgetsInSpecifiedSlot.add(child);
            }
        }
        return widgetsInSpecifiedSlot;
    }


    public boolean isChildWidget(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget parent,
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget prospectiveChild)
    {
        if(parent.getWidget().contains(prospectiveChild))
        {
            return true;
        }
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget : parent.getWidget())
        {
            if(isChildWidget(widget, prospectiveChild))
            {
                return true;
            }
        }
        return false;
    }


    public com.hybris.cockpitng.core.persistence.impl.jaxb.Widget findWidgetById(final Widgets widgets, final String widgetId)
    {
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget : widgets.getWidget())
        {
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget result = findWidgetByIdRecursively(widget, widgetId);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }
}
