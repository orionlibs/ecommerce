package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;

public class CsCustomerEventModel extends CsTicketEventModel
{
    public static final String _TYPECODE = "CsCustomerEvent";
    public static final String INTERVENTIONTYPE = "interventionType";
    public static final String REASON = "reason";


    public CsCustomerEventModel()
    {
    }


    public CsCustomerEventModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsCustomerEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsCustomerEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, ItemModel _owner, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setOwner(_owner);
        setText(_text);
    }


    @Accessor(qualifier = "interventionType", type = Accessor.Type.GETTER)
    public CsInterventionType getInterventionType()
    {
        return (CsInterventionType)getPersistenceContext().getPropertyValue("interventionType");
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.GETTER)
    public CsEventReason getReason()
    {
        return (CsEventReason)getPersistenceContext().getPropertyValue("reason");
    }


    @Accessor(qualifier = "interventionType", type = Accessor.Type.SETTER)
    public void setInterventionType(CsInterventionType value)
    {
        getPersistenceContext().setPropertyValue("interventionType", value);
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.SETTER)
    public void setReason(CsEventReason value)
    {
        getPersistenceContext().setPropertyValue("reason", value);
    }
}
