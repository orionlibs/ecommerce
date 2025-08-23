package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCleanUpCronJob extends CronJob
{
    public static final String XDAYSOLD = "xDaysOld";
    public static final String EXCLUDECRONJOBS = "excludeCronJobs";
    public static final String RESULTCOLL = "resultcoll";
    public static final String STATUSCOLL = "statuscoll";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("xDaysOld", Item.AttributeMode.INITIAL);
        tmp.put("excludeCronJobs", Item.AttributeMode.INITIAL);
        tmp.put("resultcoll", Item.AttributeMode.INITIAL);
        tmp.put("statuscoll", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CronJob> getExcludeCronJobs(SessionContext ctx)
    {
        List<CronJob> coll = (List<CronJob>)getProperty(ctx, "excludeCronJobs");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<CronJob> getExcludeCronJobs()
    {
        return getExcludeCronJobs(getSession().getSessionContext());
    }


    public void setExcludeCronJobs(SessionContext ctx, List<CronJob> value)
    {
        setProperty(ctx, "excludeCronJobs", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setExcludeCronJobs(List<CronJob> value)
    {
        setExcludeCronJobs(getSession().getSessionContext(), value);
    }


    public Collection<EnumerationValue> getResultcoll(SessionContext ctx)
    {
        Collection<EnumerationValue> coll = (Collection<EnumerationValue>)getProperty(ctx, "resultcoll");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<EnumerationValue> getResultcoll()
    {
        return getResultcoll(getSession().getSessionContext());
    }


    public void setResultcoll(SessionContext ctx, Collection<EnumerationValue> value)
    {
        setProperty(ctx, "resultcoll", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setResultcoll(Collection<EnumerationValue> value)
    {
        setResultcoll(getSession().getSessionContext(), value);
    }


    public Collection<EnumerationValue> getStatuscoll(SessionContext ctx)
    {
        Collection<EnumerationValue> coll = (Collection<EnumerationValue>)getProperty(ctx, "statuscoll");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<EnumerationValue> getStatuscoll()
    {
        return getStatuscoll(getSession().getSessionContext());
    }


    public void setStatuscoll(SessionContext ctx, Collection<EnumerationValue> value)
    {
        setProperty(ctx, "statuscoll", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setStatuscoll(Collection<EnumerationValue> value)
    {
        setStatuscoll(getSession().getSessionContext(), value);
    }


    public Integer getXDaysOld(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "xDaysOld");
    }


    public Integer getXDaysOld()
    {
        return getXDaysOld(getSession().getSessionContext());
    }


    public int getXDaysOldAsPrimitive(SessionContext ctx)
    {
        Integer value = getXDaysOld(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getXDaysOldAsPrimitive()
    {
        return getXDaysOldAsPrimitive(getSession().getSessionContext());
    }


    public void setXDaysOld(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "xDaysOld", value);
    }


    public void setXDaysOld(Integer value)
    {
        setXDaysOld(getSession().getSessionContext(), value);
    }


    public void setXDaysOld(SessionContext ctx, int value)
    {
        setXDaysOld(ctx, Integer.valueOf(value));
    }


    public void setXDaysOld(int value)
    {
        setXDaysOld(getSession().getSessionContext(), value);
    }
}
