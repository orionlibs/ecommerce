package de.hybris.platform.ruleengineservices.jobs;

import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import java.util.List;
import java.util.function.Supplier;

public interface RuleEngineCronJobSupplierFactory
{
    Supplier<RuleEngineCronJobModel> createCompileAndPublishSupplier(List<SourceRuleModel> paramList, String paramString, boolean paramBoolean);


    Supplier<RuleEngineCronJobModel> createUndeploySupplier(List<SourceRuleModel> paramList, String paramString);


    Supplier<RuleEngineCronJobModel> createSynchronizeSupplier(String paramString1, String paramString2);


    Supplier<RuleEngineCronJobModel> createModuleInitializationSupplier(String paramString);


    Supplier<RuleEngineCronJobModel> createAllModulesInitializationSupplier();
}
