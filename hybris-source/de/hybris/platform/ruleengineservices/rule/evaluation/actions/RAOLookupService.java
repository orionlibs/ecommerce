package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface RAOLookupService
{
    <T> Optional<T> lookupRAOByType(Class<T> paramClass, RuleActionContext paramRuleActionContext, Predicate<T>... paramVarArgs);


    <T> List<T> lookupRAOObjectsByType(Class<T> paramClass, RuleActionContext paramRuleActionContext, Predicate<T>... paramVarArgs);
}
