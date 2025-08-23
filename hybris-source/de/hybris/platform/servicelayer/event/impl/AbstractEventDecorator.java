package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.event.EventDecorator;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public abstract class AbstractEventDecorator<T extends AbstractEvent> implements EventDecorator<T>
{
    private int priority = 0;


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
