package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import java.util.Iterator;

public class RegularCustomerOrderTotalRestriction extends GeneratedRegularCustomerOrderTotalRestriction
{
    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()),
                        getAllOrdersTotal().toString() + getAllOrdersTotal().toString()};
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        double currentTotal = 0.0D;
        Currency currency = getCurrency();
        for(Iterator<Order> iterator = anOrder.getUser().getOrders().iterator(); iterator.hasNext(); )
        {
            Order order = iterator.next();
            if(!order.equals(anOrder))
            {
                currentTotal += order.getCurrency().convert(currency, order.getSubtotal().doubleValue());
                if(isNetAsPrimitive() != order.isNet().booleanValue())
                {
                    if(order.isNet().booleanValue())
                    {
                        currentTotal += order.getTotalTax().doubleValue();
                    }
                    else
                    {
                        currentTotal -= order.getTotalTax().doubleValue();
                    }
                }
                if(!isValueofgoodsonlyAsPrimitive())
                {
                    currentTotal += order.getDeliveryCosts();
                    currentTotal += order.getPaymentCosts();
                }
            }
        }
        return (currentTotal >= getAllOrdersTotalAsPrimitive());
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
