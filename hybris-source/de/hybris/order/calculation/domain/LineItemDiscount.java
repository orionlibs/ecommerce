package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.AbstractAmount;

public class LineItemDiscount extends AbstractDiscount
{
    private final boolean perUnit;
    private int applicableUnits;


    public LineItemDiscount(AbstractAmount amount)
    {
        this(amount, false, 0);
    }


    public LineItemDiscount(AbstractAmount amount, boolean perUnit)
    {
        this(amount, perUnit, perUnit ? Integer.MAX_VALUE : 0);
    }


    public LineItemDiscount(AbstractAmount amount, boolean perUnit, int applicableUnits)
    {
        super(amount);
        this.perUnit = perUnit;
        if(perUnit && applicableUnits < 0)
        {
            throw new IllegalArgumentException("This LineItemDiscount is perUnit and therefore applicableUnits cannot be negative");
        }
        this.applicableUnits = applicableUnits;
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
        return "" + getAmount() + getAmount();
    }
}
