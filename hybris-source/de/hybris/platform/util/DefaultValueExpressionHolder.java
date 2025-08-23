package de.hybris.platform.util;

import java.io.Serializable;

public class DefaultValueExpressionHolder implements Serializable
{
    private static final long serialVersionUID = 9465463416879847L;
    private final String expression;


    public DefaultValueExpressionHolder(String expression)
    {
        this.expression = expression;
    }


    public String getExpression()
    {
        return this.expression;
    }
}
