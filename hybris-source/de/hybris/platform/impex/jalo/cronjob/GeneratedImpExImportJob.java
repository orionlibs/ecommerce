package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpExImportJob extends Job
{
    public static final String MAXTHREADS = "maxThreads";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Job.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("maxThreads", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getMaxThreads(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxThreads");
    }


    public Integer getMaxThreads()
    {
        return getMaxThreads(getSession().getSessionContext());
    }


    public int getMaxThreadsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxThreads(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxThreadsAsPrimitive()
    {
        return getMaxThreadsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxThreads(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxThreads", value);
    }


    public void setMaxThreads(Integer value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }


    public void setMaxThreads(SessionContext ctx, int value)
    {
        setMaxThreads(ctx, Integer.valueOf(value));
    }


    public void setMaxThreads(int value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }
}
