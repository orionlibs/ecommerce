package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsTicketCategory;

public class CsTicketChangeEventCsTicketCategoryEntryModel extends CsTicketChangeEventEntryModel
{
    public static final String _TYPECODE = "CsTicketChangeEventCsTicketCategoryEntry";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";


    public CsTicketChangeEventCsTicketCategoryEntryModel()
    {
    }


    public CsTicketChangeEventCsTicketCategoryEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventCsTicketCategoryEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public CsTicketCategory getNewValue()
    {
        return (CsTicketCategory)getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public CsTicketCategory getOldValue()
    {
        return (CsTicketCategory)getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(CsTicketCategory value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(CsTicketCategory value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }
}
