package de.hybris.platform.ruleengineservices.jobs.impl;

import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import java.util.Optional;

public class RuleEngineModuleInitJobPerformable extends AbstractRuleEngineJob
{
    protected Optional<RuleCompilerPublisherResult> performInternal(RuleEngineCronJobModel cronJob, CronJobProgressTracker tracker)
    {
        setTrackerProgress(tracker, 50.0D);
        RuleCompilerPublisherResult result = getRuleMaintenanceService().initializeModule(cronJob.getTargetModuleName(), true);
        setTrackerProgress(tracker, 100.0D);
        return Optional.ofNullable(result);
    }


    protected String getJobName()
    {
        return "RuleEngineModuleInitJob";
    }
}
