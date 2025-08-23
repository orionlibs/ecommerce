/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * The cockpit widget engine is responsible for injecting the view definitions into the zul page as well as sending
 * socket events between the widgets.
 */
public interface CockpitWidgetEngine
{
    /**
     * Used as an alias for WIDGET_ROOT_PARAM
     */
    String WR_PARAM = "wr";
    /**
     * Attribute name for the absolute web path (widget root) of the widget definition file and other widget resources,
     * cf. {@link com.hybris.cockpitng.core.AbstractCockpitComponentDefinition#getLocationPath()}.
     */
    String WIDGET_ROOT_PARAM = "widgetRoot";
    String COMPONENT_ROOT_PARAM = "componentRoot";
    String WIDGET_RESOURCE_PATH_PARAM = "widgetResourcePath";
    String COMPONENT_RESOURCE_PATH_PARAM = "componentResourcePath";
    String LABELS_PARAM = "labels";


    /**
     * Checks whether provided URL points to widget's view, which is defined as not dependent from ZK framework (i.e.
     * uses pure html or some third party javascript library)
     *
     * @param widget
     *           widget definition to be checked
     * @param uri
     *           requested URL
     * @return <code>true</code> if a URL points to view that is defined as independent
     */
    default boolean isIndependentView(final WidgetDefinition widget, final String uri)
    {
        final String viewURI = widget.getViewURI();
        return viewURI != null && viewURI.equals(uri)
                        && (StringUtils.endsWith(viewURI, ".html") || StringUtils.contains(StringUtils.deleteWhitespace(viewURI), ".html@"));
    }


    /**
     * Creates the view components for the specified widgetslot. If no widget is attached to the slot, an error is
     * displayed, otherwise the view representation of the attached widget is injected.
     */
    void createWidgetView(Widgetslot widgetslot);


    /**
     * Creates the view components for the specified widgetchildren slot. For each matching child widget it creates a
     * container, according to the type of the widgetchildren.
     */
    void createWidgetView(Widgetchildren widgetChildrenComponent, Map<String, Object> ctx);


    /**
     * Same as {@link #createWidgetView(Widgetchildren, Map)} with empty map as second arg.
     */
    void createWidgetView(Widgetchildren widgetChildren);


    /**
     * Calls sendOutput(widgetslot, outputID, null)}.
     */
    void sendOutput(Widgetslot widgetslot, String outputID);


    /**
     * Calls sendOutput(widgetslot, outputID, data, false)}.
     */
    void sendOutput(Widgetslot widgetslot, String outputID, Object data);


    /**
     * Sends an output event from the widget attached to the widgetslot parameter with the given outputID. This method is
     * finally called from {@link DefaultWidgetController#sendOutput(String, Object)}.
     *
     * @param widgetslot
     *           The source widget slot
     * @param outputID
     *           The output socket id
     * @param data
     *           The event data
     * @param ignoreSockets
     *           If false, the event is only sent if the output socket with outputID exists. Otherwise no check is done
     *           and the event is sent. This is only needed for internal use, especially for composed widget internal
     *           event handling.
     */
    void sendOutput(Widgetslot widgetslot, String outputID, Object data, boolean ignoreSockets);
}
