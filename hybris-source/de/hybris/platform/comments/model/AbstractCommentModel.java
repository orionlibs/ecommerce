package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AbstractCommentModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractComment";
    public static final String _COMMENTUSERSETTINGABSTRACTCOMMENTRELATION = "CommentUserSettingAbstractCommentRelation";
    public static final String SUBJECT = "subject";
    public static final String TEXT = "text";
    public static final String ATTACHMENTS = "attachments";
    public static final String AUTHOR = "author";
    public static final String COMMENTUSERSETTINGS = "commentUserSettings";


    public AbstractCommentModel()
    {
    }


    public AbstractCommentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCommentModel(UserModel _author, String _text)
    {
        setAuthor(_author);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCommentModel(UserModel _author, ItemModel _owner, String _text)
    {
        setAuthor(_author);
        setOwner(_owner);
        setText(_text);
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
    public Collection<CommentAttachmentModel> getAttachments()
    {
        return (Collection<CommentAttachmentModel>)getPersistenceContext().getPropertyValue("attachments");
    }


    @Accessor(qualifier = "author", type = Accessor.Type.GETTER)
    public UserModel getAuthor()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("author");
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
    public String getSubject()
    {
        return (String)getPersistenceContext().getPropertyValue("subject");
    }


    @Accessor(qualifier = "text", type = Accessor.Type.GETTER)
    public String getText()
    {
        return (String)getPersistenceContext().getPropertyValue("text");
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
    public void setAttachments(Collection<CommentAttachmentModel> value)
    {
        getPersistenceContext().setPropertyValue("attachments", value);
    }


    @Accessor(qualifier = "author", type = Accessor.Type.SETTER)
    public void setAuthor(UserModel value)
    {
        getPersistenceContext().setPropertyValue("author", value);
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
    public void setSubject(String value)
    {
        getPersistenceContext().setPropertyValue("subject", value);
    }


    @Accessor(qualifier = "text", type = Accessor.Type.SETTER)
    public void setText(String value)
    {
        getPersistenceContext().setPropertyValue("text", value);
    }
}
