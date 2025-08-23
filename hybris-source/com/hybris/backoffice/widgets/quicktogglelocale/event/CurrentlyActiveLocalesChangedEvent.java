/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicktogglelocale.event;

import com.hybris.cockpitng.core.events.CockpitEvent;
import java.util.Locale;

public class CurrentlyActiveLocalesChangedEvent implements CockpitEvent
{
    private static final String EVENT_NAME = "currentlyActiveLocalesChangedEvent";
    private final Locale locale;


    public CurrentlyActiveLocalesChangedEvent(final Locale locale)
    {
        this.locale = locale;
    }


    @Override
    public String getName()
    {
        return EVENT_NAME;
    }


    @Override
    public Object getData()
    {
        return this.locale;
    }


    @Override
    public Object getSource()
    {
        return null;
    }
}
