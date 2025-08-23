package de.hybris.order.calculation.money;

import de.hybris.bootstrap.annotations.UnitTest;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class PercentageTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testNullParam()
    {
        new Percentage((String)null);
    }


    @Test
    public void testNegativeAndZeroValue()
    {
        Percentage val1 = new Percentage(-10);
        Percentage val3 = new Percentage(new BigDecimal("-35"));
        Assert.assertEquals(Percentage.TWENTYFIVE, val1.subtract(val3));
        Percentage val2 = new Percentage(new BigDecimal("0.0000000"));
        Assert.assertEquals(new Percentage(BigDecimal.ZERO), val2);
    }


    @Test
    public void testComplexAdd()
    {
        Percentage val1 = new Percentage(0);
        Percentage val3 = new Percentage(new BigDecimal("35.333"));
        Percentage result = val1.add(Percentage.TEN).add(val3);
        Assert.assertEquals(new Percentage(new BigDecimal("45.333")), result);
        Assert.assertEquals("45.333%", result.toString());
    }


    @Test
    public void testAdd()
    {
        Percentage add1 = new Percentage(new BigDecimal(2));
        Percentage add2 = new Percentage(new BigDecimal(3));
        Percentage res = new Percentage(new BigDecimal(5));
        Assert.assertEquals(res, add1.add(add2));
    }


    @Test
    public void testSubtract()
    {
        Percentage add1 = new Percentage(new BigDecimal(5));
        Percentage add2 = new Percentage(new BigDecimal(2));
        Percentage res = new Percentage(new BigDecimal(3));
        Assert.assertEquals(res, add1.subtract(add2));
    }


    @Test
    public void testEquality()
    {
        Percentage value1 = new Percentage(0);
        Percentage value2 = new Percentage(BigDecimal.ZERO);
        Percentage value3 = new Percentage(new BigDecimal("0.0000000000"));
        Assert.assertEquals(value1, value2);
        Assert.assertEquals(value1, value3);
        Assert.assertEquals(value2, value3);
        Assert.assertEquals(value2, value1);
        Assert.assertEquals(value3, value1);
        Assert.assertEquals(value3, value2);
        Percentage val3 = new Percentage(new BigDecimal("1.000000000000001"));
        Percentage val4 = new Percentage(new BigDecimal("1.0000000000000001"));
        Percentage val5 = new Percentage(new BigDecimal("0.0000000000000001"));
        Assert.assertFalse(val3.equals(val4));
        Assert.assertFalse(val4.equals(val5));
        Assert.assertFalse(val3.equals(val5));
        Assert.assertFalse(val5.equals(val3));
        Assert.assertFalse(val5.equals(val4));
        Assert.assertFalse(val4.equals(val3));
        Assert.assertFalse(val5.equals(value3));
        Assert.assertFalse(val5.equals(value2));
        Assert.assertFalse(val5.equals(value1));
        Assert.assertFalse(val5.equals(new Object()));
    }


    @Test
    public void testHastCode()
    {
        Percentage value1 = new Percentage(0);
        Percentage value2 = new Percentage(BigDecimal.ZERO);
        Assert.assertEquals(value1.hashCode(), value2.hashCode());
    }
}
