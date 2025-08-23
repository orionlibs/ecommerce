package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;

public abstract class AbstractCharge
{
    private ChargeType chargeType;
    private boolean disabled;
    private final AbstractAmount amount;


    protected AbstractCharge(AbstractAmount amount)
    {
        if(amount == null)
        {
            throw new IllegalArgumentException("The amount was null");
        }
        this.amount = amount;
    }


    public boolean isDisabled()
    {
        return this.disabled;
    }


    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }


    public AbstractAmount getAmount()
    {
        return this.amount;
    }


    public void setChargeType(ChargeType chargeType)
    {
        this.chargeType = chargeType;
    }


    public ChargeType getChargeType()
    {
        return this.chargeType;
    }
}
