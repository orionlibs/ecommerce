package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.platform.ruleengineservices.calculation.MinimumAmountValidationStrategy;
import java.math.BigDecimal;

public class DefaultMinimumAmountValidationStrategy implements MinimumAmountValidationStrategy
{
    private static final BigDecimal DEFAULT_ORDER_LOWER_LIMIT_AMOUNT = BigDecimal.ZERO;
    private static final BigDecimal DEFAULT_LINE_ITEM_LOWER_LIMIT_AMOUNT = BigDecimal.ZERO;
    private BigDecimal orderLowerLimitAmount = DEFAULT_ORDER_LOWER_LIMIT_AMOUNT;
    private BigDecimal lineItemLowerLimitAmount = DEFAULT_LINE_ITEM_LOWER_LIMIT_AMOUNT;


    public boolean isOrderLowerLimitValid(Order order, OrderDiscount discount)
    {
        if(order.getDiscounts().contains(discount))
        {
            throw new IllegalArgumentException("The order already has the discount.");
        }
        try
        {
            order.addDiscount(discount);
            return isOrderLowerLimitValid(order);
        }
        finally
        {
            order.removeDiscount(discount);
        }
    }


    protected boolean isOrderLowerLimitValid(Order order)
    {
        return (order.getSubTotal().subtract(order.getTotalDiscount()).getAmount().compareTo(getOrderLowerLimitAmount()) >= 0);
    }


    public boolean isLineItemLowerLimitValid(LineItem lineItem, LineItemDiscount discount)
    {
        if(lineItem.getDiscounts().contains(discount))
        {
            throw new IllegalArgumentException("The line item already has the discount.");
        }
        try
        {
            lineItem.addDiscount(discount);
            return (isLineItemLowerLimitValid(lineItem) && isOrderLowerLimitValid(lineItem.getOrder()));
        }
        finally
        {
            lineItem.removeDiscount(discount);
        }
    }


    protected boolean isLineItemLowerLimitValid(LineItem lineItem)
    {
        return (lineItem.getSubTotal().subtract(lineItem.getTotalDiscount()).getAmount().compareTo(getLineItemLowerLimitAmount()) >= 0);
    }


    protected BigDecimal getOrderLowerLimitAmount()
    {
        return this.orderLowerLimitAmount;
    }


    public void setOrderLowerLimitAmount(BigDecimal orderLowerLimitAmount)
    {
        this.orderLowerLimitAmount = orderLowerLimitAmount;
    }


    protected BigDecimal getLineItemLowerLimitAmount()
    {
        return this.lineItemLowerLimitAmount;
    }


    public void setLineItemLowerLimitAmount(BigDecimal lineItemLowerLimitAmount)
    {
        this.lineItemLowerLimitAmount = lineItemLowerLimitAmount;
    }
}
