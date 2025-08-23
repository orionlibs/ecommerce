package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.warehousing.util.builder.ComponentModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Components extends AbstractItems<ComponentModel>
{
    public static final String COMPONENT_CODE = "warehousing";
    public static final String COMPONENT_NAME = "Warehousing";
    public static final String TICKET_COMPONENT = "ticketSystem";
    private CommentService commentService;
    private Domains domains;


    public ComponentModel warehousingComponent()
    {
        return (ComponentModel)getOrSaveAndReturn(() -> getCommentService().getComponentForCode(getDomains().warehousingDomain(), "warehousing"), () -> ComponentModelBuilder.aModel().withCode("warehousing").withName("Warehousing").withDomain(getDomains().warehousingDomain()).build());
    }


    public ComponentModel ticketComponent()
    {
        return (ComponentModel)getOrSaveAndReturn(() -> getCommentService().getComponentForCode(getDomains().ticketSystem(), "ticketSystem"), () -> ComponentModelBuilder.aModel().withCode("ticketSystem").withDomain(getDomains().ticketSystem()).build());
    }


    protected CommentService getCommentService()
    {
        return this.commentService;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    public Domains getDomains()
    {
        return this.domains;
    }


    @Required
    public void setDomains(Domains domains)
    {
        this.domains = domains;
    }
}
