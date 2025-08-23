package de.hybris.platform.y2ysync.backoffice.data;

import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;

public class Y2YSyncJobForm
{
    private Y2YSyncJobModel syncJob;
    private Y2YSyncCronJobModel syncCronJob;
    private boolean async = true;


    public Y2YSyncJobModel getSyncJob()
    {
        return this.syncJob;
    }


    public void setSyncJob(Y2YSyncJobModel syncJob)
    {
        this.syncJob = syncJob;
    }


    public boolean isAsync()
    {
        return this.async;
    }


    public void setAsync(boolean async)
    {
        this.async = async;
    }


    public Y2YSyncCronJobModel getSyncCronJob()
    {
        return this.syncCronJob;
    }


    public void setSyncCronJob(Y2YSyncCronJobModel syncCronJob)
    {
        this.syncCronJob = syncCronJob;
    }
}
