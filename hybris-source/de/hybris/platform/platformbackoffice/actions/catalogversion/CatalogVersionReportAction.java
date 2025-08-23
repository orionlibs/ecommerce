package de.hybris.platform.platformbackoffice.actions.catalogversion;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.platformbackoffice.data.CatalogVersionReportDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogVersionReportAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<CatalogVersionModel, CatalogVersionReportDTO>
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionReportAction.class);
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private CatalogTypeService catalogTypeService;


    public ActionResult<CatalogVersionReportDTO> perform(ActionContext<CatalogVersionModel> ctx)
    {
        ActionResult<CatalogVersionReportDTO> result = null;
        CatalogVersionModel catVer = (CatalogVersionModel)ctx.getData();
        if(catVer != null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Generating report for catalog version: %s from catalog %s", new Object[] {catVer.getVersion(), catVer.getCatalog().getId()}));
            }
            CatalogVersionReportDTO report = new CatalogVersionReportDTO(catVer.getCatalog().getId(), catVer.getVersion());
            report.setActive(catVer.getActive());
            report.setLanguages((List)catVer.getLanguages().stream().map(e -> e.getName()).collect(Collectors.toList()));
            report.setReadPrincipals((List)catVer.getReadPrincipals().stream().map(e -> e.getUid()).collect(Collectors.toList()));
            report.setWritePrincipals((List)catVer.getWritePrincipals().stream().map(e -> e.getUid()).collect(Collectors.toList()));
            report.setRootCategories((List)catVer.getRootCategories().stream().map(e -> e.getCode()).collect(Collectors.toList()));
            Map<ComposedTypeModel, Long> types = this.catalogTypeService.getCatalogVersionOverview(catVer).getTypeAmounts();
            report.setTypeStatistics(types);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("{}", report);
            }
            result = new ActionResult("success", report);
            sendOutput("catalogversionreport", report);
        }
        else
        {
            result = new ActionResult("error");
        }
        return result;
    }


    public boolean canPerform(ActionContext<CatalogVersionModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<CatalogVersionModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<CatalogVersionModel> ctx)
    {
        return ctx.getLabel("report.confirm");
    }
}
