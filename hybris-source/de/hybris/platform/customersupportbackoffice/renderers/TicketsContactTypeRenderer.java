package de.hybris.platform.customersupportbackoffice.renderers;

import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.service.TicketService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class TicketsContactTypeRenderer extends DefaultCustomViewRenderer
{
    protected static final String CONTACT_TYPE_CUSTOMER = "Customer";
    protected static final String CONTACT_TYPE_CUSTOMER_SUPPORT = "CustomerSupport";
    protected static final String DIV_WRAPPER = "yw-wizard-property";
    private TicketService ticketService;
    private LabelService labelService;


    public void render(Component component, ViewType customView, Map<String, String> parameters, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Div contactTypePanel = new Div();
        contactTypePanel.setSclass("yw-wizard-property");
        Label contactTypeLabel = new Label(Labels.getLabel("customersupport_backoffice_closeTicketForm.contact") + ":");
        Combobox contactTypeCombo = configureContactTypeCombo();
        contactTypePanel.appendChild((Component)contactTypeLabel);
        contactTypePanel.appendChild((Component)contactTypeCombo);
        Div replyToPanel = new Div();
        replyToPanel.setSclass("yw-wizard-property");
        Label replyToCustomerLabel = new Label(Labels.getLabel("customersupport_backoffice_tickets_correspondence_replyto") + ":");
        replyToPanel.appendChild((Component)replyToCustomerLabel);
        Combobox replyToCombo = configureReplyToCombo();
        replyToPanel.appendChild((Component)replyToCombo);
        replyToCombo.addEventListener("onSelect", addReplyToComboEventListener(replyToCombo, contactTypeCombo));
        contactTypeCombo.addEventListener("onSelect", addContactTypeComboEventListener(contactTypeCombo, replyToCombo));
        widgetInstanceManager.getModel().setValue("replyTo", contactTypeCombo);
        component.appendChild((Component)contactTypePanel);
        component.appendChild((Component)replyToPanel);
    }


    protected EventListener addContactTypeComboEventListener(Combobox contactTypeCombo, Combobox replyToCombo)
    {
        return (EventListener)new Object(this, contactTypeCombo, replyToCombo);
    }


    protected EventListener addReplyToComboEventListener(Combobox replyToCombo, Combobox contactTypeCombo)
    {
        return (EventListener)new Object(this, replyToCombo, contactTypeCombo);
    }


    protected Combobox configureReplyToCombo()
    {
        Combobox replyToCombo = new Combobox();
        Comboitem customerComboItem = new Comboitem();
        Comboitem customerSupportComboItem = new Comboitem();
        customerComboItem.setLabel(Labels.getLabel("customersupport_backoffice_tickets_correspondence_replyto_customer"));
        customerComboItem.setValue(CsInterventionType.TICKETMESSAGE);
        customerSupportComboItem.setLabel(
                        Labels.getLabel("customersupport_backoffice_tickets_correspondence_replyto_customersupport"));
        customerSupportComboItem.setValue(CsInterventionType.PRIVATE);
        replyToCombo.appendChild((Component)customerComboItem);
        replyToCombo.appendChild((Component)customerSupportComboItem);
        replyToCombo.setSelectedIndex(0);
        replyToCombo.setReadonly(true);
        return replyToCombo;
    }


    protected Combobox configureContactTypeCombo()
    {
        Combobox contactTypeCombo = new Combobox();
        contactTypeCombo.setReadonly(true);
        List<CsInterventionType> interventionTypes = this.ticketService.getInterventionTypes();
        for(CsInterventionType csInterventionType : interventionTypes)
        {
            Comboitem comboItem = new Comboitem();
            comboItem.setLabel(getLabelService().getObjectLabel(csInterventionType));
            comboItem.setValue(csInterventionType);
            contactTypeCombo.appendChild((Component)comboItem);
            if(CsInterventionType.TICKETMESSAGE.equals(csInterventionType))
            {
                contactTypeCombo.setSelectedItem(comboItem);
            }
        }
        return contactTypeCombo;
    }


    @Required
    public void setTicketService(TicketService ticketService)
    {
        this.ticketService = ticketService;
    }


    protected TicketService getTicketService()
    {
        return this.ticketService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
