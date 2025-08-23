package com.hybris.backoffice.events;

import com.hybris.backoffice.events.processes.ProcessStartEvent;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobPerformEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;

public class BeforeCronJobStartEventListener extends AbstractBackofficeCronJobEventListener<BeforeCronJobStartEvent>
{
    protected void onEvent(BeforeCronJobStartEvent event)
    {
        if(isProcessUpdateEvent((AbstractCronJobPerformEvent)event))
        {
            getEventService().publishEvent((AbstractEvent)new ProcessStartEvent(event));
        }
    }


    @Deprecated(since = "6.6", forRemoval = true)
    protected boolean isSyncJob(BeforeCronJobStartEvent event)
    {
        return typesMatch(event.getJobType(), "SyncItemJob");
    }
}
