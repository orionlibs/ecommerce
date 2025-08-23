package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedZoneDeliveryModeValue extends GenericItem
{
    public static final String CURRENCY = "currency";
    public static final String MINIMUM = "minimum";
    public static final String VALUE = "value";
    public static final String ZONE = "zone";
    public static final String DELIVERYMODE = "deliveryMode";
    protected static final BidirectionalOneToManyHandler<GeneratedZoneDeliveryModeValue> DELIVERYMODEHANDLER = new BidirectionalOneToManyHandler(GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE, false, "deliveryMode", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("minimum", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("zone", Item.AttributeMode.INITIAL);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        DELIVERYMODEHANDLER.newInstance(ctx, allAttributes);
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


    public ZoneDeliveryMode getDeliveryMode(SessionContext ctx)
    {
        return (ZoneDeliveryMode)getProperty(ctx, "deliveryMode");
    }


    public ZoneDeliveryMode getDeliveryMode()
    {
        return getDeliveryMode(getSession().getSessionContext());
    }


    protected void setDeliveryMode(SessionContext ctx, ZoneDeliveryMode value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'deliveryMode' is not changeable", 0);
        }
        DELIVERYMODEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setDeliveryMode(ZoneDeliveryMode value)
    {
        setDeliveryMode(getSession().getSessionContext(), value);
    }


    public Double getMinimum(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "minimum");
    }


    public Double getMinimum()
    {
        return getMinimum(getSession().getSessionContext());
    }


    public double getMinimumAsPrimitive(SessionContext ctx)
    {
        Double value = getMinimum(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getMinimumAsPrimitive()
    {
        return getMinimumAsPrimitive(getSession().getSessionContext());
    }


    public void setMinimum(SessionContext ctx, Double value)
    {
        setProperty(ctx, "minimum", value);
    }


    public void setMinimum(Double value)
    {
        setMinimum(getSession().getSessionContext(), value);
    }


    public void setMinimum(SessionContext ctx, double value)
    {
        setMinimum(ctx, Double.valueOf(value));
    }


    public void setMinimum(double value)
    {
        setMinimum(getSession().getSessionContext(), value);
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


    public Zone getZone(SessionContext ctx)
    {
        return (Zone)getProperty(ctx, "zone");
    }


    public Zone getZone()
    {
        return getZone(getSession().getSessionContext());
    }


    public void setZone(SessionContext ctx, Zone value)
    {
        setProperty(ctx, "zone", value);
    }


    public void setZone(Zone value)
    {
        setZone(getSession().getSessionContext(), value);
    }
}
