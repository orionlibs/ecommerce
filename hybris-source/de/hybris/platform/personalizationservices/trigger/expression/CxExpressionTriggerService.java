package de.hybris.platform.personalizationservices.trigger.expression;

import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;

public interface CxExpressionTriggerService
{
    CxExpression extractExpression(CxExpressionTriggerModel paramCxExpressionTriggerModel);


    void saveExpression(CxExpressionTriggerModel paramCxExpressionTriggerModel, CxExpression paramCxExpression);
}
