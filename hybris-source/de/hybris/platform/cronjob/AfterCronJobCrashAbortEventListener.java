package de.hybris.platform.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.event.events.AfterCronJobCrashAbortEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import javax.annotation.Resource;

public class AfterCronJobCrashAbortEventListener extends AbstractEventListener<AfterCronJobCrashAbortEvent>
{
    @Resource
    ModelService modelService;
    @Resource
    CronJobService cronJobService;


    AfterCronJobCrashAbortEventListener(ModelService modelService, CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
        this.modelService = modelService;
    }


    protected void onEvent(AfterCronJobCrashAbortEvent event)
    {
        CronJobModel cronJob = (CronJobModel)this.modelService.get(event.getCronJobPK());
        boolean isRetryEnabled = Config.getBoolean("cronjob.enableRepeat", false);
        if(isRetryEnabled)
        {
            restartCronJob(cronJob);
        }
    }


    private void restartCronJob(CronJobModel cronJob)
    {
        int currentRepeat = cronJob.getCurrentRetry();
        int maxRepeats = getMaxNumberOfRetries(cronJob);
        boolean wasManuallyAborted = (cronJob.getRequestAbort() != null && cronJob.getRequestAbort().booleanValue());
        if(maxRepeats > 0 && currentRepeat < maxRepeats && !wasManuallyAborted)
        {
            cronJob.setCurrentRetry(currentRepeat + 1);
            this.modelService.save(cronJob);
            this.cronJobService.performCronJob(cronJob, false);
        }
    }


    private int getMaxNumberOfRetries(CronJobModel cronJob)
    {
        int numberOfRetriesFromJob = cronJob.getJob().getNumberOfRetries();
        int numberOfRetriesFromCronJob = cronJob.getNumberOfRetries();
        return Math.max(numberOfRetriesFromJob, numberOfRetriesFromCronJob);
    }
}
