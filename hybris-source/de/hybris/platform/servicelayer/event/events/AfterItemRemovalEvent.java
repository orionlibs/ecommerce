package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterItemRemovalEvent extends AbstractPersistenceEvent
{
    public AfterItemRemovalEvent()
    {
    }


    public AfterItemRemovalEvent(Serializable source)
    {
        super(source);
    }
}
