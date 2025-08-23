package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingMethodResolver;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

public class SpelExpressionMetricFilter implements MetricFilter
{
    private final SpelExpression expression;


    public SpelExpressionMetricFilter(String expression)
    {
        Objects.requireNonNull(expression);
        if("".equals(expression))
        {
            this.expression = null;
        }
        else
        {
            this.expression = (new SpelExpressionParser()).parseRaw(expression);
            evaluateExpression("testMetricName");
        }
    }


    public boolean matches(String name, Metric metric)
    {
        if(this.expression != null)
        {
            return evaluateExpression(name);
        }
        return false;
    }


    private boolean evaluateExpression(String metricName)
    {
        SimpleEvaluationContext simpleEvaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().withMethodResolvers(new MethodResolver[] {(MethodResolver)DataBindingMethodResolver.forInstanceMethodInvocation()}).build();
        simpleEvaluationContext.setVariable("metric", metricName);
        return BooleanUtils.toBooleanDefaultIfNull((Boolean)this.expression.getValue((EvaluationContext)simpleEvaluationContext, Boolean.class), false);
    }
}
