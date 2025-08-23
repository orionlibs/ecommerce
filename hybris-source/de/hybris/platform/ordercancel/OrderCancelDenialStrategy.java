package de.hybris.platform.ordercancel;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;

public interface OrderCancelDenialStrategy
{
    OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel paramOrderCancelConfigModel, OrderModel paramOrderModel, PrincipalModel paramPrincipalModel, boolean paramBoolean1, boolean paramBoolean2);
}
