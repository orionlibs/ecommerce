package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CsTicketEventModel extends CommentModel
{
    public static final String _TYPECODE = "CsTicketEvent";
    public static final String STARTDATETIME = "startDateTime";
    public static final String ENDDATETIME = "endDateTime";
    public static final String TICKET = "ticket";
    public static final String EMAILS = "emails";
    public static final String ENTRIES = "entries";


    public CsTicketEventModel()
    {
    }


    public CsTicketEventModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketEventModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, ItemModel _owner, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setOwner(_owner);
        setText(_text);
    }


    @Accessor(qualifier = "emails", type = Accessor.Type.GETTER)
    public List<CsTicketEmailModel> getEmails()
    {
        return (List<CsTicketEmailModel>)getPersistenceContext().getPropertyValue("emails");
    }


    @Accessor(qualifier = "endDateTime", type = Accessor.Type.GETTER)
    public Date getEndDateTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDateTime");
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
    public Set<CsTicketChangeEventEntryModel> getEntries()
    {
        return (Set<CsTicketChangeEventEntryModel>)getPersistenceContext().getPropertyValue("entries");
    }


    @Accessor(qualifier = "startDateTime", type = Accessor.Type.GETTER)
    public Date getStartDateTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDateTime");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "ticket", type = Accessor.Type.GETTER)
    public CsTicketModel getTicket()
    {
        return (CsTicketModel)getPersistenceContext().getPropertyValue("ticket");
    }


    @Accessor(qualifier = "emails", type = Accessor.Type.SETTER)
    public void setEmails(List<CsTicketEmailModel> value)
    {
        getPersistenceContext().setPropertyValue("emails", value);
    }


    @Accessor(qualifier = "endDateTime", type = Accessor.Type.SETTER)
    public void setEndDateTime(Date value)
    {
        getPersistenceContext().setPropertyValue("endDateTime", value);
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
    public void setEntries(Set<CsTicketChangeEventEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("entries", value);
    }


    @Accessor(qualifier = "startDateTime", type = Accessor.Type.SETTER)
    public void setStartDateTime(Date value)
    {
        getPersistenceContext().setPropertyValue("startDateTime", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "ticket", type = Accessor.Type.SETTER)
    public void setTicket(CsTicketModel value)
    {
        getPersistenceContext().setPropertyValue("ticket", value);
    }
}
