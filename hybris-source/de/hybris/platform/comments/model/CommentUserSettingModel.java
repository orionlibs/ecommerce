package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CommentUserSettingModel extends ItemModel
{
    public static final String _TYPECODE = "CommentUserSetting";
    public static final String READ = "read";
    public static final String IGNORED = "ignored";
    public static final String PRIORITY = "priority";
    public static final String COMMENT = "comment";
    public static final String USER = "user";


    public CommentUserSettingModel()
    {
    }


    public CommentUserSettingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentUserSettingModel(AbstractCommentModel _comment, UserModel _user)
    {
        setComment(_comment);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentUserSettingModel(AbstractCommentModel _comment, ItemModel _owner, UserModel _user)
    {
        setComment(_comment);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public AbstractCommentModel getComment()
    {
        return (AbstractCommentModel)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "ignored", type = Accessor.Type.GETTER)
    public Boolean getIgnored()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("ignored");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "read", type = Accessor.Type.GETTER)
    public Boolean getRead()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("read");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(AbstractCommentModel value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "ignored", type = Accessor.Type.SETTER)
    public void setIgnored(Boolean value)
    {
        getPersistenceContext().setPropertyValue("ignored", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "read", type = Accessor.Type.SETTER)
    public void setRead(Boolean value)
    {
        getPersistenceContext().setPropertyValue("read", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
