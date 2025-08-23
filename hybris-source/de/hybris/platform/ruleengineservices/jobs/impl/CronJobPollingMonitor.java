package de.hybris.platform.ruleengineservices.jobs.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.function.Supplier;

public abstract class CronJobPollingMonitor implements Runnable
{
    private Supplier<RuleEngineCronJobModel> ruleEngineCronJobModelSupplier;
    private CronJobService cronJobService;
    private ModelService modelService;


    public CronJobPollingMonitor(CronJobService cronJobService, ModelService modelService, Supplier<RuleEngineCronJobModel> ruleEngineCronJobModelSupplier)
    {
        this.cronJobService = cronJobService;
        this.modelService = modelService;
        this.ruleEngineCronJobModelSupplier = ruleEngineCronJobModelSupplier;
    }


    public void run()
    {
        try
        {
            RuleEngineCronJobModel cronJob = this.ruleEngineCronJobModelSupplier.get();
            waitTillCronJobFinished(cronJob.getCode());
        }
        finally
        {
            onCronJobFinished();
        }
    }


    protected void waitTillCronJobFinished(String cronJobCode)
    {
        try
        {
            Thread.sleep(5000L);
            CronJobModel cronJob = this.cronJobService.getCronJob(cronJobCode);
            this.modelService.refresh(cronJob);
            while(this.cronJobService.isRunning(cronJob))
            {
                Thread.sleep(1000L);
                this.modelService.refresh(cronJob);
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            onCronJobFinished();
        }
    }


    abstract void onCronJobFinished();
}
