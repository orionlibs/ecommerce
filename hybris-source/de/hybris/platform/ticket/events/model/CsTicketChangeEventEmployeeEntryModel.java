package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CsTicketChangeEventEmployeeEntryModel extends CsTicketChangeEventEntryModel
{
    public static final String _TYPECODE = "CsTicketChangeEventEmployeeEntry";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";


    public CsTicketChangeEventEmployeeEntryModel()
    {
    }


    public CsTicketChangeEventEmployeeEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventEmployeeEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public EmployeeModel getNewValue()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public EmployeeModel getOldValue()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }
}
