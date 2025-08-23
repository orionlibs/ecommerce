package de.hybris.platform.paymentstandard.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.paymentstandard.constants.GeneratedStandardPaymentModeConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedStandardPaymentModeValue extends GenericItem
{
    public static final String CURRENCY = "currency";
    public static final String VALUE = "value";
    public static final String PAYMENTMODE = "paymentMode";
    protected static final BidirectionalOneToManyHandler<GeneratedStandardPaymentModeValue> PAYMENTMODEHANDLER = new BidirectionalOneToManyHandler(GeneratedStandardPaymentModeConstants.TC.STANDARDPAYMENTMODEVALUE, false, "paymentMode", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("paymentMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PAYMENTMODEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public StandardPaymentMode getPaymentMode(SessionContext ctx)
    {
        return (StandardPaymentMode)getProperty(ctx, "paymentMode");
    }


    public StandardPaymentMode getPaymentMode()
    {
        return getPaymentMode(getSession().getSessionContext());
    }


    protected void setPaymentMode(SessionContext ctx, StandardPaymentMode value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'paymentMode' is not changeable", 0);
        }
        PAYMENTMODEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setPaymentMode(StandardPaymentMode value)
    {
        setPaymentMode(getSession().getSessionContext(), value);
    }


    public Double getValue(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "value");
    }


    public Double getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public double getValueAsPrimitive(SessionContext ctx)
    {
        Double value = getValue(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getValueAsPrimitive()
    {
        return getValueAsPrimitive(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, Double value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(Double value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public void setValue(SessionContext ctx, double value)
    {
        setValue(ctx, Double.valueOf(value));
    }


    public void setValue(double value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
