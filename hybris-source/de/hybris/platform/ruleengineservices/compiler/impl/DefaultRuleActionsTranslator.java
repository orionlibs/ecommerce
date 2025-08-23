package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleActionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleActionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleActionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleActionsTranslator implements RuleActionsTranslator, ApplicationContextAware
{
    public static final String ACTION_DEFINITIONS_ATTRIBUTE = "actionDefinitions";
    public static final String MANDATORY_PARAMETER_VALIDATOR = "ruleRequiredParameterValidator";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;
    private ApplicationContext applicationContext;


    public void validate(RuleCompilerContext context, List<RuleActionData> actions)
    {
        Map<String, RuleActionDefinitionData> actionDefinitions = context.getActionDefinitions();
        for(RuleActionData action : actions)
        {
            RuleActionDefinitionData actionDefinition = actionDefinitions.get(action.getDefinitionId());
            if(actionDefinition == null)
            {
                RuleCompilerProblem problem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.actionstranslator.action.definition.empty", new Object[] {action
                                .getDefinitionId()});
                context.addProblem(problem);
                continue;
            }
            validateParameters(context, actionDefinition, action.getParameters(), actionDefinition.getParameters());
            RuleActionTranslator actionTranslator = getActionTranslator(actionDefinition.getTranslatorId());
            if(actionTranslator instanceof RuleActionValidator)
            {
                ((RuleActionValidator)actionTranslator).validate(context, action, actionDefinition);
            }
        }
    }


    protected void validateParameters(RuleCompilerContext context, RuleActionDefinitionData ruleDefinition, Map<String, RuleParameterData> parameters, Map<String, RuleParameterDefinitionData> parameterDefinitions)
    {
        for(Map.Entry<String, RuleParameterData> entry : parameters.entrySet())
        {
            String parameterId = entry.getKey();
            RuleParameterDefinitionData parameterDefinition = parameterDefinitions.get(parameterId);
            List<String> validatorIds = new ArrayList<>();
            validatorIds.add("ruleRequiredParameterValidator");
            validatorIds.addAll(parameterDefinition.getValidators());
            for(String validatorId : validatorIds)
            {
                try
                {
                    getParameterValidator(validatorId).validate(context, (AbstractRuleDefinitionData)ruleDefinition, entry.getValue(), parameterDefinition);
                }
                catch(RuleEngineServiceException e)
                {
                    throw new RuleCompilerException(e);
                }
            }
        }
    }


    public List<RuleIrAction> translate(RuleCompilerContext context, List<RuleActionData> actions)
    {
        Map<String, RuleActionDefinitionData> actionDefinitions = context.getActionDefinitions();
        List<RuleIrAction> ruleIrActions = new ArrayList<>();
        for(RuleActionData action : actions)
        {
            RuleActionDefinitionData actionDefinition = actionDefinitions.get(action.getDefinitionId());
            if(actionDefinition != null)
            {
                RuleActionTranslator actionTranslator = getActionTranslator(actionDefinition.getTranslatorId());
                RuleIrAction ruleIrAction = actionTranslator.translate(context, action, actionDefinition);
                ruleIrActions.add(ruleIrAction);
            }
        }
        return ruleIrActions;
    }


    protected RuleParameterValidator getParameterValidator(String validatorId)
    {
        try
        {
            return (RuleParameterValidator)this.applicationContext.getBean(validatorId, RuleParameterValidator.class);
        }
        catch(BeansException e)
        {
            throw new RuleCompilerException(e);
        }
    }


    protected RuleActionTranslator getActionTranslator(String translatorId)
    {
        try
        {
            return (RuleActionTranslator)this.applicationContext.getBean(translatorId, RuleActionTranslator.class);
        }
        catch(BeansException e)
        {
            throw new RuleCompilerException(e);
        }
    }


    public RuleCompilerProblemFactory getRuleCompilerProblemFactory()
    {
        return this.ruleCompilerProblemFactory;
    }


    @Required
    public void setRuleCompilerProblemFactory(RuleCompilerProblemFactory ruleCompilerProblemFactory)
    {
        this.ruleCompilerProblemFactory = ruleCompilerProblemFactory;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
