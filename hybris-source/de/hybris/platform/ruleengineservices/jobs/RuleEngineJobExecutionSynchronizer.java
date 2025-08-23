package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;

public interface RuleEngineJobExecutionSynchronizer
{
    boolean acquireLock(RuleEngineCronJobModel paramRuleEngineCronJobModel);


    void releaseLock(RuleEngineCronJobModel paramRuleEngineCronJobModel);
}
