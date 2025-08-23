package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineJobModel;
import java.util.function.Supplier;

public interface RuleEngineJobService
{
    RuleEngineJobModel getRuleEngineJob(String paramString1, String paramString2);


    boolean isRunning(String paramString);


    int countRunningJobs(String paramString);


    RuleEngineCronJobModel triggerCronJob(String paramString1, String paramString2, Supplier<RuleEngineCronJobModel> paramSupplier);
}
