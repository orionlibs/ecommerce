package de.hybris.order.calculation.domain;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DiscountsTest
{
    private Money zeroEuro;
    private Percentage zeroPercent;


    @Before
    public void setup()
    {
        Currency euro = new Currency("EUR", 2);
        this.zeroEuro = new Money(0L, euro);
        this.zeroPercent = new Percentage(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateLineItemDiscountWithNullOne()
    {
        new LineItemDiscount(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateLineItemDiscountWithNullTwo()
    {
        new LineItemDiscount(null, false, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateLineItemDiscountWithNegativeValue()
    {
        new LineItemDiscount((AbstractAmount)this.zeroEuro, true, -10);
    }


    public void testCreateLineItemDiscountWithNegativeValueButNotPErUnit()
    {
        LineItemDiscount prodDisc = new LineItemDiscount((AbstractAmount)this.zeroEuro, false, -10);
        Assert.assertNotNull(prodDisc);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrderDiscountWithNull()
    {
        new OrderDiscount(null);
    }


    @Test
    public void testCreateOrderDiscount()
    {
        OrderDiscount orderDiscount = new OrderDiscount((AbstractAmount)Percentage.EIGHTY);
        Assert.assertEquals("80%", orderDiscount.toString());
    }


    @Test
    public void testToStringByLineItemDiscount()
    {
        LineItemDiscount lineItemDiscount = new LineItemDiscount((AbstractAmount)Percentage.EIGHTY, true, 42);
        Assert.assertEquals("80% applicableUnits:42", lineItemDiscount.toString());
        lineItemDiscount = new LineItemDiscount((AbstractAmount)Percentage.EIGHTY, false, 42);
        Assert.assertEquals("80%", lineItemDiscount.toString());
    }


    @Test
    public void testDiscountAndLineItemAssigning()
    {
        LineItem liONE = new LineItem(this.zeroEuro);
        LineItem liTWO = new LineItem(this.zeroEuro);
        LineItemDiscount discount = new LineItemDiscount((AbstractAmount)this.zeroPercent);
        liONE.addDiscount(discount);
        Assert.assertTrue((liONE.getDiscounts().size() == 1));
        Assert.assertEquals(discount, liONE.getDiscounts().get(0));
        Assert.assertTrue(liTWO.getDiscounts().isEmpty());
    }


    @Test
    public void testClearDiscountbyLineItemAssigning()
    {
        LineItem liONE = new LineItem(this.zeroEuro);
        LineItem liTWO = new LineItem(this.zeroEuro);
        LineItemDiscount discount = new LineItemDiscount((AbstractAmount)this.zeroPercent);
        liONE.addDiscount(discount);
        liONE.clearDiscounts();
        Assert.assertTrue(liONE.getDiscounts().isEmpty());
        Assert.assertTrue(liTWO.getDiscounts().isEmpty());
        liONE.addDiscount(discount);
        Assert.assertFalse(liONE.getDiscounts().isEmpty());
        Assert.assertTrue(liTWO.getDiscounts().isEmpty());
    }
}
