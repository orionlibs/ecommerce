package de.hybris.platform.ticket.service.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketAttachmentsService;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.strategies.TicketEventEmailStrategy;
import de.hybris.platform.ticket.strategies.TicketEventStrategy;
import de.hybris.platform.ticket.strategies.TicketRenderStrategy;
import de.hybris.platform.ticket.strategies.TicketResolutionStrategy;
import de.hybris.platform.ticket.strategies.TicketUpdateStrategy;
import de.hybris.platform.ticketsystem.data.CsTicketParameter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.multipart.MultipartFile;

public class DefaultTicketBusinessService implements TicketBusinessService
{
    private static final Logger LOG = Logger.getLogger(DefaultTicketBusinessService.class);
    private static final String CANNOT_UPDATE_TICKET_TO_NULL_STATE = "Cannot update ticket to null state";
    private static final String CANNOT_UPDATE_NULL_TICKET = "Cannot update null ticket";
    private static final String DEFAULT_SUBJECT_MESSAGE = "Support Ticket Status Updated from %s to  %s";
    private TicketUpdateStrategy ticketUpdateStrategy;
    private TicketEventStrategy ticketEventStrategy;
    private TicketEventEmailStrategy ticketEventEmailStrategy;
    private TicketResolutionStrategy ticketResolutionStrategy;
    private TicketRenderStrategy ticketRenderStrategy;
    private BaseSiteService baseSiteService;
    private Converter<CsTicketParameter, CsTicketModel> ticketParameterConverter;
    private TicketAttachmentsService ticketAttachmentsService;
    private UserService userService;
    private TimeService timeService;
    private ModelService modelService;


