package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterSessionCreationEvent extends AbstractEvent
{
    public AfterSessionCreationEvent()
    {
    }


    public AfterSessionCreationEvent(Serializable source)
    {
        super(source);
    }
}
