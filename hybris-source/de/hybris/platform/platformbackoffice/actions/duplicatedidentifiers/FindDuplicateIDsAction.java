package de.hybris.platform.platformbackoffice.actions.duplicatedidentifiers;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.platformbackoffice.data.DuplicatedItemsReport;
import java.util.Collection;
import javax.annotation.Resource;

public class FindDuplicateIDsAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<CatalogVersionModel, Object>
{
    private static final String REPORT_SOCKET = "duplicatedIdentifiersReport";
    @Resource
    CatalogVersionService catalogVersionService;


    public ActionResult<Object> perform(ActionContext<CatalogVersionModel> actionContext)
    {
        CatalogVersionModel cvm = (CatalogVersionModel)actionContext.getData();
        Collection<DuplicatedItemIdentifier> duplicates = this.catalogVersionService.findDuplicatedIds(cvm);
        DuplicatedItemsReport report = new DuplicatedItemsReport(cvm.getCatalog().getName(), cvm.getVersion(), duplicates);
        sendOutput("duplicatedIdentifiersReport", report);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<CatalogVersionModel> actionContext)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<CatalogVersionModel> actionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<CatalogVersionModel> actionContext)
    {
        return null;
    }
}
