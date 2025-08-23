package de.hybris.platform.returns.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderReturnRecordModel extends OrderModificationRecordModel
{
    public static final String _TYPECODE = "OrderReturnRecord";


    public OrderReturnRecordModel()
    {
    }


    public OrderReturnRecordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReturnRecordModel(boolean _inProgress, OrderModel _order)
    {
        setInProgress(_inProgress);
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReturnRecordModel(boolean _inProgress, OrderModel _order, ItemModel _owner)
    {
        setInProgress(_inProgress);
        setOrder(_order);
        setOwner(_owner);
    }
}
