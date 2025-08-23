package de.hybris.platform.personalizationservices.trigger.expression.impl;

import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionContext;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionEvaluator;

public class DefaultExpressionEvaluator implements CxExpressionEvaluator
{
    public boolean evaluate(CxExpression element, CxExpressionContext context)
    {
        return element.evaluate(context);
    }
}
