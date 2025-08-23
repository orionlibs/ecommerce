package de.hybris.order.calculation.domain;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class LineItemTest
{
    private final Currency curr = new Currency("euro", 2);


    @Test(expected = IllegalArgumentException.class)
    public void testWithNull()
    {
        new LineItem(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeValue()
    {
        new LineItem(new Money(BigDecimal.ZERO, this.curr), -3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGiveAwayCount()
    {
        LineItem lineItem = new LineItem(new Money(BigDecimal.ZERO, this.curr), 3);
        lineItem.setGiveAwayUnits(-3);
    }


    public void testEmptyLineItem()
    {
        LineItem lineItem = new LineItem(new Money(BigDecimal.ZERO, this.curr), 3);
        Assert.assertEquals(Collections.EMPTY_LIST, lineItem.getCharges());
        Assert.assertEquals(Collections.EMPTY_MAP, lineItem.getTotalCharges());
        Assert.assertEquals(Collections.EMPTY_LIST, lineItem.getDiscounts());
        Assert.assertEquals(Collections.EMPTY_MAP, lineItem.getTotalDiscounts());
    }


    @Test
    public void testToString()
    {
        LineItem lineItem1 = new LineItem(new Money(BigDecimal.ZERO, this.curr));
        Assert.assertEquals("1x 0.00 euro", lineItem1.toString());
        LineItemDiscount lid = new LineItemDiscount((AbstractAmount)Percentage.SEVENTYFIVE);
        lineItem1.addDiscount(lid);
        Assert.assertEquals("1x 0.00 euro discounts:[75%]", lineItem1.toString());
        lineItem1.setGiveAwayUnits(1);
        Assert.assertEquals("1x 0.00 euro(free:1) discounts:[75%]", lineItem1.toString());
        LineItemCharge lic = new LineItemCharge((AbstractAmount)Percentage.FIFTY);
        lineItem1.addCharge(lic);
        Assert.assertEquals("1x 0.00 euro(free:1) discounts:[75%] charges:[50% dontCharge:false]", lineItem1.toString());
        LineItem lineItem2 = new LineItem(new Money(BigDecimal.ZERO, this.curr));
        lineItem2.addCharge(lic);
        Assert.assertEquals("1x 0.00 euro charges:[50% dontCharge:false]", lineItem2.toString());
    }


    @Test
    public void testCalculatedUnitCount()
    {
        LineItem lineItem1 = new LineItem(new Money(BigDecimal.ZERO, this.curr), 10);
        Assert.assertTrue((lineItem1.getNumberOfUnitsForCalculation() == 10));
        lineItem1.setGiveAwayUnits(3);
        Assert.assertTrue((lineItem1.getNumberOfUnitsForCalculation() == 7));
        lineItem1.setGiveAwayUnits(4);
        Assert.assertTrue((lineItem1.getNumberOfUnitsForCalculation() == 6));
        lineItem1.setGiveAwayUnits(10);
        Assert.assertTrue((lineItem1.getNumberOfUnitsForCalculation() == 0));
        lineItem1.setGiveAwayUnits(40);
        Assert.assertTrue((lineItem1.getNumberOfUnitsForCalculation() == 0));
    }


    @Test
    public void testLineItemChargeGetters()
    {
        LineItem lineitem = new LineItem(new Money(this.curr), 3);
        Assert.assertNotNull(lineitem.getTotalCharges());
        Assert.assertTrue(lineitem.getTotalCharges().isEmpty());
        Assert.assertNotNull(lineitem.getCharges());
        Assert.assertTrue(lineitem.getCharges().isEmpty());
        Assert.assertNotNull(lineitem.getDiscounts());
        Assert.assertTrue(lineitem.getDiscounts().isEmpty());
        Assert.assertNotNull(lineitem.getTotalDiscounts());
        Assert.assertTrue(lineitem.getTotalDiscounts().isEmpty());
    }


    @Test
    public void testLineItemChargeWithoutOrder()
    {
        LineItem lineitem = new LineItem(new Money(BigDecimal.ZERO, this.curr), 3);
        try
        {
            lineitem.getTotal(null);
            Assert.fail("expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException e)
        {
            Assert.assertTrue(e.getMessage().contains("Order for LineItem"));
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
        try
        {
            lineitem.getSubTotal();
            Assert.fail("expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException e)
        {
            Assert.assertTrue(e.getMessage().contains("Order for LineItem"));
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
        try
        {
            lineitem.getTotalCharge();
            Assert.fail("expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException e)
        {
            Assert.assertTrue(e.getMessage().contains("Order for LineItem"));
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
        try
        {
            lineitem.getTotalDiscount();
            Assert.fail("expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException e)
        {
            Assert.assertTrue(e.getMessage().contains("Order for LineItem"));
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
    }


    @Test
    public void testSetAndModifyDiscounts()
    {
        LineItem testobject = new LineItem(new Money("22.45", this.curr), 10);
        LineItemDiscount disc1 = new LineItemDiscount((AbstractAmount)new Money("10", this.curr));
        LineItemDiscount disc2 = new LineItemDiscount((AbstractAmount)Percentage.TEN);
        LineItemDiscount disc3 = new LineItemDiscount((AbstractAmount)Percentage.FIFTY);
        testobject.addDiscount(disc2);
        testobject.addDiscount(disc1);
        Assert.assertTrue((testobject.getDiscounts().size() == 2));
        Assert.assertEquals(disc2, testobject.getDiscounts().get(0));
        Assert.assertEquals(disc1, testobject.getDiscounts().get(1));
        try
        {
            testobject.getDiscounts().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearDiscounts();
        Assert.assertTrue(testobject.getDiscounts().isEmpty());
        List<LineItemDiscount> list = new ArrayList<>();
        list.add(disc3);
        list.add(disc1);
        testobject.addDiscounts(list);
        Assert.assertEquals(disc1, testobject.getDiscounts().get(1));
        testobject.addDiscount(1, disc2);
        Assert.assertTrue((testobject.getDiscounts().size() == 3));
        Assert.assertEquals(disc2, testobject.getDiscounts().get(1));
        Assert.assertEquals(disc1, testobject.getDiscounts().get(2));
        testobject.removeDiscount(disc3);
        try
        {
            testobject.removeDiscount(disc3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
    }


    @Test
    public void testSetAndModifyAdditionalCharges()
    {
        LineItem testobject = new LineItem(new Money("22.45", this.curr), 10);
        LineItemCharge apc1 = new LineItemCharge((AbstractAmount)new Money("10", this.curr));
        LineItemCharge apc2 = new LineItemCharge((AbstractAmount)Percentage.TEN);
        LineItemCharge apc3 = new LineItemCharge((AbstractAmount)Percentage.FIFTY);
        testobject.addCharge(apc2);
        testobject.addCharge(apc1);
        Assert.assertTrue((testobject.getCharges().size() == 2));
        Assert.assertEquals(apc2, testobject.getCharges().get(0));
        Assert.assertEquals(apc1, testobject.getCharges().get(1));
        try
        {
            testobject.getCharges().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearCharges();
        Assert.assertTrue(testobject.getCharges().isEmpty());
        List<LineItemCharge> list = new ArrayList<>();
        list.add(apc3);
        list.add(apc1);
        testobject.addCharges(list);
        Assert.assertEquals(apc1, testobject.getCharges().get(1));
        testobject.addCharge(1, apc2);
        Assert.assertTrue((testobject.getCharges().size() == 3));
        Assert.assertEquals(apc2, testobject.getCharges().get(1));
        Assert.assertEquals(apc1, testobject.getCharges().get(2));
        testobject.removeCharge(apc3);
        try
        {
            testobject.removeCharge(apc3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void setIllegalGiveAwayCount()
    {
        LineItem prodc = new LineItem(new Money("100", this.curr), 10);
        prodc.setGiveAwayUnits(-10);
    }
}
