package de.hybris.platform.platformbackoffice.actions.catalogversion;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.platformbackoffice.data.CatalogVersionDiffDTO;
import de.hybris.platform.platformbackoffice.services.catalogversion.CatalogVersionCompareService;
import java.util.Collection;
import javax.annotation.Resource;

public class CatalogVersionDiffAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<CatalogVersionModel, CatalogVersionDiffDTO>
{
    @Resource
    private CatalogVersionCompareService catalogVersionCompareService;


    public ActionResult<CatalogVersionDiffDTO> perform(ActionContext<CatalogVersionModel> ctx)
    {
        Collection<CatalogVersionCompareService.CatalogVersionComparison> possibleOperations = this.catalogVersionCompareService.getPossibleComparisons((CatalogVersionModel)ctx.getData());
        sendOutput("catalogversiondiff", new CatalogVersionDiffDTO(possibleOperations));
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<CatalogVersionModel> ctx)
    {
        return (ctx.getData() != null && this.catalogVersionCompareService.canBeCompared((CatalogVersionModel)ctx.getData()));
    }


    public String getConfirmationMessage(ActionContext<CatalogVersionModel> arg0)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<CatalogVersionModel> arg0)
    {
        return false;
    }
}
