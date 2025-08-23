package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupportbackoffice.data.CsCreateTicketForm;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticketsystem.data.CsTicketParameter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CreateTicketWizardHandler extends CsCreateWizardBaseHandler implements FlowActionHandler
{
    private TicketBusinessService ticketBusinessService;
    private UserService userService;
    private DefaultCsFormInitialsFactory defaultCsFormInitialsFactory;
    private ModelService modelService;
    private NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter flowActionHandlerAdapter, Map<String, String> map)
    {
        CsCreateTicketForm form = (CsCreateTicketForm)flowActionHandlerAdapter.getWidgetInstanceManager().getModel().getValue("customersupport_backoffice_ticketForm", CsCreateTicketForm.class);
        CsTicketModel ticket = createTicket(form);
        getNotificationService().notifyUser(
                        getNotificationService().getWidgetNotificationSource(flowActionHandlerAdapter.getWidgetInstanceManager()), "CreateObject", NotificationEvent.Level.SUCCESS, new Object[] {ticket});
        DefaultContext defaultContext = new DefaultContext();
        defaultContext.addAttribute("updatedObjectIsNew", Boolean.TRUE);
        publishEvent("objectsUpdated", ticket, (Context)defaultContext);
        ConfigurableFlowController controller = (ConfigurableFlowController)flowActionHandlerAdapter.getWidgetInstanceManager().getWidgetslot().getAttribute("widgetController");
        controller.setValue("finished", Boolean.TRUE);
        controller.getBreadcrumbDiv().invalidate();
        controller.sendOutput("wizardResult", ticket);
        flowActionHandlerAdapter.done();
    }


    protected CsTicketModel createTicket(CsCreateTicketForm form)
    {
        CsTicketModel ticket = getTicketBusinessService().createTicket(createCsTicketParameter(form));
        ticket.setBaseSite(form.getBasesite());
        ticket.setOrder(form.getAssignedTo());
        getModelService().save(ticket);
        return ticket;
    }


    protected CsTicketParameter createCsTicketParameter(CsCreateTicketForm form)
    {
        CsTicketParameter ticketParameter = new CsTicketParameter();
        ticketParameter.setPriority(form.getPriority());
        ticketParameter.setReason(form.getReason());
        ticketParameter.setAssociatedTo(form.getAssignedTo());
        ticketParameter.setAssignedGroup(getCsAgentGroup(form));
        ticketParameter.setAssignedAgent(form.getAssignedAgent());
        ticketParameter.setCategory(form.getCategory());
        ticketParameter.setHeadline(form.getSubject());
        ticketParameter.setInterventionType(form.getIntervention());
        ticketParameter.setCreationNotes(form.getMessage());
        ticketParameter.setCustomer((UserModel)form.getCustomer());
        return ticketParameter;
    }


    protected CsAgentGroupModel getCsAgentGroup(CsCreateTicketForm form)
    {
        CsAgentGroupModel csAgentGroupModel;
        if(form.getAssignedAgent() != null && form.getAssignedGroup() == null)
        {
            csAgentGroupModel = (CsAgentGroupModel)getUserService().getUserGroupForUID(getDefaultCsFormInitialsFactory().getDefaultAgentGroup());
        }
        else
        {
            csAgentGroupModel = form.getAssignedGroup();
        }
        return csAgentGroupModel;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected DefaultCsFormInitialsFactory getDefaultCsFormInitialsFactory()
    {
        return this.defaultCsFormInitialsFactory;
    }


    @Required
    public void setDefaultCsFormInitialsFactory(DefaultCsFormInitialsFactory defaultCsFormInitialsFactory)
    {
        this.defaultCsFormInitialsFactory = defaultCsFormInitialsFactory;
    }


    protected TicketBusinessService getTicketBusinessService()
    {
        return this.ticketBusinessService;
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
    public void setTicketBusinessService(TicketBusinessService ticketBusinessService)
    {
        this.ticketBusinessService = ticketBusinessService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
