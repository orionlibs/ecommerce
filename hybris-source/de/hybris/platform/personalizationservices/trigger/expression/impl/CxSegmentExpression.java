package de.hybris.platform.personalizationservices.trigger.expression.impl;

import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionContext;

public class CxSegmentExpression implements CxExpression
{
    private String code;


    public CxSegmentExpression()
    {
        this.code = "";
    }


    public CxSegmentExpression(String code)
    {
        this.code = code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public boolean evaluate(CxExpressionContext context)
    {
        return (context.getSegments() != null && context.getSegments().contains(getCode()));
    }
}
