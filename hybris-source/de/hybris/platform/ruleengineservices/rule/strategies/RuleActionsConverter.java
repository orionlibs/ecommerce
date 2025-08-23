package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleActionsConverter
{
    String toString(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);


    List<RuleActionData> fromString(String paramString, Map<String, RuleActionDefinitionData> paramMap);
}
