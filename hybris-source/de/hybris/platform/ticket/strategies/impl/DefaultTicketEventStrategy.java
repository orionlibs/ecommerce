package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.factory.TicketEventFactory;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.TicketEventStrategy;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketEventStrategy implements TicketEventStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultTicketEventStrategy.class);
    private ModelService modelService;
    private TicketEventFactory ticketEventFactory;
    private String ticketCreatedType;
    private String emailCommentType;
    private String customerNoteCommentType;
    private String agentNoteCommentType;
    private CsTicketState openState;


    public CsCustomerEventModel createNoteForTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note, Collection<MediaModel> attachments)
    {
        String msgCommentType = CsInterventionType.PRIVATE.equals(intervention) ? getAgentNoteCommentType() : getCustomerNoteCommentType();
        CsCustomerEventModel noteEvent = (CsCustomerEventModel)this.ticketEventFactory.createEvent(msgCommentType);
        noteEvent.setText(note);
        noteEvent.setTicket(ticket);
        noteEvent.setSubject("Support Ticket Message Updated");
        noteEvent.setInterventionType(intervention);
        noteEvent.setReason(reason);
        addAttachmentsToEvent((CsTicketEventModel)noteEvent, attachments);
        getModelService().save(noteEvent);
        if(attachments != null && !attachments.isEmpty())
        {
            for(MediaModel attachment : attachments)
            {
                if(getModelService().isNew(attachment))
                {
                    getModelService().save(attachment);
                }
            }
        }
        return noteEvent;
    }


    public CsCustomerEventModel createAssignAgentToTicket(CsTicketModel ticket)
    {
        CsCustomerEventModel csAgentEvent = (CsCustomerEventModel)this.ticketEventFactory.createEvent(this.ticketCreatedType);
        csAgentEvent.setTicket(ticket);
        csAgentEvent.setSubject("Support Ticket Assigned");
        csAgentEvent.setText("Assigned");
        getModelService().save(csAgentEvent);
        return csAgentEvent;
    }


    public CsCustomerEventModel createCustomerEmailForTicket(CsTicketModel ticket, CsEventReason reason, String subject, String emailBody, Collection<MediaModel> attachments)
    {
        CsCustomerEventModel emailEvent = (CsCustomerEventModel)this.ticketEventFactory.createEvent(this.emailCommentType);
        emailEvent.setSubject(subject);
        emailEvent.setText(emailBody);
        emailEvent.setTicket(ticket);
        emailEvent.setInterventionType(CsInterventionType.EMAIL);
        emailEvent.setReason(reason);
        addAttachmentsToEvent((CsTicketEventModel)emailEvent, attachments);
        getModelService().save(emailEvent);
        if(attachments != null)
        {
            for(MediaModel attachment : attachments)
            {
                if(getModelService().isNew(attachment))
                {
                    getModelService().save(attachment);
                }
            }
        }
        return emailEvent;
    }


    public CsCustomerEventModel createCreationEventForTicket(CsTicketModel ticket, CsEventReason reason, CsInterventionType interventionType, String text)
    {
        CsCustomerEventModel creationEvent = (CsCustomerEventModel)this.ticketEventFactory.createEvent(this.ticketCreatedType);
        creationEvent.setSubject("");
        creationEvent.setText(text);
        creationEvent.setTicket(ticket);
        creationEvent.setInterventionType(interventionType);
        creationEvent.setReason(reason);
        getModelService().save(creationEvent);
        return creationEvent;
    }


    public CsCustomerEventModel ensureTicketEventSetupForCreationEvent(CsTicketModel ticket, CsCustomerEventModel creationEvent)
    {
        creationEvent.setTicket(ticket);
        CsCustomerEventModel ret = (CsCustomerEventModel)this.ticketEventFactory.ensureTicketSetup((CsTicketEventModel)creationEvent, this.ticketCreatedType);
        onTicketCreation(ticket, ret);
        return ret;
    }


    protected void onTicketCreation(CsTicketModel ticket, CsCustomerEventModel creationEvent)
    {
        if(ticket.getAssignedAgent() != null)
        {
            LOG.info("Newly created ticket is assigned, setting it to open state [" + this.openState + "]");
            ticket.setState(this.openState);
        }
        else if(ticket.getAssignedGroup() != null && ticket.getAssignedGroup().getDefaultAssignee() != null)
        {
            LOG.info("Newly created ticket is unassign but there is a default assignee, assigning to [" + ticket
                            .getAssignedGroup().getDefaultAssignee() + "]");
            ticket.setAssignedAgent(ticket.getAssignedGroup().getDefaultAssignee());
        }
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


    @Required
    public void setEmailCommentType(String emailCommentType)
    {
        this.emailCommentType = emailCommentType;
    }


    @Required
    public void setTicketCreatedType(String ticketCreatedType)
    {
        this.ticketCreatedType = ticketCreatedType;
    }


    @Required
    public void setTicketEventFactory(TicketEventFactory ticketEventFactory)
    {
        this.ticketEventFactory = ticketEventFactory;
    }


    @Required
    public void setOpenState(CsTicketState openState)
    {
        this.openState = openState;
    }


    @Required
    public void setCustomerNoteCommentType(String customerNoteCommentType)
    {
        this.customerNoteCommentType = customerNoteCommentType;
    }


    @Required
    public void setAgentNoteCommentType(String agentNoteCommentType)
    {
        this.agentNoteCommentType = agentNoteCommentType;
    }


    protected TicketEventFactory getTicketEventFactory()
    {
        return this.ticketEventFactory;
    }


    protected String getTicketCreatedType()
    {
        return this.ticketCreatedType;
    }


    protected String getEmailCommentType()
    {
        return this.emailCommentType;
    }


    protected CsTicketState getOpenState()
    {
        return this.openState;
    }


    public String getCustomerNoteCommentType()
    {
        return this.customerNoteCommentType;
    }


    public String getAgentNoteCommentType()
    {
        return this.agentNoteCommentType;
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
