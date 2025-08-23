package com.hybris.backoffice.events;

import com.hybris.backoffice.events.processes.ProcessFinishedEvent;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobPerformEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;

public class AfterCronJobFinishedEventListener extends AbstractBackofficeCronJobEventListener<AfterCronJobFinishedEvent>
{
    protected void onEvent(AfterCronJobFinishedEvent event)
    {
        if(isProcessUpdateEvent((AbstractCronJobPerformEvent)event))
        {
            getEventService().publishEvent((AbstractEvent)new ProcessFinishedEvent(event));
        }
    }


    @Deprecated(since = "6.6", forRemoval = true)
    protected boolean isSyncJob(AfterCronJobFinishedEvent event)
    {
        return typesMatch(event.getJobType(), "SyncItemJob");
    }
}
