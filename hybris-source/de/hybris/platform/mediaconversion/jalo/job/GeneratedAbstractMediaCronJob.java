package de.hybris.platform.mediaconversion.jalo.job;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractMediaCronJob extends CronJob
{
    public static final String MAXTHREADS = "maxThreads";
    public static final String CATALOGVERSION = "catalogVersion";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("maxThreads", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
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
