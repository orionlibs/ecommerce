package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;

public interface RuleParametersConverter
{
    String toString(List<RuleParameterData> paramList);


    List<RuleParameterData> fromString(String paramString);
}
