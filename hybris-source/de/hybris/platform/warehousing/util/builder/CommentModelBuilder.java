package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.user.UserModel;

public class CommentModelBuilder
{
    private final CommentModel model = new CommentModel();


    private CommentModel getModel()
    {
        return this.model;
    }


    public static CommentModelBuilder aModel()
    {
        return new CommentModelBuilder();
    }


    public CommentModel build()
    {
        return getModel();
    }


    public CommentModelBuilder withComponent(ComponentModel component)
    {
        getModel().setComponent(component);
        return this;
    }


    public CommentModelBuilder withAuthor(UserModel user)
    {
        getModel().setAuthor(user);
        return this;
    }


    public CommentModelBuilder withCommentType(CommentTypeModel commentType)
    {
        getModel().setCommentType(commentType);
        return this;
    }


    public CommentModelBuilder withText(String text)
    {
        getModel().setText(text);
        return this;
    }
}
