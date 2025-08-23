package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.factory.TicketEventFactory;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.strategies.TicketAttributeChangeEventStrategy;
import de.hybris.platform.ticket.strategies.TicketResolutionStrategy;
import de.hybris.platform.ticket.strategies.TicketUpdateStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketResolutionStrategy implements TicketResolutionStrategy
{
    private CsTicketState defaultResolvedTicketState;
    private CsTicketState defaultUnresolvedTicketState;
    private String resolveEventType;
    private String unresolveEventType;
    private String agentNoteEventType;
    private TicketUpdateStrategy ticketUpdateStrategy;
    private TicketAttributeChangeEventStrategy ticketAttributeChangeEventStrategy;
    private TicketEventFactory ticketEventFactory;
    private ModelService modelService;


    public CsTicketResolutionEventModel resolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsResolutionType resolutionType, String note) throws TicketException
    {
        return resolveTicket(ticket, intervention, resolutionType, note);
    }


    public CsTicketResolutionEventModel resolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsResolutionType resolutionType, String note, Collection<MediaModel> attachments) throws TicketException
    {
        if(this.ticketUpdateStrategy.getTicketNextStates(ticket.getState()).contains(this.defaultResolvedTicketState))
        {
            String oldState = ticket.getState().getCode();
            CsTicketResolutionEventModel resolutionEvent = (CsTicketResolutionEventModel)this.ticketEventFactory.createEvent(this.resolveEventType);
            resolutionEvent.setText(note);
            resolutionEvent.setTicket(ticket);
            resolutionEvent.setResolutionType(resolutionType);
            resolutionEvent.setInterventionType(intervention);
            resolutionEvent.setReason(CsEventReason.UPDATE);
            ticket.setState(this.defaultResolvedTicketState);
            resolutionEvent
                            .setSubject("Support Ticket Status Updated from " + oldState + " to " + this.defaultResolvedTicketState.getCode());
            Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
            resolutionEvent.setEntries(changedValues);
            addAttachmentsToEvent((CsTicketEventModel)resolutionEvent, attachments);
            getModelService().save(ticket);
            getModelService().save(resolutionEvent);
            if(attachments == null)
            {
                return resolutionEvent;
            }
            for(MediaModel attachment : attachments)
            {
                if(getModelService().isNew(attachment))
                {
                    getModelService().save(attachment);
                }
            }
            return resolutionEvent;
        }
        throw new TicketException("Cannot resolve Ticket " + ticket
                        .getTicketID() + ": configuation does not allow Tickets in state " + ticket
                        .getState() + " to be moved to state " + this.defaultResolvedTicketState);
    }


    protected void addAttachmentsToEvent(CsTicketEventModel event, Collection<MediaModel> attachments)
    {
        if(attachments != null && !attachments.isEmpty())
        {
            for(MediaModel m : attachments)
            {
                CommentAttachmentModel attachment = (CommentAttachmentModel)getModelService().create(CommentAttachmentModel.class);
                attachment.setAbstractComment((AbstractCommentModel)event);
                attachment.setItem((ItemModel)m);
                getModelService().save(attachment);
            }
        }
    }


    public CsCustomerEventModel unResolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note) throws TicketException
    {
        return unResolveTicket(ticket, intervention, reason, note);
    }


    public CsCustomerEventModel unResolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note, Collection<MediaModel> attachments) throws TicketException
    {
        if(this.ticketUpdateStrategy.getTicketNextStates(ticket.getState()).contains(this.defaultUnresolvedTicketState))
        {
            String oldState = ticket.getState().getCode();
            CsCustomerEventModel unResolveEvent = (CsCustomerEventModel)this.ticketEventFactory.createEvent(CsInterventionType.PRIVATE.equals(intervention) ? this.agentNoteEventType : this.unresolveEventType);
            unResolveEvent.setText(note);
            unResolveEvent.setTicket(ticket);
            unResolveEvent.setReason(reason);
            unResolveEvent.setInterventionType(intervention);
            ticket.setState(this.defaultUnresolvedTicketState);
            unResolveEvent
                            .setSubject("Support Ticket Status Updated from " + oldState + " to " + this.defaultUnresolvedTicketState.getCode());
            Set<CsTicketChangeEventEntryModel> changedValues = this.ticketAttributeChangeEventStrategy.getEntriesForChangedAttributes(ticket);
            unResolveEvent.setEntries(changedValues);
            addAttachmentsToEvent((CsTicketEventModel)unResolveEvent, attachments);
            getModelService().save(ticket);
            getModelService().save(unResolveEvent);
            if(attachments == null || attachments.isEmpty())
            {
                return unResolveEvent;
            }
            for(MediaModel attachment : attachments)
            {
                if(getModelService().isNew(attachment))
                {
                    getModelService().save(attachment);
                }
            }
            return unResolveEvent;
        }
        throw new TicketException("Cannot resolve Ticket " + ticket
                        .getTicketID() + ": configuation does not allow Tickets in state " + ticket
                        .getState() + " to be moved to state " + this.defaultUnresolvedTicketState);
    }


    public List<CsTicketState> filterTicketStatesToRemovedClosedStates(List<CsTicketState> states)
    {
        List<CsTicketState> ret = new ArrayList<>(states);
        ret.remove(this.defaultResolvedTicketState);
        return ret;
    }


    public boolean isTicketClosed(CsTicketModel ticket)
    {
        return (ticket.getResolution() != null && CsTicketState.CLOSED.equals(ticket.getState()));
    }


    public boolean isTicketResolvable(CsTicketModel ticket)
    {
        return (ticket.getResolution() == null && this.ticketUpdateStrategy
                        .getTicketNextStates(ticket.getState()).contains(this.defaultResolvedTicketState));
    }


    @Required
    public void setDefaultResolvedTicketState(CsTicketState defaultResolvedTicketState)
    {
        this.defaultResolvedTicketState = defaultResolvedTicketState;
    }


    @Required
    public void setDefaultUnresolvedTicketState(CsTicketState defaultUnresolvedTicketState)
    {
        this.defaultUnresolvedTicketState = defaultUnresolvedTicketState;
    }


    @Required
    public void setTicketUpdateStrategy(TicketUpdateStrategy ticketUpdateStrategy)
    {
        this.ticketUpdateStrategy = ticketUpdateStrategy;
    }


    @Required
    public void setTicketEventFactory(TicketEventFactory ticketEventFactory)
    {
        this.ticketEventFactory = ticketEventFactory;
    }


    @Required
    public void setResolveEventType(String resolveEventType)
    {
        this.resolveEventType = resolveEventType;
    }


    @Required
    public void setUnresolveEventType(String unresolveEventType)
    {
        this.unresolveEventType = unresolveEventType;
    }


    @Required
    public void setAgentNoteEventType(String agentNoteEventType)
    {
        this.agentNoteEventType = agentNoteEventType;
    }


    @Required
    public void setTicketAttributeChangeEventStrategy(TicketAttributeChangeEventStrategy ticketAttributeChangeEventStrategy)
    {
        this.ticketAttributeChangeEventStrategy = ticketAttributeChangeEventStrategy;
    }


    public String getAgentNoteEventType()
    {
        return this.agentNoteEventType;
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
