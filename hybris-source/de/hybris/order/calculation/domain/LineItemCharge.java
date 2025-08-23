package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;

public class LineItemCharge extends AbstractCharge
{
    private final boolean perUnit;
    private int applicableUnits;


    public LineItemCharge(AbstractAmount amount)
    {
        this(amount, false);
    }


    public LineItemCharge(AbstractAmount amount, boolean perUnit)
    {
        this(amount, perUnit, 2147483647);
    }


    public LineItemCharge(AbstractAmount amount, boolean perUnit, int applicableForUnits)
    {
        super(amount);
        this.perUnit = perUnit;
        if(perUnit && applicableForUnits < 0)
        {
            throw new IllegalArgumentException("This LineItemCharge is perUnit and therefore applicableForUnits cannot be negative");
        }
        this.applicableUnits = perUnit ? applicableForUnits : 0;
    }


    public boolean isPerUnit()
    {
        return this.perUnit;
    }


    public int getApplicableUnits()
    {
        return this.applicableUnits;
    }


    public void setApplicableUnits(int numberOfUnits)
    {
        if(numberOfUnits < 0)
        {
            throw new IllegalArgumentException("number of applicable units must be greater or equal than zero");
        }
        this.applicableUnits = numberOfUnits;
    }


    public String toString()
    {
        return "" + getAmount() + " dontCharge:" + getAmount() + isDisabled() + (isPerUnit() ? (" applicableUnit:" + getApplicableUnits()) : "");
    }
}
