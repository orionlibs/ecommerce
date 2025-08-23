package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderDiscount;

public interface MinimumAmountValidationStrategy
{
    boolean isOrderLowerLimitValid(Order paramOrder, OrderDiscount paramOrderDiscount);


    boolean isLineItemLowerLimitValid(LineItem paramLineItem, LineItemDiscount paramLineItemDiscount);
}
