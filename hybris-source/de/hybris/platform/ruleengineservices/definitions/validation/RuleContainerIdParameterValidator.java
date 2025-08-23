package de.hybris.platform.ruleengineservices.definitions.validation;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleContainerIdParameterValidator implements RuleParameterValidator
{
    protected static final String MESSAGE_KEY = "rule.validation.error.containerid.invalid";
    protected static final Pattern CONTAINER_ID_PATTERN = Pattern.compile("[a-zA-Z0-9_-]*$");
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        if(parameter == null || StringUtils.isBlank((String)parameter.getValue()))
        {
            return;
        }
        String containerID = (String)parameter.getValue();
        if(!CONTAINER_ID_PATTERN.matcher(containerID).matches())
        {
            context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.containerid.invalid", parameter, parameterDefinition, new Object[] {containerID, parameterDefinition
                            .getName()}));
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
}
