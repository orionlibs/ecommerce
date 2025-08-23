package de.hybris.platform.ruleengine.test;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface RuleEngineTestSupportService
{
    AbstractRuleEngineRuleModel createRuleModel();


    AbstractRulesModuleModel associateRulesToNewModule(String paramString, Set<? extends AbstractRuleEngineRuleModel> paramSet);


    void associateRulesModule(AbstractRulesModuleModel paramAbstractRulesModuleModel, Set<? extends AbstractRuleEngineRuleModel> paramSet);


    AbstractRulesModuleModel getTestRulesModule(AbstractRuleEngineContextModel paramAbstractRuleEngineContextModel, Set<AbstractRuleEngineRuleModel> paramSet);


    Optional<AbstractRulesModuleModel> resolveAssociatedRuleModule(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel);


    Consumer<AbstractRuleEngineRuleModel> decorateRuleForTest(Map<String, String> paramMap);


    String getTestModuleName(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel);
}
