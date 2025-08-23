package de.hybris.order.calculation.strategies;

import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.impl.DefaultRoundingStrategy;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class DefaultRoundingStrategyTest
{
    private final RoundingStrategy roundingStrategy = (RoundingStrategy)new DefaultRoundingStrategy();
    private final Currency euro = new Currency("EUR", 2);
    private final Currency fourDigits = new Currency("fourDigits", 4);


    @Test
    public void testMultiply()
    {
        Money result1 = this.roundingStrategy.multiply(new Money(BigDecimal.ONE, this.euro), BigDecimal.ONE);
        Assert.assertEquals(new Money(BigDecimal.ONE, this.euro), result1);
        Assert.assertNotSame(new Money(BigDecimal.ONE, this.fourDigits), result1);
        Money result2 = this.roundingStrategy.multiply(new Money(BigDecimal.ONE, this.euro), new BigDecimal("0.5"));
        Assert.assertEquals(new Money(new BigDecimal("0.5"), this.euro), result2);
        Money result3 = this.roundingStrategy.multiply(new Money(BigDecimal.ONE, this.euro), new BigDecimal("0.333333333333"));
        Assert.assertEquals(new Money(new BigDecimal("0.33"), this.euro), result3);
        Money result4 = this.roundingStrategy.multiply(new Money(BigDecimal.ONE, this.fourDigits), new BigDecimal("0.333333333333"));
        Assert.assertEquals(new Money(new BigDecimal("0.3333"), this.fourDigits), result4);
        Money result5 = this.roundingStrategy.multiply(new Money("23.34", this.euro), new BigDecimal("1.00"));
        Assert.assertEquals(new Money(new BigDecimal("23.34"), this.euro), result5);
    }


    @Test
    public void testDividide()
    {
        Money oneEuro = new Money(BigDecimal.ONE, this.euro);
        Money twoEuro = new Money(BigDecimal.valueOf(2L), this.euro);
        Assert.assertEquals(new Money("0.33", this.euro), this.roundingStrategy.divide(oneEuro, BigDecimal.valueOf(3L)));
        Assert.assertEquals(new Money("0.50", this.euro), this.roundingStrategy.divide(oneEuro, BigDecimal.valueOf(2L)));
        Assert.assertEquals(oneEuro, this.roundingStrategy.divide(oneEuro, BigDecimal.ONE));
        Assert.assertEquals(new Money("0.67", this.euro), this.roundingStrategy.divide(twoEuro, BigDecimal.valueOf(3L)));
    }


    @Test
    public void testPercentageValue()
    {
        Money oneEuro = new Money(BigDecimal.ONE, this.euro);
        Assert.assertEquals(new Money("0.33", this.euro), this.roundingStrategy.getPercentValue(oneEuro, new Percentage(33)));
        Assert.assertEquals(new Money("0.66", this.euro), this.roundingStrategy.getPercentValue(oneEuro, new Percentage(66)));
        Assert.assertEquals(new Money("0.67", this.euro), this.roundingStrategy
                        .getPercentValue(oneEuro, new Percentage(new BigDecimal("66.666666666"))));
        Assert.assertEquals(new Money("0.99", this.euro), this.roundingStrategy.getPercentValue(oneEuro, new Percentage(99)));
        Assert.assertEquals(new Money("0.99", this.euro), this.roundingStrategy.getPercentValue(oneEuro, new Percentage(new BigDecimal("99.1"))));
        Assert.assertEquals(oneEuro, this.roundingStrategy.getPercentValue(oneEuro, new Percentage(new BigDecimal("99.6"))));
        Assert.assertEquals(new Money("0", this.euro), this.roundingStrategy.getPercentValue(oneEuro, new Percentage(0)));
    }
}
