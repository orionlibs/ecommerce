package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import java.util.List;

public interface RuleEngineCronJobLauncher
{
    RuleEngineCronJobModel triggerCompileAndPublish(List<SourceRuleModel> paramList, String paramString, boolean paramBoolean);


    RuleEngineCronJobModel triggerUndeployRules(List<SourceRuleModel> paramList, String paramString);


    RuleEngineCronJobModel triggerSynchronizeModules(String paramString1, String paramString2);


    RuleEngineCronJobModel triggerModuleInitialization(String paramString);


    RuleEngineCronJobModel triggerAllModulesInitialization();
}
