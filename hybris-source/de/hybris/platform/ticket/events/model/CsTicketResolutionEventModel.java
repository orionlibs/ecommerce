package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsResolutionType;

public class CsTicketResolutionEventModel extends CsCustomerEventModel
{
    public static final String _TYPECODE = "CsTicketResolutionEvent";
    public static final String RESOLUTIONTYPE = "resolutionType";


    public CsTicketResolutionEventModel()
    {
    }


    public CsTicketResolutionEventModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketResolutionEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, CsResolutionType _resolutionType, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setResolutionType(_resolutionType);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketResolutionEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, ItemModel _owner, CsResolutionType _resolutionType, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setOwner(_owner);
        setResolutionType(_resolutionType);
        setText(_text);
    }


    @Accessor(qualifier = "resolutionType", type = Accessor.Type.GETTER)
    public CsResolutionType getResolutionType()
    {
        return (CsResolutionType)getPersistenceContext().getPropertyValue("resolutionType");
    }


    @Accessor(qualifier = "resolutionType", type = Accessor.Type.SETTER)
    public void setResolutionType(CsResolutionType value)
    {
        getPersistenceContext().setPropertyValue("resolutionType", value);
    }
}
