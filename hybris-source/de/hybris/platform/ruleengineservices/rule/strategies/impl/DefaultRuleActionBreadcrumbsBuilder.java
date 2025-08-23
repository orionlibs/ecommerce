package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleActionBreadcrumbsBuilder;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionBreadcrumbsBuilder extends AbstractRuleBreadcrumbsBuilder implements RuleActionBreadcrumbsBuilder
{
    protected static final String DEFAULT_SEPARATOR = ", ";
    private I18NService i18NService;


    public String buildActionBreadcrumbs(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return buildBreadcrumbs(actions, actionDefinitions, false);
    }


    public String buildStyledActionBreadcrumbs(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return buildBreadcrumbs(actions, actionDefinitions, true);
    }


    protected String buildBreadcrumbs(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions, boolean styled)
    {
        ServicesUtil.validateParameterNotNull(actions, "actions cannot be null");
        ServicesUtil.validateParameterNotNull(actionDefinitions, "action definitions cannot be null");
        StringBuilder breadcrumbBuilder = new StringBuilder();
        Locale locale = this.i18NService.getCurrentLocale();
        String separator = buildSeparator(styled);
        int index = 0;
        for(RuleActionData action : actions)
        {
            RuleActionDefinitionData actionDefinition = actionDefinitions.get(action.getDefinitionId());
            if(actionDefinition == null)
            {
                throw new RuleEngineServiceException("No action definition found for id " + action.getDefinitionId());
            }
            if(StringUtils.isNotEmpty(actionDefinition.getBreadcrumb()))
            {
                if(index != 0)
                {
                    breadcrumbBuilder.append(separator);
                }
                String breadcrumb = formatBreadcrumb(actionDefinition.getBreadcrumb(), action.getParameters(), locale, styled, true);
                breadcrumbBuilder.append(breadcrumb);
                index++;
            }
        }
        return breadcrumbBuilder.toString();
    }


    protected String buildSeparator(boolean styled)
    {
        return decorateValue(", ", "rule-separator", styled);
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
