package de.hybris.order.calculation.money;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class CurrencyTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testNullCurrency()
    {
        new Currency(null, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCurrency()
    {
        new Currency("", 0);
    }


    @Test
    public void testMinusZeroCurrency()
    {
        Currency curr = new Currency("x", 0);
        Assert.assertEquals("x", curr.toString());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCurrency()
    {
        new Currency("EUR", -10);
    }


    @Test
    public void testEquality()
    {
        Currency curr1 = new Currency("A", 1);
        Currency curr2 = new Currency("A", 2);
        Currency curr3 = new Currency("B", 1);
        Currency curr4 = new Currency("B", 1);
        Currency eur1 = new Currency("eur", 1);
        Currency eur2 = new Currency("EUR", 1);
        Assert.assertTrue(curr3.equals(curr4));
        Assert.assertTrue(curr2.equals(curr2));
        Assert.assertTrue(eur1.equals(eur2));
        Assert.assertFalse(curr1.equals(curr2));
        Assert.assertFalse(curr1.equals(curr3));
        Assert.assertFalse(curr2.equals(curr3));
        Assert.assertFalse(curr2.equals(new Object()));
    }


    @Test
    public void testHashCode()
    {
        Currency curr1 = new Currency("A", 1);
        Currency curr2 = new Currency("A", 2);
        Currency curr3 = new Currency("B", 1);
        Currency curr4 = new Currency("B", 1);
        Assert.assertTrue((curr3.hashCode() == curr4.hashCode()));
        Assert.assertFalse((curr1.hashCode() == curr2.hashCode()));
        Assert.assertFalse((curr1.hashCode() == curr3.hashCode()));
        Assert.assertFalse((curr2.hashCode() == curr3.hashCode()));
        Assert.assertTrue((curr2.hashCode() == curr2.hashCode()));
        Currency eur1 = new Currency("eur", 1);
        Currency eur2 = new Currency("EUR", 1);
        Assert.assertTrue((eur1.hashCode() == eur2.hashCode()));
    }


    @Test
    public void testAnotherHashCodeProblem()
    {
        Currency curr1 = new Currency("A", 0);
        Currency curr2 = new Currency("B", 0);
        Assert.assertFalse((curr1.hashCode() == curr2.hashCode()));
    }
}
