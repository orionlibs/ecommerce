package de.hybris.platform.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

public final class ExpressionUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ExpressionUtils.class);


    public static Integer evaluateToInteger(String expression, Map<String, Object> variables)
    {
        Object value;
        try
        {
            value = evaluate(expression, variables);
        }
        catch(SystemException | ArithmeticException exception)
        {
            LOG.warn("Exception has been thrown during evaluation of '{}'. Null will be returned.", expression, exception);
            return null;
        }
        if(value instanceof Integer)
        {
            return (Integer)value;
        }
        if(value instanceof Number)
        {
            return Integer.valueOf(((Number)value).intValue());
        }
        return null;
    }


    public static Object evaluate(String expression, Map<String, Object> variables)
    {
        Preconditions.checkArgument(Strings.isNotBlank(expression), "expression mustn't be blank.");
        Preconditions.checkNotNull(variables, "variables mustn't be empty");
        SimpleEvaluationContext simpleEvaluationContext = (new SimpleEvaluationContext.Builder(new org.springframework.expression.PropertyAccessor[0])).build();
        Objects.requireNonNull(simpleEvaluationContext);
        variables.forEach(simpleEvaluationContext::setVariable);
        return evaluate(expression, (EvaluationContext)simpleEvaluationContext);
    }


    public static Object evaluate(String expression, EvaluationContext evaluationContext)
    {
        Preconditions.checkArgument(Strings.isNotBlank(expression), "expression mustn't be blank.");
        Preconditions.checkNotNull(evaluationContext, "evaluationContext mustn't be null.");
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expr = spelExpressionParser.parseExpression(expression);
        try
        {
            return expr.getValue(evaluationContext);
        }
        catch(SpelEvaluationException e)
        {
            throw new SystemException(e);
        }
    }
}
