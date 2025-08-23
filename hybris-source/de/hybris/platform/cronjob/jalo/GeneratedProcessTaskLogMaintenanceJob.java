package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProcessTaskLogMaintenanceJob extends ServicelayerJob
{
    public static final String AGE = "age";
    public static final String NUMBEROFLOGS = "numberOfLogs";
    public static final String QUERYCOUNT = "queryCount";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("age", Item.AttributeMode.INITIAL);
        tmp.put("numberOfLogs", Item.AttributeMode.INITIAL);
        tmp.put("queryCount", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAge(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "age");
    }


    public Integer getAge()
    {
        return getAge(getSession().getSessionContext());
    }


    public int getAgeAsPrimitive(SessionContext ctx)
    {
        Integer value = getAge(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAgeAsPrimitive()
    {
        return getAgeAsPrimitive(getSession().getSessionContext());
    }


    public void setAge(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "age", value);
    }


    public void setAge(Integer value)
    {
        setAge(getSession().getSessionContext(), value);
    }


    public void setAge(SessionContext ctx, int value)
    {
        setAge(ctx, Integer.valueOf(value));
    }


    public void setAge(int value)
    {
        setAge(getSession().getSessionContext(), value);
    }


    public Integer getNumberOfLogs(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "numberOfLogs");
    }


    public Integer getNumberOfLogs()
    {
        return getNumberOfLogs(getSession().getSessionContext());
    }


    public int getNumberOfLogsAsPrimitive(SessionContext ctx)
    {
        Integer value = getNumberOfLogs(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumberOfLogsAsPrimitive()
    {
        return getNumberOfLogsAsPrimitive(getSession().getSessionContext());
    }


    public void setNumberOfLogs(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "numberOfLogs", value);
    }


    public void setNumberOfLogs(Integer value)
    {
        setNumberOfLogs(getSession().getSessionContext(), value);
    }


    public void setNumberOfLogs(SessionContext ctx, int value)
    {
        setNumberOfLogs(ctx, Integer.valueOf(value));
    }


    public void setNumberOfLogs(int value)
    {
        setNumberOfLogs(getSession().getSessionContext(), value);
    }


    public Integer getQueryCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "queryCount");
    }


    public Integer getQueryCount()
    {
        return getQueryCount(getSession().getSessionContext());
    }


    public int getQueryCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getQueryCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQueryCountAsPrimitive()
    {
        return getQueryCountAsPrimitive(getSession().getSessionContext());
    }


    public void setQueryCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "queryCount", value);
    }


    public void setQueryCount(Integer value)
    {
        setQueryCount(getSession().getSessionContext(), value);
    }


    public void setQueryCount(SessionContext ctx, int value)
    {
        setQueryCount(ctx, Integer.valueOf(value));
    }


    public void setQueryCount(int value)
    {
        setQueryCount(getSession().getSessionContext(), value);
    }
}
