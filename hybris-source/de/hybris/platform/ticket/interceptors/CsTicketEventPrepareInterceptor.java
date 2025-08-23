package de.hybris.platform.ticket.interceptors;

import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Required;

public class CsTicketEventPrepareInterceptor implements PrepareInterceptor, InitDefaultsInterceptor
{
    private CommentService commentService;
    private UserService userService;
    private KeyGenerator keyGenerator;
    private String ticketSystemDomain = "ticketSystemDomain";
    private String ticketSystemComponent = "ticketSystem";


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CsTicketEventModel)
        {
            CsTicketEventModel event = (CsTicketEventModel)model;
            if(event.getCode() == null)
            {
                event.setCode(this.keyGenerator.generate().toString());
            }
        }
    }


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CsTicketEventModel)
        {
            CsTicketEventModel event = (CsTicketEventModel)model;
            UserModel user = this.userService.getCurrentUser();
            DomainModel domain = this.commentService.getDomainByCode(this.ticketSystemDomain);
            ComponentModel component = this.commentService.getComponentByCode(domain, this.ticketSystemComponent);
            event.setAuthor(user);
            event.setComponent(component);
            event.setStartDateTime(Calendar.getInstance().getTime());
            event.setEndDateTime(Calendar.getInstance().getTime());
            event.setSubject("");
        }
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
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
}
