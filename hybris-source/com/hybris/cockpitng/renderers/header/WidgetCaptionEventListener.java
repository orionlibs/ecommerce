/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.header;

/**
 * Event listener interface used by {@link WidgetCaptionWrapper}.
 */
public interface WidgetCaptionEventListener
{
    /**
     * Implement this method to handle events fired by a widget container. See the "ON_xxx" constants at
     * {@link WidgetCaptionWrapper}.
     */
    void onEvent(String eventName, WidgetCaptionWrapper captionWrapper);
}
