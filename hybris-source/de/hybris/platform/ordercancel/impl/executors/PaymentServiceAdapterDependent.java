package de.hybris.platform.ordercancel.impl.executors;

import de.hybris.platform.ordercancel.OrderCancelPaymentServiceAdapter;

public interface PaymentServiceAdapterDependent
{
    void setPaymentServiceAdapter(OrderCancelPaymentServiceAdapter paramOrderCancelPaymentServiceAdapter);
}
