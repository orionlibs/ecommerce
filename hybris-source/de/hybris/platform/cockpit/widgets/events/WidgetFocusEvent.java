package de.hybris.platform.cockpit.widgets.events;

import de.hybris.platform.cockpit.events.impl.AbstractCockpitEvent;

public class WidgetFocusEvent extends AbstractCockpitEvent
{
    private final String widgetCode;


    public WidgetFocusEvent(Object source, String widgetCode)
    {
        super(source);
        this.widgetCode = widgetCode;
    }


    public String getWidgetCode()
    {
        return this.widgetCode;
    }
}
