package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;
import org.apache.log4j.Logger;

public class NewCustomerRestriction extends GeneratedNewCustomerRestriction
{
    private static final Logger LOGGER = Logger.getLogger(NewCustomerRestriction.class.getName());


    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive())};
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        Collection orders = anOrder.getUser().getOrders();
        return (isPositiveAsPrimitive() == ((orders.isEmpty() || (orders.size() == 1 && orders.contains(anOrder)))));
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
