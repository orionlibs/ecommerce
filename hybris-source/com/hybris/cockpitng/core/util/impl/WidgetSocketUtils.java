/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class to get the list of all widget sockets - those from the widget definition as well as the virtual ones
 * defined on the widget.
 */
public final class WidgetSocketUtils
{
    private WidgetSocketUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Returns all input sockets for the given widget. Includes input sockets from the widget definition as well as the
     * virtual ones defined on the widget.
     *
     * @param widget the widget to get all input sockets for
     * @param definition the widget definition
     * @return all input sockets for the given widget. Includes input sockets from the widget definition as well as the
     *         virtual ones defined on the widget.
     */
    public static List<WidgetSocket> getAllInputs(final Widget widget, final WidgetDefinition definition)
    {
        final List<WidgetSocket> inputs = new ArrayList<WidgetSocket>(
                        cloneSocketsFromMetadataToWidget(widget, definition.getInputs()));
        inputs.addAll(widget.getVirtualInputs());
        return inputs;
    }


    /**
     * Returns all output sockets for the given widget. Includes output sockets from the widget definition as well as the
     * virtual ones defined on the widget.
     *
     * @param widget the widget to get all output sockets for
     * @param definition the widget definition
     * @return all output sockets for the given widget. Includes output sockets from the widget definition as well as the
     *         virtual ones defined on the widget.
     */
    public static List<WidgetSocket> getAllOutputs(final Widget widget, final WidgetDefinition definition)
    {
        final List<WidgetSocket> outputs = new ArrayList<>(
                        cloneSocketsFromMetadataToWidget(widget, definition.getOutputs()));
        outputs.addAll(cloneSocketsFromMetadataToWidget(widget, widget.getVirtualOutputs()));
        return outputs;
    }


    /**
     *
     * @param widget the widget to get the output socket with a specific id
     * @param definition the widget definition
     * @param socketOutputId the socket output id to verify
     * @return
     */
    public static boolean hasOutputSocketWithId(final Widget widget, final WidgetDefinition definition,
                    final String socketOutputId)
    {
        final Collection<WidgetSocket> outputs = getAllOutputs(widget, definition);
        for(final WidgetSocket widgetOutputSocket : outputs)
        {
            if(widgetOutputSocket.getId().equals(socketOutputId))
            {
                return true;
            }
        }
        return false;
    }


    private static Collection<WidgetSocket> cloneSocketsFromMetadataToWidget(final Widget widget,
                    final Collection<WidgetSocket> sockets)
    {
        final Collection<WidgetSocket> result = Lists.newArrayList();
        for(final WidgetSocket socket : sockets)
        {
            try
            {
                result.add(new WidgetSocket(widget, socket));
            }
            catch(final Exception e)
            {
                throw new IllegalStateException("Could not clone widget socket metadata information.", e);
            }
        }
        return result;
    }


    /**
     * Returns WidgetSocket instance with given socketOutputId from widget. Returns null if no instance found.
     *
     * @param widget
     * @param definition
     * @param socketOutputId
     * @return WidgetSocket instance with given socketOutputId from widget. Returns null if no instance found.
     */
    public static WidgetSocket getOutputSocketWithId(final Widget widget, final WidgetDefinition definition,
                    final String socketOutputId)
    {
        final Collection<WidgetSocket> outputs = getAllOutputs(widget, definition);
        for(final WidgetSocket widgetOutputSocket : outputs)
        {
            if(widgetOutputSocket.getId().equals(socketOutputId))
            {
                return widgetOutputSocket;
            }
        }
        return null;
    }
}
