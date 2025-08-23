package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;

public abstract class AbstractDiscount
{
    private final AbstractAmount amount;


    protected AbstractDiscount(AbstractAmount amount)
    {
        if(amount == null)
        {
            throw new IllegalArgumentException("The amount was null");
        }
        this.amount = amount;
    }


    public AbstractAmount getAmount()
    {
        return this.amount;
    }
}
