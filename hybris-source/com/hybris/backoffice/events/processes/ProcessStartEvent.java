package com.hybris.backoffice.events.processes;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;

public class ProcessStartEvent extends AbstractProcessEvent<BeforeCronJobStartEvent>
{
    public static final String EVENT_NAME = "com.hybris.backoffice.events.processes.ProcessStartEvent";


    public ProcessStartEvent(BeforeCronJobStartEvent syncEvent)
    {
        super((AbstractEvent)syncEvent);
    }
}
