package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class B2BCommentModel extends ItemModel
{
    public static final String _TYPECODE = "B2BComment";
    public static final String _ABSTRACTORDER2B2BCOMMENT = "AbstractOrder2B2BComment";
    public static final String CODE = "code";
    public static final String COMMENT = "comment";
    public static final String MODIFIEDDATE = "modifiedDate";
    public static final String ORDER = "order";


    public B2BCommentModel()
    {
    }


    public B2BCommentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCommentModel(UserModel _owner)
    {
        setOwner((ItemModel)_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "modifiedDate", type = Accessor.Type.GETTER)
    public Date getModifiedDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("modifiedDate");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public UserModel getOwner()
    {
        return (UserModel)super.getOwner();
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "modifiedDate", type = Accessor.Type.SETTER)
    public void setModifiedDate(Date value)
    {
        getPersistenceContext().setPropertyValue("modifiedDate", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        if(value == null || value instanceof UserModel)
        {
            super.setOwner(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.user.UserModel");
        }
    }
}
