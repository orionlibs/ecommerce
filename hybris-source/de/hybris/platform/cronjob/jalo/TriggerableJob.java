package de.hybris.platform.cronjob.jalo;

@Deprecated(since = "ages", forRemoval = false)
public interface TriggerableJob
{
    CronJob newExecution();
}
