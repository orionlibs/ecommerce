package de.hybris.platform.personalizationpromotions.action;

import de.hybris.platform.personalizationpromotions.data.CxPromotionActionResult;
import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.personalizationservices.CxContext;
import de.hybris.platform.personalizationservices.action.impl.CxAbstractActionPerformable;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;

public class CxPromotionActionPerformable extends CxAbstractActionPerformable<CxPromotionActionModel>
{
    protected CxAbstractActionResult executeAction(CxPromotionActionModel action, CxContext context)
    {
        CxPromotionActionResult actionResult = new CxPromotionActionResult();
        actionResult.setPromotionId(action.getPromotionId());
        return (CxAbstractActionResult)actionResult;
    }
}
