package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedZoneDeliveryMode extends DeliveryMode
{
    public static final String PROPERTYNAME = "propertyName";
    public static final String NET = "net";
    public static final String VALUES = "values";
    protected static final OneToManyHandler<ZoneDeliveryModeValue> VALUESHANDLER = new OneToManyHandler(GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE, true, "deliveryMode", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(DeliveryMode.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("propertyName", Item.AttributeMode.INITIAL);
        tmp.put("net", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isNet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "net");
    }


    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    public boolean isNetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNetAsPrimitive()
    {
        return isNetAsPrimitive(getSession().getSessionContext());
    }


    public void setNet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "net", value);
    }


    public void setNet(Boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public void setNet(SessionContext ctx, boolean value)
    {
        setNet(ctx, Boolean.valueOf(value));
    }


    public void setNet(boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public String getPropertyName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "propertyName");
    }


    public String getPropertyName()
    {
        return getPropertyName(getSession().getSessionContext());
    }


    public void setPropertyName(SessionContext ctx, String value)
    {
        setProperty(ctx, "propertyName", value);
    }


    public void setPropertyName(String value)
    {
        setPropertyName(getSession().getSessionContext(), value);
    }


    public Set<ZoneDeliveryModeValue> getValues(SessionContext ctx)
    {
        return (Set<ZoneDeliveryModeValue>)VALUESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<ZoneDeliveryModeValue> getValues()
    {
        return getValues(getSession().getSessionContext());
    }


    public void setValues(SessionContext ctx, Set<ZoneDeliveryModeValue> value)
    {
        VALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setValues(Set<ZoneDeliveryModeValue> value)
    {
        setValues(getSession().getSessionContext(), value);
    }


    public void addToValues(SessionContext ctx, ZoneDeliveryModeValue value)
    {
        VALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToValues(ZoneDeliveryModeValue value)
    {
        addToValues(getSession().getSessionContext(), value);
    }


    public void removeFromValues(SessionContext ctx, ZoneDeliveryModeValue value)
    {
        VALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromValues(ZoneDeliveryModeValue value)
    {
        removeFromValues(getSession().getSessionContext(), value);
    }
}
