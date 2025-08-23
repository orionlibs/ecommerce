package de.hybris.platform.ruledefinitions.validation;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Required;

public class RulePercentageParameterValidator implements RuleParameterValidator
{
    protected static final String MESSAGE_KEY = "rule.validation.error.percentage.invalid";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        BigDecimal percentage = (BigDecimal)parameter.getValue();
        if(percentage != null && (percentage.doubleValue() <= 0.0D || percentage.doubleValue() > 100.0D))
        {
            context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.percentage.invalid", parameter, parameterDefinition, new Object[] {parameterDefinition
                            .getName(), parameter.getUuid()}));
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
