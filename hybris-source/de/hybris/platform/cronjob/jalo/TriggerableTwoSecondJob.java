package de.hybris.platform.cronjob.jalo;

public class TriggerableTwoSecondJob extends Job implements TriggerableJob
{
    private static String STATICCRONJOBCODE = "TriggerableTestCronJob";


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        try
        {
            Thread.sleep(2000L);
        }
        catch(Exception e1)
        {
            return cronJob.getFinishedResult(false);
        }
        return cronJob.getFinishedResult(true);
    }


    public CronJob newExecution()
    {
        return CronJobManager.getInstance().createCronJob(this, STATICCRONJOBCODE, true);
    }
}
