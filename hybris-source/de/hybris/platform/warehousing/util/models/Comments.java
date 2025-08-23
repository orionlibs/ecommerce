package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.warehousing.util.builder.CommentModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Comments extends AbstractItems<CommentModel>
{
    public static final String COMMENT_TEXT = "Test comment";
    private Components components;
    private Users users;
    private CommentTypes commentTypes;


    public CommentModel commentAdjustmentNote()
    {
        CommentModel model = CommentModelBuilder.aModel().withText("Test comment").withComponent(getComponents().warehousingComponent()).withAuthor(getUsers().Bob()).withCommentType(getCommentTypes().adjustmentNote()).build();
        getModelService().save(model);
        return model;
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


    public Users getUsers()
    {
        return this.users;
    }


    @Required
    public void setUsers(Users users)
    {
        this.users = users;
    }


    public CommentTypes getCommentTypes()
    {
        return this.commentTypes;
    }


    @Required
    public void setCommentTypes(CommentTypes commentTypes)
    {
        this.commentTypes = commentTypes;
    }
}
