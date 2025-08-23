package de.hybris.platform.b2bcommerce.backoffice.actions;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;

public class DisableB2BCustomerAction implements CockpitAction<B2BCustomerModel, Object>
{
    private static final String CONFIRMATION_MESSAGE = "hmc.action.b2bemployeedisable.confirm";
    private static final String DISABLE_B2B_CUSTOMER_EVENT = "b2bcustomerdisable";
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "notificationService")
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<B2BCustomerModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BCustomerModel)
        {
            B2BCustomerModel b2bCustomerModel = (B2BCustomerModel)data;
            UserModel currentUser = this.userService.getCurrentUser();
            boolean isActive = b2bCustomerModel.getActive().booleanValue();
            boolean isUserMemberOfAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService.getAdminUserGroup());
            boolean isUserMemberOfB2BAdminGroup = this.userService.isMemberOfGroup(currentUser, this.userService
                            .getUserGroupForUID("b2badmingroup"));
            return ((isUserMemberOfAdminGroup || isUserMemberOfB2BAdminGroup) && isActive);
        }
        return false;
    }


    public String getConfirmationMessage(ActionContext<B2BCustomerModel> ctx)
    {
        return ctx.getLabel("hmc.action.b2bemployeedisable.confirm");
    }


    public boolean needsConfirmation(ActionContext<B2BCustomerModel> ctx)
    {
        return true;
    }


    public ActionResult<Object> perform(ActionContext<B2BCustomerModel> ctx)
    {
        Object data = ctx.getData();
        if(data != null && data instanceof B2BCustomerModel)
        {
            B2BCustomerModel b2bCustomerModel = (B2BCustomerModel)data;
            b2bCustomerModel.setActive(Boolean.FALSE);
            b2bCustomerModel.setLoginDisabled(true);
            this.modelService.save(b2bCustomerModel);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(ctx), "b2bcustomerdisable", NotificationEvent.Level.SUCCESS, new Object[] {b2bCustomerModel});
            return new ActionResult("success", b2bCustomerModel);
        }
        return new ActionResult("error");
    }
}
