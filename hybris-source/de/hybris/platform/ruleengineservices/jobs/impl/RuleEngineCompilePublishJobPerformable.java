package de.hybris.platform.ruleengineservices.jobs.impl;

import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import java.util.Optional;

public class RuleEngineCompilePublishJobPerformable extends AbstractRuleEngineJob
{
    protected Optional<RuleCompilerPublisherResult> performInternal(RuleEngineCronJobModel cronJob, CronJobProgressTracker tracker)
    {
        RuleCompilerPublisherResult result = getRuleMaintenanceService().compileAndPublishRulesWithBlocking(cronJob.getSourceRules(), cronJob.getTargetModuleName(), cronJob
                        .getEnableIncrementalUpdate().booleanValue(), tracker);
        return Optional.ofNullable(result);
    }


    protected String getJobName()
    {
        return "RuleEngineCompilePublishJob";
    }
}
