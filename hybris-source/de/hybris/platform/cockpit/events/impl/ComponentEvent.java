package de.hybris.platform.cockpit.events.impl;

public class ComponentEvent
{
    private final Object source;


    public ComponentEvent(Object source)
    {
        this.source = source;
    }


    public Object getSource()
    {
        return this.source;
    }
}
