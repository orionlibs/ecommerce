package de.hybris.platform.ticket.factory.impl;

import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.factory.TicketEventFactory;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;

public class DefaultTicketEventFactoryImpl implements TicketEventFactory
{
    private CommentService commentService;
    private UserService userService;
    private ModelService modelService;
    private Tenant currentTenant;
    private String ticketSystemDomain = "ticketSystemDomain";
    private String ticketSystemComponent = "ticketSystem";
    private String defaultCommentType = "customerNote";
    protected SessionService sessionService;
    @Deprecated(since = "6.7", forRemoval = true)
    protected PlatformTransactionManager txManager;


    public CsTicketEventModel createEvent(String type)
    {
        UserModel user = this.userService.getCurrentUser();
        DomainModel domain = this.commentService.getDomainForCode(this.ticketSystemDomain);
        ComponentModel component = this.commentService.getComponentForCode(domain, this.ticketSystemComponent);
        CommentTypeModel commentType = this.commentService.getCommentTypeForCode(component,
                        StringUtils.isNotEmpty(type) ? type : this.defaultCommentType);
        CsTicketEventModel event = (CsTicketEventModel)getModelService().create(commentType.getMetaType().getCode());
        event.setAuthor(user);
        event.setComponent(component);
        event.setCommentType(commentType);
        event.setStartDateTime(Calendar.getInstance().getTime());
        event.setEndDateTime(Calendar.getInstance().getTime());
        event.setSubject("");
        return event;
    }


    public CsTicketEventModel ensureTicketSetup(CsTicketEventModel event, String type)
    {
        UserModel user = this.userService.getCurrentUser();
        DomainModel domain = this.commentService.getDomainForCode(this.ticketSystemDomain);
        ComponentModel component = this.commentService.getComponentForCode(domain, this.ticketSystemComponent);
        CommentTypeModel commentType = this.commentService.getCommentTypeForCode(component,
                        StringUtils.isNotEmpty(type) ? type : this.defaultCommentType);
        if(event.getAuthor() == null)
        {
            event.setAuthor(user);
        }
        if(event.getComponent() == null)
        {
            event.setComponent(component);
        }
        if(event.getCommentType() == null)
        {
            event.setCommentType(commentType);
        }
        if(event.getStartDateTime() == null)
        {
            event.setStartDateTime(Calendar.getInstance().getTime());
        }
        if(event.getEndDateTime() == null)
        {
            event.setEndDateTime(Calendar.getInstance().getTime());
        }
        if(event.getSubject() == null)
        {
            event.setSubject("");
        }
        return event;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setTicketSystemDomain(String ticketSystemDomain)
    {
        this.ticketSystemDomain = ticketSystemDomain;
    }


    public void setTicketSystemComponent(String ticketSystemComponent)
    {
        this.ticketSystemComponent = ticketSystemComponent;
    }


    public void setDefaultCommentType(String defaultCommentType)
    {
        this.defaultCommentType = defaultCommentType;
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


    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
    }


    protected Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setTxManager(PlatformTransactionManager txManager)
    {
        this.txManager = txManager;
    }


    protected PlatformTransactionManager getTxManager()
    {
        return this.txManager;
    }
}
