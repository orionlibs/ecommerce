package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleActionBreadcrumbsBuilder
{
    String buildActionBreadcrumbs(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);


    String buildStyledActionBreadcrumbs(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);
}
