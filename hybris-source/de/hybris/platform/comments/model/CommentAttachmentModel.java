package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CommentAttachmentModel extends ItemModel
{
    public static final String _TYPECODE = "CommentAttachment";
    public static final String _ABSTRACTCOMMENTATTACHMENTRELATION = "AbstractCommentAttachmentRelation";
    public static final String ITEM = "item";
    public static final String ABSTRACTCOMMENT = "abstractComment";


    public CommentAttachmentModel()
    {
    }


    public CommentAttachmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentAttachmentModel(ItemModel _item)
    {
        setItem(_item);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentAttachmentModel(ItemModel _item, ItemModel _owner)
    {
        setItem(_item);
        setOwner(_owner);
    }


    @Accessor(qualifier = "abstractComment", type = Accessor.Type.GETTER)
    public AbstractCommentModel getAbstractComment()
    {
        return (AbstractCommentModel)getPersistenceContext().getPropertyValue("abstractComment");
    }


    @Accessor(qualifier = "item", type = Accessor.Type.GETTER)
    public ItemModel getItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("item");
    }


    @Accessor(qualifier = "abstractComment", type = Accessor.Type.SETTER)
    public void setAbstractComment(AbstractCommentModel value)
    {
        getPersistenceContext().setPropertyValue("abstractComment", value);
    }


    @Accessor(qualifier = "item", type = Accessor.Type.SETTER)
    public void setItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("item", value);
    }
}
