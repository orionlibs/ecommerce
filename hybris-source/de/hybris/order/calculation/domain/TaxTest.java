package de.hybris.order.calculation.domain;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.exception.MissingCalculationDataException;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class TaxTest
{
    private final Currency curr = new Currency("xxx", 2);


    @Test(expected = IllegalArgumentException.class)
    public void testWithNull()
    {
        new Tax(null);
    }


    @Test
    public void testTaxGetters()
    {
        Tax tax = new Tax((AbstractAmount)Percentage.FIFTY);
        Assert.assertEquals(Percentage.FIFTY, tax.getAmount());
        Assert.assertNotNull(tax.getTargets());
        Assert.assertTrue(tax.getTargets().isEmpty());
    }


    @Test
    public void testSetAndModifyOrderDiscounts()
    {
        Tax testobject = new Tax((AbstractAmount)Percentage.TEN);
        LineItem pc1 = new LineItem(new Money("10", this.curr));
        LineItem pc2 = new LineItem(new Money("10", this.curr), 2);
        LineItem pc3 = new LineItem(new Money("10", this.curr), 3);
        testobject.addTarget((Taxable)pc2);
        testobject.addTarget((Taxable)pc1);
        Assert.assertTrue((testobject.getTargets().size() == 2));
        Assert.assertTrue(testobject.getTargets().contains(pc2));
        Assert.assertTrue(testobject.getTargets().contains(pc1));
        try
        {
            testobject.getTargets().clear();
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
        testobject.clearTargets();
        Assert.assertTrue(testobject.getTargets().isEmpty());
        List<Taxable> list = new ArrayList<>();
        list.add(pc3);
        list.add(pc1);
        testobject.addTargets(list);
        Assert.assertTrue(testobject.getTargets().contains(pc1));
        try
        {
            Assert.assertNull(pc2.getOrder());
        }
        catch(MissingCalculationDataException missingCalculationDataException)
        {
        }
        testobject.addTarget((Taxable)pc2);
        Assert.assertTrue(testobject.getTargets().contains(pc2));
        Assert.assertTrue((testobject.getTargets().size() == 3));
        Assert.assertTrue(testobject.getTargets().contains(pc2));
        Assert.assertTrue(testobject.getTargets().contains(pc1));
        Assert.assertTrue(testobject.getTargets().contains(pc3));
        testobject.removeTarget((Taxable)pc3);
        Assert.assertFalse(testobject.getTargets().contains(pc3));
        try
        {
            testobject.removeTarget((Taxable)pc3);
            Assert.fail("IllegalArgumentException expecte");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
        Assert.assertFalse(testobject.getTargets().contains(pc3));
    }


    public void testChargeListInTax()
    {
    }
}
