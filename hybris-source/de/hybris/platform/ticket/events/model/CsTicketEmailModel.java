package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CsTicketEmailModel extends ItemModel
{
    public static final String _TYPECODE = "CsTicketEmail";
    public static final String _CSTICKETEVENT2CSTICKETEMAIL = "CsTicketEvent2CsTicketEmail";
    public static final String MESSAGEID = "messageId";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String TICKETPOS = "ticketPOS";
    public static final String TICKET = "ticket";


    public CsTicketEmailModel()
    {
    }


    public CsTicketEmailModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketEmailModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "body", type = Accessor.Type.GETTER)
    public String getBody()
    {
        return (String)getPersistenceContext().getPropertyValue("body");
    }


    @Accessor(qualifier = "from", type = Accessor.Type.GETTER)
    public String getFrom()
    {
        return (String)getPersistenceContext().getPropertyValue("from");
    }


    @Accessor(qualifier = "messageId", type = Accessor.Type.GETTER)
    public String getMessageId()
    {
        return (String)getPersistenceContext().getPropertyValue("messageId");
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
    public String getSubject()
    {
        return (String)getPersistenceContext().getPropertyValue("subject");
    }


    @Accessor(qualifier = "ticket", type = Accessor.Type.GETTER)
    public CsTicketEventModel getTicket()
    {
        return (CsTicketEventModel)getPersistenceContext().getPropertyValue("ticket");
    }


    @Accessor(qualifier = "to", type = Accessor.Type.GETTER)
    public String getTo()
    {
        return (String)getPersistenceContext().getPropertyValue("to");
    }


    @Accessor(qualifier = "body", type = Accessor.Type.SETTER)
    public void setBody(String value)
    {
        getPersistenceContext().setPropertyValue("body", value);
    }


    @Accessor(qualifier = "from", type = Accessor.Type.SETTER)
    public void setFrom(String value)
    {
        getPersistenceContext().setPropertyValue("from", value);
    }


    @Accessor(qualifier = "messageId", type = Accessor.Type.SETTER)
    public void setMessageId(String value)
    {
        getPersistenceContext().setPropertyValue("messageId", value);
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
    public void setSubject(String value)
    {
        getPersistenceContext().setPropertyValue("subject", value);
    }


    @Accessor(qualifier = "ticket", type = Accessor.Type.SETTER)
    public void setTicket(CsTicketEventModel value)
    {
        getPersistenceContext().setPropertyValue("ticket", value);
    }


    @Accessor(qualifier = "to", type = Accessor.Type.SETTER)
    public void setTo(String value)
    {
        getPersistenceContext().setPropertyValue("to", value);
    }
}
