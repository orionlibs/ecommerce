package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleActionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatter;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatterException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleExecutableAction;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultDroolsRuleActionsGenerator implements DroolsRuleActionsGenerator
{
    private static final String EXECUTABLE_ACTION_METHOD = "executeAction";
    private static final String VARIABLES_PARAM = "variables";
    private static final String DROOLS_CONTEXT_PARAM = "kcontext";
    private DroolsRuleValueFormatter droolsRuleValueFormatter;
    private DroolsStringUtils droolsStringUtils;


    public String generateActions(DroolsRuleGeneratorContext context, String indentation)
    {
        StringBuilder actionsBuffer = new StringBuilder();
        generateVariables(context, indentation, actionsBuffer);
        List<RuleIrAction> actions = context.getRuleIr().getActions();
        if(CollectionUtils.isEmpty(actions))
        {
            actions = Collections.emptyList();
        }
        for(RuleIrAction action : actions)
        {
            if(action instanceof RuleIrExecutableAction)
            {
                try
                {
                    generateExecutableAction(context, (RuleIrExecutableAction)action, indentation, actionsBuffer);
                }
                catch(DroolsRuleValueFormatterException e)
                {
                    throw new RuleCompilerException(e);
                }
                continue;
            }
            if(!(action instanceof de.hybris.platform.ruleengineservices.compiler.RuleIrNoOpAction))
            {
                throw new RuleCompilerException("Not supported RuleIrAction");
            }
        }
        actionsBuffer.append(indentation).append("$executionTracker.trackRuleExecution(kcontext);\n");
        return actionsBuffer.toString();
    }


    protected void generateVariables(DroolsRuleGeneratorContext context, String indentation, StringBuilder actionsBuffer)
    {
        String mapClassName = getDroolsStringUtils().validateFQCN(context.generateClassName(Map.class));
        actionsBuffer.append(indentation).append(mapClassName).append(' ').append("variables").append(" = [\n");
        String variableIndentation = getDroolsStringUtils().validateIndentation(indentation + indentation);
        Map<String, RuleIrVariable> variables = context.getVariables();
        int remainingVariables = variables.size();
        for(RuleIrVariable variable : variables.values())
        {
            String variableClassName = getDroolsStringUtils().encodeMvelStringLiteral(getDroolsStringUtils().validateFQCN(variable.getType().getName()));
            actionsBuffer.append(variableIndentation);
            actionsBuffer.append('"');
            String[] path = variable.getPath();
            if(path != null && path.length > 0)
            {
                for(String groupId : variable.getPath())
                {
                    actionsBuffer.append(getDroolsStringUtils().encodeMvelStringLiteral(groupId));
                    actionsBuffer.append("/");
                }
            }
            actionsBuffer.append(variableClassName);
            actionsBuffer.append("\" : ");
            actionsBuffer.append(getDroolsStringUtils().validateVariableName(context.getVariablePrefix() + context.getVariablePrefix()));
            actionsBuffer.append("_set");
            if(remainingVariables > 1)
            {
                actionsBuffer.append(',');
            }
            actionsBuffer.append('\n');
            remainingVariables--;
        }
        actionsBuffer.append(indentation).append("];\n");
    }


    protected void generateExecutableAction(DroolsRuleGeneratorContext context, RuleIrExecutableAction ruleIrAction, String indentation, StringBuilder actionsBuffer)
    {
        context.addGlobal(getDroolsStringUtils().validateSpringBeanName(ruleIrAction.getActionId()), RuleExecutableAction.class);
        String actionContextClassName = getDroolsStringUtils().validateFQCN(context.generateClassName(DefaultDroolsRuleActionContext.class));
        actionsBuffer.append(indentation);
        actionsBuffer.append(getDroolsStringUtils().validateSpringBeanName(ruleIrAction.getActionId())).append('.').append("executeAction");
        actionsBuffer.append('(');
        actionsBuffer.append("new ").append(actionContextClassName).append('(').append("variables").append(", ")
                        .append("kcontext").append(')');
        actionsBuffer.append(", ");
        Map<String, Object> actionParameters = ruleIrAction.getActionParameters();
        actionsBuffer.append(getDroolsRuleValueFormatter().formatValue(context, actionParameters));
        actionsBuffer.append(");\n");
    }


    public DroolsRuleValueFormatter getDroolsRuleValueFormatter()
    {
        return this.droolsRuleValueFormatter;
    }


    @Required
    public void setDroolsRuleValueFormatter(DroolsRuleValueFormatter droolsRuleValueFormatter)
    {
        this.droolsRuleValueFormatter = droolsRuleValueFormatter;
    }


    protected DroolsStringUtils getDroolsStringUtils()
    {
        return this.droolsStringUtils;
    }


    public void setDroolsStringUtils(DroolsStringUtils droolsStringUtils)
    {
        this.droolsStringUtils = droolsStringUtils;
    }
}
