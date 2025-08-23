package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.Order;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItemLookupStrategy;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultNumberedLineItemLookupStrategy implements NumberedLineItemLookupStrategy
{
    public NumberedLineItem lookup(Order cart, OrderEntryRAO entryRao)
    {
        ServicesUtil.validateParameterNotNull(cart, "cart must not be null");
        ServicesUtil.validateParameterNotNull(entryRao, "entry rao must not be null");
        ServicesUtil.validateParameterNotNull(entryRao.getEntryNumber(), "entry rao must have an entry number!");
        for(LineItem item : cart.getLineItems())
        {
            if(item instanceof NumberedLineItem && entryRao.getEntryNumber().equals(((NumberedLineItem)item).getEntryNumber()))
            {
                return (NumberedLineItem)item;
            }
        }
        throw new IllegalArgumentException("can't find corresponding LineItem for the given orderEntryRao:" + entryRao);
    }
}
