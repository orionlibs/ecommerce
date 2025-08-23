package de.hybris.platform.util;

import de.hybris.platform.core.CoreAlgorithms;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class TaxValue implements Cloneable, Serializable, PDTValue
{
    private static final String DELIMITER = "|";
    private static final String EMPTY = "[]";
    private static final String TV_HEADER = "<TV<";
    private static final String TV_FOOTER = ">VT>";
    private static final String INTERNAL_DELIMITER = "#";
    private final String code;
    private final double value;
    private final double appliedValue;
    private final boolean absolute;
    private final String isoCode;


    public TaxValue(String code, double value, boolean absolute, String currencyIsoCode)
    {
        this(code, value, absolute, 0.0D, currencyIsoCode);
    }


    public TaxValue(String code, double value, boolean absolute, double appliedValue, String currencyIsoCode)
    {
        if(code == null)
        {
            throw new IllegalArgumentException("tax value code may not be null");
        }
        this.code = code;
        this.value = value;
        this.absolute = absolute;
        this.appliedValue = appliedValue;
        this.isoCode = currencyIsoCode;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getCurrencyIsoCode()
    {
        return this.isoCode;
    }


    public double getValue()
    {
        return this.value;
    }


    public double getAppliedValue()
    {
        return this.appliedValue;
    }


    public boolean isAbsolute()
    {
        return this.absolute;
    }


    public Object clone()
    {
        return new TaxValue(getCode(), getValue(), isAbsolute(), getAppliedValue(), getCurrencyIsoCode());
    }


    public TaxValue apply(double quantity, double price, int digits, boolean priceIsNet, String currencyIsoCode)
    {
        if(isAbsolute() && currencyIsoCode != getCurrencyIsoCode() && (currencyIsoCode == null ||
                        !currencyIsoCode.equals(getCurrencyIsoCode())))
        {
            throw new IllegalArgumentException("cannot apply price " + price + " with currency " + currencyIsoCode + " to absolute tax value with currency " +
                            getCurrencyIsoCode() + " currencies must be equal");
        }
        return new TaxValue(getCode(), getValue(), isAbsolute(),
                        CoreAlgorithms.round(
                                        isAbsolute() ? (getValue() * quantity) : (
                                                        priceIsNet ? (price * getValue() / 100.0D) : (price * getValue() / (getValue() + 100.0D))),
                                        (digits > 0) ? digits : 0), currencyIsoCode);
    }


    public static Collection apply(double quantity, double price, int digits, Collection taxValues, boolean priceIsNet, String currencyIso)
    {
        if(taxValues == null || taxValues.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<TaxValue> ret = new ArrayList(taxValues.size());
        for(Iterator<TaxValue> it = taxValues.iterator(); it.hasNext(); )
        {
            ret.add(((TaxValue)it.next()).apply(quantity, price, digits, priceIsNet, currencyIso));
        }
        return ret;
    }


    public static double sumRelativeTaxValues(Collection taxValues)
    {
        if(taxValues == null || taxValues.isEmpty())
        {
            return 0.0D;
        }
        double sum = 0.0D;
        for(Iterator<TaxValue> iter = taxValues.iterator(); iter.hasNext(); )
        {
            TaxValue taxValue = iter.next();
            if(!taxValue.isAbsolute())
            {
                sum += taxValue.getValue();
            }
        }
        return sum;
    }


    public static double sumAbsoluteTaxValues(Collection taxValues)
    {
        if(taxValues == null || taxValues.isEmpty())
        {
            return 0.0D;
        }
        double sum = 0.0D;
        for(Iterator<TaxValue> iter = taxValues.iterator(); iter.hasNext(); )
        {
            TaxValue taxValue = iter.next();
            if(taxValue.isAbsolute())
            {
                sum += taxValue.getValue();
            }
        }
        return sum;
    }


    public static double sumAppliedTaxValues(Collection taxValues)
    {
        if(taxValues == null || taxValues.isEmpty())
        {
            return 0.0D;
        }
        double sum = 0.0D;
        for(Iterator<TaxValue> iter = taxValues.iterator(); iter.hasNext(); )
        {
            TaxValue taxValue = iter.next();
            sum += taxValue.getAppliedValue();
        }
        return sum;
    }


    public String toString()
    {
        return "<TV<" + getCode() + "#" + getValue() + "#" + isAbsolute() + "#" +
                        getAppliedValue() + "#" + ((getCurrencyIsoCode() != null) ? getCurrencyIsoCode() : "NULL") + ">VT>";
    }


    public static String toString(Collection taxValueCollection)
    {
        if(taxValueCollection == null)
        {
            return null;
        }
        if(taxValueCollection.isEmpty())
        {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        for(Iterator<TaxValue> it = taxValueCollection.iterator(); it.hasNext(); )
        {
            stringBuilder.append(((TaxValue)it.next()).toString());
            if(it.hasNext())
            {
                stringBuilder.append("|");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    public static Collection parseTaxValueCollection(String str) throws IllegalArgumentException
    {
        if(str == null)
        {
            return null;
        }
        if(str.equals("[]"))
        {
            return new LinkedList();
        }
        Collection<TaxValue> ret = new LinkedList();
        for(StringTokenizer st = new StringTokenizer(str.substring(1, str.length() - 1), "|"); st.hasMoreTokens(); )
        {
            ret.add(parseTaxValue(st.nextToken()));
        }
        return ret;
    }


    public static TaxValue parseTaxValue(String str) throws IllegalArgumentException
    {
        try
        {
            int start = str.indexOf("<TV<");
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find <TV< in tax value string '" + str + "'");
            }
            int end = str.indexOf(">VT>", start);
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find >VT> in tax value string '" + str + "'");
            }
            int pos = 0;
            String code = null;
            double value = 0.0D;
            boolean absolute = false;
            double appliedValue = 0.0D;
            String iso = null;
            StringTokenizer st = new StringTokenizer(str.substring(start + "<TV<".length(), end), "#");
            while(st.hasMoreTokens())
            {
                String token = st.nextToken();
                switch(pos)
                {
                    case 0:
                        code = token;
                        break;
                    case 1:
                        value = Double.parseDouble(token);
                        break;
                    case 2:
                        absolute = "true".equalsIgnoreCase(token);
                        break;
                    case 3:
                        appliedValue = Double.parseDouble(token);
                        break;
                    case 4:
                        iso = token;
                        break;
                    default:
                        throw new IllegalArgumentException("illgeal tax value string '" + str + "' (pos=" + pos + ",moreTokens=" + st
                                        .hasMoreTokens() + ",nextToke='" + token + "')");
                }
                pos++;
            }
            if(pos < 3)
            {
                throw new IllegalArgumentException("illgeal tax value string '" + str + "' (pos=" + pos + ")");
            }
            return new TaxValue(code, value, absolute, appliedValue, "NULL".equals(iso) ? null : iso);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("error parsing tax value string '" + str + "' : " + e);
        }
    }


    public int hashCode()
    {
        if(isAbsolute())
        {
            return getCode().hashCode() ^ (isAbsolute() ? 1 : 0) ^ (int)getValue() ^ (
                            (getCurrencyIsoCode() == null) ? 0 : getCurrencyIsoCode().hashCode());
        }
        return getCode().hashCode() ^ (isAbsolute() ? 1 : 0) ^ (int)getValue();
    }


    public boolean equals(Object object)
    {
        String iso = getCurrencyIsoCode();
        return (object instanceof TaxValue && getCode().equals(((TaxValue)object).getCode()) && this.absolute == ((TaxValue)object)
                        .isAbsolute() && (!this.absolute || (iso == null && null == ((TaxValue)object)
                        .getCurrencyIsoCode()) || (iso != null && iso
                        .equals(((TaxValue)object).getCurrencyIsoCode()))) &&
                        areDoubleValuesEqual(getValue(), ((TaxValue)object).getValue()) && areDoubleValuesEqual(getAppliedValue(), ((TaxValue)object)
                        .getAppliedValue()));
    }


    public TaxValue unapply()
    {
        return new TaxValue(getCode(), getValue(), isAbsolute(), getCurrencyIsoCode());
    }


    private boolean areDoubleValuesEqual(double value1, double value2)
    {
        return (BigDecimal.valueOf(value1).compareTo(BigDecimal.valueOf(value2)) == 0);
    }
}
