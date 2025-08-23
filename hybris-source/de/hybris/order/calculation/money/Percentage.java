package de.hybris.order.calculation.money;

import de.hybris.order.calculation.exception.AmountException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Percentage extends AbstractAmount
{
    public static final Percentage ZERO = new Percentage(0);
    public static final Percentage TEN = new Percentage(10);
    public static final Percentage TWENTY = new Percentage(20);
    public static final Percentage TWENTYFIVE = new Percentage(25);
    public static final Percentage THIRTY = new Percentage(30);
    public static final Percentage FOURTY = new Percentage(40);
    public static final Percentage FIFTY = new Percentage(50);
    public static final Percentage SIXTY = new Percentage(60);
    public static final Percentage SEVENTY = new Percentage(70);
    public static final Percentage SEVENTYFIVE = new Percentage(75);
    public static final Percentage EIGHTY = new Percentage(80);
    public static final Percentage NINETY = new Percentage(90);
    public static final Percentage HUNDRED = new Percentage(100);
    private final BigDecimal rate;


    public Percentage(String rate)
    {
        if(rate == null)
        {
            throw new IllegalArgumentException("Parameter 'rate' is null!");
        }
        this.rate = new BigDecimal(rate);
    }


    public Percentage(int rate)
    {
        this(BigDecimal.valueOf(rate));
    }


    public Percentage(BigDecimal rate)
    {
        if(rate == null)
        {
            throw new IllegalArgumentException("Parameter 'rate' is null!");
        }
        this.rate = rate;
    }


    public static List<Percentage> valueOf(String... rates)
    {
        Percentage[] ret = new Percentage[rates.length];
        int index = 0;
        for(String s : rates)
        {
            ret[index++] = new Percentage(s);
        }
        return Arrays.asList(ret);
    }


    public static List<Percentage> valueOf(int... rates)
    {
        Percentage[] ret = new Percentage[rates.length];
        int index = 0;
        for(int r : rates)
        {
            ret[index++] = new Percentage(r);
        }
        return Arrays.asList(ret);
    }


    public static List<Percentage> valueOf(BigDecimal... rates)
    {
        Percentage[] ret = new Percentage[rates.length];
        int index = 0;
        for(BigDecimal r : rates)
        {
            ret[index++] = new Percentage(r);
        }
        return Arrays.asList(ret);
    }


    public BigDecimal getRate()
    {
        return this.rate;
    }


    public Percentage add(Percentage percentage)
    {
        return new Percentage(this.rate.add(percentage.getRate()));
    }


    public Percentage subtract(Percentage percentage)
    {
        return new Percentage(this.rate.subtract(percentage.getRate()));
    }


    public int hashCode()
    {
        return this.rate.hashCode();
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
        if(!obj.getClass().equals(Percentage.class))
        {
            return false;
        }
        return (((Percentage)obj).getRate().compareTo(this.rate) == 0);
    }


    public String toString()
    {
        return "" + this.rate + "%";
    }


    public static final Percentage sum(Percentage... percent)
    {
        if(percent == null || percent.length == 0)
        {
            throw new AmountException("Cannot sum nothing");
        }
        return sum(Arrays.asList(percent));
    }


    public static final Percentage sum(Collection<Percentage> elements)
    {
        if(elements == null)
        {
            throw new IllegalArgumentException("Cannot sum null");
        }
        BigDecimal res = BigDecimal.ZERO;
        for(Percentage x : elements)
        {
            res = res.add(x.getRate());
        }
        return new Percentage(res);
    }


    public List<Percentage> split(int parts)
    {
        if(parts < 1)
        {
            throw new IllegalArgumentException("parts is less than 1");
        }
        long total = this.rate.unscaledValue().longValue();
        long[] results = new long[parts];
        Arrays.fill(results, total / parts);
        long remainder = total % parts;
        for(int index = 0; index < remainder; index++)
        {
            results[index] = results[index] + 1L;
        }
        List<Percentage> result = new ArrayList<>();
        for(int i = 0; i < parts; i++)
        {
            result.add(new Percentage(new BigDecimal(BigInteger.valueOf(results[i]), this.rate.scale())));
        }
        return result;
    }
}
