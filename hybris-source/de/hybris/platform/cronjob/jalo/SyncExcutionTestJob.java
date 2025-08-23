package de.hybris.platform.cronjob.jalo;

import org.apache.log4j.Logger;

public class SyncExcutionTestJob extends Job
{
    private static final Logger log = Logger.getLogger(SyncExcutionTestJob.class);


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        CronJob cjToPerform = (CronJob)cronJob.getProperty("nested");
        cjToPerform.getJob().perform(cjToPerform, true);
        log.warn("outer");
        return cronJob.getFinishedResult(true);
    }
}
