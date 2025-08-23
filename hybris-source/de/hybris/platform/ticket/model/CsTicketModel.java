package de.hybris.platform.ticket.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import java.util.Date;
import java.util.List;

public class CsTicketModel extends ItemModel
{
    public static final String _TYPECODE = "CsTicket";
    public static final String TICKETID = "ticketID";
    public static final String CUSTOMER = "customer";
    public static final String ORDER = "order";
    public static final String HEADLINE = "headline";
    public static final String CATEGORY = "category";
    public static final String PRIORITY = "priority";
    public static final String STATE = "state";
    public static final String ASSIGNEDAGENT = "assignedAgent";
    public static final String ASSIGNEDGROUP = "assignedGroup";
    public static final String RESOLUTION = "resolution";
    public static final String BASESITE = "baseSite";
    public static final String EVENTS = "events";
    public static final String RETENTIONDATE = "retentionDate";


    public CsTicketModel()
    {
    }


    public CsTicketModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketModel(ItemModel _owner, String _ticketID)
    {
        setOwner(_owner);
        setTicketID(_ticketID);
    }


    @Accessor(qualifier = "assignedAgent", type = Accessor.Type.GETTER)
    public EmployeeModel getAssignedAgent()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("assignedAgent");
    }


    @Accessor(qualifier = "assignedGroup", type = Accessor.Type.GETTER)
    public CsAgentGroupModel getAssignedGroup()
    {
        return (CsAgentGroupModel)getPersistenceContext().getPropertyValue("assignedGroup");
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
    public BaseSiteModel getBaseSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("baseSite");
    }


    @Accessor(qualifier = "category", type = Accessor.Type.GETTER)
    public CsTicketCategory getCategory()
    {
        return (CsTicketCategory)getPersistenceContext().getPropertyValue("category");
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
    public UserModel getCustomer()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("customer");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "events", type = Accessor.Type.GETTER)
    public List<CsTicketEventModel> getEvents()
    {
        return (List<CsTicketEventModel>)getPersistenceContext().getPropertyValue("events");
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline()
    {
        return (String)getPersistenceContext().getPropertyValue("headline");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public CsTicketPriority getPriority()
    {
        return (CsTicketPriority)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "resolution", type = Accessor.Type.GETTER)
    public CsTicketResolutionEventModel getResolution()
    {
        return (CsTicketResolutionEventModel)getPersistenceContext().getPropertyValue("resolution");
    }


    @Accessor(qualifier = "retentionDate", type = Accessor.Type.GETTER)
    public Date getRetentionDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("retentionDate");
    }


    @Accessor(qualifier = "state", type = Accessor.Type.GETTER)
    public CsTicketState getState()
    {
        return (CsTicketState)getPersistenceContext().getPropertyValue("state");
    }


    @Accessor(qualifier = "ticketID", type = Accessor.Type.GETTER)
    public String getTicketID()
    {
        return (String)getPersistenceContext().getPropertyValue("ticketID");
    }


    @Accessor(qualifier = "assignedAgent", type = Accessor.Type.SETTER)
    public void setAssignedAgent(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("assignedAgent", value);
    }


    @Accessor(qualifier = "assignedGroup", type = Accessor.Type.SETTER)
    public void setAssignedGroup(CsAgentGroupModel value)
    {
        getPersistenceContext().setPropertyValue("assignedGroup", value);
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
    public void setBaseSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("baseSite", value);
    }


    @Accessor(qualifier = "category", type = Accessor.Type.SETTER)
    public void setCategory(CsTicketCategory value)
    {
        getPersistenceContext().setPropertyValue("category", value);
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
    public void setCustomer(UserModel value)
    {
        getPersistenceContext().setPropertyValue("customer", value);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value)
    {
        getPersistenceContext().setPropertyValue("headline", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(CsTicketPriority value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "resolution", type = Accessor.Type.SETTER)
    public void setResolution(CsTicketResolutionEventModel value)
    {
        getPersistenceContext().setPropertyValue("resolution", value);
    }


    @Accessor(qualifier = "retentionDate", type = Accessor.Type.SETTER)
    public void setRetentionDate(Date value)
    {
        getPersistenceContext().setPropertyValue("retentionDate", value);
    }


    @Accessor(qualifier = "state", type = Accessor.Type.SETTER)
    public void setState(CsTicketState value)
    {
        getPersistenceContext().setPropertyValue("state", value);
    }


    @Accessor(qualifier = "ticketID", type = Accessor.Type.SETTER)
    public void setTicketID(String value)
    {
        getPersistenceContext().setPropertyValue("ticketID", value);
    }
}
