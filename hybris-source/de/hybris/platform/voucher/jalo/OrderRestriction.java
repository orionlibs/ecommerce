package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import org.apache.log4j.Logger;

public class OrderRestriction extends GeneratedOrderRestriction
{
    private static final Logger LOG = Logger.getLogger(OrderRestriction.class);


    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()),
                        getTotal().toString() + getTotal().toString()};
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        Currency minimumOrderValueCurrency = getCurrency();
        Currency currentOrderCurrency = anOrder.getCurrency();
        if(minimumOrderValueCurrency == null || currentOrderCurrency == null)
        {
            return false;
        }
        double minimumTotal = minimumOrderValueCurrency.convert(currentOrderCurrency, getTotalAsPrimitive());
        try
        {
            anOrder.calculateTotals(false);
        }
        catch(JaloPriceFactoryException e)
        {
            LOG.warn("Order.calculateTotals(false) failed", (Throwable)e);
        }
        double currentTotal = anOrder.getSubtotal().doubleValue();
        if(isNetAsPrimitive() != anOrder.isNet().booleanValue())
        {
            if(anOrder.isNet().booleanValue())
            {
                currentTotal += anOrder.getTotalTax().doubleValue();
            }
            else
            {
                currentTotal -= anOrder.getTotalTax().doubleValue();
            }
        }
        if(!isValueofgoodsonlyAsPrimitive())
        {
            currentTotal += anOrder.getDeliveryCosts();
            currentTotal += anOrder.getPaymentCosts();
        }
        if(isPositiveAsPrimitive())
        {
            return (currentTotal >= minimumTotal);
        }
        return (currentTotal <= minimumTotal);
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
