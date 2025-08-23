package de.hybris.platform.cronjob.jalo;

public class UnPerformableJob extends TestJob
{
    protected boolean canPerform(CronJob cronJob)
    {
        return false;
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        throw new IllegalStateException("... calling 'performCronJob(final CronJob cronJob)' shouldn't be possible at all!!!");
    }
}
