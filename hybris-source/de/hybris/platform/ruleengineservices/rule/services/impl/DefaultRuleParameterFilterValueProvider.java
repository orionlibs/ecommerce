package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.rule.services.RuleParameterFilterValueProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

public class DefaultRuleParameterFilterValueProvider implements RuleParameterFilterValueProvider
{
    private static final String SEPARATOR = "#";
    private ExpressionParser parser;


    public String getParameterId(String value)
    {
        return StringUtils.substringBefore(value, "#");
    }


    public Object evaluate(String value, Object contextObject)
    {
        if(!value.contains("#"))
        {
            return value;
        }
        String expression = StringUtils.substringAfter(value, "#");
        Expression parsedExpression = getParser().parseExpression(expression);
        SimpleEvaluationContext evaluationContext = SimpleEvaluationContext.forReadWriteDataBinding().withInstanceMethods().withRootObject(contextObject).build();
        return parsedExpression.getValue((EvaluationContext)evaluationContext);
    }


    protected ExpressionParser getParser()
    {
        return this.parser;
    }


    @Required
    public void setParser(ExpressionParser parser)
    {
        this.parser = parser;
    }
}
