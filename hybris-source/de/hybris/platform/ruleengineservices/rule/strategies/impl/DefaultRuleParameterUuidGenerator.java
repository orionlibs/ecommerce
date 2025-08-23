package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterUuidGenerator;
import java.util.UUID;

public class DefaultRuleParameterUuidGenerator implements RuleParameterUuidGenerator
{
    public String generateUuid(RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
