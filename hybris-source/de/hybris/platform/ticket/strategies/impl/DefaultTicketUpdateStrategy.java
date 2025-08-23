package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventCsTicketStateEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.factory.TicketEventFactory;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.strategies.TicketAttributeChangeEventStrategy;
import de.hybris.platform.ticket.strategies.TicketUpdateStrategy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketUpdateStrategy implements TicketUpdateStrategy
{
    private TicketAttributeChangeEventStrategy ticketAttributeChangeEventStrategy;
    private Map<CsTicketState, List<CsTicketState>> availableNextStates;
    private String ticketChangeEventType;
    private String ticketAssigndEventType;
    private TicketEventFactory ticketEventFactory;
    private ModelService modelService;


    public CsTicketModel updateTicket(CsTicketModel ticket) throws TicketException
    {
        return updateTicket(ticket, "");
    }


    public CsTicketModel updateTicket(CsTicketModel ticket, String note) throws TicketException
    {
        Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
        for(CsTicketChangeEventEntryModel changedValue : changedValues)
        {
            if(changedValue instanceof CsTicketChangeEventCsTicketStateEntryModel)
            {
                CsTicketChangeEventCsTicketStateEntryModel changedValueImpl = (CsTicketChangeEventCsTicketStateEntryModel)changedValue;
                if(!isValidStateTransition(changedValueImpl.getOldValue(), changedValueImpl.getNewValue()))
                {
                    throw new TicketException("Ticket " + ticket.getTicketID() + " in state " + changedValueImpl.getOldValue() + " cannot be changed to state " + changedValueImpl
                                    .getNewValue() + " according to configured rules");
                }
            }
        }
        if(!changedValues.isEmpty())
        {
            CsTicketEventModel changeEvent = createChangeEvent(ticket, changedValues);
            changeEvent.setText(note);
            getModelService().saveAll(new Object[] {ticket, changeEvent});
            getModelService().saveAll(changedValues);
        }
        return ticket;
    }


    public void setTicketState(CsTicketModel ticket, CsTicketState newState) throws TicketException
    {
        setTicketState(ticket, newState, "");
    }


    public void setTicketState(CsTicketModel ticket, CsTicketState newState, String note) throws TicketException
    {
        if(getContext((AbstractItemModel)ticket).getValueHistory().isDirty())
        {
            throw new TicketException("The ticket must not have been previously updated before specifically changing the state");
        }
        preSetTicketState(ticket, newState);
        ticket.setState(newState);
        Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
        if(!changedValues.isEmpty())
        {
            CsTicketEventModel changeEvent = createChangeEvent(ticket, changedValues, note);
            getModelService().saveAll(new Object[] {ticket, changeEvent});
            getModelService().saveAll(changedValues);
        }
    }


    public List<CsTicketState> getTicketNextStates(CsTicketState currentState)
    {
        if(this.availableNextStates.get(currentState) == null)
        {
            return Collections.emptyList();
        }
        return this.availableNextStates.get(currentState);
    }


    protected boolean isValidStateTransition(CsTicketState oldState, CsTicketState newState)
    {
        return (this.availableNextStates.get(oldState) != null && ((List)this.availableNextStates.get(oldState)).contains(newState));
    }


    protected void preSetTicketState(CsTicketModel ticket, CsTicketState newState) throws TicketException
    {
        if(!isValidStateTransition(ticket.getState(), newState))
        {
            throw new TicketException("Ticket " + ticket.getTicketID() + " in state " + ticket.getState() + " cannot be changed to state " + newState + " according to configured rules");
        }
    }


    public CsTicketEventModel assignTicketToAgent(CsTicketModel ticket, EmployeeModel agent) throws TicketException
    {
        preChangeTicketAssignment(ticket, agent);
        ticket.setAssignedAgent(agent);
        Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
        if(!changedValues.isEmpty())
        {
            CsTicketEventModel changeEvent = createAssignTicketEvent(ticket, changedValues);
            getModelService().saveAll(new Object[] {ticket, changeEvent});
            getModelService().saveAll(changedValues);
            return changeEvent;
        }
        return null;
    }


    protected void preChangeTicketAssignment(CsTicketModel ticket, EmployeeModel agent) throws TicketException
    {
        if(getContext((AbstractItemModel)ticket).getValueHistory().isDirty())
        {
            throw new TicketException("The ticket must not have been previously updated before specifically changing the agent");
        }
    }


    public CsTicketEventModel assignTicketToGroup(CsTicketModel ticket, CsAgentGroupModel group) throws TicketException
    {
        preChangeTicketGroup(ticket, group);
        ticket.setAssignedGroup(group);
        Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
        if(!changedValues.isEmpty())
        {
            CsTicketEventModel changeEvent = createAssignTicketEvent(ticket, changedValues);
            getModelService().saveAll(new Object[] {ticket, changeEvent});
            getModelService().saveAll(changedValues);
            return changeEvent;
        }
        return null;
    }


    protected void preChangeTicketGroup(CsTicketModel ticket, CsAgentGroupModel group) throws TicketException
    {
        if(getContext((AbstractItemModel)ticket).getValueHistory().isDirty())
        {
            throw new TicketException("The ticket must not have been previously updated before specifically changing the agent");
        }
    }


    protected CsTicketEventModel createAssignTicketEvent(CsTicketModel ticket, Set<CsTicketChangeEventEntryModel> changedValues)
    {
        CsTicketEventModel event = this.ticketEventFactory.createEvent(this.ticketAssigndEventType);
        event.setText("");
        event.setTicket(ticket);
        event.setEntries(changedValues);
        return event;
    }


    protected CsTicketEventModel createChangeEvent(CsTicketModel ticket, Set<CsTicketChangeEventEntryModel> changedValues)
    {
        return createChangeEvent(ticket, changedValues, "");
    }


    protected CsTicketEventModel createChangeEvent(CsTicketModel ticket, Set<CsTicketChangeEventEntryModel> changedValues, String note)
    {
        CsTicketEventModel event = this.ticketEventFactory.createEvent(this.ticketChangeEventType);
        event.setText(note);
        event.setTicket(ticket);
        event.setEntries(changedValues);
        return event;
    }


    @Required
    public void setTicketAttributeChangeEventStrategy(TicketAttributeChangeEventStrategy ticketAttributeChangeEventStrategy)
    {
        this.ticketAttributeChangeEventStrategy = ticketAttributeChangeEventStrategy;
    }


    @Required
    public void setAvailableNextStates(Map<CsTicketState, List<CsTicketState>> availableNextStates)
    {
        this.availableNextStates = availableNextStates;
    }


    @Required
    public void setTicketEventFactory(TicketEventFactory ticketEventFactory)
    {
        this.ticketEventFactory = ticketEventFactory;
    }


    @Required
    public void setTicketChangeEventType(String ticketChangeEventType)
    {
        this.ticketChangeEventType = ticketChangeEventType;
    }


    protected ItemModelContextImpl getContext(AbstractItemModel model)
    {
        return (ItemModelContextImpl)ModelContextUtils.getItemModelContext(model);
    }


    public String getTicketAssigndEventType()
    {
        return this.ticketAssigndEventType;
    }


    @Required
    public void setTicketAssigndEventType(String ticketAssigndEventType)
    {
        this.ticketAssigndEventType = ticketAssigndEventType;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
