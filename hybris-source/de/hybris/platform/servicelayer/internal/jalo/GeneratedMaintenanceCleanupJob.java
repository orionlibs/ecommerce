package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMaintenanceCleanupJob extends ServicelayerJob
{
    public static final String THRESHOLD = "threshold";
    public static final String SEARCHTYPE = "searchType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("threshold", Item.AttributeMode.INITIAL);
        tmp.put("searchType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ComposedType getSearchType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "searchType");
    }


    public ComposedType getSearchType()
    {
        return getSearchType(getSession().getSessionContext());
    }


    public void setSearchType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "searchType", value);
    }


    public void setSearchType(ComposedType value)
    {
        setSearchType(getSession().getSessionContext(), value);
    }


    public Integer getThreshold(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "threshold");
    }


    public Integer getThreshold()
    {
        return getThreshold(getSession().getSessionContext());
    }


    public int getThresholdAsPrimitive(SessionContext ctx)
    {
        Integer value = getThreshold(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getThresholdAsPrimitive()
    {
        return getThresholdAsPrimitive(getSession().getSessionContext());
    }


    public void setThreshold(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "threshold", value);
    }


    public void setThreshold(Integer value)
    {
        setThreshold(getSession().getSessionContext(), value);
    }


    public void setThreshold(SessionContext ctx, int value)
    {
        setThreshold(ctx, Integer.valueOf(value));
    }


    public void setThreshold(int value)
    {
        setThreshold(getSession().getSessionContext(), value);
    }
}
