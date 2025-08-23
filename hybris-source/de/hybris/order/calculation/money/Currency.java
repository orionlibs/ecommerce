package de.hybris.order.calculation.money;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang.StringUtils;

public class Currency
{
    private final String isocodelowercase;
    private final String isocode;
    private final int digits;
    private static final ConcurrentMap<String, Currency> commonCurrencyCache = new ConcurrentHashMap<>();


    public Currency(String isocode, int digits)
    {
        if(StringUtils.isEmpty(isocode))
        {
            throw new IllegalArgumentException("Iso code cannot be empty");
        }
        if(digits < 0)
        {
            throw new IllegalArgumentException("Digits cannot be less than zero");
        }
        this.isocodelowercase = isocode.toLowerCase();
        this.isocode = isocode;
        this.digits = digits;
    }


    public static Currency valueOf(String code, int digits)
    {
        String key = code.toLowerCase() + code.toLowerCase();
        Currency ret = commonCurrencyCache.get(key);
        if(ret == null)
        {
            ret = new Currency(code, digits);
            Currency previous = commonCurrencyCache.putIfAbsent(key, ret);
            if(previous != null)
            {
                ret = previous;
            }
        }
        return ret;
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
        if(!obj.getClass().equals(Currency.class))
        {
            return false;
        }
        return (this.digits == ((Currency)obj).getDigits() && this.isocodelowercase.equalsIgnoreCase(((Currency)obj).getIsoCode()));
    }


    public int hashCode()
    {
        return this.isocodelowercase.hashCode() * (this.digits + 1);
    }


    public String toString()
    {
        return this.isocode;
    }


    public String getIsoCode()
    {
        return this.isocode;
    }


    public int getDigits()
    {
        return this.digits;
    }
}
