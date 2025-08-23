package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedGeocodeAddressesCronJob extends CronJob
{
    public static final String BATCHSIZE = "batchSize";
    public static final String INTERNALDELAY = "internalDelay";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("batchSize", Item.AttributeMode.INITIAL);
        tmp.put("internalDelay", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getBatchSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "batchSize");
    }


    public Integer getBatchSize()
    {
        return getBatchSize(getSession().getSessionContext());
    }


    public int getBatchSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getBatchSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getBatchSizeAsPrimitive()
    {
        return getBatchSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setBatchSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "batchSize", value);
    }


    public void setBatchSize(Integer value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public void setBatchSize(SessionContext ctx, int value)
    {
        setBatchSize(ctx, Integer.valueOf(value));
    }


    public void setBatchSize(int value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public Integer getInternalDelay(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "internalDelay");
    }


    public Integer getInternalDelay()
    {
        return getInternalDelay(getSession().getSessionContext());
    }


    public int getInternalDelayAsPrimitive(SessionContext ctx)
    {
        Integer value = getInternalDelay(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getInternalDelayAsPrimitive()
    {
        return getInternalDelayAsPrimitive(getSession().getSessionContext());
    }


    public void setInternalDelay(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "internalDelay", value);
    }


    public void setInternalDelay(Integer value)
    {
        setInternalDelay(getSession().getSessionContext(), value);
    }


    public void setInternalDelay(SessionContext ctx, int value)
    {
        setInternalDelay(ctx, Integer.valueOf(value));
    }


    public void setInternalDelay(int value)
    {
        setInternalDelay(getSession().getSessionContext(), value);
    }
}
