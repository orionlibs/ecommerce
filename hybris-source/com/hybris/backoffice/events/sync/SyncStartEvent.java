package com.hybris.backoffice.events.sync;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;

@Deprecated(since = "6.6", forRemoval = true)
public class SyncStartEvent extends AbstractSyncEvent<BeforeCronJobStartEvent>
{
    public static final String EVENT_NAME = "com.hybris.backoffice.events.sync.SyncStartEvent";


    public SyncStartEvent(BeforeCronJobStartEvent syncEvent)
    {
        super((AbstractEvent)syncEvent);
    }
}
