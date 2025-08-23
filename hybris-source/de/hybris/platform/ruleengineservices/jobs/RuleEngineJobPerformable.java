package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.servicelayer.cronjob.JobPerformable;

public interface RuleEngineJobPerformable<T extends de.hybris.platform.cronjob.model.CronJobModel> extends JobPerformable<T>
{
    boolean isPerformable(T paramT);
}
