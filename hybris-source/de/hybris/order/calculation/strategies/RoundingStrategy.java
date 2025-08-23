package de.hybris.order.calculation.strategies;

import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.math.BigDecimal;

public interface RoundingStrategy
{
    Money divide(Money paramMoney, BigDecimal paramBigDecimal);


    Money multiply(Money paramMoney, BigDecimal paramBigDecimal);


    Money getPercentValue(Money paramMoney, Percentage paramPercentage);


    Money roundToMoney(BigDecimal paramBigDecimal, Currency paramCurrency);
}
