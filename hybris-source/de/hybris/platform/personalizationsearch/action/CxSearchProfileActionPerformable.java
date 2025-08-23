package de.hybris.platform.personalizationsearch.action;

import de.hybris.platform.personalizationsearch.data.CxSearchProfileActionResult;
import de.hybris.platform.personalizationsearch.model.CxSearchProfileActionModel;
import de.hybris.platform.personalizationservices.CxContext;
import de.hybris.platform.personalizationservices.action.impl.CxAbstractActionPerformable;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;

public class CxSearchProfileActionPerformable extends CxAbstractActionPerformable<CxSearchProfileActionModel>
{
    protected CxAbstractActionResult executeAction(CxSearchProfileActionModel action, CxContext context)
    {
        CxSearchProfileActionResult actionResult = new CxSearchProfileActionResult();
        actionResult.setSearchProfileCode(action.getSearchProfileCode());
        actionResult.setSearchProfileCatalog(action.getSearchProfileCatalog());
        return (CxAbstractActionResult)actionResult;
    }
}
