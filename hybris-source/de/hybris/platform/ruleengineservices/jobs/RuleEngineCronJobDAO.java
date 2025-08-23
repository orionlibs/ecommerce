package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ruleengineservices.model.RuleEngineJobModel;

public interface RuleEngineCronJobDAO
{
    int countCronJobsByJob(String paramString, CronJobStatus... paramVarArgs);


    RuleEngineJobModel findRuleEngineJobByCode(String paramString);
}
