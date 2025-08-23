package de.hybris.platform.ruleengine;

import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RuleEngineService
{
    RuleEvaluationResult evaluate(RuleEvaluationContext paramRuleEvaluationContext);


    <M extends AbstractRulesModuleModel> void initializeNonBlocking(M paramM, String paramString, boolean paramBoolean1, boolean paramBoolean2, RuleEngineActionResult paramRuleEngineActionResult);


    InitializationFuture initialize(List<AbstractRulesModuleModel> paramList, boolean paramBoolean1, boolean paramBoolean2);


    InitializationFuture initialize(List<AbstractRulesModuleModel> paramList, boolean paramBoolean1, boolean paramBoolean2, ExecutionContext paramExecutionContext);


    void initialize(AbstractRulesModuleModel paramAbstractRulesModuleModel, String paramString, boolean paramBoolean1, boolean paramBoolean2, RuleEngineActionResult paramRuleEngineActionResult);


    void initialize(AbstractRulesModuleModel paramAbstractRulesModuleModel, String paramString, boolean paramBoolean1, boolean paramBoolean2, RuleEngineActionResult paramRuleEngineActionResult, ExecutionContext paramExecutionContext);


    List<RuleEngineActionResult> initializeAllRulesModules();


    List<RuleEngineActionResult> initializeAllRulesModules(boolean paramBoolean);


    RuleEngineActionResult updateEngineRule(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel, AbstractRulesModuleModel paramAbstractRulesModuleModel);


    <T extends de.hybris.platform.ruleengine.model.DroolsRuleModel> Optional<InitializationFuture> archiveRules(Collection<T> paramCollection);


    AbstractRuleEngineRuleModel getRuleForCodeAndModule(String paramString1, String paramString2);


    AbstractRuleEngineRuleModel getRuleForUuid(String paramString);


    <T extends AbstractRuleEngineRuleModel> void deactivateRulesModuleEngineRules(String paramString, Collection<T> paramCollection);
}
