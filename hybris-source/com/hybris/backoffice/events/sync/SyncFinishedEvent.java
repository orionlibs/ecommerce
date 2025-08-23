package com.hybris.backoffice.events.sync;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;

@Deprecated(since = "6.6", forRemoval = true)
public class SyncFinishedEvent extends AbstractSyncEvent<AfterCronJobFinishedEvent>
{
    public static final String EVENT_NAME = "com.hybris.backoffice.events.sync.SyncFinishedEvent";


    public SyncFinishedEvent(AfterCronJobFinishedEvent syncEvent)
    {
        super((AbstractEvent)syncEvent);
    }
}
