package de.hybris.platform.personalizationservices.trigger.expression;

import java.io.Serializable;

public interface CxExpression extends Serializable
{
    boolean evaluate(CxExpressionContext paramCxExpressionContext);
}
