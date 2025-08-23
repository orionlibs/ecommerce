package de.hybris.platform.ruleengine.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;

public class EngineRulePreconditions
{
    public static <T extends AbstractRuleEngineRuleModel> void checkRulesHaveSameType(Collection<T> engineRules)
    {
        if(CollectionUtils.isNotEmpty(engineRules))
        {
            AbstractRuleEngineRuleModel abstractRuleEngineRuleModel = engineRules.iterator().next();
            RuleType ruleType = abstractRuleEngineRuleModel.getRuleType();
            if(Objects.isNull(ruleType))
            {
                throw new IllegalStateException("RuleType of engine rule [" + abstractRuleEngineRuleModel.getCode() + "] is NULL");
            }
            if(!engineRules.stream().allMatch(r -> ruleType.equals(r.getRuleType())))
            {
                throw new IllegalStateException("One or more rules in the collection are having different rule types");
            }
        }
    }


    public static <T extends AbstractRuleEngineRuleModel> void checkRuleHasKieModule(T rule)
    {
        Preconditions.checkArgument(Objects.nonNull(rule), "Rule should not be null");
        Preconditions.checkArgument(rule instanceof DroolsRuleModel, "Rule must be instance of DroolsRuleModel");
        DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
        if(Objects.isNull(droolsRule.getKieBase()))
        {
            throw new IllegalStateException("Rule [" + droolsRule.getCode() + "] has no KieBase assigned to it");
        }
        if(Objects.isNull(droolsRule.getKieBase().getKieModule()))
        {
            throw new IllegalStateException("Rule [" + droolsRule.getCode() + "] has no KieModule assigned to it");
        }
    }
}
