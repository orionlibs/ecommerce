package de.hybris.platform.cronjob.jalo;

import org.apache.log4j.Logger;

public class CronJobHistory extends GeneratedCronJobHistory
{
    private static final Logger LOG = Logger.getLogger(CronJobHistory.class.getName());


    public String toString()
    {
        return String.format("cronJobCode: %s, jobCode: %s, startTime: %s, progress: %3.2f%%", new Object[] {getCronJobCode(), getJobCode(),
                        getStartTime(), getProgress()});
    }
}
