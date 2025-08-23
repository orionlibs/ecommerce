package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.io.Serializable;

@FunctionalInterface
public interface RuleMessageParameterDecorator extends Serializable
{
    String decorate(String paramString, RuleParameterData paramRuleParameterData);
}
