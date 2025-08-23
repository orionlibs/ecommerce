package de.hybris.platform.util;

import de.hybris.platform.core.CoreAlgorithms;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

public class DiscountValue implements Cloneable, Serializable, PDTValue
{
    private static final String DV_HEADER = "<DV<";
    private static final String DV_FOOTER = ">VD>";
    private static final String INTERNAL_DELIMITER = "#";
    private static final String DELIMITER = "|";
    private static final String EMPTY = "[]";
    private final String code;
    private final double value;
    private final double appliedValue;
    private final boolean absolute;
    private final String isoCode;
    private final boolean asTargetPrice;


    public DiscountValue(String code, double value, boolean absolute, String currencyIsoCode)
    {
        this(code, value, absolute, 0.0D, currencyIsoCode);
    }


    public DiscountValue(String code, double value, String currencyIsoCode, boolean asTargetPrice)
    {
        this(code, value, true, 0.0D, currencyIsoCode, asTargetPrice);
    }


    public DiscountValue(String code, double value, boolean absolute, double appliedValue, String isoCode)
    {
        this(code, value, absolute, appliedValue, isoCode, false);
    }


    public DiscountValue(String code, double value, boolean absolute, double appliedValue, String isoCode, boolean asTargetPrice)
    {
        if(code == null)
        {
            throw new IllegalArgumentException("discount value code may not be null");
        }
        if(asTargetPrice && StringUtils.isBlank(isoCode))
        {
            throw new IllegalArgumentException("discount value cannot be target price without currency iso code");
        }
        this.code = code;
        this.value = value;
        this.absolute = absolute;
        this.appliedValue = appliedValue;
        this.isoCode = isoCode;
        this.asTargetPrice = asTargetPrice;
    }


    public static DiscountValue createRelative(String code, Double doubleValue)
    {
        return new DiscountValue(code, doubleValue.doubleValue(), false, null);
    }


    public static DiscountValue createAbsolute(String code, Double doubleValue, String currencyIsoCode)
    {
        return new DiscountValue(code, doubleValue.doubleValue(), true, currencyIsoCode);
    }


    public static DiscountValue createTargetPrice(String code, Double doubleValue, String currencyIsoCode)
    {
        return new DiscountValue(code, doubleValue.doubleValue(), currencyIsoCode, true);
    }


    public DiscountValue apply(double quantity, double price, int digits, String currencyIsoCode)
    {
        if(isAbsolute() && currencyIsoCode != getCurrencyIsoCode() && (currencyIsoCode == null ||
                        !currencyIsoCode.equals(getCurrencyIsoCode())))
        {
            throw new IllegalArgumentException("cannot apply price " + price + " with currency " + currencyIsoCode + " to absolute discount with currency " +
                            getCurrencyIsoCode());
        }
        if(isAbsolute())
        {
            return isAsTargetPrice() ? createTargetPriceAppliedValue(quantity, price, digits, currencyIsoCode) :
                            createAbsoluteAppliedValue(quantity, digits, currencyIsoCode);
        }
        return createRelativeAppliedValue(price, digits, currencyIsoCode);
    }


    protected DiscountValue createRelativeAppliedValue(double price, int digits, String currencyIsoCode)
    {
        return new DiscountValue(getCode(), getValue(), false,
                        CoreAlgorithms.round(price * getValue() / 100.0D, (digits > 0) ? digits : 0), currencyIsoCode);
    }


    protected DiscountValue createAbsoluteAppliedValue(double quantity, int digits, String currencyIsoCode)
    {
        return new DiscountValue(getCode(), getValue(), true,
                        CoreAlgorithms.round(getValue() * quantity, (digits > 0) ? digits : 0), currencyIsoCode);
    }


    protected DiscountValue createTargetPriceAppliedValue(double quantity, double totalPriceWithoutDiscounts, int digits, String currencyIsoCode)
    {
        double targetPricePerPiece = getValue();
        double totalTargetPrice = targetPricePerPiece * quantity;
        double differenceToTargetPrice = totalPriceWithoutDiscounts - totalTargetPrice;
        return new DiscountValue(getCode(), getValue(), true,
                        CoreAlgorithms.round(differenceToTargetPrice, (digits > 0) ? digits : 0), currencyIsoCode, true);
    }


    public static List apply(double quantity, double startPrice, int digits, List values, String currencyIsoCode)
    {
        List<DiscountValue> ret = new ArrayList(values.size());
        double tmp = startPrice;
        for(Iterator<DiscountValue> it = values.iterator(); it.hasNext(); )
        {
            DiscountValue discountValue = ((DiscountValue)it.next()).apply(quantity, tmp, digits, currencyIsoCode);
            tmp -= discountValue.getAppliedValue();
            ret.add(discountValue);
        }
        return ret;
    }


