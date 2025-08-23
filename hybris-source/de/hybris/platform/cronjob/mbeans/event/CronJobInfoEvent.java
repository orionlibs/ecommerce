package de.hybris.platform.cronjob.mbeans.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Collection;
import java.util.Collections;

public class CronJobInfoEvent extends AbstractEvent implements ClusterAwareEvent
{
    private final Collection<String> cronJobsIds;


    public CronJobInfoEvent(Collection<String> cronJobsIds)
    {
        this.cronJobsIds = Collections.unmodifiableCollection(cronJobsIds);
    }


    public boolean canPublish(PublishEventContext publishEventContext)
    {
        return true;
    }


    public Collection<String> getCronJobs()
    {
        return this.cronJobsIds;
    }
}
