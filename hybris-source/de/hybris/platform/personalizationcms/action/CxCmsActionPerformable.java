package de.hybris.platform.personalizationcms.action;

import de.hybris.platform.personalizationcms.data.CxCmsActionResult;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationservices.CxContext;
import de.hybris.platform.personalizationservices.action.impl.CxAbstractActionPerformable;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;

public class CxCmsActionPerformable extends CxAbstractActionPerformable<CxCmsActionModel>
{
    protected CxAbstractActionResult executeAction(CxCmsActionModel action, CxContext context)
    {
        CxCmsActionResult actionResult = new CxCmsActionResult();
        actionResult.setComponentId(action.getComponentId());
        actionResult.setContainerId(action.getContainerId());
        actionResult.setComponentCatalog(action.getComponentCatalog());
        return (CxAbstractActionResult)actionResult;
    }
}
