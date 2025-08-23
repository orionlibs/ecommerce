package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.List;

public interface RuleParametersService
{
    RuleParameterData createParameterFromDefinition(RuleParameterDefinitionData paramRuleParameterDefinitionData);


    String convertParametersToString(List<RuleParameterData> paramList);


    List<RuleParameterData> convertParametersFromString(String paramString);
}
