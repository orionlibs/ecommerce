package de.hybris.platform.b2bcommerce.backoffice.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BReportingService;
import javax.annotation.Resource;

public class GenerateReportingSetAction implements CockpitAction<B2BUnitModel, Object>
{
    private static final String GENERATE_REPORT_B2B_UNIT_EVENT = "b2bunitreportingset";
    @Resource(name = "b2bReportingService")
    private B2BReportingService b2bReportingService;
    @Resource(name = "notificationService")
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<B2BUnitModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<B2BUnitModel> ctx)
    {
        return "";
    }


    public boolean needsConfirmation(ActionContext<B2BUnitModel> arg0)
    {
        return false;
    }


    public ActionResult<Object> perform(ActionContext<B2BUnitModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BUnitModel)
        {
            B2BUnitModel b2bUnitModel = (B2BUnitModel)data;
            this.b2bReportingService.setReportSetForUnit(b2bUnitModel);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bunitreportingset", NotificationEvent.Level.SUCCESS, new Object[] {b2bUnitModel});
            return new ActionResult("success", b2bUnitModel);
        }
        return new ActionResult("error");
    }
}
