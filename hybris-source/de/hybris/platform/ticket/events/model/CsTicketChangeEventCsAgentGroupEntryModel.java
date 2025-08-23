package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.model.CsAgentGroupModel;

public class CsTicketChangeEventCsAgentGroupEntryModel extends CsTicketChangeEventEntryModel
{
    public static final String _TYPECODE = "CsTicketChangeEventCsAgentGroupEntry";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";


    public CsTicketChangeEventCsAgentGroupEntryModel()
    {
    }


    public CsTicketChangeEventCsAgentGroupEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CsTicketChangeEventCsAgentGroupEntryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public CsAgentGroupModel getNewValue()
    {
        return (CsAgentGroupModel)getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public CsAgentGroupModel getOldValue()
    {
        return (CsAgentGroupModel)getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(CsAgentGroupModel value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(CsAgentGroupModel value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }
}
