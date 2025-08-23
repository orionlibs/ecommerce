package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Locale;
import java.util.Map;

public interface RuleMessageFormatStrategy
{
    String format(String paramString, Map<String, RuleParameterData> paramMap, Locale paramLocale);


    String format(String paramString, Map<String, RuleParameterData> paramMap, Locale paramLocale, RuleMessageParameterDecorator paramRuleMessageParameterDecorator);
}