    public CsTicketModel createTicket(CsTicketParameter ticketParameter)
    {
        List<CommentAttachmentModel> attachments = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ticketParameter.getAttachments()))
        {
            for(MultipartFile file : ticketParameter.getAttachments())
            {
                try
                {
                    CommentAttachmentModel attachmentModel = (CommentAttachmentModel)getModelService().create(CommentAttachmentModel.class);
                    attachmentModel.setItem((ItemModel)getTicketAttachmentsService().createAttachment(file.getOriginalFilename(), file
                                    .getContentType(), file.getBytes(), getUserService().getCurrentUser()));
                    attachments.add(attachmentModel);
                }
                catch(IOException e)
                {
                    LOG.error(e.getMessage(), e);
                    return null;
                }
            }
        }
        CsTicketModel ticket = (CsTicketModel)this.ticketParameterConverter.convert(ticketParameter);
        CsCustomerEventModel creationEvent = this.ticketEventStrategy.createCreationEventForTicket(ticket, ticketParameter
                        .getReason(), ticketParameter.getInterventionType(), ticketParameter.getCreationNotes());
        attachments.forEach(attachmentModel -> attachmentModel.setAbstractComment((AbstractCommentModel)creationEvent));
        getModelService().saveAll(attachments);
        return createTicketInternal(ticket, creationEvent);
    }


    @Deprecated(since = "6.0", forRemoval = true)
    public CsTicketModel createTicket(CsTicketModel ticket, CsCustomerEventModel creationEvent)
    {
        return createTicketInternal(ticket, creationEvent);
    }


    protected CsTicketModel createTicketInternal(CsTicketModel ticket, CsCustomerEventModel creationEvent)
    {
        BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
        if(currentBaseSite != null)
        {
            ticket.setBaseSite(currentBaseSite);
        }
        this.ticketEventStrategy.ensureTicketEventSetupForCreationEvent(ticket, creationEvent);
        getModelService().saveAll(new Object[] {ticket, creationEvent});
        getModelService().refresh(ticket);
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, (CsTicketEventModel)creationEvent);
        return ticket;
    }


    protected CsTicketModel populateTicketDetails(UserModel customer, AbstractOrderModel abstractOrder, CsTicketCategory category, CsTicketPriority priority, EmployeeModel assignedAgent, CsAgentGroupModel assignedGroup, String headline)
    {
        CsTicketModel ticket = (CsTicketModel)getModelService().create(CsTicketModel.class);
        ticket.setCustomer(customer);
        ticket.setOrder(abstractOrder);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setAssignedAgent(assignedAgent);
        ticket.setAssignedGroup(assignedGroup);
        ticket.setHeadline(headline);
        BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
        if(currentBaseSite != null)
        {
            ticket.setBaseSite(currentBaseSite);
        }
        else if(abstractOrder != null && abstractOrder.getSite() != null)
        {
            ticket.setBaseSite(abstractOrder.getSite());
        }
        return ticket;
    }


    public CsTicketModel updateTicket(CsTicketModel ticket) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot update null ticket");
        }
        CsTicketModel ret = this.ticketUpdateStrategy.updateTicket(ticket);
        getModelService().refresh(ticket);
        this.ticketEventEmailStrategy.sendEmailsForEvent(ret, getLastEvent(ret));
        return ret;
    }


    public CsTicketModel updateTicket(CsTicketModel ticket, String note) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot update null ticket");
        }
        if(note == null || "".equals(note))
        {
            throw new IllegalArgumentException("No note text found");
        }
        CsTicketModel ret = this.ticketUpdateStrategy.updateTicket(ticket, note);
        getModelService().save(ret);
        this.ticketEventEmailStrategy.sendEmailsForEvent(ret, getLastEvent(ret));
        return ret;
    }


    public CsTicketModel setTicketState(CsTicketModel ticket, CsTicketState newState) throws TicketException
    {
        return setTicketState(ticket, newState, "");
    }


    public CsTicketModel setTicketState(CsTicketModel ticket, CsTicketState newState, String note) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot update null ticket");
        }
        if(newState == null)
        {
            throw new IllegalArgumentException("Cannot update ticket to null state");
        }
        String oldState = ticket.getState().getCode();
        this.ticketUpdateStrategy.setTicketState(ticket, newState, note);
        getModelService().refresh(ticket);
        CsTicketEventModel lastEvent = getLastEvent(ticket);
        lastEvent.setSubject(String.format("Support Ticket Status Updated from %s to  %s", new Object[] {oldState, newState}));
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, getLastEvent(ticket));
        return ticket;
    }


    public List<CsTicketState> getTicketNextStates(CsTicketModel ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("ticket must not be null");
        }
        return this.ticketResolutionStrategy
                        .filterTicketStatesToRemovedClosedStates(this.ticketUpdateStrategy.getTicketNextStates(ticket.getState()));
    }


    public List<CsTicketState> getTicketNextStates(CsTicketState state)
    {
        if(state == null)
        {
            throw new IllegalArgumentException("state must not be null");
        }
        return this.ticketResolutionStrategy.filterTicketStatesToRemovedClosedStates(this.ticketUpdateStrategy.getTicketNextStates(state));
    }


    public CsTicketModel assignTicketToAgent(CsTicketModel ticket, EmployeeModel agent) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot update null ticket");
        }
        CsTicketEventModel csTicketEventModel = this.ticketUpdateStrategy.assignTicketToAgent(ticket, agent);
        getModelService().refresh(ticket);
        if(csTicketEventModel != null)
        {
            this.ticketEventEmailStrategy.sendEmailsForAssignAgentTicketEvent(ticket, csTicketEventModel, CsEmailRecipients.ASSIGNEDAGENT);
        }
        return ticket;
    }


    public CsTicketModel assignTicketToGroup(CsTicketModel ticket, CsAgentGroupModel group) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot update null ticket");
        }
        CsTicketEventModel csTicketEventModel = this.ticketUpdateStrategy.assignTicketToGroup(ticket, group);
        getModelService().refresh(ticket);
        if(csTicketEventModel != null)
        {
            this.ticketEventEmailStrategy.sendEmailsForAssignAgentTicketEvent(ticket, csTicketEventModel, CsEmailRecipients.ASSIGNEDGROUP);
        }
        return ticket;
    }


    public CsCustomerEventModel addNoteToTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note, Collection<MediaModel> attachments)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot add note to null ticket");
        }
        if(intervention == null || reason == null || note == null || "".equals(note))
        {
            throw new IllegalArgumentException("Missing arguments required to create note");
        }
        CsCustomerEventModel ret = this.ticketEventStrategy.createNoteForTicket(ticket, intervention, reason, note, attachments);
        getModelService().save(ret);
        getModelService().saveAll(ret.getAttachments());
        getModelService().refresh(ticket);
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, (CsTicketEventModel)ret);
        return ret;
    }


    public CsCustomerEventModel addCustomerEmailToTicket(CsTicketModel ticket, CsEventReason reason, String subject, String emailBody, Collection<MediaModel> attachments)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot add email to null ticket");
        }
        if(StringUtils.isBlank(subject) || reason == null || StringUtils.isBlank(emailBody))
        {
            throw new IllegalArgumentException("Missing arguments required to create email");
        }
        CsCustomerEventModel ret = this.ticketEventStrategy.createCustomerEmailForTicket(ticket, reason, subject, emailBody, attachments);
        getModelService().save(ret);
        getModelService().saveAll(ret.getAttachments());
        getModelService().refresh(ticket);
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, (CsTicketEventModel)ret);
        return ret;
    }


    public CsTicketResolutionEventModel resolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsResolutionType resolutionType, String note) throws TicketException
    {
        return resolveTicket(ticket, intervention, resolutionType, note, Collections.emptyList());
    }


    public CsTicketResolutionEventModel resolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsResolutionType resolutionType, String note, Collection<MediaModel> attachments) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot resolve null ticket");
        }
        if(intervention == null || resolutionType == null || note == null || "".equals(note))
        {
            throw new IllegalArgumentException("Missing arguments required to resolve ticket");
        }
        CsTicketResolutionEventModel ret = this.ticketResolutionStrategy.resolveTicket(ticket, intervention, resolutionType, note, attachments);
        getModelService().save(ret);
        ticket.setResolution(ret);
        ticket.setRetentionDate(getTimeService().getCurrentTime());
        getModelService().save(ticket);
        getModelService().refresh(ticket);
        LOG.info("Ticket [" + ticket.getTicketID() + "] has been marked as resolved with resolution type [" + resolutionType
                        .getCode() + "]");
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, (CsTicketEventModel)ret);
        return ret;
    }


    public CsCustomerEventModel unResolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note) throws TicketException
    {
        return unResolveTicket(ticket, intervention, reason, note, Collections.emptyList());
    }


    public CsCustomerEventModel unResolveTicket(CsTicketModel ticket, CsInterventionType intervention, CsEventReason reason, String note, Collection<MediaModel> attachments) throws TicketException
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot unresolve null ticket");
        }
        if(intervention == null || reason == null || note == null || "".equals(note))
        {
            throw new IllegalArgumentException("Missing arguments required to unresolve ticket");
        }
        CsCustomerEventModel ret = this.ticketResolutionStrategy.unResolveTicket(ticket, intervention, reason, note, attachments);
        getModelService().save(ret);
        ticket.setResolution(null);
        ticket.setRetentionDate(null);
        getModelService().save(ticket);
        getModelService().refresh(ticket);
        LOG.info("Ticket [" + ticket.getTicketID() + "] has been marked as unresolved");
        this.ticketEventEmailStrategy.sendEmailsForEvent(ticket, (CsTicketEventModel)ret);
        return ret;
    }


    public boolean isTicketClosed(CsTicketModel ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot close null ticket");
        }
        return this.ticketResolutionStrategy.isTicketClosed(ticket);
    }


    public boolean isTicketResolvable(CsTicketModel ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("Cannot resolve null ticket");
        }
        return this.ticketResolutionStrategy.isTicketResolvable(ticket);
    }


    public CsTicketEventModel getLastEvent(CsTicketModel ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("ticket must not be null.");
        }
        if(!ticket.getEvents().isEmpty())
        {
            return ticket.getEvents().get(ticket.getEvents().size() - 1);
        }
        LOG.error("Not events found for ticket [" + ticket + "] when looking for last event");
        return null;
    }


    public String renderTicketEventText(CsTicketEventModel ticketEvent)
    {
        return this.ticketRenderStrategy.renderTicketEvent(ticketEvent);
    }


    @Required
    public void setTicketUpdateStrategy(TicketUpdateStrategy ticketUpdateStrategy)
    {
        this.ticketUpdateStrategy = ticketUpdateStrategy;
    }


    @Required
    public void setTicketEventStrategy(TicketEventStrategy ticketEventStrategy)
    {
        this.ticketEventStrategy = ticketEventStrategy;
    }


    @Required
    public void setTicketEventEmailStrategy(TicketEventEmailStrategy ticketEventEmailStrategy)
    {
        this.ticketEventEmailStrategy = ticketEventEmailStrategy;
    }


    @Required
    public void setTicketResolutionStrategy(TicketResolutionStrategy ticketResolutionStrategy)
    {
        this.ticketResolutionStrategy = ticketResolutionStrategy;
    }


    @Required
    public void setTicketRenderStrategy(TicketRenderStrategy ticketRenderStrategy)
    {
        this.ticketRenderStrategy = ticketRenderStrategy;
    }


    public TicketEventEmailStrategy getTicketEventEmailStrategy()
    {
        return this.ticketEventEmailStrategy;
    }


    public BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected Converter<CsTicketParameter, CsTicketModel> getTicketParameterConverter()
    {
        return this.ticketParameterConverter;
    }


    @Required
    public void setTicketParameterConverter(Converter<CsTicketParameter, CsTicketModel> ticketParameterConverter)
    {
        this.ticketParameterConverter = ticketParameterConverter;
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


    protected TicketAttachmentsService getTicketAttachmentsService()
    {
        return this.ticketAttachmentsService;
    }


    @Required
    public void setTicketAttachmentsService(TicketAttachmentsService ticketAttachmentsService)
    {
        this.ticketAttachmentsService = ticketAttachmentsService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
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
