package de.hybris.platform.servicelayer.cronjob;

public interface CronJobFactory<T extends de.hybris.platform.cronjob.model.CronJobModel, C extends de.hybris.platform.cronjob.model.JobModel>
{
    T createCronJob(C paramC);
}
