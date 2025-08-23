package de.hybris.platform.ruleengineservices.compiler.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultRuleConditionsTranslator implements RuleConditionsTranslator, ApplicationContextAware
{
    public static final String CONDITION_DEFINITIONS_ATTRIBUTE = "conditionDefinitions";
    public static final String MANDATORY_PARAMETER_VALIDATOR = "ruleRequiredParameterValidator";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;
    private ApplicationContext applicationContext;


    public void validate(RuleCompilerContext context, List<RuleConditionData> conditions)
    {
        Map<String, RuleConditionDefinitionData> conditionDefinitions = context.getConditionDefinitions();
        for(RuleConditionData condition : conditions)
        {
            RuleConditionDefinitionData conditionDefinition = conditionDefinitions.get(condition.getDefinitionId());
            if(conditionDefinition == null)
            {
                RuleCompilerProblem problem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.conditionstranslator.condition.definition.empty", new Object[] {condition
                                .getDefinitionId()});
                context.addProblem(problem);
                continue;
            }
            validateParameters(context, conditionDefinition, condition.getParameters(), conditionDefinition.getParameters());
            RuleConditionTranslator conditionTranslator = getConditionTranslator(conditionDefinition.getTranslatorId());
            if(conditionTranslator instanceof RuleConditionValidator)
            {
                ((RuleConditionValidator)conditionTranslator).validate(context, condition, conditionDefinition);
            }
        }
    }


    protected void validateParameters(RuleCompilerContext context, RuleConditionDefinitionData ruleDefinition, Map<String, RuleParameterData> parameters, Map<String, RuleParameterDefinitionData> parameterDefinitions)
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


    public synchronized List<RuleIrCondition> translate(RuleCompilerContext context, List<RuleConditionData> conditions)
    {
        Map<String, RuleConditionDefinitionData> conditionDefinitions = context.getConditionDefinitions();
        List<RuleIrCondition> ruleIrConditions = Lists.newArrayList();
        for(RuleConditionData condition : conditions)
        {
            RuleConditionDefinitionData conditionDefinition = conditionDefinitions.get(condition.getDefinitionId());
            if(conditionDefinition != null)
            {
                RuleConditionTranslator conditionTranslator = getConditionTranslator(conditionDefinition.getTranslatorId());
                RuleIrCondition ruleIrCondition = conditionTranslator.translate(context, condition, conditionDefinition);
                ruleIrConditions.add(ruleIrCondition);
            }
        }
        return ruleIrConditions;
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


    protected RuleConditionTranslator getConditionTranslator(String translatorId)
    {
        try
        {
            return (RuleConditionTranslator)this.applicationContext.getBean(translatorId, RuleConditionTranslator.class);
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
