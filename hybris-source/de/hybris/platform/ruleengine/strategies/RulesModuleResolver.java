package de.hybris.platform.ruleengine.strategies;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.List;

public interface RulesModuleResolver
{
    String lookupForModuleName(RuleType paramRuleType);


    <T extends de.hybris.platform.ruleengine.model.AbstractRulesModuleModel> T lookupForRulesModule(RuleType paramRuleType);


    <T extends de.hybris.platform.ruleengine.model.AbstractRulesModuleModel> List<T> lookupForRulesModules(AbstractRuleModel paramAbstractRuleModel);
}
