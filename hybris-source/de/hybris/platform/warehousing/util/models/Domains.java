package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.warehousing.util.builder.DomainModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Domains extends AbstractItems<DomainModel>
{
    public static final String DOMAIN_CODE = "warehousingDomain";
    public static final String DOMAIN_NAME = "Warehousing Domain";
    public static final String TICKET_SYSTEM_DOMAIN = "ticketSystemDomain";
    private CommentService commentService;


    public DomainModel warehousingDomain()
    {
        return (DomainModel)getOrSaveAndReturn(() -> getCommentService().getDomainForCode("warehousingDomain"), () -> DomainModelBuilder.aModel().withCode("warehousingDomain").withName("Warehousing Domain").build());
    }


    public DomainModel ticketSystem()
    {
        return (DomainModel)getOrSaveAndReturn(() -> getCommentService().getDomainForCode("ticketSystemDomain"), () -> DomainModelBuilder.aModel().withCode("ticketSystemDomain").build());
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
}
