package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.JaloInvalidParameterException;

public abstract class MediaProcessorStep extends GeneratedMediaProcessorStep
{
    protected JobMedia prepareMedia(CronJob cronJob) throws JaloInvalidParameterException
    {
        if(cronJob instanceof MediaProcessCronJob)
        {
            return ((MediaProcessCronJob)cronJob).getJobMedia();
        }
        throw new JaloInvalidParameterException("CronJob was no instance of " + MediaProcessCronJob.class.getName() + " but " + cronJob
                        .getClass().getName(), 0);
    }


    protected boolean canPerform(CronJob cronJob)
    {
        try
        {
            JobMedia media = prepareMedia(cronJob);
            if(cronJob.isRunningRestart())
            {
                return (media != null);
            }
            return (media != null && !media.isLockedAsPrimitive());
        }
        catch(JaloInvalidParameterException e)
        {
            cronJob.addLog(e.getMessage(), (Step)this);
            return false;
        }
    }
}
