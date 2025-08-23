package de.hybris.order.calculation.strategies.impl;

import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.RoundingStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultRoundingStrategy implements RoundingStrategy
{
    public Money divide(Money price, BigDecimal divisor)
    {
        Currency curr = price.getCurrency();
        BigDecimal divide = price.getAmount().divide(divisor, RoundingMode.HALF_UP);
        return createMoney(divide, curr);
    }


    public Money multiply(Money price, BigDecimal multiplicant)
    {
        Currency curr = price.getCurrency();
        BigDecimal multiply = price.getAmount().multiply(multiplicant);
        return createMoney(multiply, curr);
    }


    public Money getPercentValue(Money price, Percentage percent)
    {
        Currency curr = price.getCurrency();
        BigDecimal amount = price.getAmount().multiply(percent.getRate()).divide(BigDecimal.valueOf(100L));
        return createMoney(amount, curr);
    }


    public Money roundToMoney(BigDecimal amount, Currency currency)
    {
        return createMoney(amount, currency);
    }


    protected Money createMoney(BigDecimal amount, Currency curr)
    {
        return new Money(amount.setScale(curr.getDigits(), RoundingMode.HALF_UP), curr);
    }
}
