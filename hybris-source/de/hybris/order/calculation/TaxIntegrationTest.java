package de.hybris.order.calculation;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.order.calculation.domain.AbstractCharge;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemCharge;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.domain.Tax;
import de.hybris.order.calculation.domain.Taxable;
import de.hybris.order.calculation.exception.CurrenciesAreNotEqualException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.CalculationStrategies;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class TaxIntegrationTest extends ServicelayerTest
{
    @Resource
    private CalculationStrategies calculationStrategies;
    private final Currency euro = new Currency("EUR", 2);
    private final Currency dollar = new Currency("USD", 2);
    private LineItem lineItem1;
    private LineItem lineItem2;
    private LineItem lineItem3;
    private Tax tax19;
    private Tax tax7;
    private Order ocEuroNet;
    private Order ocEuroGross;


    @Before
    public void setup()
    {
        this.ocEuroNet = new Order(this.euro, true, this.calculationStrategies);
        this.ocEuroGross = new Order(this.euro, false, this.calculationStrategies);
        Assert.assertNotNull(this.ocEuroNet);
        Assert.assertNotNull(this.ocEuroGross);
        this.tax19 = new Tax((AbstractAmount)new Percentage(19));
        this.tax7 = new Tax((AbstractAmount)new Percentage(7));
        this.lineItem1 = new LineItem(new Money("52.99", this.euro), 1);
        this.lineItem2 = new LineItem(new Money("15.37", this.euro), 5);
        this.lineItem3 = new LineItem(new Money("24.89", this.euro), 10);
        this.tax19.addTarget((Taxable)this.lineItem1);
        this.tax19.addTarget((Taxable)this.lineItem3);
        this.tax7.addTarget((Taxable)this.lineItem2);
    }


    @Test
    public void testCalculateEverythingWithZeroAmounts()
    {
        Money zeroMoney = new Money(this.euro);
        Percentage zeroPercent = new Percentage(0);
        Order oNet = new Order(this.euro, true, this.calculationStrategies);
        Order oGross = new Order(this.euro, false, this.calculationStrategies);
        OrderCharge ocMoney = new OrderCharge((AbstractAmount)zeroMoney);
        OrderCharge ocPercentage = new OrderCharge((AbstractAmount)zeroPercent, AbstractCharge.ChargeType.SHIPPING);
        oNet.addCharge(ocMoney);
        oNet.addCharge(ocPercentage);
        oGross.addCharge(ocMoney);
        oGross.addCharge(ocPercentage);
        OrderDiscount odMoney = new OrderDiscount((AbstractAmount)zeroMoney);
        OrderDiscount odPecent = new OrderDiscount((AbstractAmount)zeroPercent);
        oNet.addDiscount(odPecent);
        oNet.addDiscount(odMoney);
        oGross.addDiscount(odPecent);
        oGross.addDiscount(odMoney);
        LineItem lineItem = new LineItem(zeroMoney, 0);
        LineItemCharge licMoney = new LineItemCharge((AbstractAmount)zeroMoney);
        LineItemCharge licPercent = new LineItemCharge((AbstractAmount)zeroPercent);
        LineItemDiscount lidMoney = new LineItemDiscount((AbstractAmount)zeroMoney);
        LineItemDiscount lidPercent = new LineItemDiscount((AbstractAmount)zeroPercent);
        lineItem.addCharges(Arrays.asList(new LineItemCharge[] {licMoney, licPercent}));
        lineItem.addDiscounts(Arrays.asList(new LineItemDiscount[] {lidMoney, lidPercent}));
        oNet.addLineItem(lineItem);
        oGross.addLineItem(lineItem);
        Tax zeroMoneyTax = new Tax((AbstractAmount)zeroMoney);
        Tax zeroPercentTax = new Tax((AbstractAmount)zeroPercent);
        Tax tenPercentTax = new Tax((AbstractAmount)Percentage.TEN);
        Tax tenMoneyTax = new Tax((AbstractAmount)new Money("10", this.euro));
        oNet.addTaxes(Arrays.asList(new Tax[] {zeroMoneyTax, zeroPercentTax, tenPercentTax}));
        oGross.addTaxes(Arrays.asList(new Tax[] {zeroMoneyTax, zeroPercentTax, tenPercentTax}));
        zeroMoneyTax.addTargets(Arrays.asList(new Taxable[] {(Taxable)lineItem, (Taxable)ocMoney}));
        zeroPercentTax.addTarget((Taxable)ocPercentage);
        tenPercentTax.addTargets(Arrays.asList(new Taxable[] {(Taxable)lineItem, (Taxable)ocMoney, (Taxable)ocPercentage}));
        tenMoneyTax.addTarget((Taxable)lineItem);
        checkCorrectOrderCalculation(oGross, "0", "0", "0", "0", "0", "0", "0", "0");
        checkCorrectOrderCalculation(oNet, "0", "0", "0", "0", "0", "0", "0", "0");
        checkCorrectLineItemCalculation(lineItem, "0", "0", "0", "0", 0);
    }


    @Test
    public void testGrossCalculationWithTaxRates()
    {
        this.ocEuroGross.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroGross.addTax(this.tax19);
        this.ocEuroGross.addTax(this.tax7);
        checkCorrectOrderCalculation(this.ocEuroGross, "378.74", "378.74", "0", "0", "378.74", "53.22", "0", "0");
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "52.99", "0", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "76.85", "0", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.90", "0", "0", 10);
        checkGermanTaxes(this.ocEuroGross, "5.02", "48.20");
    }


    @Test
    public void testNetCalculationWithTaxRates()
    {
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroNet.addTax(this.tax19);
        this.ocEuroNet.addTax(this.tax7);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "52.99", "0", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "76.85", "0", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.90", "0", "0", 10);
        checkGermanTaxes(this.ocEuroNet, "5.37", "57.35");
        checkCorrectOrderCalculation(this.ocEuroNet, "378.74", "378.74", "0", "0", "441.46", "62.72", "0", "0");
    }


    @Test
    public void testNetWithAdditionalLineItemCharges()
    {
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroNet.addTax(this.tax19);
        this.ocEuroNet.addTax(this.tax7);
        LineItemCharge apc10percent = new LineItemCharge((AbstractAmount)Percentage.TEN);
        LineItemCharge apc10euro = new LineItemCharge((AbstractAmount)new Money("10", this.euro));
        this.lineItem1.addCharge(apc10euro);
        this.lineItem2.addCharge(apc10percent);
        this.lineItem3.addCharge(apc10percent);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "62.99", "10", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "84.54", "7.69", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "273.79", "24.89", "0", 10);
        checkGermanTaxes(this.ocEuroNet, "5.91", "63.98");
        checkCorrectOrderCalculation(this.ocEuroNet, "421.32", "421.32", "0", "0", "491.21", "69.89", "0", "0");
    }


    @Test
    public void testGrossWithAdditionalLineItemCharges()
    {
        this.ocEuroGross.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroGross.addTax(this.tax19);
        this.ocEuroGross.addTax(this.tax7);
        LineItemCharge apc10percent = new LineItemCharge((AbstractAmount)Percentage.TEN);
        LineItemCharge apc10euro = new LineItemCharge((AbstractAmount)new Money("10", this.euro));
        this.lineItem1.addCharge(apc10euro);
        this.lineItem2.addCharge(apc10percent);
        this.lineItem3.addCharge(apc10percent);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "62.99", "10", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "84.54", "7.69", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "273.79", "24.89", "0", 10);
        checkGermanTaxes(this.ocEuroGross, "5.53", "53.77");
        checkCorrectOrderCalculation(this.ocEuroGross, "421.32", "421.32", "0", "0", "421.32", "59.3", "0", "0");
    }


    @Test
    public void testNetWithLineItemDiscounts()
    {
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroNet.addTax(this.tax19);
        this.ocEuroNet.addTax(this.tax7);
        LineItem additionalLineItem = new LineItem(new Money("100", this.euro), 10);
        this.ocEuroNet.addLineItem(additionalLineItem);
        this.tax19.addTarget((Taxable)additionalLineItem);
        LineItemDiscount fivePercentDiscount = new LineItemDiscount((AbstractAmount)new Percentage(5));
        LineItemDiscount fiveEuroDiscount = new LineItemDiscount((AbstractAmount)new Money("5", this.euro));
        LineItemDiscount fiftyCentOnOneItem = new LineItemDiscount((AbstractAmount)new Money("0.5", this.euro), true, 1);
        LineItemDiscount minus10EuroFor5Items = new LineItemDiscount((AbstractAmount)new Money("10", this.euro), true, 5);
        this.lineItem1.addDiscount(fiveEuroDiscount);
        this.lineItem1.addDiscount(fivePercentDiscount);
        this.lineItem2.setGiveAwayUnits(2);
        this.lineItem3.addDiscount(fiftyCentOnOneItem);
        additionalLineItem.setGiveAwayUnits(1);
        additionalLineItem.addDiscount(minus10EuroFor5Items);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "45.59", "0", "7.4", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "46.11", "46.11", "0", "0", 3);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.40", "0", "0.5", 10);
        checkCorrectLineItemCalculation(additionalLineItem, "900", "850", "0", "50", 9);
        checkGermanTaxes(this.ocEuroNet, "3.22", "217.35");
        checkCorrectOrderCalculation(this.ocEuroNet, "1190.10", "1190.10", "0", "0", "1410.67", "220.57", "0", "0");
    }


    @Test
    public void testNetWithOrderCharges()
    {
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroNet.addTax(this.tax19);
        this.ocEuroNet.addTax(this.tax7);
        OrderCharge shippingFee = new OrderCharge((AbstractAmount)new Money("25", this.euro), AbstractCharge.ChargeType.SHIPPING);
        OrderCharge paymentFee = new OrderCharge((AbstractAmount)Percentage.TEN, AbstractCharge.ChargeType.PAYMENT);
        this.ocEuroNet.addCharge(shippingFee);
        this.ocEuroNet.addCharge(paymentFee);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "52.99", "0", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "76.85", "0", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.90", "0", "0", 10);
        checkGermanTaxes(this.ocEuroNet, "6.30", "67.25");
        checkCorrectOrderCalculation(this.ocEuroNet, "378.74", "444.11", "0", "65.37", "517.66", "73.55", "25", "40.37");
    }


    @Test
    public void testGrossWithOrderCharges()
    {
        this.ocEuroGross.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroGross.addTax(this.tax19);
        this.ocEuroGross.addTax(this.tax7);
        OrderCharge shippingFee = new OrderCharge((AbstractAmount)new Money("30", this.euro));
        this.ocEuroGross.addCharge(shippingFee);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "52.99", "0", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "76.85", "0", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.90", "0", "0", 10);
    }


    @Test
    public void testNetWithOrderDiscounts()
    {
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroNet.addTax(this.tax19);
        this.ocEuroNet.addTax(this.tax7);
        Collection<Tax> taxColl1 = this.ocEuroNet.getTaxesFor((Taxable)this.lineItem1);
        Collection<Tax> taxColl2 = this.ocEuroNet.getTaxesFor((Taxable)this.lineItem2);
        Collection<Tax> taxColl3 = this.ocEuroNet.getTaxesFor((Taxable)this.lineItem3);
        Assert.assertTrue(taxColl1.contains(this.tax19));
        Assert.assertFalse(taxColl1.contains(this.tax7));
        Assert.assertTrue(taxColl2.contains(this.tax7));
        Assert.assertFalse(taxColl2.contains(this.tax19));
        Assert.assertTrue(taxColl3.contains(this.tax19));
        Assert.assertFalse(taxColl3.contains(this.tax7));
        Assert.assertTrue(this.ocEuroNet.hasAssignedTaxes((Taxable)this.lineItem1));
        Assert.assertTrue(this.ocEuroNet.hasAssignedTaxes((Taxable)this.lineItem2));
        Assert.assertTrue(this.ocEuroNet.hasAssignedTaxes((Taxable)this.lineItem3));
        OrderDiscount firstCustomer = new OrderDiscount((AbstractAmount)new Money("10", this.euro));
        this.ocEuroNet.addDiscount(firstCustomer);
        OrderDiscount friday10percentOff = new OrderDiscount((AbstractAmount)Percentage.TEN);
        this.ocEuroNet.addDiscount(0, friday10percentOff);
        checkCorrectLineItemCalculation(this.lineItem1, "52.99", "52.99", "0", "0", 1);
        checkCorrectLineItemCalculation(this.lineItem2, "76.85", "76.85", "0", "0", 5);
        checkCorrectLineItemCalculation(this.lineItem3, "248.90", "248.90", "0", "0", 10);
        checkGermanTaxes(this.ocEuroNet, "4.69", "50.10");
        checkCorrectOrderCalculation(this.ocEuroNet, "378.74", "330.87", "47.87", "0", "385.66", "54.79", "0", "0");
    }


    @Test
    public void veryBigCalculationWithEverythingInNet()
    {
        LineItem productONE = new LineItem(new Money("29.89", this.euro), 13);
        LineItem productZWO = new LineItem(new Money("16.67", this.euro), 10);
        productZWO.setGiveAwayUnits(3);
        LineItem productTHREE = new LineItem(new Money("899", this.euro), 2);
        LineItem productFOUR = new LineItem(new Money("45.99", this.euro), 1);
        productFOUR.setGiveAwayUnits(1);
        this.ocEuroNet.addLineItems(Arrays.asList(new LineItem[] {productONE, productZWO, productTHREE, productFOUR}));
    }


    private void checkGermanTaxes(Order order, String sevenPercent, String nineteenPercent)
    {
        Assert.assertEquals("invalid 7% tax:", new Money(sevenPercent, order.getCurrency()), order.getTotalTaxes().get(this.tax7));
        Assert.assertEquals("invalid 19% tax:", new Money(nineteenPercent, order.getCurrency()), order.getTotalTaxes().get(this.tax19));
    }


    private void checkCorrectLineItemCalculation(LineItem lineItem, String subtotal, String taxableTotal, String totalCharges, String totalDiscount, int calculatedNumberOfUnits)
    {
        Assert.assertEquals("invalid subtotal:", new Money(subtotal, lineItem.getOrder().getCurrency()), lineItem.getSubTotal());
        Assert.assertEquals("invalid taxableTotal:", new Money(taxableTotal, lineItem.getOrder().getCurrency()), lineItem.getTotal(null));
        Assert.assertEquals("invalid totalCharges:", new Money(totalCharges, lineItem.getOrder().getCurrency()), lineItem.getTotalCharge());
        Assert.assertEquals("invalid totalDiscount:", new Money(totalDiscount, lineItem.getOrder().getCurrency()), lineItem
                        .getTotalDiscount());
        Assert.assertEquals("invalid calculated number of units:", calculatedNumberOfUnits, lineItem.getNumberOfUnitsForCalculation());
    }


    private void checkCorrectOrderCalculation(Order order, String subtotal, String total, String totalOrderDiscount, String totalOrderCharge, String totalIncludingTaxes, String totalTaxes, String shippingCosts, String paymentCosts)
    {
        Assert.assertEquals("invalid subtotal:", new Money(subtotal, order.getCurrency()), order.getSubTotal());
        Assert.assertEquals("invalid total:", new Money(total, order.getCurrency()), order.getTotal());
        Assert.assertEquals("invalid totalOrderDiscount:", new Money(totalOrderDiscount, order.getCurrency()), order.getTotalDiscount());
        Assert.assertEquals("invalid totalOrderCharges:", new Money(totalOrderCharge, order.getCurrency()), order.getTotalCharge());
        Assert.assertEquals("invalid totalIncludingTaxes:", new Money(totalIncludingTaxes, order.getCurrency()), order
                        .getTotalIncludingTaxes());
        Assert.assertEquals("invalid totalTaxes:", new Money(totalTaxes, order.getCurrency()), order.getTotalTax());
        Assert.assertEquals("invalid shipping OrderCharge:", new Money(shippingCosts, order.getCurrency()), order
                        .getTotalChargeOfType(AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals("invalid payment OrderCharge:", new Money(paymentCosts, order.getCurrency()), order
                        .getTotalChargeOfType(AbstractCharge.ChargeType.PAYMENT));
    }


    @Test
    public void testTaxCalculationWithDifferentCurrencies()
    {
        this.ocEuroGross.addLineItems(Arrays.asList(new LineItem[] {this.lineItem1, this.lineItem2, this.lineItem3}));
        this.ocEuroGross.addTax(this.tax19);
        Tax dollarTax = new Tax((AbstractAmount)new Money("0", this.dollar));
        try
        {
            this.ocEuroGross.addTax(dollarTax);
            Assert.fail("Expected CurrenciesAreNotEqualException here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
    }
}
