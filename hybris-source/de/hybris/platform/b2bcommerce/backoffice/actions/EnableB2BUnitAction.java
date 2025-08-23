package de.hybris.platform.b2bcommerce.backoffice.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class EnableB2BUnitAction implements CockpitAction<B2BUnitModel, Object>
{
    private static final Logger LOG = Logger.getLogger(EnableB2BUnitAction.class);
    private static final String CONFIRMATION_MESSAGE = "hmc.action.b2bunitenable.confirm";
    private static final String ENABLE_B2B_UNIT_EVENT = "b2bunitenable";
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
    @Resource(name = "notificationService")
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<B2BUnitModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BUnitModel)
        {
            B2BUnitModel b2bUnitModel = (B2BUnitModel)data;
            UserModel currentUser = this.userService.getCurrentUser();
            boolean isUserMemberOfAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService.getAdminUserGroup());
            boolean isUserMemberOfB2BAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService
                            .getUserGroupForUID("b2badmingroup"));
            boolean isActive = b2bUnitModel.getActive().booleanValue();
            return ((isUserMemberOfAdminGroup || isUserMemberOfB2BAdminGroup) && !isActive);
        }
        return false;
    }


    public String getConfirmationMessage(ActionContext<B2BUnitModel> ctx)
    {
        return ctx.getLabel("hmc.action.b2bunitenable.confirm");
    }


    public boolean needsConfirmation(ActionContext<B2BUnitModel> ctx)
    {
        return true;
    }


    public ActionResult<Object> perform(ActionContext<B2BUnitModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BUnitModel)
        {
            B2BUnitModel b2bUnitModel = (B2BUnitModel)data;
            if(!canActivate(b2bUnitModel))
            {
                this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bunitenable", NotificationEvent.Level.FAILURE, new Object[] {b2bUnitModel});
                return new ActionResult("error", b2bUnitModel);
            }
            b2bUnitModel.setActive(Boolean.TRUE);
            this.modelService.save(b2bUnitModel);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bunitenable", NotificationEvent.Level.SUCCESS, new Object[] {b2bUnitModel});
            return new ActionResult("success", b2bUnitModel);
        }
        return new ActionResult("error");
    }


    protected boolean canActivate(B2BUnitModel b2bUnitModel)
    {
        boolean canActivate = false;
        try
        {
            B2BUnitModel parent = (B2BUnitModel)this.b2bUnitService.getParent((CompanyModel)b2bUnitModel);
            if(parent != null)
            {
                return parent.getActive().booleanValue();
            }
            canActivate = true;
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return canActivate;
    }
}
