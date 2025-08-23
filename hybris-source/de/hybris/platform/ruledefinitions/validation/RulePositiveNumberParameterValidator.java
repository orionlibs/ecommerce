package de.hybris.platform.ruledefinitions.validation;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class RulePositiveNumberParameterValidator implements RuleParameterValidator
{
    protected static final String MESSAGE_KEY = "rule.validation.error.negative.quantity";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        Object parameterValue = parameter.getValue();
        if(parameterValue instanceof Number)
        {
            Number number = (Number)parameter.getValue();
            validatePositiveNumber(context, ruleDefinition, parameter, parameterDefinition, number);
        }
        else if(parameterValue instanceof Collection)
        {
            validatePositiveCollectionValue(context, ruleDefinition, parameter, parameterDefinition);
        }
        else if(parameterValue instanceof Map)
        {
            validatePositiveMapValue(context, ruleDefinition, parameter, parameterDefinition);
        }
    }


    protected void validatePositiveNumber(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition, Number number)
    {
        if(checkIsNegativeNumber(number))
        {
            context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.negative.quantity", parameter, parameterDefinition, new Object[] {parameterDefinition
                            .getName(), parameter.getUuid(), ruleDefinition.getName()}));
        }
    }


    protected void validatePositiveMapValue(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        Map<?, Number> mapValue = (Map<?, Number>)parameter.getValue();
        for(Map.Entry<?, Number> entry : mapValue.entrySet())
        {
            Number number = entry.getValue();
            validatePositiveNumber(context, ruleDefinition, parameter, parameterDefinition, number);
        }
    }


    protected void validatePositiveCollectionValue(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        Collection<Number> collectionValue = (Collection<Number>)parameter.getValue();
        for(Number number : collectionValue)
        {
            validatePositiveNumber(context, ruleDefinition, parameter, parameterDefinition, number);
        }
    }


    protected boolean checkIsNegativeNumber(Number number)
    {
        if(Objects.isNull(number) || !(number instanceof Comparable))
        {
            return true;
        }
        return (((Comparable<Number>)number).compareTo(ZeroNumberFactory.newInstance(number.getClass())) < 0);
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
