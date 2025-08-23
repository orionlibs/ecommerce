package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class RegularCustomerOrderQuantityRestriction extends GeneratedRegularCustomerOrderQuantityRestriction
{
    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), getOrderQuantity().toString()};
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        Collection orders = anOrder.getUser().getOrders();
        int orderQuantity = getOrderQuantityAsPrimitive();
        int currentOrderQuantity = orders.size();
        return (currentOrderQuantity > orderQuantity || (currentOrderQuantity == orderQuantity && !orders.contains(anOrder)));
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
