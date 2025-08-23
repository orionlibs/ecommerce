package de.hybris.order.calculation.money;

import de.hybris.order.calculation.exception.AmountException;
import de.hybris.order.calculation.exception.CurrenciesAreNotEqualException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.collections.CollectionUtils;

public class Money extends AbstractAmount
{
    public static final String CURRENCY_CANNOT_BE_NULL = "Currency cannot be null";
    private static final Comparator<Money> MONEY_COMPARATOR;

    static
    {
        MONEY_COMPARATOR = ((money1, money2) -> {
            if(money1 == null || money2 == null)
            {
                throw new IllegalArgumentException("cannot sort NULL money elements");
            }
            if(!money1.getCurrency().equals(money2.getCurrency()))
            {
                throw new CurrenciesAreNotEqualException("cannot sort mixed-currency money elements");
            }
            return money2.getAmount().compareTo(money1.getAmount());
        });
    }

    private static final ConcurrentMap<String, Money> commonMoneyCacheMap = new ConcurrentHashMap<>();
    private static final Comparator<Map.Entry<?, Money>> MAP_ASC_COMP = (Comparator<Map.Entry<?, Money>>)new Object();
    private static final Comparator<Map.Entry<?, Money>> MAP_DESC_COMP = (Comparator<Map.Entry<?, Money>>)new Object();
    private static final Comparator<Money> ASC_COMP = (Comparator<Money>)new Object();
    public static final String CANNOT_SUM_NOTHING = "Cannot sum nothing";
    private final Currency currency;
    private final BigDecimal amount;


