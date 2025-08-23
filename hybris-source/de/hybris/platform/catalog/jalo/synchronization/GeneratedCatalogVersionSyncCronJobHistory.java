package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.cronjob.jalo.CronJobHistory;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCatalogVersionSyncCronJobHistory extends CronJobHistory
{
    public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
    public static final String SCHEDULEDITEMSCOUNT = "scheduledItemsCount";
    public static final String DUMPEDITEMSCOUNT = "dumpedItemsCount";
    public static final String FULLSYNC = "fullSync";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJobHistory.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("processedItemsCount", Item.AttributeMode.INITIAL);
        tmp.put("scheduledItemsCount", Item.AttributeMode.INITIAL);
        tmp.put("dumpedItemsCount", Item.AttributeMode.INITIAL);
        tmp.put("fullSync", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getDumpedItemsCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "dumpedItemsCount");
    }


    public Integer getDumpedItemsCount()
    {
        return getDumpedItemsCount(getSession().getSessionContext());
    }


    public int getDumpedItemsCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getDumpedItemsCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDumpedItemsCountAsPrimitive()
    {
        return getDumpedItemsCountAsPrimitive(getSession().getSessionContext());
    }


    public void setDumpedItemsCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "dumpedItemsCount", value);
    }


    public void setDumpedItemsCount(Integer value)
    {
        setDumpedItemsCount(getSession().getSessionContext(), value);
    }


    public void setDumpedItemsCount(SessionContext ctx, int value)
    {
        setDumpedItemsCount(ctx, Integer.valueOf(value));
    }


    public void setDumpedItemsCount(int value)
    {
        setDumpedItemsCount(getSession().getSessionContext(), value);
    }


    public Boolean isFullSync(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "fullSync");
    }


    public Boolean isFullSync()
    {
        return isFullSync(getSession().getSessionContext());
    }


    public boolean isFullSyncAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFullSync(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFullSyncAsPrimitive()
    {
        return isFullSyncAsPrimitive(getSession().getSessionContext());
    }


    public void setFullSync(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "fullSync", value);
    }


    public void setFullSync(Boolean value)
    {
        setFullSync(getSession().getSessionContext(), value);
    }


    public void setFullSync(SessionContext ctx, boolean value)
    {
        setFullSync(ctx, Boolean.valueOf(value));
    }


    public void setFullSync(boolean value)
    {
        setFullSync(getSession().getSessionContext(), value);
    }


    public Integer getProcessedItemsCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "processedItemsCount");
    }


    public Integer getProcessedItemsCount()
    {
        return getProcessedItemsCount(getSession().getSessionContext());
    }


    public int getProcessedItemsCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getProcessedItemsCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getProcessedItemsCountAsPrimitive()
    {
        return getProcessedItemsCountAsPrimitive(getSession().getSessionContext());
    }


    public void setProcessedItemsCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "processedItemsCount", value);
    }


    public void setProcessedItemsCount(Integer value)
    {
        setProcessedItemsCount(getSession().getSessionContext(), value);
    }


    public void setProcessedItemsCount(SessionContext ctx, int value)
    {
        setProcessedItemsCount(ctx, Integer.valueOf(value));
    }


    public void setProcessedItemsCount(int value)
    {
        setProcessedItemsCount(getSession().getSessionContext(), value);
    }


    public Integer getScheduledItemsCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "scheduledItemsCount");
    }


    public Integer getScheduledItemsCount()
    {
        return getScheduledItemsCount(getSession().getSessionContext());
    }


    public int getScheduledItemsCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getScheduledItemsCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getScheduledItemsCountAsPrimitive()
    {
        return getScheduledItemsCountAsPrimitive(getSession().getSessionContext());
    }


    public void setScheduledItemsCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "scheduledItemsCount", value);
    }


    public void setScheduledItemsCount(Integer value)
    {
        setScheduledItemsCount(getSession().getSessionContext(), value);
    }


    public void setScheduledItemsCount(SessionContext ctx, int value)
    {
        setScheduledItemsCount(ctx, Integer.valueOf(value));
    }


    public void setScheduledItemsCount(int value)
    {
        setScheduledItemsCount(getSession().getSessionContext(), value);
    }
}
