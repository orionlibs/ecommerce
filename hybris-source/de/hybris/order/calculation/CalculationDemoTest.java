package de.hybris.order.calculation;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.order.calculation.domain.AbstractCharge;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemCharge;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.domain.Tax;
import de.hybris.order.calculation.domain.Taxable;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.CalculationStrategies;
import de.hybris.order.calculation.strategies.RoundingStrategy;
import de.hybris.order.calculation.strategies.impl.DefaultRoundingStrategy;
import de.hybris.order.calculation.strategies.impl.DefaultTaxRoundingStrategy;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@DemoTest
public class CalculationDemoTest
{
    private Currency euro;
    private CalculationStrategies strategies;
    private Tax tax19;
    private Tax tax7;


    @Before
    public void showInitialization()
    {
        this.euro = new Currency("EUR", 2);
        this.strategies = new CalculationStrategies();
        this.strategies.setRoundingStrategy((RoundingStrategy)new DefaultRoundingStrategy());
        this.strategies.setTaxRoundingStrategy((RoundingStrategy)new DefaultTaxRoundingStrategy());
        this.tax19 = new Tax((AbstractAmount)new Percentage(19));
        this.tax7 = new Tax((AbstractAmount)new Percentage(7));
    }


    @Test
    public void testPlain()
    {
        Order order = new Order(this.euro, this.strategies);
        order.addLineItems(new LineItem[] {new LineItem(new Money("1.33", this.euro), 5), new LineItem(new Money("19.99", this.euro)), new LineItem(new Money("5.55", this.euro), 3)});
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("43.29", this.euro), order.getTotal());
    }


    @Test
    public void testVATGross()
    {
        Order order = new Order(this.euro, this.strategies);
        order.addLineItems(new LineItem[] {new LineItem(new Money("1.33", this.euro), 5), new LineItem(new Money("19.99", this.euro)), new LineItem(new Money("5.55", this.euro), 3)});
        Tax tax19 = new Tax((AbstractAmount)new Percentage(19));
        Tax tax7 = new Tax((AbstractAmount)new Percentage(7));
        order.addTax(tax7);
        order.addTax(tax19);
        tax19.addTargets(new Taxable[] {order.getLineItems().get(0), order.getLineItems().get(1)});
        tax7.addTarget(order.getLineItems().get(2));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("4.25", this.euro), order.getTotalTaxFor(tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(tax7));
        Assert.assertEquals(new Money("43.29", this.euro), order.getTotal());
    }


    @Test
    public void testVATnet()
    {
        Order order = new Order(this.euro, true, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.06", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.16", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("43.29", this.euro), order.getTotal());
        Assert.assertEquals(new Money("49.51", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderChargesPlain()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        order.addCharge(new OrderCharge((AbstractAmount)new Money("5.99", this.euro), AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.99", this.euro), order.getTotalChargeOfType(AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotal());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderChargesVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        order.addCharge(new OrderCharge((AbstractAmount)new Money("5.99", this.euro), AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("4.84", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.23", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("5.99", this.euro), order.getTotalCharge());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotal());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderChargesVATnet()
    {
        Order order = new Order(this.euro, true, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        order.addCharge(new OrderCharge((AbstractAmount)new Money("5.99", this.euro), AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.76", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.32", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("5.99", this.euro), order.getTotalCharge());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotal());
        Assert.assertEquals(new Money("56.36", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderDiscountsPlain()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        order.addDiscount(new OrderDiscount((AbstractAmount)new Money("5", this.euro)));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5", this.euro), order.getTotalDiscount());
        Assert.assertEquals(new Money("38.29", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderDiscountVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        order.addDiscount(new OrderDiscount((AbstractAmount)new Money("5", this.euro)));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("3.76", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("0.96", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("5.00", this.euro), order.getTotalDiscount());
        Assert.assertEquals(new Money("38.29", this.euro), order.getTotal());
        Assert.assertEquals(new Money("38.29", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testOrderDiscountVATnet()
    {
        Order order = new Order(this.euro, true, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        order.addDiscount(new OrderDiscount((AbstractAmount)new Money("5", this.euro)));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.00", this.euro), order.getTotalDiscount());
        Assert.assertEquals(new Money("4.47", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.03", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("38.29", this.euro), order.getTotal());
        Assert.assertEquals(new Money("43.79", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testMixedOrderDiscountsAndChargesVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        order.addDiscount(new OrderDiscount((AbstractAmount)new Money("5", this.euro)));
        order.addCharge(new OrderCharge((AbstractAmount)new Money("10", this.euro), AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.00", this.euro), order.getTotalDiscount());
        Assert.assertEquals(new Money("10.00", this.euro), order.getTotalChargeOfType(AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("4.74", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.21", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("48.29", this.euro), order.getTotal());
        Assert.assertEquals(new Money("48.29", this.euro), order.getTotalIncludingTaxes());
    }


    @Test
    public void testLineItemDiscountPlain()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        LineItemDiscount discount1 = new LineItemDiscount((AbstractAmount)Percentage.TEN, true, 5);
        LineItemDiscount discount2 = new LineItemDiscount((AbstractAmount)new Money("5", this.euro), true, 10);
        lineitem1.addDiscount(discount1);
        lineitem2.addDiscount(discount2);
        Assert.assertEquals(new Money("5.98", this.euro), lineitem1.getTotal(null));
        Assert.assertEquals(new Money("14.99", this.euro), lineitem2.getTotal(null));
        Assert.assertEquals(new Money("16.65", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("0.67", this.euro), lineitem1.getTotalDiscounts().get(discount1));
        Assert.assertEquals(new Money("5", this.euro), lineitem2.getTotalDiscounts().get(discount2));
        Assert.assertEquals(new Money("37.62", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("37.62", this.euro), order.getTotal());
    }


    @Test
    public void testLineItemDiscountVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        LineItemDiscount discount1 = new LineItemDiscount((AbstractAmount)Percentage.TEN, true, 5);
        LineItemDiscount discount2 = new LineItemDiscount((AbstractAmount)new Money("5", this.euro), true, 10);
        lineitem1.addDiscount(discount1);
        lineitem2.addDiscount(discount2);
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        Assert.assertEquals(new Money("3.34", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("37.62", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("37.62", this.euro), order.getTotal());
    }


    @Test
    public void testLineItemChargesVAT()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        lineitem1.addCharge(new LineItemCharge((AbstractAmount)new Money("5", this.euro), true));
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        Assert.assertEquals(new Money("8.24", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("68.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("68.29", this.euro), order.getTotal());
    }


    @Test
    public void testLineItemForFreeVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        lineitem3.setGiveAwayUnits(1);
        Assert.assertEquals(new Money("11.10", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("4.25", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("0.72", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("37.74", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("37.74", this.euro), order.getTotal());
    }


    @Test
    public void testLineItemForFreeAndDiscountVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        lineitem3.setGiveAwayUnits(1);
        lineitem3.addDiscount(new LineItemDiscount((AbstractAmount)Percentage.TEN));
        Assert.assertEquals(new Money("9.99", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("4.25", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("0.65", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("36.63", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("36.63", this.euro), order.getTotal());
    }


    @Test
    public void testLimitedQuantityLineItemDiscountVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        LineItemDiscount discount1 = new LineItemDiscount((AbstractAmount)new Money("1", this.euro), true, 2);
        LineItemDiscount discount3 = new LineItemDiscount((AbstractAmount)Percentage.TEN, true, 1);
        lineitem1.addDiscount(discount1);
        lineitem3.addDiscount(discount3);
        Assert.assertEquals(new Money("4.65", this.euro), lineitem1.getTotal(null));
        Assert.assertEquals(new Money("19.99", this.euro), lineitem2.getTotal(null));
        Assert.assertEquals(new Money("16.09", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("2", this.euro), lineitem1.getTotalDiscounts().get(discount1));
        Assert.assertEquals(new Money("0.56", this.euro), lineitem3.getTotalDiscounts().get(discount3));
        Assert.assertEquals(new Money("3.93", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.05", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("40.73", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("40.73", this.euro), order.getTotal());
    }


    @Test
    public void testAbsoluteLineItemDiscountsAndChargesVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        LineItemCharge giftwrapping = new LineItemCharge((AbstractAmount)new Money("5", this.euro));
        LineItemDiscount couponXYZ = new LineItemDiscount((AbstractAmount)new Money("2.5", this.euro));
        LineItemDiscount couponABC = new LineItemDiscount((AbstractAmount)new Money("5", this.euro));
        lineitem1.addCharge(giftwrapping);
        lineitem2.addCharge(giftwrapping);
        lineitem2.addDiscount(couponXYZ);
        lineitem3.addDiscount(couponABC);
        Assert.assertEquals(new Money("11.65", this.euro), lineitem1.getTotal(null));
        Assert.assertEquals(new Money("22.49", this.euro), lineitem2.getTotal(null));
        Assert.assertEquals(new Money("11.65", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("5", this.euro), lineitem1.getTotalCharges().get(giftwrapping));
        Assert.assertEquals(new Money("5", this.euro), lineitem2.getTotalCharges().get(giftwrapping));
        Assert.assertEquals(new Money("2.5", this.euro), lineitem2.getTotalDiscounts().get(couponXYZ));
        Assert.assertEquals(new Money("5", this.euro), lineitem3.getTotalDiscounts().get(couponABC));
        Assert.assertEquals(new Money("45.79", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("45.79", this.euro), order.getTotal());
        Assert.assertEquals(new Money("5.45", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("0.76", this.euro), order.getTotalTaxFor(this.tax7));
    }


    @Test
    public void testAbsoluteLineItemChargeAndAllForFree()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        LineItemCharge giftwrapping = new LineItemCharge((AbstractAmount)new Money("5", this.euro));
        lineitem1.setGiveAwayUnits(5);
        lineitem1.addCharge(giftwrapping);
        Assert.assertEquals(new Money("5", this.euro), lineitem1.getTotal(null));
        Assert.assertEquals(new Money("41.64", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("3.98", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
    }


    @Test
    public void testDiscountOnProductCombination()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem laptop = new LineItem(new Money("599", this.euro), 2);
        LineItem bag = new LineItem(new Money("59", this.euro), 1);
        order.addLineItems(new LineItem[] {laptop, bag});
        LineItemDiscount bundleDiscount = new LineItemDiscount((AbstractAmount)Percentage.TEN, true, 1);
        laptop.addDiscount(bundleDiscount);
        bag.addDiscount(bundleDiscount);
        Assert.assertEquals(new Money("1138.10", this.euro), laptop.getTotal(null));
        Assert.assertEquals(new Money("53.10", this.euro), bag.getTotal(null));
        Assert.assertEquals(new Money("59.9", this.euro), laptop.getTotalDiscounts().get(bundleDiscount));
        Assert.assertEquals(new Money("5.9", this.euro), bag.getTotalDiscounts().get(bundleDiscount));
        Assert.assertEquals(new Money("1191.20", this.euro), order.getTotal());
    }


    @Test
    public void testFixedPriceBundle()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem screen = new LineItem(new Money("1199.00", this.euro), 1);
        LineItem wallMount = new LineItem(new Money("109.00", this.euro), 1);
        LineItem dvd = new LineItem(new Money("19.99", this.euro));
        order.addLineItems(new LineItem[] {screen, wallMount, dvd});
        Money screenSubTotal = screen.getSubTotal();
        Money wallMountSubTotal = wallMount.getSubTotal();
        Money dvdSubTotal = dvd.getSubTotal();
        List<Percentage> priceDistribution = Money.getPercentages(new Money[] {screenSubTotal, wallMountSubTotal, dvdSubTotal});
        Assert.assertEquals(Percentage.valueOf(new String[] {"91", "8", "1"}, ), priceDistribution);
        Money overallDiscount = Money.sum(new Money[] {screenSubTotal, wallMountSubTotal, dvdSubTotal}).subtract(new Money("999.00", this.euro));
        Assert.assertEquals(new Money("328.99", this.euro), overallDiscount);
        List<Money> splitUpDiscount = overallDiscount.split(priceDistribution);
        Assert.assertEquals(Money.valueOf(this.euro, new String[] {"299.39", "26.32", "3.28"}), splitUpDiscount);
        Money screenOff = splitUpDiscount.get(0);
        LineItemDiscount bundleScreenDiscount = new LineItemDiscount((AbstractAmount)screenOff, true, 1);
        screen.addDiscount(bundleScreenDiscount);
        Money wallMountOff = splitUpDiscount.get(1);
        LineItemDiscount bundleWallMountDiscount = new LineItemDiscount((AbstractAmount)wallMountOff, true, 1);
        wallMount.addDiscount(bundleWallMountDiscount);
        Money dvdOff = splitUpDiscount.get(2);
        LineItemDiscount bundleDVDDiscount = new LineItemDiscount((AbstractAmount)dvdOff, true, 1);
        dvd.addDiscount(bundleDVDDiscount);
        Assert.assertEquals(new Money("899.61", this.euro), screen.getTotal(null));
        Assert.assertEquals(new Money("82.68", this.euro), wallMount.getTotal(null));
        Assert.assertEquals(new Money("16.71", this.euro), dvd.getTotal(null));
        Assert.assertEquals(screenOff, screen.getTotalDiscounts().get(bundleScreenDiscount));
        Assert.assertEquals(wallMountOff, wallMount.getTotalDiscounts().get(bundleWallMountDiscount));
        Assert.assertEquals(dvdOff, dvd.getTotalDiscounts().get(bundleDVDDiscount));
        Assert.assertEquals(new Money("999.00", this.euro), order.getTotal());
    }


    @Test
    public void testFreeOrderShippingVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        OrderCharge shippingCharge = new OrderCharge((AbstractAmount)new Money("5.99", this.euro), AbstractCharge.ChargeType.SHIPPING);
        order.addCharge(shippingCharge);
        shippingCharge.setDisabled(true);
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(Money.zero(this.euro), order.getTotalChargeOfType(AbstractCharge.ChargeType.SHIPPING));
        Assert.assertEquals(new Money("43.29", this.euro), order.getTotal());
        Assert.assertEquals(new Money("43.29", this.euro), order.getTotalIncludingTaxes());
        Assert.assertEquals(new Money("4.25", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
    }


    @Test
    public void testFreeItemShippingVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        LineItemCharge charge1 = new LineItemCharge((AbstractAmount)new Money("4.90", this.euro));
        LineItemCharge charge2 = new LineItemCharge((AbstractAmount)new Money("4.90", this.euro));
        LineItemCharge charge3 = new LineItemCharge((AbstractAmount)new Money("4.90", this.euro));
        lineitem1.addCharge(charge1);
        lineitem2.addCharge(charge2);
        lineitem3.addCharge(charge3);
        charge1.setDisabled(true);
        charge3.setDisabled(true);
        Assert.assertEquals(new Money("6.65", this.euro), lineitem1.getTotal(null));
        Assert.assertEquals(new Money("24.89", this.euro), lineitem2.getTotal(null));
        Assert.assertEquals(new Money("16.65", this.euro), lineitem3.getTotal(null));
        Assert.assertEquals(new Money("48.19", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("48.19", this.euro), order.getTotal());
        Assert.assertEquals(new Money("5.03", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
    }


    @Test
    public void testFixedTaxOrderChargesVATgross()
    {
        Order order = new Order(this.euro, this.strategies);
        LineItem lineitem1 = new LineItem(new Money("1.33", this.euro), 5);
        LineItem lineitem2 = new LineItem(new Money("19.99", this.euro));
        LineItem lineitem3 = new LineItem(new Money("5.55", this.euro), 3);
        order.addLineItems(new LineItem[] {lineitem1, lineitem2, lineitem3});
        this.tax19.addTargets(new Taxable[] {(Taxable)lineitem1, (Taxable)lineitem2});
        this.tax7.addTarget((Taxable)lineitem3);
        order.addTaxes(new Tax[] {this.tax7, this.tax19});
        OrderCharge shippingCharge = new OrderCharge((AbstractAmount)new Money("5.99", this.euro), AbstractCharge.ChargeType.SHIPPING);
        order.addCharge(shippingCharge);
        this.tax19.addTarget((Taxable)shippingCharge);
        Assert.assertEquals(new Money("43.29", this.euro), order.getSubTotal());
        Assert.assertEquals(new Money("5.20", this.euro), order.getTotalTaxFor(this.tax19));
        Assert.assertEquals(new Money("1.08", this.euro), order.getTotalTaxFor(this.tax7));
        Assert.assertEquals(new Money("5.99", this.euro), order.getTotalCharge());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotal());
        Assert.assertEquals(new Money("49.28", this.euro), order.getTotalIncludingTaxes());
    }
}
