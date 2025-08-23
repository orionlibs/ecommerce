package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTimeRestriction extends AbstractRestriction
{
    public static final String ACTIVEFROM = "activeFrom";
    public static final String ACTIVEUNTIL = "activeUntil";
    public static final String USESTORETIMEZONE = "useStoreTimeZone";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("activeFrom", Item.AttributeMode.INITIAL);
        tmp.put("activeUntil", Item.AttributeMode.INITIAL);
        tmp.put("useStoreTimeZone", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getActiveFrom(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeFrom");
    }


    public Date getActiveFrom()
    {
        return getActiveFrom(getSession().getSessionContext());
    }


    public void setActiveFrom(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeFrom", value);
    }


    public void setActiveFrom(Date value)
    {
        setActiveFrom(getSession().getSessionContext(), value);
    }


    public Date getActiveUntil(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeUntil");
    }


    public Date getActiveUntil()
    {
        return getActiveUntil(getSession().getSessionContext());
    }


    public void setActiveUntil(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeUntil", value);
    }


    public void setActiveUntil(Date value)
    {
        setActiveUntil(getSession().getSessionContext(), value);
    }


    public Boolean isUseStoreTimeZone(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useStoreTimeZone");
    }


    public Boolean isUseStoreTimeZone()
    {
        return isUseStoreTimeZone(getSession().getSessionContext());
    }


    public boolean isUseStoreTimeZoneAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseStoreTimeZone(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseStoreTimeZoneAsPrimitive()
    {
        return isUseStoreTimeZoneAsPrimitive(getSession().getSessionContext());
    }


    public void setUseStoreTimeZone(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useStoreTimeZone", value);
    }


    public void setUseStoreTimeZone(Boolean value)
    {
        setUseStoreTimeZone(getSession().getSessionContext(), value);
    }


    public void setUseStoreTimeZone(SessionContext ctx, boolean value)
    {
        setUseStoreTimeZone(ctx, Boolean.valueOf(value));
    }


    public void setUseStoreTimeZone(boolean value)
    {
        setUseStoreTimeZone(getSession().getSessionContext(), value);
    }
}
