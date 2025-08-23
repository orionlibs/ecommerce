package de.hybris.platform.ruleengineservices.definitions.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleExecutableCondition;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RuleExecutableConditionTranslator implements RuleConditionTranslator, RuleConditionValidator, ApplicationContextAware
{
    public static final String CONDITION_ID_PARAM = "conditionId";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;
    private ApplicationContext applicationContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExecutableConditionTranslator.class);


    public void validate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        String conditionId = (String)conditionDefinition.getTranslatorParameters().get("conditionId");
        if(StringUtils.isBlank(conditionId))
        {
            RuleCompilerProblem ruleCompilerProblem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.executablecondition.conditionid.empty", new Object[0]);
            context.addProblem(ruleCompilerProblem);
        }
        RuleExecutableCondition ruleExecutableCondition = null;
        try
        {
            ruleExecutableCondition = getRuleExecutableCondition(conditionId);
        }
        catch(RuleCompilerException e)
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Exception while compiling rule", (Throwable)e);
            }
            RuleCompilerProblem ruleCompilerProblem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.executablecondition.beanid.invalid", new Object[0]);
            context.addProblem(ruleCompilerProblem);
        }
        if(ruleExecutableCondition instanceof RuleConditionValidator)
        {
            ((RuleConditionValidator)ruleExecutableCondition).validate(context, condition, conditionDefinition);
        }
    }


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        String conditionId = (String)conditionDefinition.getTranslatorParameters().get("conditionId");
        if(conditionId == null)
        {
            return (RuleIrCondition)new RuleIrEmptyCondition();
        }
        Map<String, Object> conditionParameters = null;
        if(MapUtils.isNotEmpty(condition.getParameters()))
        {
            conditionParameters = new HashMap<>();
            for(Map.Entry<String, RuleParameterData> entry : (Iterable<Map.Entry<String, RuleParameterData>>)condition.getParameters().entrySet())
            {
                String parameterId = entry.getKey();
                Object parameterValue = (entry.getValue() == null) ? null : ((RuleParameterData)entry.getValue()).getValue();
                conditionParameters.put(parameterId, parameterValue);
            }
        }
        RuleIrExecutableCondition irExecutableCondition = new RuleIrExecutableCondition();
        irExecutableCondition.setConditionId(conditionId);
        irExecutableCondition.setConditionParameters(conditionParameters);
        return (RuleIrCondition)irExecutableCondition;
    }


    protected RuleExecutableCondition getRuleExecutableCondition(String conditionId)
    {
        try
        {
            return (RuleExecutableCondition)this.applicationContext.getBean(conditionId, RuleExecutableCondition.class);
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
