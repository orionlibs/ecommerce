package de.hybris.platform.payment.strategy;

import de.hybris.platform.core.model.order.OrderModel;

public interface MerchantTransactionCodeGenerator
{
    String getCode(OrderModel paramOrderModel);
}
