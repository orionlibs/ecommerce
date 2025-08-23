package de.hybris.platform.ruleengineservices.rule.interceptors;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleTypeMappingException;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class SourceRuleTemplateValidateInterceptor implements ValidateInterceptor
{
    private static final String RULE_TYPE_ERROR_MSG = "exception.sourceruletemplatevalidateinterceptor.rule.type";
    private static final String CONDITIONS_ERROR_MSG = "exception.sourceruletemplatevalidateinterceptor.conditions";
    private static final String ACTIONS_ERROR_MSG = "exception.sourceruletemplatevalidateinterceptor.actions";
    private RuleConditionsService ruleConditionsService;
    private RuleConditionsRegistry ruleConditionsRegistry;
    private RuleActionsService ruleActionsService;
    private RuleActionsRegistry ruleActionsRegistry;
    private RuleService ruleService;
    private L10NService l10NService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SourceRuleTemplateModel)
        {
            Class<? extends AbstractRuleModel> ruleClass;
            SourceRuleTemplateModel sourceRuleTemplate = (SourceRuleTemplateModel)model;
            try
            {
                ruleClass = this.ruleService.getRuleTypeFromTemplate(sourceRuleTemplate.getClass());
            }
            catch(RuleTypeMappingException ce)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.sourceruletemplatevalidateinterceptor.rule.type", new Object[] {sourceRuleTemplate.getClass()}), ce);
            }
            try
            {
                Map<String, RuleConditionDefinitionData> conditionDefinitions = this.ruleConditionsRegistry.getConditionDefinitionsForRuleTypeAsMap(ruleClass);
                this.ruleConditionsService.convertConditionsFromString(sourceRuleTemplate.getConditions(), conditionDefinitions);
            }
            catch(RuleEngineServiceException ce)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.sourceruletemplatevalidateinterceptor.conditions", new Object[] {sourceRuleTemplate.getCode()}), ce);
            }
            try
            {
                Map<String, RuleActionDefinitionData> actionDefinitions = this.ruleActionsRegistry.getActionDefinitionsForRuleTypeAsMap(ruleClass);
                this.ruleActionsService.convertActionsFromString(sourceRuleTemplate.getActions(), actionDefinitions);
            }
            catch(RuleEngineServiceException ce)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.sourceruletemplatevalidateinterceptor.actions", new Object[] {sourceRuleTemplate.getCode()}), ce);
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


    public RuleConditionsRegistry getRuleConditionsRegistry()
    {
        return this.ruleConditionsRegistry;
    }


    @Required
    public void setRuleConditionsRegistry(RuleConditionsRegistry ruleConditionsRegistry)
    {
        this.ruleConditionsRegistry = ruleConditionsRegistry;
    }


    public RuleActionsRegistry getRuleActionsRegistry()
    {
        return this.ruleActionsRegistry;
    }


    @Required
    public void setRuleActionsRegistry(RuleActionsRegistry ruleActionsRegistry)
    {
        this.ruleActionsRegistry = ruleActionsRegistry;
    }


    public RuleService getRuleService()
    {
        return this.ruleService;
    }


    @Required
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
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
