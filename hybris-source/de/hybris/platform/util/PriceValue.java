package de.hybris.platform.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class PriceValue implements Serializable, PDTValue
{
    private final double value;
    private final boolean netto;
    private final String currencyIso;
    private static final String PV_HEADER = "<PV<";
    private static final String INTERNAL_DELIMITER = "#";
    private static final String PV_FOOTER = ">VP>";
    private static final String DELIMITER = "|";
    private static final String EMPTY = "[]";


    public PriceValue(String currencyIso, double price, boolean netto)
    {
        if(currencyIso == null)
        {
            throw new IllegalArgumentException("currency iso code cannot be NULL");
        }
        this.value = price;
        this.netto = netto;
        this.currencyIso = currencyIso;
    }


    public PriceValue getOtherPrice(double relativeTotalTaxes)
    {
        return getOtherPrice(relativeTotalTaxes, 0.0D);
    }


    public PriceValue getOtherPrice(double relativeTotalTaxes, double absoluteTotalTaxes)
    {
        double relativeTotalTaxRate = (100.0D + relativeTotalTaxes) / 100.0D;
        double newValue = isNet() ? ((getValue() + absoluteTotalTaxes) * relativeTotalTaxRate) : (getValue() / relativeTotalTaxRate - absoluteTotalTaxes);
        return new PriceValue(getCurrencyIso(), newValue, !isNet());
    }


    public PriceValue getOtherPrice(Collection taxValues)
    {
        if(taxValues != null && !taxValues.isEmpty())
        {
            double entryTotalTaxRate = 0.0D;
            double entryTotalAbsoluteTaxes = 0.0D;
            for(Iterator<TaxValue> it = taxValues.iterator(); it.hasNext(); )
            {
                TaxValue taxValue = it.next();
                if(taxValue.isAbsolute())
                {
                    entryTotalAbsoluteTaxes += taxValue.getValue();
                    continue;
                }
                entryTotalTaxRate += taxValue.getValue();
            }
            return getOtherPrice(entryTotalTaxRate, entryTotalAbsoluteTaxes);
        }
        return new PriceValue(getCurrencyIso(), getValue(), !isNet());
    }


    public String toString()
    {
        return "<PV<" + getCurrencyIso() + "#" + getValue() + "#" + isNet() + ">VP>";
    }


    public static final String toString(Collection prices)
    {
        if(prices == null)
        {
            return null;
        }
        if(prices.isEmpty())
        {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        for(Iterator<PriceValue> it = prices.iterator(); it.hasNext(); )
        {
            stringBuilder.append(((PriceValue)it.next()).toString());
            if(it.hasNext())
            {
                stringBuilder.append("|");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    public static Collection parsePriceValueCollection(String str) throws IllegalArgumentException
    {
        if(str == null)
        {
            return null;
        }
        if(str.equals("[]"))
        {
            return new LinkedList();
        }
        Collection<PriceValue> ret = new LinkedList();
        for(StringTokenizer st = new StringTokenizer(str.substring(1, str.length() - 1), "|"); st.hasMoreTokens(); )
        {
            ret.add(parsePriceValue(st.nextToken()));
        }
        return ret;
    }


    public static PriceValue parsePriceValue(String str) throws IllegalArgumentException
    {
        try
        {
            int start = str.indexOf("<PV<");
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find <PV< in price value string '" + str + "'");
            }
            int end = str.indexOf(">VP>", start);
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find >VP> in price value string '" + str + "'");
            }
            int pos = 0;
            String iso = null;
            double value = 0.0D;
            boolean net = false;
            StringTokenizer st = new StringTokenizer(str.substring(start + "<PV<".length(), end), "#");
            while(st.hasMoreTokens())
            {
                String token = st.nextToken();
                switch(pos)
                {
                    case 0:
                        iso = token;
                        break;
                    case 1:
                        value = Double.parseDouble(token);
                        break;
                    case 2:
                        net = "true".equalsIgnoreCase(token);
                        break;
                    default:
                        throw new IllegalArgumentException("illgeal price value string '" + str + "' (pos=" + pos + ",moreTokens=" + st
                                        .hasMoreTokens() + ",nextToke='" + token + "')");
                }
                pos++;
            }
            if(pos < 2)
            {
                throw new IllegalArgumentException("illgeal price value string '" + str + "' (pos=" + pos + ")");
            }
            return new PriceValue(iso, value, net);
        }
        catch(RuntimeException e)
        {
            throw new IllegalArgumentException("error parsing price value string '" + str + "' : " + e);
        }
    }


    public double getValue()
    {
        return this.value;
    }


    public boolean isNet()
    {
        return this.netto;
    }


    public String getCurrencyIso()
    {
        return this.currencyIso;
    }


    public int hashCode()
    {
        return this.currencyIso.hashCode() ^ (this.netto ? 1 : 0) ^ (int)this.value;
    }


    public boolean equals(Object object)
    {
        return (object instanceof PriceValue && this.currencyIso.equals(((PriceValue)object).currencyIso) && this.netto == ((PriceValue)object).netto &&
                        areDoubleValuesEqual(this.value, ((PriceValue)object).value));
    }


    private boolean areDoubleValuesEqual(double value1, double value2)
    {
        return (BigDecimal.valueOf(value1).compareTo(BigDecimal.valueOf(value2)) == 0);
    }
}
