package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.StandardDateRange;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedTrigger extends GenericItem
{
    public static final String ACTIVE = "active";
    public static final String SECOND = "second";
    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String RELATIVE = "relative";
    public static final String DAYSOFWEEK = "daysOfWeek";
    public static final String WEEKINTERVAL = "weekInterval";
    public static final String DATERANGE = "dateRange";
    public static final String ACTIVATIONTIME = "activationTime";
    public static final String CRONEXPRESSION = "cronExpression";
    public static final String MAXACCEPTABLEDELAY = "maxAcceptableDelay";
    public static final String JOB = "job";
    public static final String CRONJOB = "cronJob";
    protected static final BidirectionalOneToManyHandler<GeneratedTrigger> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.TRIGGER, false, "job", null, false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedTrigger> CRONJOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.TRIGGER, false, "cronJob", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("second", Item.AttributeMode.INITIAL);
        tmp.put("minute", Item.AttributeMode.INITIAL);
        tmp.put("hour", Item.AttributeMode.INITIAL);
        tmp.put("day", Item.AttributeMode.INITIAL);
        tmp.put("month", Item.AttributeMode.INITIAL);
        tmp.put("year", Item.AttributeMode.INITIAL);
        tmp.put("relative", Item.AttributeMode.INITIAL);
        tmp.put("daysOfWeek", Item.AttributeMode.INITIAL);
        tmp.put("weekInterval", Item.AttributeMode.INITIAL);
        tmp.put("dateRange", Item.AttributeMode.INITIAL);
        tmp.put("activationTime", Item.AttributeMode.INITIAL);
        tmp.put("cronExpression", Item.AttributeMode.INITIAL);
        tmp.put("maxAcceptableDelay", Item.AttributeMode.INITIAL);
        tmp.put("job", Item.AttributeMode.INITIAL);
        tmp.put("cronJob", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getActivationTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activationTime");
    }


    public Date getActivationTime()
    {
        return getActivationTime(getSession().getSessionContext());
    }


    public void setActivationTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activationTime", value);
    }


    public void setActivationTime(Date value)
    {
        setActivationTime(getSession().getSessionContext(), value);
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        CRONJOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getCronExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "cronExpression");
    }


    public String getCronExpression()
    {
        return getCronExpression(getSession().getSessionContext());
    }


    public void setCronExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "cronExpression", value);
    }


    public void setCronExpression(String value)
    {
        setCronExpression(getSession().getSessionContext(), value);
    }


    public CronJob getCronJob(SessionContext ctx)
    {
        return (CronJob)getProperty(ctx, "cronJob");
    }


    public CronJob getCronJob()
    {
        return getCronJob(getSession().getSessionContext());
    }


    protected void setCronJob(SessionContext ctx, CronJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'cronJob' is not changeable", 0);
        }
        CRONJOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setCronJob(CronJob value)
    {
        setCronJob(getSession().getSessionContext(), value);
    }


    public StandardDateRange getDateRange(SessionContext ctx)
    {
        return (StandardDateRange)getProperty(ctx, "dateRange");
    }


    public StandardDateRange getDateRange()
    {
        return getDateRange(getSession().getSessionContext());
    }


    public void setDateRange(SessionContext ctx, StandardDateRange value)
    {
        setProperty(ctx, "dateRange", value);
    }


    public void setDateRange(StandardDateRange value)
    {
        setDateRange(getSession().getSessionContext(), value);
    }


    public Integer getDay(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "day");
    }


    public Integer getDay()
    {
        return getDay(getSession().getSessionContext());
    }


    public int getDayAsPrimitive(SessionContext ctx)
    {
        Integer value = getDay(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDayAsPrimitive()
    {
        return getDayAsPrimitive(getSession().getSessionContext());
    }


    public void setDay(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "day", value);
    }


    public void setDay(Integer value)
    {
        setDay(getSession().getSessionContext(), value);
    }


    public void setDay(SessionContext ctx, int value)
    {
        setDay(ctx, Integer.valueOf(value));
    }


    public void setDay(int value)
    {
        setDay(getSession().getSessionContext(), value);
    }


    public List<EnumerationValue> getDaysOfWeek(SessionContext ctx)
    {
        List<EnumerationValue> coll = (List<EnumerationValue>)getProperty(ctx, "daysOfWeek");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<EnumerationValue> getDaysOfWeek()
    {
        return getDaysOfWeek(getSession().getSessionContext());
    }


    public void setDaysOfWeek(SessionContext ctx, List<EnumerationValue> value)
    {
        setProperty(ctx, "daysOfWeek", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setDaysOfWeek(List<EnumerationValue> value)
    {
        setDaysOfWeek(getSession().getSessionContext(), value);
    }


    public Integer getHour(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "hour");
    }


    public Integer getHour()
    {
        return getHour(getSession().getSessionContext());
    }


    public int getHourAsPrimitive(SessionContext ctx)
    {
        Integer value = getHour(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getHourAsPrimitive()
    {
        return getHourAsPrimitive(getSession().getSessionContext());
    }


    public void setHour(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "hour", value);
    }


    public void setHour(Integer value)
    {
        setHour(getSession().getSessionContext(), value);
    }


    public void setHour(SessionContext ctx, int value)
    {
        setHour(ctx, Integer.valueOf(value));
    }


    public void setHour(int value)
    {
        setHour(getSession().getSessionContext(), value);
    }


    public Job getJob(SessionContext ctx)
    {
        return (Job)getProperty(ctx, "job");
    }


    public Job getJob()
    {
        return getJob(getSession().getSessionContext());
    }


    protected void setJob(SessionContext ctx, Job value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'job' is not changeable", 0);
        }
        JOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setJob(Job value)
    {
        setJob(getSession().getSessionContext(), value);
    }


    public Integer getMaxAcceptableDelay(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxAcceptableDelay");
    }


    public Integer getMaxAcceptableDelay()
    {
        return getMaxAcceptableDelay(getSession().getSessionContext());
    }


    public int getMaxAcceptableDelayAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxAcceptableDelay(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxAcceptableDelayAsPrimitive()
    {
        return getMaxAcceptableDelayAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxAcceptableDelay(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxAcceptableDelay", value);
    }


    public void setMaxAcceptableDelay(Integer value)
    {
        setMaxAcceptableDelay(getSession().getSessionContext(), value);
    }


    public void setMaxAcceptableDelay(SessionContext ctx, int value)
    {
        setMaxAcceptableDelay(ctx, Integer.valueOf(value));
    }


    public void setMaxAcceptableDelay(int value)
    {
        setMaxAcceptableDelay(getSession().getSessionContext(), value);
    }


    public Integer getMinute(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "minute");
    }


    public Integer getMinute()
    {
        return getMinute(getSession().getSessionContext());
    }


    public int getMinuteAsPrimitive(SessionContext ctx)
    {
        Integer value = getMinute(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMinuteAsPrimitive()
    {
        return getMinuteAsPrimitive(getSession().getSessionContext());
    }


    public void setMinute(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "minute", value);
    }


    public void setMinute(Integer value)
    {
        setMinute(getSession().getSessionContext(), value);
    }


    public void setMinute(SessionContext ctx, int value)
    {
        setMinute(ctx, Integer.valueOf(value));
    }


    public void setMinute(int value)
    {
        setMinute(getSession().getSessionContext(), value);
    }


    public Integer getMonth(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "month");
    }


    public Integer getMonth()
    {
        return getMonth(getSession().getSessionContext());
    }


    public int getMonthAsPrimitive(SessionContext ctx)
    {
        Integer value = getMonth(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMonthAsPrimitive()
    {
        return getMonthAsPrimitive(getSession().getSessionContext());
    }


    public void setMonth(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "month", value);
    }


    public void setMonth(Integer value)
    {
        setMonth(getSession().getSessionContext(), value);
    }


    public void setMonth(SessionContext ctx, int value)
    {
        setMonth(ctx, Integer.valueOf(value));
    }


    public void setMonth(int value)
    {
        setMonth(getSession().getSessionContext(), value);
    }


    public Boolean isRelative(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "relative");
    }


    public Boolean isRelative()
    {
        return isRelative(getSession().getSessionContext());
    }


    public boolean isRelativeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRelative(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRelativeAsPrimitive()
    {
        return isRelativeAsPrimitive(getSession().getSessionContext());
    }


    public void setRelative(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "relative", value);
    }


    public void setRelative(Boolean value)
    {
        setRelative(getSession().getSessionContext(), value);
    }


    public void setRelative(SessionContext ctx, boolean value)
    {
        setRelative(ctx, Boolean.valueOf(value));
    }


    public void setRelative(boolean value)
    {
        setRelative(getSession().getSessionContext(), value);
    }


    public Integer getSecond(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "second");
    }


    public Integer getSecond()
    {
        return getSecond(getSession().getSessionContext());
    }


    public int getSecondAsPrimitive(SessionContext ctx)
    {
        Integer value = getSecond(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSecondAsPrimitive()
    {
        return getSecondAsPrimitive(getSession().getSessionContext());
    }


    public void setSecond(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "second", value);
    }


    public void setSecond(Integer value)
    {
        setSecond(getSession().getSessionContext(), value);
    }


    public void setSecond(SessionContext ctx, int value)
    {
        setSecond(ctx, Integer.valueOf(value));
    }


    public void setSecond(int value)
    {
        setSecond(getSession().getSessionContext(), value);
    }


    public Integer getWeekInterval(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "weekInterval");
    }


    public Integer getWeekInterval()
    {
        return getWeekInterval(getSession().getSessionContext());
    }


    public int getWeekIntervalAsPrimitive(SessionContext ctx)
    {
        Integer value = getWeekInterval(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getWeekIntervalAsPrimitive()
    {
        return getWeekIntervalAsPrimitive(getSession().getSessionContext());
    }


    public void setWeekInterval(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "weekInterval", value);
    }


    public void setWeekInterval(Integer value)
    {
        setWeekInterval(getSession().getSessionContext(), value);
    }


    public void setWeekInterval(SessionContext ctx, int value)
    {
        setWeekInterval(ctx, Integer.valueOf(value));
    }


    public void setWeekInterval(int value)
    {
        setWeekInterval(getSession().getSessionContext(), value);
    }


    public Integer getYear(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "year");
    }


    public Integer getYear()
    {
        return getYear(getSession().getSessionContext());
    }


    public int getYearAsPrimitive(SessionContext ctx)
    {
        Integer value = getYear(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getYearAsPrimitive()
    {
        return getYearAsPrimitive(getSession().getSessionContext());
    }


    public void setYear(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "year", value);
    }


    public void setYear(Integer value)
    {
        setYear(getSession().getSessionContext(), value);
    }


    public void setYear(SessionContext ctx, int value)
    {
        setYear(ctx, Integer.valueOf(value));
    }


    public void setYear(int value)
    {
        setYear(getSession().getSessionContext(), value);
    }
}
