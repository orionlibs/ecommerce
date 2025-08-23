package de.hybris.platform.commerceservices.backoffice.orgunits.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;

@Deprecated(since = "2205", forRemoval = true)
public class DisableOrgUnitAction implements CockpitAction<OrgUnitModel, Object>
{
    private static final String CONFIRMATION_MESSAGE = "organization.unit.confirm.disable.msg";
    private static final String SUCCESS_MESSAGE = "organization.unit.confirm.disable.success.msg";
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "orgUnitService")
    private OrgUnitService orgUnitService;
    @Resource(name = "orgUnitAuthorizationStrategy")
    private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;
    @Resource(name = "notificationService")
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<OrgUnitModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof OrgUnitModel)
        {
            return (((OrgUnitModel)data).getActive().booleanValue() && this.orgUnitAuthorizationStrategy
                            .canEditUnit(this.userService.getCurrentUser()));
        }
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrgUnitModel> ctx)
    {
        return ctx.getLabel("organization.unit.confirm.disable.msg");
    }


    public boolean needsConfirmation(ActionContext<OrgUnitModel> ctx)
    {
        return true;
    }


    public ActionResult<Object> perform(ActionContext<OrgUnitModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof OrgUnitModel)
        {
            OrgUnitModel orgUnitModel = (OrgUnitModel)data;
            this.orgUnitService.deactivateUnit(orgUnitModel);
            getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {ctx
                            .getLabel("organization.unit.confirm.disable.success.msg")});
            return new ActionResult("success", orgUnitModel);
        }
        return new ActionResult("error");
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
