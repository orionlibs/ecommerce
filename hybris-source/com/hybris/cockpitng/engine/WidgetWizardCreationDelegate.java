/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * The purpose of this interface is to delegate operations related to widget wizard creation in Application Orchestrator
 */
public interface WidgetWizardCreationDelegate
{
    EventListener<Event> createAddWidgetWizardSelectListener(final Widgetslot widgetSlot, final String slotId);


    EventListener<Event> createAddWidgetWizardEventListener(final String slotId, final Widget parentWidget,
                    final Executable executable);


    Widget createWidget(final String slotId, final Widget parent, final String id, final WidgetDefinition definition);
}
