package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CsTicketChangeEventEntryModel extends ItemModel
{
    public static final String _TYPECODE = "CsTicketChangeEventEntry";
    public static final String _CSTICKETEVENT2CSTICKETCHANGEENTRY = "CsTicketEvent2CsTicketChangeEntry";
    public static final String ALTEREDATTRIBUTE = "alteredAttribute";
    public static final String OLDSTRINGVALUE = "oldStringValue";
    public static final String NEWSTRINGVALUE = "newStringValue";
    public static final String OLDBINARYVALUE = "oldBinaryValue";
    public static final String NEWBINARYVALUE = "newBinaryValue";
    public static final String EVENT = "event";


    public CsTicketChangeEventEntryModel()
    {
    }


    public CsTicketChangeEventEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "alteredAttribute", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getAlteredAttribute()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("alteredAttribute");
    }


    @Accessor(qualifier = "event", type = Accessor.Type.GETTER)
    public CsTicketEventModel getEvent()
    {
        return (CsTicketEventModel)getPersistenceContext().getPropertyValue("event");
    }


    @Accessor(qualifier = "newBinaryValue", type = Accessor.Type.GETTER)
    public Object getNewBinaryValue()
    {
        return getPersistenceContext().getPropertyValue("newBinaryValue");
    }


    @Accessor(qualifier = "newStringValue", type = Accessor.Type.GETTER)
    public String getNewStringValue()
    {
        return (String)getPersistenceContext().getPropertyValue("newStringValue");
    }


    @Accessor(qualifier = "oldBinaryValue", type = Accessor.Type.GETTER)
    public Object getOldBinaryValue()
    {
        return getPersistenceContext().getPropertyValue("oldBinaryValue");
    }


    @Accessor(qualifier = "oldStringValue", type = Accessor.Type.GETTER)
    public String getOldStringValue()
    {
        return (String)getPersistenceContext().getPropertyValue("oldStringValue");
    }


    @Accessor(qualifier = "alteredAttribute", type = Accessor.Type.SETTER)
    public void setAlteredAttribute(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("alteredAttribute", value);
    }


    @Accessor(qualifier = "event", type = Accessor.Type.SETTER)
    public void setEvent(CsTicketEventModel value)
    {
        getPersistenceContext().setPropertyValue("event", value);
    }


    @Accessor(qualifier = "newBinaryValue", type = Accessor.Type.SETTER)
    public void setNewBinaryValue(Object value)
    {
        getPersistenceContext().setPropertyValue("newBinaryValue", value);
    }


    @Accessor(qualifier = "newStringValue", type = Accessor.Type.SETTER)
    public void setNewStringValue(String value)
    {
        getPersistenceContext().setPropertyValue("newStringValue", value);
    }


    @Accessor(qualifier = "oldBinaryValue", type = Accessor.Type.SETTER)
    public void setOldBinaryValue(Object value)
    {
        getPersistenceContext().setPropertyValue("oldBinaryValue", value);
    }


    @Accessor(qualifier = "oldStringValue", type = Accessor.Type.SETTER)
    public void setOldStringValue(String value)
    {
        getPersistenceContext().setPropertyValue("oldStringValue", value);
    }
}
