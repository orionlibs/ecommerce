package de.hybris.platform.payment.order.strategies.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.order.strategies.ordercloning.impl.DefaultOrderPartOfMembersCloningStrategy;

public class PaymentOrderPartOfMembersCloningStrategy extends DefaultOrderPartOfMembersCloningStrategy
{
    public PaymentInfoModel clonePaymentInfoForOrder(PaymentInfoModel paymentInfo, OrderModel order)
    {
        PaymentInfoModel newPaymentInfo = super.clonePaymentInfoForOrder(paymentInfo, order);
        if(newPaymentInfo.getBillingAddress() != null)
        {
            newPaymentInfo.getBillingAddress().setOwner((ItemModel)newPaymentInfo);
            newPaymentInfo.getBillingAddress().setDuplicate(Boolean.TRUE);
            newPaymentInfo.getBillingAddress().setOriginal(paymentInfo.getBillingAddress());
        }
        return newPaymentInfo;
    }
}
