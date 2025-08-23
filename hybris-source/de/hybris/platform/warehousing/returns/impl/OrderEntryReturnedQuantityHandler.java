package de.hybris.platform.warehousing.returns.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.warehousing.orderentry.service.OrderEntryQuantityService;
import org.springframework.beans.factory.annotation.Required;

public class OrderEntryReturnedQuantityHandler implements DynamicAttributeHandler<Long, OrderEntryModel>
{
    private OrderEntryQuantityService orderEntryQuantityService;


    public Long get(OrderEntryModel orderEntry)
    {
        return getOrderEntryQuantityService().getQuantityReturned(orderEntry);
    }


    public void set(OrderEntryModel orderEntry, Long quantity)
    {
        throw new UnsupportedOperationException();
    }


    public OrderEntryQuantityService getOrderEntryQuantityService()
    {
        return this.orderEntryQuantityService;
    }


    @Required
    public void setOrderEntryQuantityService(OrderEntryQuantityService orderEntryQuantityService)
    {
        this.orderEntryQuantityService = orderEntryQuantityService;
    }
}