    public static double sumAppliedValues(Collection values)
    {
        if(values == null || values.isEmpty())
        {
            return 0.0D;
        }
        double sum = 0.0D;
        for(Iterator<DiscountValue> it = values.iterator(); it.hasNext(); )
        {
            sum += ((DiscountValue)it.next()).getAppliedValue();
        }
        return sum;
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


    public boolean isAsTargetPrice()
    {
        return this.asTargetPrice;
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
        return new DiscountValue(getCode(), getValue(), isAbsolute(), getAppliedValue(), getCurrencyIsoCode());
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("<DV<");
        sb.append(getCode());
        sb.append("#").append(getValue());
        sb.append("#").append(isAbsolute());
        sb.append("#").append(getAppliedValue());
        sb.append("#").append((getCurrencyIsoCode() != null) ? getCurrencyIsoCode() : "NULL");
        sb.append("#").append(isAsTargetPrice());
        sb.append(">VD>");
        return sb.toString();
    }


    public static String toString(Collection discountValueCollection)
    {
        if(discountValueCollection == null)
        {
            return null;
        }
        if(discountValueCollection.isEmpty())
        {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        for(Iterator<DiscountValue> it = discountValueCollection.iterator(); it.hasNext(); )
        {
            stringBuilder.append(((DiscountValue)it.next()).toString());
            if(it.hasNext())
            {
                stringBuilder.append("|");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    public static Collection parseDiscountValueCollection(String str) throws IllegalArgumentException
    {
        if(str == null)
        {
            return null;
        }
        if(str.equals("[]"))
        {
            return new LinkedList();
        }
        Collection<DiscountValue> ret = new LinkedList();
        for(StringTokenizer st = new StringTokenizer(str.substring(1, str.length() - 1), "|"); st.hasMoreTokens(); )
        {
            ret.add(parseDiscountValue(st.nextToken()));
        }
        return ret;
    }


    public static DiscountValue parseDiscountValue(String str) throws IllegalArgumentException
    {
        try
        {
            int start = str.indexOf("<DV<");
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find <DV< in discount value string '" + str + "'");
            }
            int end = str.indexOf(">VD>", start);
            if(start < 0)
            {
                throw new IllegalArgumentException("could not find >VD> in discount value string '" + str + "'");
            }
            int pos = 0;
            String code = null;
            double value = 0.0D;
            boolean absolute = false;
            boolean asTargetPrice = false;
            double appliedValue = 0.0D;
            String iso = null;
            String substr = str.substring(start + "<DV<".length(), end);
            if(substr.startsWith("#"))
            {
                pos = 1;
                code = "";
            }
            for(StringTokenizer st = new StringTokenizer(substr, "#"); st.hasMoreTokens(); )
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
                        absolute = Boolean.parseBoolean(token);
                        break;
                    case 3:
                        appliedValue = Double.parseDouble(token);
                        break;
                    case 4:
                        iso = token;
                        break;
                    case 5:
                        asTargetPrice = Boolean.parseBoolean(token);
                        break;
                    default:
                        throw new IllegalArgumentException("illegal discount value string '" + str + "' (pos=" + pos + ", moreTokens=" + st
                                        .hasMoreTokens() + ", nextToken='" + token + "')");
                }
                pos++;
            }
            if(pos < 3)
            {
                throw new IllegalArgumentException("illegal discount value string '" + str + "' (pos=" + pos + ")");
            }
            return new DiscountValue(code, value, absolute, appliedValue, "NULL".equals(iso) ? null : iso, asTargetPrice);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("error parsing discount value string '" + str + "' : " + e);
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
        return (object instanceof DiscountValue && getCode().equals(((DiscountValue)object).getCode()) && this.absolute == ((DiscountValue)object)
                        .isAbsolute() && (!this.absolute || (iso == null && null == ((DiscountValue)object)
                        .getCurrencyIsoCode()) || (iso != null && iso
                        .equals(((DiscountValue)object).getCurrencyIsoCode()))) &&
                        areDoubleValuesEqual(this.value, ((DiscountValue)object).getValue()) && areDoubleValuesEqual(this.appliedValue, ((DiscountValue)object)
                        .getAppliedValue()));
    }


    public boolean equalsIgnoreAppliedValue(DiscountValue discountValue)
    {
        String iso = getCurrencyIsoCode();
        return (getCode().equals(discountValue.getCode()) && this.absolute == discountValue.isAbsolute() && (!this.absolute || (iso == null && null == discountValue
                        .getCurrencyIsoCode()) || (iso != null && iso.equals(discountValue
                        .getCurrencyIsoCode()))) &&
                        areDoubleValuesEqual(this.value, discountValue.getValue()));
    }


    private boolean areDoubleValuesEqual(double value1, double value2)
    {
        return (BigDecimal.valueOf(value1).compareTo(BigDecimal.valueOf(value2)) == 0);
    }
}
