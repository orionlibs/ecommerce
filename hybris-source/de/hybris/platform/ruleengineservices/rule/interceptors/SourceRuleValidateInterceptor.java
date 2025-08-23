package de.hybris.platform.ruleengineservices.rule.interceptors;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class SourceRuleValidateInterceptor implements ValidateInterceptor
{
    private static final String CONDITIONS_ERROR_MSG = "exception.sourcerulevalidateinterceptor.conditions";
    private static final String ACTIONS_ERROR_MSG = "exception.sourcerulevalidateinterceptor.actions";
    private RuleConditionsService ruleConditionsService;
    private RuleConditionsRegistry ruleConditionsRegistry;
    private RuleActionsService ruleActionsService;
    private RuleActionsRegistry ruleActionsRegistry;
    private L10NService l10NService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SourceRuleModel)
        {
            SourceRuleModel sourceRule = (SourceRuleModel)model;
            try
            {
                Map<String, RuleConditionDefinitionData> conditionDefinitions = getRuleConditionsRegistry().getConditionDefinitionsForRuleTypeAsMap(sourceRule.getClass());
                getRuleConditionsService().convertConditionsFromString(sourceRule.getConditions(), conditionDefinitions);
            }
            catch(RuleEngineServiceException ce)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.sourcerulevalidateinterceptor.conditions", new Object[] {sourceRule.getCode()}), ce);
            }
            try
            {
                Map<String, RuleActionDefinitionData> actionDefinitions = getRuleActionsRegistry().getActionDefinitionsForRuleTypeAsMap(sourceRule.getClass());
                getRuleActionsService().convertActionsFromString(sourceRule.getActions(), actionDefinitions);
            }
            catch(RuleEngineServiceException ce)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.sourcerulevalidateinterceptor.actions", new Object[] {sourceRule.getCode()}), ce);
            }
        }
    }


    public RuleConditionsService getRuleConditionsService()
    {
        return this.ruleConditionsService;
    }


    @Required
    public void setRuleConditionsService(RuleConditionsService ruleConditionsService)
    {
        this.ruleConditionsService = ruleConditionsService;
    }


    public RuleActionsService getRuleActionsService()
    {
        return this.ruleActionsService;
    }


    @Required
    public void setRuleActionsService(RuleActionsService ruleActionsService)
    {
        this.ruleActionsService = ruleActionsService;
    }


    protected RuleConditionsRegistry getRuleConditionsRegistry()
    {
        return this.ruleConditionsRegistry;
    }


    @Required
    public void setRuleConditionsRegistry(RuleConditionsRegistry ruleConditionsRegistry)
    {
        this.ruleConditionsRegistry = ruleConditionsRegistry;
    }


    protected RuleActionsRegistry getRuleActionsRegistry()
    {
        return this.ruleActionsRegistry;
    }


    @Required
    public void setRuleActionsRegistry(RuleActionsRegistry ruleActionsRegistry)
    {
        this.ruleActionsRegistry = ruleActionsRegistry;
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
