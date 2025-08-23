package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CsTicketChangeEventStringEntryModel extends CsTicketChangeEventEntryModel
{
    public static final String _TYPECODE = "CsTicketChangeEventStringEntry";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";


    public CsTicketChangeEventStringEntryModel()
    {
    }


    public CsTicketChangeEventStringEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventStringEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public String getNewValue()
    {
        return (String)getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public String getOldValue()
    {
        return (String)getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(String value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(String value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }
}
