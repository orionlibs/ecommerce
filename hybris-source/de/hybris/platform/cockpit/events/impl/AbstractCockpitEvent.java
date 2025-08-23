package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;

public abstract class AbstractCockpitEvent implements CockpitEvent
{
    private final Object source;


    public AbstractCockpitEvent(Object source)
    {
        this.source = source;
    }


    public Object getSource()
    {
        return this.source;
    }


    public String toString()
    {
        return getClass().getSimpleName() + "[source='" + getClass().getSimpleName() + "']";
    }
}
