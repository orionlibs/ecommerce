package de.hybris.platform.b2bcommerce.backoffice.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class EnableB2BCustomerAction implements CockpitAction<B2BCustomerModel, Object>
{
    private static final Logger LOG = Logger.getLogger(EnableB2BCustomerAction.class);
    private static final String ENABLE_B2B_CUSTOMER_EVENT = "b2bcustomerenable";
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
    @Resource(name = "notificationService")
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<B2BCustomerModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BCustomerModel)
        {
            B2BCustomerModel b2bCustomerModel = (B2BCustomerModel)data;
            UserModel currentUser = this.userService.getCurrentUser();
            boolean isUserMemberOfAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService.getAdminUserGroup());
            boolean isUserMemberOfB2BAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService
                            .getUserGroupForUID("b2badmingroup"));
            boolean isActive = b2bCustomerModel.getActive().booleanValue();
            return ((isUserMemberOfAdminGroup || isUserMemberOfB2BAdminGroup) && !isActive);
        }
        return false;
    }


    public String getConfirmationMessage(ActionContext<B2BCustomerModel> ctx)
    {
        return "";
    }


    public boolean needsConfirmation(ActionContext<B2BCustomerModel> ctx)
    {
        return false;
    }


    public ActionResult<Object> perform(ActionContext<B2BCustomerModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BCustomerModel)
        {
            B2BCustomerModel b2bCustomerModel = (B2BCustomerModel)data;
            if(!canActivate(b2bCustomerModel))
            {
                this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bcustomerenable", NotificationEvent.Level.FAILURE, new Object[] {b2bCustomerModel});
                return new ActionResult("error", b2bCustomerModel);
            }
            b2bCustomerModel.setActive(Boolean.TRUE);
            b2bCustomerModel.setLoginDisabled(false);
            this.modelService.save(b2bCustomerModel);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bcustomerenable", NotificationEvent.Level.SUCCESS, new Object[] {b2bCustomerModel});
            return new ActionResult("success", b2bCustomerModel);
        }
        return new ActionResult("error");
    }


    protected boolean canActivate(B2BCustomerModel b2bCustomerModel)
    {
        boolean canActivate = false;
        try
        {
            B2BUnitModel parent = (B2BUnitModel)this.b2bUnitService.getParent(b2bCustomerModel);
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
