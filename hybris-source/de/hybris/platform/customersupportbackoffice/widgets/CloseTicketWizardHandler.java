package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.customersupportbackoffice.data.CsCloseTicketForm;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Combobox;

public class CloseTicketWizardHandler implements FlowActionHandler
{
    private TicketBusinessService ticketBusinessService;
    private static final Logger LOG = LoggerFactory.getLogger(CloseTicketWizardHandler.class);
    protected NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        CsTicketModel ticket = (CsTicketModel)((HashMap)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", HashMap.class)).get("parentObject");
        CsCloseTicketForm closeTicketForm = (CsCloseTicketForm)adapter.getWidgetInstanceManager().getModel().getValue("customersupport_backoffice_closeTicketForm", CsCloseTicketForm.class);
        WidgetInstanceManager wim = (WidgetInstanceManager)((HashMap)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", HashMap.class)).get("wim");
        Combobox contactTypeCombo = (Combobox)adapter.getWidgetInstanceManager().getModel().getValue("replyTo", Combobox.class);
        CsInterventionType contactType = (CsInterventionType)contactTypeCombo.getSelectedItem().getValue();
        try
        {
            getTicketBusinessService().resolveTicket(ticket, contactType, closeTicketForm.getResolution(), closeTicketForm
                            .getMessage());
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(wim), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[] {Collections.singletonList(ticket)});
            adapter.done();
            wim.getWidgetslot().updateView();
        }
        catch(TicketException e)
        {
            LOG.warn(String.format("Exception on closing ticket %s", new Object[] {ticket.getTicketID()}), (Throwable)e);
        }
    }


    protected TicketBusinessService getTicketBusinessService()
    {
        return this.ticketBusinessService;
    }


    @Required
    public void setTicketBusinessService(TicketBusinessService ticketBusinessService)
    {
        this.ticketBusinessService = ticketBusinessService;
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
}
