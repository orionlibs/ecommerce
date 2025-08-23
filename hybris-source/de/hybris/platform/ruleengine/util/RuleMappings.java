package de.hybris.platform.ruleengine.util;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;

public class RuleMappings
{
    public static <R extends de.hybris.platform.ruleengine.model.DroolsRuleModel, M extends AbstractRulesModuleModel> M module(R rule)
    {
        EngineRulePreconditions.checkRuleHasKieModule((AbstractRuleEngineRuleModel)rule);
        return (M)rule.getKieBase().getKieModule();
    }


    public static <T extends de.hybris.platform.ruleengine.model.DroolsRuleModel> String moduleName(T rule)
    {
        return module(rule).getName();
    }
}
