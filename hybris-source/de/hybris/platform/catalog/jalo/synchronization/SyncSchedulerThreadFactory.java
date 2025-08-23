package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.cronjob.jalo.CronJob;

class SyncSchedulerThreadFactory extends TenantAwareThreadFactory
{
    private SyncSchedulerThreadFactory()
    {
        this(null);
    }


    public SyncSchedulerThreadFactory(Tenant tenant)
    {
        super(tenant);
    }


    protected void afterPrepareThread()
    {
        CronJob.activateCronJobThreadSettings(CronJob.getCronJobThreadSettings());
    }


    protected void afterUnprepareThread()
    {
        CronJob.unsetCronJobThreadSettings(CronJob.getCronJobThreadSettings());
    }
}