    public Money(BigDecimal amount, Currency currency)
    {
        if(currency == null)
        {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.amount = amount.setScale(currency.getDigits(), RoundingMode.DOWN);
        this.currency = currency;
    }


    public Money(Currency currency)
    {
        if(currency == null)
        {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.amount = BigDecimal.ZERO;
        this.currency = currency;
    }


    public Money(long amountInSmallestPieces, Currency currency)
    {
        if(currency == null)
        {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.amount = new BigDecimal(BigInteger.valueOf(amountInSmallestPieces), currency.getDigits());
        this.currency = currency;
    }


    public Money(String amount, Currency currency)
    {
        this(new BigDecimal(amount), currency);
    }


    public static List<Money> valueOf(Currency curr, String... amounts)
    {
        Money[] ret = new Money[amounts.length];
        int index = 0;
        for(String s : amounts)
        {
            ret[index++] = new Money(s, curr);
        }
        return Arrays.asList(ret);
    }


    public static List<Money> valueOf(Currency curr, long... amounts)
    {
        Money[] ret = new Money[amounts.length];
        int index = 0;
        for(long l : amounts)
        {
            ret[index++] = new Money(l, curr);
        }
        return Arrays.asList(ret);
    }


    public static List<Money> valueOf(Currency curr, BigDecimal... amounts)
    {
        Money[] ret = new Money[amounts.length];
        int index = 0;
        for(BigDecimal a : amounts)
        {
            ret[index++] = new Money(a, curr);
        }
        return Arrays.asList(ret);
    }


    public static Money zero(Currency curr)
    {
        String cacheKey = "Z" + curr.getIsoCode().toLowerCase() + curr.getDigits();
        Money ret = commonMoneyCacheMap.get(cacheKey);
        if(ret == null)
        {
            ret = new Money(curr);
            Money previous = commonMoneyCacheMap.putIfAbsent(cacheKey, ret);
            if(previous != null)
            {
                ret = previous;
            }
        }
        return ret;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void assertCurreniesAreEqual(Currency curr)
    {
        if(curr == null)
        {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if(!getCurrency().equals(curr))
        {
            throw new CurrenciesAreNotEqualException("The Currencies are not the same. " + getCurrency() + " <-> " + curr);
        }
    }


    public void assertCurreniesAreEqual(Money other)
    {
        if(other == null)
        {
            throw new IllegalArgumentException("Money cannot be null");
        }
        if(!getCurrency().equals(other.getCurrency()))
        {
            throw new CurrenciesAreNotEqualException("The Currencies are not the same. " +
                            getCurrency() + " <-> " + other.getCurrency());
        }
    }


    public Money add(Money money)
    {
        assertCurreniesAreEqual(money);
        return new Money(this.amount.add(money.getAmount()), this.currency);
    }


    public Money subtract(Money money)
    {
        assertCurreniesAreEqual(money);
        return new Money(getAmount().subtract(money.getAmount()), this.currency);
    }


    public int hashCode()
    {
        return this.currency.hashCode() + this.amount.hashCode() + super.hashCode();
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(!obj.getClass().equals(Money.class))
        {
            return false;
        }
        return (((Money)obj).getCurrency().equals(this.currency) && ((Money)obj).getAmount().compareTo(this.amount) == 0);
    }


    public String toString()
    {
        return this.amount.toPlainString() + " " + this.amount.toPlainString();
    }


    public List<Money> split(Percentage... percentages)
    {
        BigDecimal hundertPercent = BigDecimal.valueOf(100L);
        List<Percentage> perc = new ArrayList<>();
        if(percentages == null || percentages.length == 0)
        {
            perc.add(new Percentage(hundertPercent));
        }
        else
        {
            for(Percentage val : percentages)
            {
                perc.add(val);
                hundertPercent = hundertPercent.subtract(val.getRate());
            }
            if(hundertPercent.compareTo(BigDecimal.ZERO) > 0)
            {
                perc.add(new Percentage(hundertPercent));
            }
        }
        return split(perc);
    }


    public List<Money> split(List<Percentage> percentages)
    {
        checkPercentages(percentages);
        long total = this.amount.unscaledValue().longValue();
        long remainder = total;
        long[] results = new long[percentages.size()];
        int index;
        for(index = 0; index < percentages.size(); index++)
        {
            results[index] = (long)(total * ((Percentage)percentages.get(index)).getRate().doubleValue() / 100.0D);
            remainder -= results[index];
        }
        for(index = 0; index < remainder; index++)
        {
            results[index] = results[index] + 1L;
        }
        List<Money> result = new ArrayList<>();
        for(int i = 0; i < percentages.size(); i++)
        {
            result.add(new Money(results[i], this.currency));
        }
        return result;
    }


    protected BigDecimal checkPercentages(List<Percentage> percentages)
    {
        if(percentages == null || percentages.isEmpty())
        {
            throw new IllegalArgumentException("Parameter 'percentages' is null or empty!");
        }
        BigDecimal sum = BigDecimal.ZERO;
        for(Percentage percentage : percentages)
        {
            sum = sum.add(percentage.getRate());
        }
        int compareRes = sum.compareTo(BigDecimal.valueOf(100L));
        if(compareRes != 0)
        {
            throw new AmountException("Sum of all given Percentages is " + ((compareRes > 0) ? "greater" : "less") + " than 100%");
        }
        return sum;
    }


    public static <T> List<T> sortByMoneyAscending(Map<T, Money> moneyMap)
    {
        return sortByMoney(moneyMap, MAP_ASC_COMP);
    }


    protected static <T> List<T> sortByMoney(Map<T, Money> moneyMap, Comparator<Map.Entry<?, Money>> comp)
    {
        List<Map.Entry<T, Money>> entryList = new ArrayList<>(moneyMap.entrySet());
        Collections.sort(entryList, comp);
        T[] ret = (T[])new Object[entryList.size()];
        int index = 0;
        for(Map.Entry<T, Money> e : entryList)
        {
            ret[index++] = e.getKey();
        }
        return Arrays.asList(ret);
    }


    public static <T> List<T> sortByMoneyDescending(Map<T, Money> moneyMap)
    {
        return sortByMoney(moneyMap, MAP_DESC_COMP);
    }


    public static void sortAscending(List<Money> elements)
    {
        Collections.sort(elements, ASC_COMP);
    }


    public static void sortDescending(List<Money> elements)
    {
        Collections.sort(elements, MONEY_COMPARATOR);
    }


    public static final <T> Money sum(Collection<T> elements, MoneyExtractor<T> extractor)
    {
        if(CollectionUtils.isEmpty(elements))
        {
            throw new AmountException("Cannot sum nothing");
        }
        return sumUnscaled(elements, extractor);
    }


    public static final Money sum(Money... money)
    {
        if(money == null || money.length == 0)
        {
            throw new AmountException("Cannot sum nothing");
        }
        return sum(Arrays.asList(money));
    }


    public static final Money sum(Collection<Money> elements)
    {
        return new Money(sumUnscaled(elements), ((Money)elements.iterator().next()).getCurrency());
    }


    protected static final <T> Money sumUnscaled(Collection<T> elements, MoneyExtractor<T> extractor)
    {
        if(elements == null || elements.isEmpty())
        {
            throw new AmountException("Cannot sum nothing");
        }
        long res = 0L;
        Currency curr = null;
        for(T t : elements)
        {
            Money money = extractor.extractMoney(t);
            if(curr == null)
            {
                curr = money.getCurrency();
            }
            else if(!curr.equals(money.getCurrency()))
            {
                throw new CurrenciesAreNotEqualException("Cannot sum up Money with different currencies");
            }
            res += money.getAmount().unscaledValue().longValue();
        }
        return new Money(res, curr);
    }


    protected static final long sumUnscaled(Collection<Money> elements)
    {
        if(elements == null || elements.isEmpty())
        {
            throw new AmountException("Cannot sum nothing");
        }
        long res = 0L;
        Currency curr = null;
        for(Money x : elements)
        {
            if(curr == null)
            {
                curr = x.getCurrency();
            }
            else if(!curr.equals(x.getCurrency()))
            {
                throw new CurrenciesAreNotEqualException("Cannot sum up Money with different currencies");
            }
            res += x.getAmount().unscaledValue().longValue();
        }
        return res;
    }


    public static List<Percentage> getPercentages(Money... moneys)
    {
        return getPercentages(0, moneys);
    }


    public static List<Percentage> getPercentages(int scale, Money... moneys)
    {
        return getPercentages(Arrays.asList(moneys), scale);
    }


    public static List<Percentage> getPercentages(List<Money> moneys, int scale)
    {
        long percentageFactor = BigDecimal.TEN.pow(2 + scale).longValue();
        long[] ratios = new long[moneys.size()];
        long sumUnscaled = sumUnscaled(moneys);
        long remainderPercentage = percentageFactor;
        int index = 0;
        for(Money m : moneys)
        {
            long moneyUnscaled = m.getAmount().unscaledValue().longValue();
            long moneyPercentageUnscaled = moneyUnscaled * percentageFactor / sumUnscaled;
            ratios[index++] = moneyPercentageUnscaled;
            remainderPercentage -= moneyPercentageUnscaled;
        }
        if(remainderPercentage > 0L)
        {
            int[] biggestMoneyOrderedPositionList = getSortedPositionTable(moneys);
            for(int i = 0; remainderPercentage > 0L && i < biggestMoneyOrderedPositionList.length; i++, remainderPercentage--)
            {
                ratios[biggestMoneyOrderedPositionList[i]] = ratios[biggestMoneyOrderedPositionList[i]] + 1L;
            }
        }
        Percentage[] ret = new Percentage[ratios.length];
        int idx = 0;
        for(long ratio : ratios)
        {
            ret[idx++] = new Percentage(new BigDecimal(BigInteger.valueOf(ratio), scale));
        }
        return Arrays.asList(ret);
    }


    protected static int[] getSortedPositionTable(List<Money> moneyList)
    {
        Map<Integer, Money> posMap = new HashMap<>(moneyList.size());
        int index = 0;
        for(Money m : moneyList)
        {
            posMap.put(Integer.valueOf(index++), m);
        }
        int[] ret = new int[moneyList.size()];
        index = 0;
        for(Integer originalPos : sortByMoneyDescending(posMap))
        {
            ret[index++] = originalPos.intValue();
        }
        return ret;
    }
}
