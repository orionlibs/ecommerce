package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.order.calculation.domain.Order;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

@FunctionalInterface
public interface NumberedLineItemLookupStrategy
{
    NumberedLineItem lookup(Order paramOrder, OrderEntryRAO paramOrderEntryRAO);
}
