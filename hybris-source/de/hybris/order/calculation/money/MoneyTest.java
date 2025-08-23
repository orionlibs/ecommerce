package de.hybris.order.calculation.money;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.exception.AmountException;
import de.hybris.order.calculation.exception.CurrenciesAreNotEqualException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class MoneyTest
{
    private final Currency euro = new Currency("EUR", 2);


    @Test
    public void testCreateMoney() throws Exception
    {
        checkCreateMoneyProcess(BigDecimal.ZERO, 0, BigInteger.valueOf(0L), false);
        checkCreateMoneyProcess(BigDecimal.ZERO, 10, BigInteger.valueOf(0L), false);
        checkCreateMoneyProcess(BigDecimal.ONE, 0, BigInteger.valueOf(1L), false);
        checkCreateMoneyProcess(BigDecimal.ONE, 3, BigInteger.valueOf(1000L), false);
        checkCreateMoneyProcess(BigDecimal.valueOf(3.5D), 3, BigInteger.valueOf(3500L), false);
        checkCreateMoneyProcess(BigDecimal.valueOf(3.5D), 1, BigInteger.valueOf(35L), false);
        checkCreateMoneyProcess(new BigDecimal("1.000"), 0, BigInteger.valueOf(1L), false);
        checkCreateMoneyProcess(new BigDecimal("1.2345"), 4, BigInteger.valueOf(12345L), false);
        checkCreateMoneyProcess(new BigDecimal("1.2345"), 5, BigInteger.valueOf(123450L), false);
        checkCreateMoneyProcess(new BigDecimal("-1.2"), 5, BigInteger.valueOf(-120000L), false);
        checkCreateMoneyProcess(new BigDecimal("1.2"), 0, BigInteger.valueOf(1L), false);
        checkCreateMoneyProcess(new BigDecimal("1.000001"), 2, BigInteger.valueOf(100L), false);
        checkCreateMoneyProcess(new BigDecimal("1.2"), -1, null, true);
        Assert.assertEquals(new Money(new BigDecimal("1.2"), this.euro), new Money("1.20", this.euro));
        Assert.assertEquals(new Money(new BigDecimal("1.2"), this.euro), new Money(120L, this.euro));
    }


    private void checkCreateMoneyProcess(BigDecimal amount, int digits, BigInteger unscaledValue, boolean shouldFail) throws Exception
    {
        try
        {
            Currency dummyCurr = new Currency("dummyCurrency", digits);
            Money money = new Money(amount, dummyCurr);
            if(shouldFail)
            {
                Assert.fail("expected  ArithmeticException!");
            }
            Assert.assertEquals(unscaledValue, money.getAmount().unscaledValue());
        }
        catch(Exception e)
        {
            if(!shouldFail)
            {
                throw e;
            }
        }
    }


    @Test
    public void testCreateMoneyShortWriting()
    {
        Money money = new Money(".1", this.euro);
        Assert.assertEquals(new Money("0.1", this.euro), money);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCheckCurrencyWithNull()
    {
        Money amount1 = new Money(BigDecimal.valueOf(3.45D), new Currency("TietzeT", 4));
        amount1.assertCurreniesAreEqual((Currency)null);
    }


    @Test
    public void testCheckCurrencyEqual()
    {
        Money amount1 = new Money(BigDecimal.valueOf(3.45D), new Currency("TietzeT", 4));
        Money amount2 = new Money(BigDecimal.valueOf(3343.45D), new Currency("TietzeT", 4));
        amount1.assertCurreniesAreEqual(amount2.getCurrency());
        amount2.assertCurreniesAreEqual(amount1);
        Currency xxx = new Currency("xXx", 3);
        Money amount3 = new Money(BigDecimal.valueOf(322.45D), xxx);
        Money amount4 = new Money(BigDecimal.valueOf(33343.45D), xxx);
        amount3.assertCurreniesAreEqual(amount4.getCurrency());
        amount4.assertCurreniesAreEqual(amount3);
        amount3.assertCurreniesAreEqual(amount3.getCurrency());
    }


    @Test
    public void testCheckCurrencyDifferent()
    {
        Money amount1 = new Money(BigDecimal.valueOf(3.45D), new Currency("TietzeT", 4));
        Money amount2 = new Money(BigDecimal.valueOf(3343.45D), new Currency("TietzeT", 3));
        Money amount3 = new Money(BigDecimal.valueOf(3343.45D), new Currency("xxx", 3));
        checkCurrAndFail(amount1, amount2);
        checkCurrAndFail(amount2, amount1);
        checkCurrAndFail(amount2, amount3);
        checkCurrAndFail(amount3, amount2);
        checkCurrAndFail(amount1, amount3);
        checkCurrAndFail(amount3, amount1);
    }


    private void checkCurrAndFail(Money amt1, Money amt2)
    {
        try
        {
            amt1.assertCurreniesAreEqual(amt2.getCurrency());
            Assert.fail("expected CurrenciesAreNotEqualException");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        catch(Exception e2)
        {
            Assert.fail("got unexpected exception: " + e2);
        }
    }


    @Test
    public void testAddMoneySuccess()
    {
        Currency curr = new Currency("EUR", 25);
        Money val1 = new Money(new BigDecimal(2), curr);
        Money val2 = new Money(new BigDecimal(3), curr);
        Money expectedRes = new Money(new BigDecimal(5), curr);
        Money actualRes = val1.add(val2);
        Assert.assertEquals(expectedRes, actualRes);
        Assert.assertFalse((expectedRes.hashCode() == actualRes.hashCode()));
        Money val3 = new Money(BigDecimal.valueOf(8250325.12D), curr);
        Money val4 = new Money(BigDecimal.valueOf(4321456.31D), curr);
        Money expectedRes2 = new Money(BigDecimal.valueOf(1.257178143E7D), curr);
        Assert.assertEquals(expectedRes2, val3.add(val4));
        Money val5 = new Money(new BigDecimal("8273872368712658763457862348566489.7162578164578825032512"), curr);
        Money val6 = new Money(new BigDecimal("8762347526136571645982560956723521.8374618726457432145631"), curr);
        Money expectedRes3 = new Money(new BigDecimal("17036219894849230409440423305290011.5537196891036257178143"), curr);
        Assert.assertEquals(expectedRes3, val5.add(val6));
    }


    @Test
    public void testAddMoneyFailure()
    {
        Money val3 = new Money(BigDecimal.valueOf(8250325.12D), new Currency("xXx", 2));
        Money val4 = new Money(BigDecimal.valueOf(4321456.31D), new Currency("XxX", 3));
        try
        {
            val3.add(val4);
            Assert.fail("expected CurrenciesAreNotEqualException here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
    }


    @Test
    public void testSubtract() throws CurrenciesAreNotEqualException
    {
        Currency curr = new Currency("EUR", 25);
        Money val1 = new Money(new BigDecimal(5), curr);
        Money val2 = new Money(new BigDecimal(2), curr);
        Money expectedRes = new Money(new BigDecimal(3), curr);
        Money actualRes = val1.subtract(val2);
        Assert.assertEquals(expectedRes, actualRes);
        Assert.assertFalse((expectedRes.hashCode() == actualRes.hashCode()));
        Money expectedRes2 = new Money(new BigDecimal(-3), curr);
        Assert.assertEquals(expectedRes2, val2.subtract(val1));
        Money val5 = new Money(new BigDecimal("8273872368712658763457862348566489.7162578164578825032512"), curr);
        Money val6 = new Money(new BigDecimal("8273872368712658763457862348566489.7162578164578825032511"), curr);
        Money expectedRes3 = new Money(new BigDecimal("0.0000000000000000000001"), curr);
        Assert.assertEquals(expectedRes3, val5.subtract(val6));
    }


    @Test
    public void testSubtractMoneyFailure()
    {
        Money val3 = new Money(BigDecimal.valueOf(8250325.12D), new Currency("xXx", 2));
        Money val4 = new Money(BigDecimal.valueOf(4321456.31D), new Currency("XxX", 3));
        try
        {
            val3.subtract(val4);
            Assert.fail("expected CurrenciesAreNotEqualException here");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        catch(Exception e)
        {
            Assert.fail("unexpected exception: " + e);
        }
    }


    @Test
    public void testMoneyEquals()
    {
        Money val1 = new Money(BigDecimal.valueOf(8250325.12D), new Currency("xXx", 2));
        Money val2 = new Money(BigDecimal.valueOf(8250325.12D), new Currency("xXx", 2));
        Money val3 = new Money(BigDecimal.valueOf(8250325.12D), this.euro);
        Money val4 = new Money(BigDecimal.valueOf(8250325.12D), new Currency("EUR", 3));
        Assert.assertTrue(val1.equals(val2));
        Assert.assertTrue(val2.equals(val1));
        Assert.assertTrue(val1.equals(val1));
        Assert.assertFalse(val1.equals(val3));
        Assert.assertFalse(val1.equals(val4));
        Assert.assertFalse(val4.equals(val3));
    }


    @Test
    public void testFirstSplittingMethodSuccess()
    {
        Money val1 = new Money(new BigDecimal(5), this.euro);
        List<Money> res1 = val1.split(new Percentage[0]);
        Assert.assertEquals(1L, res1.size());
        Assert.assertEquals(val1, res1.get(0));
        Assert.assertFalse((val1.hashCode() == ((Money)res1.get(0)).hashCode()));
        List<Money> res2 = val1.split(new Percentage[] {new Percentage(BigDecimal.valueOf(10L))});
        Assert.assertEquals(2L, res2.size());
        Assert.assertEquals(new BigDecimal("0.50"), ((Money)res2.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("4.50"), ((Money)res2.get(1)).getAmount());
        List<Money> res3 = val1.split(new Percentage[] {new Percentage(BigDecimal.valueOf(100L))});
        Assert.assertEquals(1L, res3.size());
        Assert.assertEquals(val1, res3.get(0));
        try
        {
            val1.split(new Percentage[] {new Percentage(BigDecimal.valueOf(101L))});
            Assert.fail("expected MoneyException");
        }
        catch(AmountException amountException)
        {
        }
        catch(Exception e)
        {
            Assert.fail("did not expect this exception: " + e);
        }
        Money val2 = new Money(new BigDecimal(100), this.euro);
        List<Money> res4 = val2.split(new Percentage[] {new Percentage(BigDecimal.valueOf(33L)), new Percentage(BigDecimal.valueOf(33L))});
        Assert.assertEquals(3L, res4.size());
        Assert.assertEquals(new BigDecimal("33.00"), ((Money)res4.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("33.00"), ((Money)res4.get(1)).getAmount());
        Assert.assertEquals(new BigDecimal("34.00"), ((Money)res4.get(2)).getAmount());
    }


    @Test
    public void testFirstSplitMethodWithRounding()
    {
        Money val1 = new Money(new BigDecimal("0.05"), this.euro);
        List<Money> res1 = val1.split(new Percentage[] {new Percentage(30)});
        Assert.assertEquals(2L, res1.size());
        Assert.assertEquals(new BigDecimal("0.02"), ((Money)res1.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.03"), ((Money)res1.get(1)).getAmount());
        List<Money> res2 = val1.split(new Percentage[] {new Percentage(70)});
        Assert.assertEquals(2L, res1.size());
        Assert.assertEquals(new BigDecimal("0.04"), ((Money)res2.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res2.get(1)).getAmount());
    }


    @Test
    public void testSplitSmallestAmount()
    {
        Money val1 = new Money(new BigDecimal("0.01"), this.euro);
        List<Money> res1 = val1.split(new Percentage[] {Percentage.TEN});
        List<Money> res2 = val1.split(new Percentage[] {Percentage.TWENTY});
        List<Money> res3 = val1.split(new Percentage[] {new Percentage(49)});
        List<Money> res4 = val1.split(new Percentage[] {new Percentage(50)});
        List<Money> res5 = val1.split(new Percentage[] {new Percentage(99)});
        List<Money> res6 = val1.split(new Percentage[] {Percentage.HUNDRED});
        List<Money> res7 = val1.split(new Percentage[] {Percentage.TWENTYFIVE, Percentage.TWENTYFIVE, Percentage.TWENTYFIVE});
        Assert.assertEquals(2L, res1.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res1.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res1.get(1)).getAmount());
        Assert.assertEquals(2L, res2.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res2.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res2.get(1)).getAmount());
        Assert.assertEquals(2L, res3.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res3.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res3.get(1)).getAmount());
        Assert.assertEquals(2L, res4.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res4.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res4.get(1)).getAmount());
        Assert.assertEquals(2L, res5.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res5.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res5.get(1)).getAmount());
        Assert.assertEquals(1L, res6.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res6.get(0)).getAmount());
        Assert.assertEquals(4L, res7.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res7.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res7.get(1)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res7.get(2)).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res7.get(3)).getAmount());
    }


    @Test
    public void splitAndShare()
    {
        Money val1 = new Money(new BigDecimal("0.96"), this.euro);
        List<Money> res1 = val1.split(new Percentage[] {Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN, Percentage.TEN});
        Assert.assertEquals(10L, res1.size());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(1)).getAmount());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(2)).getAmount());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(3)).getAmount());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(4)).getAmount());
        Assert.assertEquals(new BigDecimal("0.10"), ((Money)res1.get(5)).getAmount());
        Assert.assertEquals(new BigDecimal("0.09"), ((Money)res1.get(6)).getAmount());
        Assert.assertEquals(new BigDecimal("0.09"), ((Money)res1.get(7)).getAmount());
        Assert.assertEquals(new BigDecimal("0.09"), ((Money)res1.get(8)).getAmount());
        Assert.assertEquals(new BigDecimal("0.09"), ((Money)res1.get(9)).getAmount());
    }


    @Test
    public void testOtherSplitMethod()
    {
        Money val1 = new Money(new BigDecimal("0.02"), this.euro);
        List<Money> res1 = val1.split(Arrays.asList(new Percentage[] {new Percentage(50), new Percentage(50)}));
        Assert.assertEquals(2L, res1.size());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res1.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("0.01"), ((Money)res1.get(1)).getAmount());
    }


    @Test
    public void testFalsePercentList()
    {
        Money val1 = new Money(new BigDecimal("0.02"), this.euro);
        try
        {
            val1.split(Arrays.asList(new Percentage[] {new Percentage(50)}));
            Assert.fail("a MoneyException should be thrown here");
        }
        catch(AmountException e)
        {
            Assert.assertTrue(e.getMessage().contains("less than"));
        }
        catch(Exception e)
        {
            Assert.fail("Got unexpected exception: " + e);
        }
        try
        {
            val1.split(Arrays.asList(new Percentage[] {new Percentage(150)}));
            Assert.fail("a MoneyException should be thrown here");
        }
        catch(AmountException e)
        {
            Assert.assertTrue(e.getMessage().contains("greater than"));
        }
        catch(Exception e)
        {
            Assert.fail("Got unexpected exception: " + e);
        }
    }


    @Test
    public void testEmptyPercentList()
    {
        Money val1 = new Money(new BigDecimal("0.02"), this.euro);
        try
        {
            val1.split(Collections.EMPTY_LIST);
            Assert.fail("a IllegalArgumentException should be thrown here");
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
        }
        catch(Exception e)
        {
            Assert.fail("Got unexpected exception: " + e);
        }
    }


    @Test
    public void testZeroPercentList()
    {
        Money val1 = new Money(new BigDecimal("34.02"), this.euro);
        List<Money> res1 = val1.split(new Percentage[] {new Percentage(0)});
        Assert.assertEquals(2L, res1.size());
        Assert.assertEquals(new BigDecimal("0.00"), ((Money)res1.get(0)).getAmount());
        Assert.assertEquals(new BigDecimal("34.02"), ((Money)res1.get(1)).getAmount());
    }


    @Test
    public void testSumMoney()
    {
        Money val1 = new Money("20.3", this.euro);
        Money val2 = new Money("100", this.euro);
        Money val3 = new Money("5", this.euro);
        Assert.assertEquals(new Money("125.3", this.euro), Money.sum(new Money[] {val1, val2, val3}));
        Money neg = new Money("-20.3", this.euro);
        Assert.assertEquals(new Money("0", this.euro), Money.sum(new Money[] {val1, neg}));
        Assert.assertEquals(val1, Money.sum(new Money[] {val1}));
        Money otherMoney = new Money("5", new Currency("$", 2));
        try
        {
            Money.sum(new Money[] {otherMoney, val1});
            Assert.fail("Expected CurrenciesAreNotEqualException");
        }
        catch(CurrenciesAreNotEqualException currenciesAreNotEqualException)
        {
        }
        try
        {
            Money.sum(new Money[0]);
            Assert.fail("Expected MoneyException");
        }
        catch(AmountException amountException)
        {
        }
    }


    @Test(expected = AmountException.class)
    public void testSumMoneyCollection()
    {
        Money.sum(Collections.EMPTY_SET);
    }


    @Test
    public void testSort()
    {
        List<Money> moneyList = Money.valueOf(this.euro, new String[] {"12.33", "1999.99", "1.11", "0.01"});
        Money.sortDescending(moneyList);
        Assert.assertEquals(Money.valueOf(this.euro, new String[] {"1999.99", "12.33", "1.11", "0.01"}), moneyList);
        moneyList = Money.valueOf(this.euro, new String[] {"12.33", "1999.99", "1.11", "0.01"});
        Money.sortAscending(moneyList);
        Assert.assertEquals(Money.valueOf(this.euro, new String[] {"0.01", "1.11", "12.33", "1999.99"}), moneyList);
        Map<String, Money> moneyMap = new HashMap<>();
        moneyMap.put("Axel", new Money("1.99", this.euro));
        moneyMap.put("Marcel", new Money("2.50", this.euro));
        moneyMap.put("Andreas", new Money("0.99", this.euro));
        Assert.assertEquals(Arrays.asList(new String[] {"Andreas", "Axel", "Marcel"}, ), Money.sortByMoneyAscending(moneyMap));
        Assert.assertEquals(Arrays.asList(new String[] {"Marcel", "Axel", "Andreas"}, ), Money.sortByMoneyDescending(moneyMap));
    }


    @Test
    public void testMoneyToPercentage()
    {
        List<Money> moneyList = Money.valueOf(this.euro, new String[] {"12.33", "1999.99", "1.11", "0.01"});
        Assert.assertEquals(Percentage.valueOf(new int[] {0, 100, 0, 0}, ), Money.getPercentages(moneyList, 0));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.6", "99.4", "0.0", "0.0"}, ), Money.getPercentages(moneyList, 1));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.61", "99.34", "0.05", "0.00"}, ), Money.getPercentages(moneyList, 2));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.613", "99.332", "0.055", "0.000"}, ), Money.getPercentages(moneyList, 3));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.6124", "99.3320", "0.0552", "0.0004"}, ), Money.getPercentages(moneyList, 4));
        moneyList = Money.valueOf(this.euro, new String[] {"0.01", "12.33", "1999.99", "1.11"});
        Assert.assertEquals(Percentage.valueOf(new int[] {0, 0, 100, 0}, ), Money.getPercentages(moneyList, 0));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.0", "0.6", "99.4", "0.0"}, ), Money.getPercentages(moneyList, 1));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.00", "0.61", "99.34", "0.05"}, ), Money.getPercentages(moneyList, 2));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.000", "0.613", "99.332", "0.055"}, ), Money.getPercentages(moneyList, 3));
        Assert.assertEquals(Percentage.valueOf(new String[] {"0.0004", "0.6124", "99.3320", "0.0552"}, ), Money.getPercentages(moneyList, 4));
    }
}
