package de.hybris.platform.ruleengine;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import org.mockito.Mockito;

public class EngineRulesBuilder
{
    public static DroolsRuleModel newDroolsRule(DroolsKIEBaseModel kieBase, String code)
    {
        DroolsRuleModel droolsRule = (DroolsRuleModel)Mockito.mock(DroolsRuleModel.class);
        Mockito.lenient().when(droolsRule.getKieBase()).thenReturn(kieBase);
        Mockito.lenient().when(droolsRule.getRuleType()).thenReturn(RuleType.DEFAULT);
        Mockito.lenient().when(droolsRule.getCode()).thenReturn(code);
        Mockito.lenient().when(droolsRule.getActive()).thenReturn(Boolean.TRUE);
        Mockito.lenient().when(droolsRule.getCurrentVersion()).thenReturn(Boolean.TRUE);
        return droolsRule;
    }


    public static AbstractRuleEngineRuleModel newAbstractRule(String code)
    {
        AbstractRuleEngineRuleModel rule = (AbstractRuleEngineRuleModel)Mockito.mock(AbstractRuleEngineRuleModel.class);
        Mockito.lenient().when(rule.getCode()).thenReturn(code);
        Mockito.lenient().when(rule.getRuleType()).thenReturn(RuleType.DEFAULT);
        Mockito.lenient().when(rule.getActive()).thenReturn(Boolean.TRUE);
        Mockito.lenient().when(rule.getCurrentVersion()).thenReturn(Boolean.TRUE);
        return rule;
    }


    public static DroolsKIEBaseModel newKieBase(String moduleName)
    {
        DroolsKIEBaseModel kieBase = (DroolsKIEBaseModel)Mockito.mock(DroolsKIEBaseModel.class);
        DroolsKIEModuleModel kieModule = (DroolsKIEModuleModel)Mockito.mock(DroolsKIEModuleModel.class);
        Mockito.lenient().when(kieModule.getName()).thenReturn(moduleName);
        Mockito.lenient().when(kieBase.getKieModule()).thenReturn(kieModule);
        return kieBase;
    }
}
