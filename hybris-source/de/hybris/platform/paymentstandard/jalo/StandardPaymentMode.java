package de.hybris.platform.paymentstandard.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.JaloPaymentModeException;
import de.hybris.platform.paymentstandard.constants.GeneratedStandardPaymentModeConstants;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardPaymentMode extends GeneratedStandardPaymentMode
{
    public void setCost(Currency curr, double value)
    {
        addNewPaymentModeValue(curr, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public StandardPaymentModeValue getPaymentModeValue(Currency curr)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("curr", curr);
        List<StandardPaymentModeValue> rows = FlexibleSearch.getInstance().search("SELECT {" + PK + "} FROM {" + GeneratedStandardPaymentModeConstants.TC.STANDARDPAYMENTMODEVALUE + "} WHERE {paymentMode}=?me AND {currency}=?curr ", params, StandardPaymentModeValue.class).getResult();
        return rows.isEmpty() ? null : rows.get(0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeCost(Currency curr)
    {
        StandardPaymentModeValue val = getPaymentModeValue(curr);
        if(val != null)
        {
            try
            {
                val.remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
            return true;
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map<Currency, Double> getValues()
    {
        Collection<StandardPaymentModeValue> values = getPaymentModeValues();
        if(values.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<Currency, Double> ret = new HashMap<>(values.size());
        for(StandardPaymentModeValue val : values)
        {
            ret.put(val.getCurrency(), val.getValue());
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setValues(Map<Currency, Double> values)
    {
        Map<Currency, StandardPaymentModeValue> existing = new HashMap<>();
        for(StandardPaymentModeValue val : getPaymentModeValues())
        {
            existing.put(val.getCurrency(), val);
        }
        for(Map.Entry<Currency, Double> e : values.entrySet())
        {
            if(e.getKey() == null)
            {
                continue;
            }
            StandardPaymentModeValue val = existing.get(e.getKey());
            if(val != null)
            {
                if(e.getValue() == null)
                {
                    try
                    {
                        val.remove();
                    }
                    catch(ConsistencyCheckException e1)
                    {
                        throw new JaloSystemException(e1);
                    }
                    continue;
                }
                val.setValue(e.getValue());
                continue;
            }
            if(e.getValue() != null)
            {
                addNewPaymentModeValue(e.getKey(), ((Double)e.getValue()).doubleValue());
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public PriceValue getCost(SessionContext ctx, AbstractOrder order) throws JaloPaymentModeException
    {
        Currency curr = order.getCurrency();
        Currency base = null;
        if(curr == null)
        {
            throw new JaloPaymentModeException("getCost(): currency was NULL in order.", 0);
        }
        StandardPaymentModeValue value = getPaymentModeValue(curr);
        if(value == null && !curr.isBase().booleanValue())
        {
            base = C2LManager.getInstance().getBaseCurrency();
            value = (base != null) ? getPaymentModeValue(base) : null;
        }
        if(value == null)
        {
            throw new JaloPaymentModeException("getCost(): No price defined for paymentMode='" + this + "' and currency '" + curr + "'.", 0);
        }
        if(base != null)
        {
            return new PriceValue(curr.getIsoCode(), base.convertAndRound(curr, value.getValueAsPrimitive(ctx)),
                            isNetAsPrimitive(ctx));
        }
        return new PriceValue(curr.getIsoCode(), value.getValueAsPrimitive(), isNetAsPrimitive(ctx));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public StandardPaymentModeValue addNewDeliveryModeValue(Currency currency, double value)
    {
        return addNewPaymentModeValue(currency, value);
    }


    public StandardPaymentModeValue addNewPaymentModeValue(Currency currency, double value)
    {
        StandardPaymentModeValue val = getPaymentModeValue(currency);
        if(val != null)
        {
            val.setValue(value);
        }
        else
        {
            val = createPaymentModeValueInternal(currency, value);
        }
        return val;
    }


    protected StandardPaymentModeValue createPaymentModeValueInternal(Currency currency, double value)
    {
        Item.ItemAttributeMap params = new Item.ItemAttributeMap();
        params.put("paymentMode", this);
        params.put("currency", currency);
        params.put("value", new Double(value));
        return StandardPaymentModeManager.getInstance().createStandardPaymentModeValue((Map)params);
    }
}
