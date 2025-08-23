package de.hybris.order.calculation.domain;

import de.hybris.order.calculation.money.Money;

public interface Taxable
{
    Money getTotal(Order paramOrder);
}
