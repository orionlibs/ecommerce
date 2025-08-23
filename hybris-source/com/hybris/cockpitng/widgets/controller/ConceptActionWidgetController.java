/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * Container to demonstrate the Action concept.
 */
public class ConceptActionWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private static final String DEMO_KEY = "myProperty";
    private static final String ACTION_TRIGGER_NAME = "actionTrigger";


    /**
     * Sets resp. erases a property's value.
     */
    private void toggleProperty()
    {
        if(getValue(DEMO_KEY, String.class) == null)
        {
            setValue(DEMO_KEY, "myValue");
        }
        else
        {
            setValue(DEMO_KEY, null);
        }
    }


    /**
     * Performs corresponding actions on event.
     *
     * @param event user-triggered event
     */
    @ViewEvent(componentID = ACTION_TRIGGER_NAME, eventName = Events.ON_CLICK)
    public void handleAction(final Event event)
    {
        toggleProperty();
    }
}
