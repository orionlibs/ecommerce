package de.hybris.platform.cronjob.mbeans;

import java.util.Collection;

public interface CronJobsInfoMBean
{
    Boolean abortRunningCronJobs();


    Collection<String> getRunningCronJobs();
}
