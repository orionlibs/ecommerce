package de.hybris.order.calculation.strategies.impl;

import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultTaxRoundingStrategy extends DefaultRoundingStrategy
{
    protected Money createMoney(BigDecimal amount, Currency curr)
    {
        return new Money(amount.setScale(curr.getDigits(), RoundingMode.DOWN), curr);
    }
}
