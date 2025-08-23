package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCleanupDynamicProcessDefinitionsCronJob extends CronJob
{
    public static final String TIMETHRESHOLD = "timeThreshold";
    public static final String VERSIONTHRESHOLD = "versionThreshold";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("timeThreshold", Item.AttributeMode.INITIAL);
        tmp.put("versionThreshold", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getTimeThreshold(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "timeThreshold");
    }


    public Integer getTimeThreshold()
    {
        return getTimeThreshold(getSession().getSessionContext());
    }


    public int getTimeThresholdAsPrimitive(SessionContext ctx)
    {
        Integer value = getTimeThreshold(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getTimeThresholdAsPrimitive()
    {
        return getTimeThresholdAsPrimitive(getSession().getSessionContext());
    }


    public void setTimeThreshold(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "timeThreshold", value);
    }


    public void setTimeThreshold(Integer value)
    {
        setTimeThreshold(getSession().getSessionContext(), value);
    }


    public void setTimeThreshold(SessionContext ctx, int value)
    {
        setTimeThreshold(ctx, Integer.valueOf(value));
    }


    public void setTimeThreshold(int value)
    {
        setTimeThreshold(getSession().getSessionContext(), value);
    }


    public Integer getVersionThreshold(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "versionThreshold");
    }


    public Integer getVersionThreshold()
    {
        return getVersionThreshold(getSession().getSessionContext());
    }


    public int getVersionThresholdAsPrimitive(SessionContext ctx)
    {
        Integer value = getVersionThreshold(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVersionThresholdAsPrimitive()
    {
        return getVersionThresholdAsPrimitive(getSession().getSessionContext());
    }


    public void setVersionThreshold(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "versionThreshold", value);
    }


    public void setVersionThreshold(Integer value)
    {
        setVersionThreshold(getSession().getSessionContext(), value);
    }


    public void setVersionThreshold(SessionContext ctx, int value)
    {
        setVersionThreshold(ctx, Integer.valueOf(value));
    }


    public void setVersionThreshold(int value)
    {
        setVersionThreshold(getSession().getSessionContext(), value);
    }
}
