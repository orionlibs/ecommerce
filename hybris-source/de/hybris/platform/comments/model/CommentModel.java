package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class CommentModel extends AbstractCommentModel
{
    public static final String _TYPECODE = "Comment";
    public static final String CODE = "code";
    public static final String PRIORITY = "priority";
    public static final String RELATEDITEMS = "relatedItems";
    public static final String REPLIES = "replies";
    public static final String ASSIGNEDTO = "assignedTo";
    public static final String WATCHERS = "watchers";
    public static final String COMPONENT = "component";
    public static final String COMMENTTYPE = "commentType";


    public CommentModel()
    {
    }


    public CommentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, ItemModel _owner, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setOwner(_owner);
        setText(_text);
    }


    @Accessor(qualifier = "assignedTo", type = Accessor.Type.GETTER)
    public Collection<UserModel> getAssignedTo()
    {
        return (Collection<UserModel>)getPersistenceContext().getPropertyValue("assignedTo");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "commentType", type = Accessor.Type.GETTER)
    public CommentTypeModel getCommentType()
    {
        return (CommentTypeModel)getPersistenceContext().getPropertyValue("commentType");
    }


    @Accessor(qualifier = "component", type = Accessor.Type.GETTER)
    public ComponentModel getComponent()
    {
        return (ComponentModel)getPersistenceContext().getPropertyValue("component");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "relatedItems", type = Accessor.Type.GETTER)
    public Collection<ItemModel> getRelatedItems()
    {
        return (Collection<ItemModel>)getPersistenceContext().getPropertyValue("relatedItems");
    }


    @Accessor(qualifier = "replies", type = Accessor.Type.GETTER)
    public List<ReplyModel> getReplies()
    {
        return (List<ReplyModel>)getPersistenceContext().getPropertyValue("replies");
    }


    @Accessor(qualifier = "watchers", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getWatchers()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getPropertyValue("watchers");
    }


    @Accessor(qualifier = "assignedTo", type = Accessor.Type.SETTER)
    public void setAssignedTo(Collection<UserModel> value)
    {
        getPersistenceContext().setPropertyValue("assignedTo", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "commentType", type = Accessor.Type.SETTER)
    public void setCommentType(CommentTypeModel value)
    {
        getPersistenceContext().setPropertyValue("commentType", value);
    }


    @Accessor(qualifier = "component", type = Accessor.Type.SETTER)
    public void setComponent(ComponentModel value)
    {
        getPersistenceContext().setPropertyValue("component", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "relatedItems", type = Accessor.Type.SETTER)
    public void setRelatedItems(Collection<ItemModel> value)
    {
        getPersistenceContext().setPropertyValue("relatedItems", value);
    }


    @Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
    public void setReplies(List<ReplyModel> value)
    {
        getPersistenceContext().setPropertyValue("replies", value);
    }


    @Accessor(qualifier = "watchers", type = Accessor.Type.SETTER)
    public void setWatchers(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("watchers", value);
    }
}
