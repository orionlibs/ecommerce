package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupportbackoffice.data.CsCreateCustomerForm;
import de.hybris.platform.customersupportbackoffice.strategies.CsCreateCustomerStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CreateCustomerWizardHandler extends CsCreateWizardBaseHandler implements FlowActionHandler
{
    private CsCreateCustomerStrategy csCreateCustomerStrategy;
    private UserService userService;
    private NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        try
        {
            CsCreateCustomerForm form = (CsCreateCustomerForm)adapter.getWidgetInstanceManager().getModel().getValue("customersupport_backoffice_customerForm", CsCreateCustomerForm.class);
            getCsCreateCustomerStrategy().createCustomer(form);
            UserModel userModel = this.userService.getUserForUID(form.getEmail().toLowerCase());
            adapter.getWidgetInstanceManager().sendOutput("wizardResult", userModel);
            DefaultContext defaultContext = new DefaultContext();
            defaultContext.addAttribute("updatedObjectIsNew", Boolean.TRUE);
            publishEvent("objectsUpdated", userModel, (Context)defaultContext);
        }
        catch(DuplicateUidException e)
        {
            ConfigurableFlowController controller = (ConfigurableFlowController)adapter.getWidgetInstanceManager().getWidgetslot().getAttribute("widgetController");
            if("step2".equals(controller.getCurrentStep().getId()))
            {
                adapter.back();
            }
            this.notificationService.notifyUser(this.notificationService
                            .getWidgetNotificationSource(adapter.getWidgetInstanceManager()), "CreateObject", NotificationEvent.Level.FAILURE, new Object[] {e});
            return;
        }
        adapter.done();
    }


    protected CsCreateCustomerStrategy getCsCreateCustomerStrategy()
    {
        return this.csCreateCustomerStrategy;
    }


    @Required
    public void setCsCreateCustomerStrategy(CsCreateCustomerStrategy csCreateCustomerStrategy)
    {
        this.csCreateCustomerStrategy = csCreateCustomerStrategy;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
