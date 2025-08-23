package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelPaymentServiceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmsOrderCancelPaymentServiceAdapter implements OrderCancelPaymentServiceAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(OmsOrderCancelPaymentServiceAdapter.class);


    public void recalculateOrderAndModifyPayments(OrderModel order)
    {
        LOG.info("Recalculate and modify payments for order: {}", order.getCode());
    }
}
