package de.hybris.order.calculation;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemCharge;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.domain.Tax;
import de.hybris.order.calculation.domain.Taxable;
import de.hybris.order.calculation.exception.CalculationException;
import de.hybris.order.calculation.exception.CurrenciesAreNotEqualException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.CalculationStrategies;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;

@IntegrationTest
public class OrderIntegrationTest extends ServicelayerBaseTest
{
    private final Currency euro = new Currency("EUR", 2);
    @Resource
    private CalculationStrategies calculationStrategies;


    @Test
    public void testCalculateDiscountValues()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("100", this.euro), 1);
        LineItemDiscount tenEuroDiscount = new LineItemDiscount((AbstractAmount)new Money("10", this.euro));
        LineItemDiscount tenPercentDiscount = new LineItemDiscount((AbstractAmount)new Percentage(10));
        testobject.setOrder(ocDummy);
        testobject.addDiscount(tenEuroDiscount);
        testobject.addDiscount(tenPercentDiscount);
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(tenEuroDiscount));
        Assert.assertEquals(new Money("9", this.euro), testobject.getTotalDiscounts().get(tenPercentDiscount));
        Assert.assertEquals(new Money("81", this.euro), testobject.getTotal(null));
        testobject.clearDiscounts();
        testobject.addDiscount(tenPercentDiscount);
        testobject.addDiscount(tenEuroDiscount);
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(tenEuroDiscount));
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(tenPercentDiscount));
        Assert.assertEquals(new Money("80", this.euro), testobject.getTotal(null));
        testobject.clearDiscounts();
    }


    @Test
    public void testCalculatePercentDiscountOnSomeUnitsOnly()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        LineItemDiscount hundredPercentDiscountOn5Items = new LineItemDiscount((AbstractAmount)Percentage.HUNDRED, true, 5);
        testobject.setOrder(ocDummy);
        testobject.addDiscount(hundredPercentDiscountOn5Items);
        Assert.assertEquals(new Money("50", this.euro), testobject.getTotal(null));
    }


    @Test
    public void testCalculationWithDifferentCurrencies()
    {
        Currency dollar = new Currency("USD", 2);
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem entry1 = new LineItem(new Money("100", this.euro), 1);
        LineItem entry2 = new LineItem(new Money("100", dollar), 1);
        ocDummy.addLineItem(entry1);
        try
        {
            ocDummy.addLineItem(entry2);
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        ocDummy.getSubTotal();
        ocDummy.getTotalTax();
        ocDummy.getTotalDiscounts();
        ocDummy.getTotalCharges();
        Tax tax = new Tax((AbstractAmount)Percentage.FIFTY);
        tax.addTarget((Taxable)entry1);
        tax.addTarget((Taxable)entry2);
        ocDummy.addTax(tax);
        try
        {
            ocDummy.getTotalTax();
            Assert.fail("expected problem with different currencies here");
        }
        catch(CalculationException calculationException)
        {
        }
    }


    @Test
    public void testDifferentCurrencyInAdditionalChargesForLineItemsAndOrder()
    {
        Currency dollar = new Currency("USD", 2);
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem entry1 = new LineItem(new Money("100", this.euro), 1);
        LineItemCharge apc = new LineItemCharge((AbstractAmount)new Money("20", dollar));
        ocDummy.addLineItem(entry1);
        try
        {
            entry1.addCharge(apc);
            Assert.fail("expected problem with different currencies here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        ocDummy.getTotalCharges();
        OrderCharge aop = new OrderCharge((AbstractAmount)new Money("20", dollar));
        try
        {
            ocDummy.addCharge(aop);
            Assert.fail("expected problem with different currencies here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
    }


    @Test
    public void testDifferentCurrencyInDiscountsForLineItemsAndOrder()
    {
        Currency dollar = new Currency("USD", 2);
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem entry1 = new LineItem(new Money("100", this.euro), 1);
        ocDummy.addLineItem(entry1);
        ocDummy.getTotalDiscounts();
        entry1.getTotalDiscounts();
        LineItemDiscount prodDisc = new LineItemDiscount((AbstractAmount)new Money("10", dollar));
        OrderDiscount orderDisc = new OrderDiscount((AbstractAmount)new Money("10", dollar));
        try
        {
            entry1.addDiscount(prodDisc);
            Assert.fail("expected problem with different currencies here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        try
        {
            ocDummy.addDiscount(orderDisc);
            Assert.fail("expected problem with different currencies here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
    }


    @Test
    public void testCalculateOrderDiscounts()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem entry1 = new LineItem(new Money("100", this.euro), 1);
        ocDummy.addLineItem(entry1);
        OrderDiscount tenPercent = new OrderDiscount((AbstractAmount)Percentage.TEN);
        OrderDiscount tenEuros = new OrderDiscount((AbstractAmount)new Money(BigDecimal.valueOf(10L), this.euro));
        ocDummy.addDiscount(tenPercent);
        ocDummy.addDiscount(tenEuros);
        Map<OrderDiscount, Money> map = ocDummy.getTotalDiscounts();
        Assert.assertTrue((map.size() == 2));
        Assert.assertEquals(new Money(1000L, this.euro), map.get(tenPercent));
        Assert.assertEquals(new Money("10", this.euro), map.get(tenEuros));
        Assert.assertEquals(new Money("20", this.euro), ocDummy.getTotalDiscount());
        Assert.assertEquals(new Money("100", this.euro), ocDummy.getSubTotal());
        Assert.assertEquals(new Money("80", this.euro), ocDummy.getTotal());
        ocDummy.clearDiscounts();
        Assert.assertTrue(ocDummy.getDiscounts().isEmpty());
        ocDummy.addDiscount(tenEuros);
        ocDummy.addDiscount(tenPercent);
        map = ocDummy.getTotalDiscounts();
        Assert.assertTrue((map.size() == 2));
        Assert.assertEquals(new Money("9", this.euro), map.get(tenPercent));
        Assert.assertEquals(new Money(BigDecimal.valueOf(10L), this.euro), map.get(tenEuros));
        Assert.assertEquals(new Money("19", this.euro), ocDummy.getTotalDiscount());
        Assert.assertEquals(new Money("81", this.euro), ocDummy.getTotal());
    }


    @Test
    public void testCalculateAdditionalOrderCharges()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem entry1 = new LineItem(new Money("100", this.euro), 1);
        ocDummy.addLineItem(entry1);
        OrderCharge aoc1 = new OrderCharge((AbstractAmount)new Money("20", this.euro));
        OrderCharge aoc2 = new OrderCharge((AbstractAmount)Percentage.TWENTY);
        OrderCharge aoc3 = new OrderCharge((AbstractAmount)new Money("100", this.euro));
        aoc3.setDisabled(true);
        ocDummy.addCharges(Arrays.asList(new OrderCharge[] {aoc1, aoc2, aoc3}));
        Map<OrderCharge, Money> map = ocDummy.getTotalCharges();
        Assert.assertTrue((map.size() == 3));
    }


    @Test
    public void testGiveAwayDiscount()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        ocDummy.addLineItem(testobject);
        LineItemDiscount tenEuroDiscount = new LineItemDiscount((AbstractAmount)new Money("10", this.euro));
        LineItemDiscount tenPercentDiscount = new LineItemDiscount((AbstractAmount)new Percentage(10));
        Assert.assertEquals(new Money("100", this.euro), testobject.getTotal(null));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        testobject.addDiscounts(Arrays.asList(new LineItemDiscount[] {tenEuroDiscount, tenPercentDiscount}));
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(tenEuroDiscount));
        Assert.assertEquals(new Money("9", this.euro), testobject.getTotalDiscounts().get(tenPercentDiscount));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("81", this.euro), testobject.getTotal(null));
        testobject.setGiveAwayUnits(2);
        Assert.assertEquals(new Money("80", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("63", this.euro), testobject.getTotal(null));
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(tenEuroDiscount));
        Assert.assertEquals(new Money("7", this.euro), testobject.getTotalDiscounts().get(tenPercentDiscount));
    }


    @Test
    public void testPercentDiscountsPerUnit()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        ocDummy.addLineItem(testobject);
        LineItemDiscount lid = new LineItemDiscount((AbstractAmount)Percentage.TWENTY, true, 100);
        testobject.addDiscount(lid);
        Assert.assertEquals(new Money("20", this.euro), testobject.getTotalDiscounts().get(lid));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("80", this.euro), testobject.getTotal(null));
        testobject.setGiveAwayUnits(10);
        Assert.assertEquals(new Money("0", this.euro), testobject.getTotalDiscounts().get(lid));
        Assert.assertEquals(new Money("0", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("0", this.euro), testobject.getTotal(null));
    }


    @Test
    public void testMoneyDiscountsPerUnit()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        ocDummy.addLineItem(testobject);
        LineItemDiscount lid = new LineItemDiscount((AbstractAmount)new Money("1", this.euro), true, 100);
        testobject.addDiscount(lid);
        Assert.assertEquals(new Money("10", this.euro), testobject.getTotalDiscounts().get(lid));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("90", this.euro), testobject.getTotal(null));
    }


    @Test
    public void testMoneyChargePerUnit()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        ocDummy.addLineItem(testobject);
        LineItemCharge lic = new LineItemCharge((AbstractAmount)new Money("7.34", this.euro), true, 7);
        testobject.addCharge(lic);
        Assert.assertEquals(new Money("51.38", this.euro), testobject.getTotalCharges().get(lic));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("151.38", this.euro), testobject.getTotal(null));
        testobject.setGiveAwayUnits(10);
        Assert.assertEquals(new Money("51.38", this.euro), testobject.getTotalCharges().get(lic));
        Assert.assertEquals(new Money("0", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("51.38", this.euro), testobject.getTotal(null));
    }


    @Test
    public void testPercentChargePerUnit()
    {
        Order ocDummy = new Order(this.euro, false, this.calculationStrategies);
        LineItem testobject = new LineItem(new Money("10", this.euro), 10);
        ocDummy.addLineItem(testobject);
        LineItemCharge lic = new LineItemCharge((AbstractAmount)Percentage.TWENTYFIVE, true, 17);
        testobject.addCharge(lic);
        LineItemCharge dontcharged = new LineItemCharge((AbstractAmount)new Money("1000", this.euro), true, 1000);
        testobject.addCharge(dontcharged);
        dontcharged.setDisabled(true);
        Assert.assertEquals(new Money("0", this.euro), testobject.getTotalCharges().get(dontcharged));
        Assert.assertEquals(new Money("25", this.euro), testobject.getTotalCharges().get(lic));
        Assert.assertEquals(new Money("100", this.euro), testobject.getSubTotal());
        Assert.assertEquals(new Money("125", this.euro), testobject.getTotal(null));
    }
}
