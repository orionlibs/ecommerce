/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.header;

import org.zkoss.zk.ui.Component;

/**
 * This interface should be implemented by Controllers of widgets that want to use Caption area of a
 * container they are nested in. In case when the widget managed by the controller is not located in a collapsible
 * container or the container does not support custom captions this interface may be ignored i.e. the implementation
 * must not rely on any of the interface's methods being called.
 *
 * @see DefaultWidgetCaptionWrapper
 * @see com.hybris.cockpitng.util.WidgetSlotUtils
 * @see WidgetVisibilityStateAware
 */
public interface WidgetCaptionRenderer
{
    /**
     * @param captionWrapper The captionWrapper should be used to communicate with the parent container
     * @return The content of the caption to be displayed.
     */
    Component renderCaption(WidgetCaptionWrapper captionWrapper);
}
