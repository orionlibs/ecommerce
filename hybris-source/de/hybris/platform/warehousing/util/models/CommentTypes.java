package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.warehousing.util.builder.CommentTypeModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class CommentTypes extends AbstractItems<CommentTypeModel>
{
    public static final String COMMENT_TYPE_CODE = "stockLevelAdjustmentNote";
    public static final String COMMENT_TYPE_NAME = "Stock Level Adjustment Note";
    public static final String TICKET_CREATION_EVENT_COMMENT = "ticketCreationEvent";
    private CommentService commentService;
    private Components components;
    private Domains domains;
    private ComposedTypes composedTypes;


    public CommentTypeModel adjustmentNote()
    {
        return (CommentTypeModel)getOrSaveAndReturn(() -> getCommentService().getCommentTypeForCode(getComponents().warehousingComponent(), "stockLevelAdjustmentNote"),
                        () -> CommentTypeModelBuilder.aModel().withCode("stockLevelAdjustmentNote").withName("Stock Level Adjustment Note").withDomain(getDomains().warehousingDomain()).build());
    }


    public CommentTypeModel ticketCreationEvent()
    {
        return (CommentTypeModel)getOrSaveAndReturn(() -> getCommentService().getCommentTypeForCode(getComponents().ticketComponent(), "ticketCreationEvent"),
                        () -> CommentTypeModelBuilder.aModel().withCode("ticketCreationEvent").withDomain(getDomains().ticketSystem()).withMetaType(getComposedTypes().customerEvent()).build());
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


    public Components getComponents()
    {
        return this.components;
    }


    @Required
    public void setComponents(Components components)
    {
        this.components = components;
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


    public ComposedTypes getComposedTypes()
    {
        return this.composedTypes;
    }


    @Required
    public void setComposedTypes(ComposedTypes composedTypes)
    {
        this.composedTypes = composedTypes;
    }
}
