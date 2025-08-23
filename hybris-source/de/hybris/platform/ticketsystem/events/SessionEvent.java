package de.hybris.platform.ticketsystem.events;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.ticket.enums.EventType;
import java.util.Date;
import java.util.List;

public class SessionEvent extends AbstractEvent
{
    private UserModel customer;
    private UserModel agent;
    private List<PrincipalGroupModel> agentGroups;
    private BaseSiteModel site;
    private String sessionID;
    private Date createdAt;
    private EventType eventType;


    public void setCustomer(UserModel customer)
    {
        this.customer = customer;
    }


    public UserModel getCustomer()
    {
        return this.customer;
    }


    public void setAgent(UserModel agent)
    {
        this.agent = agent;
    }


    public UserModel getAgent()
    {
        return this.agent;
    }


    public void setAgentGroups(List<PrincipalGroupModel> agentGroups)
    {
        this.agentGroups = agentGroups;
    }


    public List<PrincipalGroupModel> getAgentGroups()
    {
        return this.agentGroups;
    }


    public void setSite(BaseSiteModel site)
    {
        this.site = site;
    }


    public BaseSiteModel getSite()
    {
        return this.site;
    }


    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }


    public String getSessionID()
    {
        return this.sessionID;
    }


    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }


    public Date getCreatedAt()
    {
        return this.createdAt;
    }


    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }


    public EventType getEventType()
    {
        return this.eventType;
    }
}
