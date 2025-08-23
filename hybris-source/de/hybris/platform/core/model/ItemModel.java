package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

public class ItemModel extends AbstractItemModel
{
    public static final String _TYPECODE = "Item";
    public static final String _COMMENTITEMRELATION = "CommentItemRelation";
    public static final String CREATIONTIME = "creationtime";
    public static final String MODIFIEDTIME = "modifiedtime";
    public static final String ITEMTYPE = "itemtype";
    public static final String OWNER = "owner";
    public static final String PK = "pk";
    public static final String SEALED = "sealed";
    public static final String COMMENTS = "comments";


    public ItemModel()
    {
    }


    public ItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "comments", type = Accessor.Type.GETTER)
    public List<CommentModel> getComments()
    {
        return (List<CommentModel>)getPersistenceContext().getPropertyValue("comments");
    }


    @Accessor(qualifier = "creationtime", type = Accessor.Type.GETTER)
    public Date getCreationtime()
    {
        return (Date)getPersistenceContext().getPropertyValue("creationtime");
    }


    @Accessor(qualifier = "modifiedtime", type = Accessor.Type.GETTER)
    public Date getModifiedtime()
    {
        return (Date)getPersistenceContext().getPropertyValue("modifiedtime");
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public ItemModel getOwner()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("owner");
    }


    @Accessor(qualifier = "sealed", type = Accessor.Type.GETTER)
    public boolean isSealed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("sealed"));
    }


    @Accessor(qualifier = "comments", type = Accessor.Type.SETTER)
    public void setComments(List<CommentModel> value)
    {
        getPersistenceContext().setPropertyValue("comments", value);
    }


    @Accessor(qualifier = "creationtime", type = Accessor.Type.SETTER)
    public void setCreationtime(Date value)
    {
        getPersistenceContext().setPropertyValue("creationtime", value);
    }


    @Accessor(qualifier = "modifiedtime", type = Accessor.Type.SETTER)
    public void setModifiedtime(Date value)
    {
        getPersistenceContext().setPropertyValue("modifiedtime", value);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("owner", value);
    }
}
