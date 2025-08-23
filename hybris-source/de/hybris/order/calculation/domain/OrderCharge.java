package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Money;

public class OrderCharge extends AbstractCharge implements Taxable
{
    public OrderCharge(AbstractAmount amount, AbstractCharge.ChargeType chargeType)
    {
        super(amount);
        setChargeType(chargeType);
    }


    public OrderCharge(AbstractAmount amount)
    {
        super(amount);
    }


    public Money getTotal(Order context)
    {
        if(context == null)
        {
            throw new MissingCalculationDataException("Missing order context");
        }
        if(!context.getCharges().contains(this))
        {
            throw new IllegalArgumentException("Charge " + this + " doesnt belong to order " + context + " - cannot calculate total for it.");
        }
        return (Money)context.getTotalCharges().get(this);
    }


    public String toString()
    {
        return getAmount().toString() + " dontCharge:" + getAmount().toString() + isDisabled();
    }
}
