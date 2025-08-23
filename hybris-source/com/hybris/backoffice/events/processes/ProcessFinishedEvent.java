package com.hybris.backoffice.events.processes;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;

public class ProcessFinishedEvent extends AbstractProcessEvent<AfterCronJobFinishedEvent>
{
    public static final String EVENT_NAME = "com.hybris.backoffice.events.processes.ProcessFinishedEvent";


    public ProcessFinishedEvent(AfterCronJobFinishedEvent syncEvent)
    {
        super((AbstractEvent)syncEvent);
    }
}
