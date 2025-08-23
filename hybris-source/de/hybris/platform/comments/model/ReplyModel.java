package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class ReplyModel extends AbstractCommentModel
{
    public static final String _TYPECODE = "Reply";
    public static final String _REPLYPARENTRELATION = "ReplyParentRelation";
    public static final String _COMMENTREPLYRELATION = "CommentReplyRelation";
    public static final String REPLIES = "replies";
    public static final String PARENTPOS = "parentPOS";
    public static final String PARENT = "parent";
    public static final String COMMENTPOS = "commentPOS";
    public static final String COMMENT = "comment";


    public ReplyModel()
    {
    }


    public ReplyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplyModel(UserModel _author, CommentModel _comment, String _text)
    {
        setAuthor(_author);
        setComment(_comment);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplyModel(UserModel _author, CommentModel _comment, ItemModel _owner, ReplyModel _parent, String _text)
    {
        setAuthor(_author);
        setComment(_comment);
        setOwner(_owner);
        setParent(_parent);
        setText(_text);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public CommentModel getComment()
    {
        return (CommentModel)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
    public ReplyModel getParent()
    {
        return (ReplyModel)getPersistenceContext().getPropertyValue("parent");
    }


    @Accessor(qualifier = "replies", type = Accessor.Type.GETTER)
    public List<ReplyModel> getReplies()
    {
        return (List<ReplyModel>)getPersistenceContext().getPropertyValue("replies");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(CommentModel value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
    public void setParent(ReplyModel value)
    {
        getPersistenceContext().setPropertyValue("parent", value);
    }


    @Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
    public void setReplies(List<ReplyModel> value)
    {
        getPersistenceContext().setPropertyValue("replies", value);
    }
}
