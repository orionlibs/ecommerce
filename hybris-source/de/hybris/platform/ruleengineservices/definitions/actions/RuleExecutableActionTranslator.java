package de.hybris.platform.ruleengineservices.definitions.actions;

import com.google.common.collect.Maps;
import de.hybris.platform.ruleengineservices.compiler.RuleActionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleActionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNoOpAction;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleExecutableAction;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RuleExecutableActionTranslator implements RuleActionTranslator, RuleActionValidator, ApplicationContextAware
{
    public static final String ACTION_ID_PARAM = "actionId";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;
    private ApplicationContext applicationContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExecutableActionTranslator.class);


    public void validate(RuleCompilerContext context, RuleActionData action, RuleActionDefinitionData actionDefinition)
    {
        String actionId = (String)actionDefinition.getTranslatorParameters().get("actionId");
        if(StringUtils.isBlank(actionId))
        {
            RuleCompilerProblem ruleCompilerProblem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.executableaction.actionid.empty", new Object[0]);
            context.addProblem(ruleCompilerProblem);
        }
        RuleExecutableAction ruleExecutableAction = null;
        try
        {
            ruleExecutableAction = getRuleExecutableAction(actionId);
        }
        catch(RuleCompilerException e)
        {
            RuleCompilerProblem ruleCompilerProblem = this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.executableaction.beanid.invalid", new Object[0]);
            context.addProblem(ruleCompilerProblem);
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Rule Compiler Error Executable Action Bean Id Invalid", (Throwable)e);
            }
        }
        if(ruleExecutableAction instanceof RuleActionValidator)
        {
            ((RuleActionValidator)ruleExecutableAction).validate(context, action, actionDefinition);
        }
    }


    public RuleIrAction translate(RuleCompilerContext context, RuleActionData action, RuleActionDefinitionData actionDefinition)
    {
        String actionId = (String)actionDefinition.getTranslatorParameters().get("actionId");
        if(StringUtils.isBlank(actionId))
        {
            return (RuleIrAction)new RuleIrNoOpAction();
        }
        Map<String, Object> actionParameters = Maps.newHashMap();
        if(MapUtils.isNotEmpty(action.getParameters()))
        {
            for(Map.Entry<String, RuleParameterData> entry : (Iterable<Map.Entry<String, RuleParameterData>>)action.getParameters().entrySet())
            {
                String parameterId = entry.getKey();
                if(entry.getValue() != null)
                {
                    actionParameters.put(parameterId, ((RuleParameterData)entry.getValue()).getValue());
                    actionParameters.put(parameterId + "_uuid", ((RuleParameterData)entry.getValue()).getUuid());
                }
            }
        }
        RuleIrExecutableAction irExecutableAction = new RuleIrExecutableAction();
        irExecutableAction.setActionId(actionId);
        irExecutableAction.setActionParameters(actionParameters);
        return (RuleIrAction)irExecutableAction;
    }


    protected RuleExecutableAction getRuleExecutableAction(String actionId)
    {
        try
        {
            return (RuleExecutableAction)this.applicationContext.getBean(actionId, RuleExecutableAction.class);
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
