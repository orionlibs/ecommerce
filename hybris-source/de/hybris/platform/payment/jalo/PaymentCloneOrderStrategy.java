package de.hybris.platform.payment.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.DefaultCloneOrderStrategy;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.payment.constants.GeneratedPaymentConstants;

public class PaymentCloneOrderStrategy extends DefaultCloneOrderStrategy
{
    public Order clone(ComposedType orderType, ComposedType entryType, AbstractOrder originalOrder, OrderManager orderManager)
    {
        Order clonedOrder = super.clone(orderType, entryType, originalOrder, orderManager);
        if(clonedOrder.getPaymentInfo() != null)
        {
            Address clonedBillingAddress = (Address)clonedOrder.getPaymentInfo().getProperty(GeneratedPaymentConstants.Attributes.PaymentInfo.BILLINGADDRESS);
            Address originalBillingAddress = (Address)originalOrder.getPaymentInfo().getProperty(GeneratedPaymentConstants.Attributes.PaymentInfo.BILLINGADDRESS);
            if(clonedBillingAddress != null)
            {
                clonedBillingAddress.setDuplicate(Boolean.TRUE);
                clonedBillingAddress.setOriginal(originalBillingAddress);
                try
                {
                    clonedBillingAddress.setOwner((Item)clonedOrder.getPaymentInfo());
                }
                catch(ConsistencyCheckException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        return clonedOrder;
    }
}
