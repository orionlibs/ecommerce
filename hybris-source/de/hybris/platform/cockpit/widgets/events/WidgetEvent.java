package de.hybris.platform.cockpit.widgets.events;

import de.hybris.platform.cockpit.events.impl.AbstractCockpitEvent;

public class WidgetEvent extends AbstractCockpitEvent
{
    private final String context;


    public WidgetEvent(Object source, String context)
    {
        super(source);
        this.context = context;
    }


    public String getContext()
    {
        return this.context;
    }
}
