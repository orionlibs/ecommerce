package de.hybris.platform.ordercancel.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderCancelRecordModel extends OrderModificationRecordModel
{
    public static final String _TYPECODE = "OrderCancelRecord";


    public OrderCancelRecordModel()
    {
    }


    public OrderCancelRecordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelRecordModel(boolean _inProgress, OrderModel _order)
    {
        setInProgress(_inProgress);
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelRecordModel(boolean _inProgress, OrderModel _order, ItemModel _owner)
    {
        setInProgress(_inProgress);
        setOrder(_order);
        setOwner(_owner);
    }
}
