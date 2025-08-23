package de.hybris.platform.personalizationservices.trigger.expression.impl;

import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionContext;

public class CxNegationExpression implements CxExpression
{
    private CxExpression element;


    public CxNegationExpression()
    {
    }


    public CxNegationExpression(CxExpression element)
    {
        this.element = element;
    }


    public CxExpression getElement()
    {
        return this.element;
    }


    public boolean evaluate(CxExpressionContext context)
    {
        if(getElement() != null)
        {
            return !this.element.evaluate(context);
        }
        return true;
    }
}
