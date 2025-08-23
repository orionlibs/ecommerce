package de.hybris.order.calculation.domain;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class OrderTest
{
    private final Currency euro = new Currency("EUR", 2);


    @Test
    public void testSetAndModifyLineItemCharges()
    {
        Order testobject = new Order(this.euro, false, null);
        LineItem charge1 = new LineItem(new Money("10", this.euro));
        LineItem charge2 = new LineItem(new Money("1.0", this.euro), 3);
        LineItem charge3 = new LineItem(new Money("33", this.euro), 5);
        try
        {
            charge2.getOrder();
            Assert.fail("expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException e)
        {
            e.getMessage().contains("Order for LineItemCharge");
        }
        testobject.addLineItem(charge2);
        Assert.assertEquals(testobject, charge2.getOrder());
        testobject.addLineItem(charge1);
        Assert.assertTrue((testobject.getLineItems().size() == 2));
        Assert.assertEquals(charge2, testobject.getLineItems().get(0));
        Assert.assertEquals(charge1, testobject.getLineItems().get(1));
        try
        {
            testobject.getLineItems().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearLineItems();
        Assert.assertTrue(testobject.getLineItems().isEmpty());
        try
        {
            charge2.getOrder();
            Assert.fail("Expected MissingCalculationDataException");
        }
        catch(MissingCalculationDataException missingCalculationDataException)
        {
        }
        List<LineItem> list = new ArrayList<>();
        list.add(charge3);
        list.add(charge1);
        testobject.addLineItems(list);
        Assert.assertEquals(testobject, charge3.getOrder());
        Assert.assertEquals(charge1, testobject.getLineItems().get(1));
        testobject.addLineItem(1, charge2);
        Assert.assertEquals(testobject, charge2.getOrder());
        Assert.assertTrue((testobject.getLineItems().size() == 3));
        Assert.assertEquals(charge2, testobject.getLineItems().get(1));
        Assert.assertEquals(charge1, testobject.getLineItems().get(2));
        Assert.assertEquals(testobject, charge3.getOrder());
        testobject.removeLineItem(charge3);
        try
        {
            testobject.removeLineItem(charge3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
        Assert.assertEquals(testobject, charge1.getOrder());
    }


    @Test
    public void testSetAndModifyTaxes()
    {
        Order testobject = new Order(this.euro, false, null);
        Tax tax1 = new Tax((AbstractAmount)Percentage.TEN);
        Tax tax2 = new Tax((AbstractAmount)Percentage.THIRTY);
        Tax tax3 = new Tax((AbstractAmount)Percentage.HUNDRED);
        testobject.addTax(tax2);
        testobject.addTax(tax1);
        Assert.assertTrue((testobject.getTaxes().size() == 2));
        Assert.assertTrue(testobject.getTaxes().contains(tax2));
        Assert.assertTrue(testobject.getTaxes().contains(tax1));
        try
        {
            testobject.getTaxes().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearTaxes();
        Assert.assertTrue(testobject.getTaxes().isEmpty());
        List<Tax> list = new ArrayList<>();
        list.add(tax3);
        list.add(tax1);
        testobject.addTaxes(list);
        Assert.assertEquals(Arrays.asList(new Tax[] {tax3, tax1}, ), testobject.getTaxes());
        testobject.addTax(tax2);
        Assert.assertEquals(Arrays.asList(new Tax[] {tax3, tax1, tax2}, ), testobject.getTaxes());
        testobject.removeTax(tax3);
        try
        {
            testobject.removeTax(tax3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
    }


    @Test
    public void testEmptyOrder()
    {
        Money zero = new Money(this.euro);
        Order order = new Order(this.euro, false, null);
        Assert.assertEquals(zero, order.getTotal());
        Assert.assertEquals(zero, order.getSubTotal());
        Assert.assertEquals(zero, order.getTotalDiscount());
        Assert.assertEquals(zero, order.getTotalIncludingTaxes());
        Assert.assertEquals(zero, order.getTotalCharge());
        Assert.assertEquals(zero, order.getTotalTax());
        Assert.assertEquals(zero, order.getTotalChargeOfType(AbstractCharge.ChargeType.PAYMENT));
        Assert.assertEquals(Collections.EMPTY_SET, order.getTaxesFor(null));
        OrderCharge lic = new OrderCharge((AbstractAmount)zero);
        Assert.assertEquals(Collections.EMPTY_SET, order.getTaxesFor((Taxable)lic));
        Assert.assertEquals(Collections.EMPTY_MAP, order.getTotalDiscounts());
        Assert.assertEquals(Collections.EMPTY_LIST, order.getDiscounts());
        Assert.assertEquals(Collections.EMPTY_MAP, order.getTotalCharges());
        Assert.assertEquals(Collections.EMPTY_LIST, order.getCharges());
        Assert.assertEquals(Collections.EMPTY_MAP, order.getTotalTaxes());
        Assert.assertEquals(Collections.EMPTY_LIST, order.getTaxes());
        Assert.assertEquals(Collections.EMPTY_LIST, order.getLineItems());
    }


    @Test
    public void testSetAndModifyAdditionalOrderCharges()
    {
        Order testobject = new Order(this.euro, false, null);
        OrderCharge charge1 = new OrderCharge((AbstractAmount)new Money("10", this.euro));
        OrderCharge charge2 = new OrderCharge((AbstractAmount)Percentage.EIGHTY);
        OrderCharge charge3 = new OrderCharge((AbstractAmount)new Money("33", this.euro));
        testobject.addCharge(charge2);
        testobject.addCharge(charge1);
        Assert.assertTrue((testobject.getCharges().size() == 2));
        Assert.assertEquals(charge2, testobject.getCharges().get(0));
        Assert.assertEquals(charge1, testobject.getCharges().get(1));
        try
        {
            testobject.getCharges().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearCharges();
        Assert.assertTrue(testobject.getCharges().isEmpty());
        List<OrderCharge> list = new ArrayList<>();
        list.add(charge3);
        list.add(charge1);
        testobject.addCharges(list);
        Assert.assertEquals(charge1, testobject.getCharges().get(1));
        testobject.addCharge(1, charge2);
        Assert.assertTrue((testobject.getCharges().size() == 3));
        Assert.assertEquals(charge2, testobject.getCharges().get(1));
        Assert.assertEquals(charge1, testobject.getCharges().get(2));
        testobject.removeCharge(charge3);
        try
        {
            testobject.removeCharge(charge3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
    }


    @Test
    public void testSetAndModifyOrderDiscounts()
    {
        Order testobject = new Order(this.euro, false, null);
        OrderDiscount od1 = new OrderDiscount((AbstractAmount)Percentage.TEN);
        OrderDiscount od2 = new OrderDiscount((AbstractAmount)Percentage.THIRTY);
        OrderDiscount od3 = new OrderDiscount((AbstractAmount)Percentage.HUNDRED);
        testobject.addDiscount(od2);
        testobject.addDiscount(od1);
        Assert.assertTrue((testobject.getDiscounts().size() == 2));
        Assert.assertEquals(od2, testobject.getDiscounts().get(0));
        Assert.assertEquals(od1, testobject.getDiscounts().get(1));
        try
        {
            testobject.getDiscounts().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearDiscounts();
        Assert.assertTrue(testobject.getDiscounts().isEmpty());
        List<OrderDiscount> list = new ArrayList<>();
        list.add(od3);
        list.add(od1);
        testobject.addDiscounts(list);
        Assert.assertEquals(od1, testobject.getDiscounts().get(1));
        testobject.addDiscount(1, od2);
        Assert.assertTrue((testobject.getDiscounts().size() == 3));
        Assert.assertEquals(od2, testobject.getDiscounts().get(1));
        Assert.assertEquals(od1, testobject.getDiscounts().get(2));
        testobject.removeDiscount(od3);
        try
        {
            testobject.removeDiscount(od3);
            Assert.fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
    }
}
