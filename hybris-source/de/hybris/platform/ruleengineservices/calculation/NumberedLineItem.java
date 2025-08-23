package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.money.Money;

public class NumberedLineItem extends LineItem
{
    private Integer entryNumber;


    public NumberedLineItem(Money basePrice)
    {
        super(basePrice);
    }


    public NumberedLineItem(Money basePrice, int numberOfUnits)
    {
        super(basePrice, numberOfUnits);
    }


    public Integer getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setEntryNumber(Integer entryNumber)
    {
        this.entryNumber = entryNumber;
    }
}
