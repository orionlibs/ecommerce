package de.hybris.order.calculation.domain;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class ChargeTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testNullLineItemChargeOne()
    {
        new LineItemCharge(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNullLineItemChargeTwo()
    {
        new LineItemCharge(null, false, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNullOrderChargeOne()
    {
        new OrderCharge(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNullOrderChargeTwo()
    {
        new OrderCharge(null, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNegativeApplicableUnit()
    {
        new LineItemCharge((AbstractAmount)Percentage.SEVENTY, true, -10);
    }


    @Test
    public void testNegativeApplicableUnitWhichIsNotPerUnit()
    {
        LineItemCharge apc = new LineItemCharge((AbstractAmount)Percentage.THIRTY, false, -10);
        Assert.assertNotNull(apc);
        Assert.assertFalse(apc.isPerUnit());
        Assert.assertTrue((apc.getApplicableUnits() == 0));
        Assert.assertEquals("30% dontCharge:false", apc.toString());
    }


    @Test
    public void testPositiveApplicableUnitWhichIsNotPerUnit()
    {
        LineItemCharge apc = new LineItemCharge((AbstractAmount)Percentage.THIRTY, true, 10);
        Assert.assertNotNull(apc);
        Assert.assertTrue(apc.isPerUnit());
        Assert.assertTrue((apc.getApplicableUnits() == 10));
        Assert.assertEquals("30% dontCharge:false applicableUnit:10", apc.toString());
    }


    @Test
    public void testWithAdditionalChargeType()
    {
        OrderCharge apc = new OrderCharge((AbstractAmount)Percentage.THIRTY, null);
        Assert.assertNull(apc.getChargeType());
        apc.setChargeType(AbstractCharge.ChargeType.PAYMENT);
        Assert.assertEquals(AbstractCharge.ChargeType.PAYMENT, apc.getChargeType());
        Assert.assertEquals("30% dontCharge:false type:PAYMENT", apc.toString());
    }


    @Test
    public void testDontCharge()
    {
        LineItemCharge apd = new LineItemCharge((AbstractAmount)Percentage.FIFTY);
        Assert.assertFalse(apd.isDisabled());
        apd = new LineItemCharge((AbstractAmount)Percentage.FIFTY, false, 0);
        apd.setDisabled(true);
        Assert.assertTrue(apd.isDisabled());
        OrderCharge aoc = new OrderCharge((AbstractAmount)Percentage.FIFTY);
        aoc.setDisabled(true);
        Assert.assertTrue(aoc.isDisabled());
        aoc = new OrderCharge((AbstractAmount)Percentage.FIFTY);
        Assert.assertFalse(aoc.isDisabled());
    }


    @Test
    public void testToStringComplete()
    {
        Currency curr = new Currency("EUR", 2);
        LineItemCharge apd = new LineItemCharge((AbstractAmount)new Money(curr), true, 23);
        apd.setDisabled(true);
        apd.setChargeType(AbstractCharge.ChargeType.SHIPPING);
        Assert.assertEquals("0 EUR dontCharge:true applicableUnit:23 type:SHIPPING", apd.toString());
    }


    @Test
    public void getOrderTotalCalculationException()
    {
        Currency curr = new Currency("xxx", 1);
        OrderCharge orderCharge = new OrderCharge((AbstractAmount)new Money("0", curr));
        orderCharge.setDisabled(true);
        try
        {
            orderCharge.getTotal(null);
            Assert.fail("Expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException missingCalculationDataException)
        {
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
    }


    @Test
    public void testOrderCharge()
    {
        Currency curr = new Currency("xxx", 1);
        OrderCharge orderCharge = new OrderCharge((AbstractAmount)new Money("0.000", curr), AbstractCharge.ChargeType.PAYMENT);
        Assert.assertEquals("0.0 xxx dontCharge:false type:PAYMENT", orderCharge.toString());
        orderCharge = new OrderCharge((AbstractAmount)new Money("0.000", curr), null);
        Assert.assertEquals("0.0 xxx dontCharge:false", orderCharge.toString());
    }
}
