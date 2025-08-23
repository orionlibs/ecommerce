package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class BeforeSessionCloseEvent extends AbstractEvent
{
    public BeforeSessionCloseEvent()
    {
    }


    public BeforeSessionCloseEvent(Serializable source)
    {
        super(source);
    }
}
