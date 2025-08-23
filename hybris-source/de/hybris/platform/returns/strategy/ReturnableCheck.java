package de.hybris.platform.returns.strategy;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

public interface ReturnableCheck
{
    boolean perform(OrderModel paramOrderModel, AbstractOrderEntryModel paramAbstractOrderEntryModel, long paramLong);


    default long maxReturnQuantity(OrderModel order, AbstractOrderEntryModel entry)
    {
        return entry.getQuantity().longValue();
    }
}
