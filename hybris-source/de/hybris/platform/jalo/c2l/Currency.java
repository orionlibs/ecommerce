package de.hybris.platform.jalo.c2l;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Currency extends GeneratedCurrency
{
    private transient String isoCodeCheck;
    private transient java.util.Currency currCache;


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("isocode"))
        {
            throw new JaloInvalidParameterException("Missing parameter( isocode) to create a Currency", 0);
        }
        checkConsistencyIsocode((String)allAttributes.get("isocode"), null, type.getCode());
        if(allAttributes.get("symbol") == null)
        {
            java.util.Currency javaCurr = matchJavaCurrency((String)allAttributes.get("isocode"));
            if(javaCurr != null && javaCurr.getSymbol() != null)
            {
                allAttributes.put("symbol", javaCurr.getSymbol());
            }
        }
        allAttributes.setAttributeMode("isocode", Item.AttributeMode.INITIAL);
        setDefaultValues(allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        Map<String, Item.AttributeMode> map = new HashMap<>(super.getDefaultAttributeModes());
        map.remove("base");
        return map;
    }


    private void setDefaultValues(Item.ItemAttributeMap allAttributes) throws ConsistencyCheckException
    {
        if(allAttributes.get("digits") == null)
        {
            allAttributes.put("digits", Double.valueOf(2.0D));
        }
        if(allAttributes.get("base") == null)
        {
            allAttributes.put("base", Boolean.FALSE);
        }
        if(allAttributes.get("conversion") == null)
        {
            allAttributes.put("conversion", Double.valueOf(1.0D));
        }
    }


    public java.util.Currency getJavaCurrency()
    {
        String isoCode = getIsoCode();
        if(!isoCode.equalsIgnoreCase(this.isoCodeCheck))
        {
            this.currCache = matchJavaCurrency(this.isoCodeCheck = isoCode);
        }
        return this.currCache;
    }


    protected java.util.Currency matchJavaCurrency(String code)
    {
        try
        {
            return java.util.Currency.getInstance(code);
        }
        catch(IllegalArgumentException e)
        {
            return null;
        }
    }


    public void setBase()
    {
        setBase(Boolean.TRUE);
    }


    @ForceJALO(reason = "consistency check")
    public void setBase(SessionContext ctx, Boolean value)
    {
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(GeneratedCoreConstants.TC.CURRENCY).append("} WHERE {").append("base")
                        .append("}=?flag");
        List<Currency> result = FlexibleSearch.getInstance().search(query.toString(), Collections.singletonMap("flag", Boolean.TRUE), Collections.singletonList(Currency.class), true, true, 0, 1).getResult();
        if(result.size() > 1)
        {
            throw new JaloSystemException("More than one baseCurrency was found!");
        }
        if(result.size() == 1)
        {
            if(Boolean.TRUE.equals(value))
            {
                ((Currency)result.get(0)).setBaseInternal(ctx, Boolean.FALSE);
                super.setBase(ctx, Boolean.TRUE);
            }
            else
            {
                if(((Currency)result.get(0)).getPK().equals(getPK()))
                {
                    throw new JaloSystemException("Cannot set 'base' attribute of" + getIsocode() + " currency to false! One base currency is required!");
                }
                super.setBase(ctx, Boolean.FALSE);
            }
        }
        else
        {
            super.setBase(ctx, Boolean.TRUE);
        }
    }


    private void setBaseInternal(SessionContext ctx, Boolean value)
    {
        super.setBase(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setConversionFactor(double conversionFactor) throws JaloInvalidParameterException
    {
        setConversionFactor(getSession().getSessionContext(), conversionFactor);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setConversionFactor(SessionContext ctx, double conversionFactor) throws JaloInvalidParameterException
    {
        setConversion(ctx, Double.valueOf(conversionFactor));
    }


    @ForceJALO(reason = "consistency check")
    public void setConversion(SessionContext ctx, Double value)
    {
        if(value.doubleValue() == 0.0D)
        {
            throw new JaloInvalidParameterException("cannot set currency conversion factory to zero", 0);
        }
        super.setConversion(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double convertAndRound(Currency targetCurrency, double value)
    {
        return CoreAlgorithms.round(
                        CoreAlgorithms.convert(getConversion().doubleValue(), targetCurrency.getConversion().doubleValue(), value), targetCurrency
                                        .getDigits().intValue());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double convert(Currency targetCurrency, double value)
    {
        return CoreAlgorithms.convert(getConversion().doubleValue(), targetCurrency.getConversion().doubleValue(), value);
    }


    @ForceJALO(reason = "consistency check")
    public void setDigits(SessionContext ctx, Integer digits)
    {
        if(digits.intValue() < 0)
        {
            throw new JaloSystemException(null, "digts < 0 : " + digits.intValue(), -1);
        }
        super.setDigits(ctx, digits);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double round(double value)
    {
        return CoreAlgorithms.round(value, getDigits().intValue());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static DecimalFormat adjustDigits(DecimalFormat nf, Currency c)
    {
        int digits = Math.max(0, c.getDigits().intValue());
        nf.setMaximumFractionDigits(digits);
        nf.setMinimumFractionDigits(digits);
        if(digits == 0)
        {
            nf.setDecimalSeparatorAlwaysShown(false);
        }
        return nf;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static DecimalFormat adjustSymbol(DecimalFormat nf, Currency c)
    {
        String symbol = c.getSymbol();
        if(symbol != null)
        {
            DecimalFormatSymbols symbols = nf.getDecimalFormatSymbols();
            String iso = c.getIsoCode();
            boolean changed = false;
            if(!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
            {
                symbols.setInternationalCurrencySymbol(iso);
                changed = true;
            }
            if(!symbol.equals(symbols.getCurrencySymbol()))
            {
                symbols.setCurrencySymbol(symbol);
                changed = true;
            }
            if(changed)
            {
                nf.setDecimalFormatSymbols(symbols);
            }
        }
        return nf;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String formatPrice(double price)
    {
        return Utilities.getNumberInstance().format(price);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String formatPrice(double price, NumberFormat numberFormat)
    {
        if(numberFormat == null)
        {
            return formatPrice(price);
        }
        return adjustDigits((DecimalFormat)numberFormat.clone(), this).format(price);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String format(double price)
    {
        return Utilities.getCurrencyInstance(this).format(price);
    }


    @ForceJALO(reason = "something else")
    public Boolean isBase(SessionContext ctx)
    {
        Boolean result = super.isBase(ctx);
        return (result == null) ? Boolean.FALSE : result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String format(double price, NumberFormat numberFormat)
    {
        if(numberFormat == null)
        {
            return format(price);
        }
        return adjustSymbol(adjustDigits((DecimalFormat)numberFormat.clone(), this), this).format(price);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getConversionFactor()
    {
        return getConversionFactor(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getConversionFactor(SessionContext ctx)
    {
        return getConversionAsPrimitive(ctx);
    }


    @ForceJALO(reason = "something else")
    public Double getConversion(SessionContext ctx)
    {
        Double result = super.getConversion(ctx);
        return (result != null) ? result : Double.valueOf(0.0D);
    }


    @ForceJALO(reason = "something else")
    public Integer getDigits(SessionContext ctx)
    {
        Integer result = super.getDigits(ctx);
        return (result != null) ? result : Integer.valueOf(0);
    }


    @ForceJALO(reason = "consistency check")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("pk", getPK());
        StringBuffer query = new StringBuffer(60);
        query.append("GET {").append(GeneratedCoreConstants.TC.ABSTRACTORDER).append("} WHERE {")
                        .append("currency").append("}=?pk");
        List result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(Order.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException(null, "currency " + this + " can not be removed since it is still used in orders", -1);
        }
    }
}
