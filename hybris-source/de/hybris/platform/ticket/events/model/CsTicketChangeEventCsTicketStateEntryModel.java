package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsTicketState;

public class CsTicketChangeEventCsTicketStateEntryModel extends CsTicketChangeEventEntryModel
{
    public static final String _TYPECODE = "CsTicketChangeEventCsTicketStateEntry";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";


    public CsTicketChangeEventCsTicketStateEntryModel()
    {
    }


    public CsTicketChangeEventCsTicketStateEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventCsTicketStateEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public CsTicketState getNewValue()
    {
        return (CsTicketState)getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public CsTicketState getOldValue()
    {
        return (CsTicketState)getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(CsTicketState value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(CsTicketState value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }
}
