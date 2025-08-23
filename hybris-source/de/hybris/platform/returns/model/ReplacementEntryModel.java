package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ReplacementEntryModel extends ReturnEntryModel
{
    public static final String _TYPECODE = "ReplacementEntry";
    public static final String REASON = "reason";


    public ReplacementEntryModel()
    {
    }


    public ReplacementEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplacementEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, ReplacementReason _reason, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setReason(_reason);
        setStatus(_status);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplacementEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, ItemModel _owner, ReplacementReason _reason, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
        setReason(_reason);
        setStatus(_status);
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.GETTER)
    public ReplacementReason getReason()
    {
        return (ReplacementReason)getPersistenceContext().getPropertyValue("reason");
    }


    @Accessor(qualifier = "reason", type = Accessor.Type.SETTER)
    public void setReason(ReplacementReason value)
    {
        getPersistenceContext().setPropertyValue("reason", value);
    }
}
