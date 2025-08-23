package de.hybris.platform.servicelayer.cronjob;

public interface JobPerformable<T extends de.hybris.platform.cronjob.model.CronJobModel>
{
    PerformResult perform(T paramT);


    boolean isPerformable();


    boolean isAbortable();
}
