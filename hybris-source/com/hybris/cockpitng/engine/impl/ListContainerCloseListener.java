/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import org.zkoss.zk.ui.event.Event;

/**
 * Listens on close event of template windows
 */
public interface ListContainerCloseListener
{
    /**
     * Notified when window is being closed via cross button in the upper-right corner.
     *
     * @param event
     *           on close event
     * @param widgetInstance
     *           widget instance that is about to close
     */
    void onClose(Event event, WidgetInstance widgetInstance);
}
