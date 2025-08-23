package de.hybris.platform.ruleengineservices.jobs.impl;

import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import java.util.Optional;

public class RuleEngineModuleSyncJobPerformable extends AbstractRuleEngineJob
{
    protected Optional<RuleCompilerPublisherResult> performInternal(RuleEngineCronJobModel cronJob, CronJobProgressTracker tracker)
    {
        Optional<RuleCompilerPublisherResult> result = getRuleMaintenanceService().synchronizeModules(cronJob.getSrcModuleName(), cronJob.getTargetModuleName());
        setTrackerProgress(tracker, 100.0D);
        return result;
    }


    protected String getJobName()
    {
        return "RuleEngineModuleSyncJob";
    }
}
