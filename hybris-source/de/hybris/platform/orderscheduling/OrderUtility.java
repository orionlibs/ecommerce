package de.hybris.platform.orderscheduling;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;

public interface OrderUtility
{
    void runOrder(OrderModel paramOrderModel);


    OrderModel createOrderFromOrderTemplate(OrderModel paramOrderModel);


    OrderModel createOrderFromCart(CartModel paramCartModel, AddressModel paramAddressModel1, AddressModel paramAddressModel2, PaymentInfoModel paramPaymentInfoModel) throws InvalidCartException;


    OrderModel runScheduledOrder(OrderModel paramOrderModel);
}
