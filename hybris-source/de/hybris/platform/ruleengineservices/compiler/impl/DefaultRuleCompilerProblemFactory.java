package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerParameterProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.servicelayer.i18n.L10NService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleCompilerProblemFactory implements RuleCompilerProblemFactory
{
    private L10NService l10nService;


    public RuleCompilerProblem createProblem(RuleCompilerProblem.Severity severity, String messageKey, Object... parameters)
    {
        return (RuleCompilerProblem)new DefaultRuleCompilerProblem(severity, this.l10nService.getLocalizedString(messageKey, parameters));
    }


    public RuleCompilerParameterProblem createParameterProblem(RuleCompilerProblem.Severity severity, String messageKey, RuleParameterData parameterData, RuleParameterDefinitionData parameterDefinitionData, Object... parameters)
    {
        return (RuleCompilerParameterProblem)new DefaultRuleCompilerParameterProblem(severity, this.l10nService.getLocalizedString(messageKey, parameters), parameterData, parameterDefinitionData);
    }


    public L10NService getL10nService()
    {
        return this.l10nService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }
}
