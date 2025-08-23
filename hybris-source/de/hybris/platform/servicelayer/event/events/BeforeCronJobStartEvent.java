package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class BeforeCronJobStartEvent extends AbstractCronJobPerformEvent
{
    public BeforeCronJobStartEvent()
    {
    }


    public BeforeCronJobStartEvent(Serializable source)
    {
        super(source);
    }
}
