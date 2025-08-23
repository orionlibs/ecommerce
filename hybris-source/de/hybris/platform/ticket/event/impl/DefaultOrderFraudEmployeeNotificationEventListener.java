package de.hybris.platform.ticket.event.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.events.OrderFraudEmployeeNotificationEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticketsystem.data.CsTicketParameter;
import de.hybris.platform.util.localization.Localization;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderFraudEmployeeNotificationEventListener extends AbstractEventListener<OrderFraudEmployeeNotificationEvent>
{
    private String fraudUserGroup;
    private CsTicketPriority priority;
    private TicketBusinessService ticketBusinessService;
    private UserService userService;
    public static final String DEFAULT_FRAUD_USER_GROUP = "fraudAgentGroup";
    public static final CsTicketPriority DEFAULT_PRIORITY = CsTicketPriority.HIGH;


    protected void onEvent(OrderFraudEmployeeNotificationEvent event)
    {
        ServicesUtil.validateParameterNotNull(event, "Event cannot be null");
        OrderModel order = event.getOrder();
        ServicesUtil.validateParameterNotNull(order, "Order in event cannot be null");
        String headline = getFraudTicketHeadline((AbstractOrderModel)order);
        String ticketText = getFraudTicketText((AbstractOrderModel)order);
        CsAgentGroupModel fraudGroup = (CsAgentGroupModel)getUserService().getUserGroupForUID(getFraudUserGroupId(), CsAgentGroupModel.class);
        CsTicketParameter ticketParameter = new CsTicketParameter();
        ticketParameter.setPriority(getPriority());
        ticketParameter.setReason(null);
        ticketParameter.setAssociatedTo((AbstractOrderModel)order);
        ticketParameter.setAssignedGroup(fraudGroup);
        ticketParameter.setCategory(CsTicketCategory.FRAUD);
        ticketParameter.setHeadline(headline);
        ticketParameter.setInterventionType(null);
        ticketParameter.setCreationNotes(ticketText);
        ticketParameter.setCustomer(order.getUser());
        getTicketBusinessService().createTicket(ticketParameter);
    }


    protected String getFraudTicketText(AbstractOrderModel order)
    {
        return Localization.getLocalizedString("csticket.fraud.content.default");
    }


    protected String getFraudTicketHeadline(AbstractOrderModel order)
    {
        return Localization.getLocalizedString("csticket.fraud.headline.default", new Object[] {order
                        .getCode()});
    }


    protected CsTicketPriority getPriority()
    {
        if(this.priority == null)
        {
            return DEFAULT_PRIORITY;
        }
        return this.priority;
    }


    protected String getFraudUserGroupId()
    {
        if(StringUtils.isEmpty(this.fraudUserGroup))
        {
            return "fraudAgentGroup";
        }
        return this.fraudUserGroup;
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setFraudUserGroup(String fraudUserGroup)
    {
        this.fraudUserGroup = fraudUserGroup;
    }


    public void setPriority(CsTicketPriority priority)
    {
        this.priority = priority;
    }
}
