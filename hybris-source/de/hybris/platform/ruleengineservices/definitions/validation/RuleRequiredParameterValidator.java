package de.hybris.platform.ruleengineservices.definitions.validation;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleParameterValidator;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleRequiredParameterValidator implements RuleParameterValidator
{
    protected static final String MESSAGE_KEY = "rule.validation.error.required";
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, AbstractRuleDefinitionData ruleDefinition, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        if(BooleanUtils.isFalse(parameterDefinition.getRequired()))
        {
            return;
        }
        if(isEmptyValue(parameter.getValue()))
        {
            context.addProblem((RuleCompilerProblem)this.ruleCompilerProblemFactory.createParameterProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.required", parameter, parameterDefinition, new Object[] {parameterDefinition
                            .getName(), ruleDefinition.getName()}));
        }
    }


    protected boolean isEmptyValue(Object value)
    {
        if(value == null)
        {
            return true;
        }
        if(value instanceof Map)
        {
            Map<?, ?> mapToValidate = (Map<?, ?>)value;
            return (MapUtils.isEmpty(mapToValidate) ||
                            Collections.frequency(mapToValidate.values(), null) > 0 ||
                            Collections.frequency(mapToValidate.keySet(), null) > 0);
        }
        if(value instanceof Collection)
        {
            return CollectionUtils.isEmpty((Collection)value);
        }
        if(value instanceof String)
        {
            return StringUtils.isBlank((String)value);
        }
        return false;
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
