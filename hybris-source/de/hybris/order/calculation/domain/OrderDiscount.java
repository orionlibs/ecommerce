package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;

public class OrderDiscount extends AbstractDiscount
{
    public OrderDiscount(AbstractAmount amount)
    {
        super(amount);
    }


    public String toString()
    {
        return getAmount().toString();
    }
}
