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
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

@Deprecated(since = "2205", forRemoval = true)
public class EnableOrgUnitAction implements CockpitAction<OrgUnitModel, Object>
{
    private static final Logger LOG = Logger.getLogger(EnableOrgUnitAction.class);
    private static final String CONFIRMATION_MESSAGE = "organization.unit.confirm.enable.msg";
    private static final String SUCCESS_MESSAGE = "organization.unit.confirm.enable.success.msg";
    private static final String CANNOT_ACTIVATE_MESSAGE = "organization.unit.enable.cannotactivate";
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
            return (!((OrgUnitModel)data).getActive().booleanValue() && this.orgUnitAuthorizationStrategy
                            .canEditUnit(this.userService.getCurrentUser()));
        }
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrgUnitModel> ctx)
    {
        return ctx.getLabel("organization.unit.confirm.enable.msg");
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
            if(!canActivate(orgUnitModel))
            {
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx), "UpdateObject", NotificationEvent.Level.FAILURE, new Object[] {ctx
                                .getLabel("organization.unit.enable.cannotactivate")});
                return new ActionResult("success", orgUnitModel);
            }
            this.orgUnitService.activateUnit(orgUnitModel);
            getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {ctx
                            .getLabel("organization.unit.confirm.enable.success.msg")});
            return new ActionResult("success", orgUnitModel);
        }
        return new ActionResult("error");
    }


    protected boolean canActivate(OrgUnitModel orgUnitModel)
    {
        boolean canActivate = false;
        try
        {
            Optional<OrgUnitModel> parentUnitOptional = this.orgUnitService.getParent(orgUnitModel);
            canActivate = parentUnitOptional.isPresent() ? ((OrgUnitModel)parentUnitOptional.get()).getActive().booleanValue() : true;
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return canActivate;
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
