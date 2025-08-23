package de.hybris.platform.task;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTaskCondition extends GenericItem
{
    public static final String UNIQUEID = "uniqueID";
    public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
    public static final String PROCESSEDDATE = "processedDate";
    public static final String FULFILLED = "fulfilled";
    public static final String CONSUMED = "consumed";
    public static final String CHOICE = "choice";
    public static final String COUNTER = "counter";
    public static final String TASK = "task";
    protected static final BidirectionalOneToManyHandler<GeneratedTaskCondition> TASKHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.TASKCONDITION, false, "task", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uniqueID", Item.AttributeMode.INITIAL);
        tmp.put("expirationTimeMillis", Item.AttributeMode.INITIAL);
        tmp.put("processedDate", Item.AttributeMode.INITIAL);
        tmp.put("fulfilled", Item.AttributeMode.INITIAL);
        tmp.put("consumed", Item.AttributeMode.INITIAL);
        tmp.put("choice", Item.AttributeMode.INITIAL);
        tmp.put("counter", Item.AttributeMode.INITIAL);
        tmp.put("task", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getChoice(SessionContext ctx)
    {
        return (String)getProperty(ctx, "choice");
    }


    public String getChoice()
    {
        return getChoice(getSession().getSessionContext());
    }


    public void setChoice(SessionContext ctx, String value)
    {
        setProperty(ctx, "choice", value);
    }


    public void setChoice(String value)
    {
        setChoice(getSession().getSessionContext(), value);
    }


    public Boolean isConsumed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "consumed");
    }


    public Boolean isConsumed()
    {
        return isConsumed(getSession().getSessionContext());
    }


    public boolean isConsumedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isConsumed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isConsumedAsPrimitive()
    {
        return isConsumedAsPrimitive(getSession().getSessionContext());
    }


    public void setConsumed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "consumed", value);
    }


    public void setConsumed(Boolean value)
    {
        setConsumed(getSession().getSessionContext(), value);
    }


    public void setConsumed(SessionContext ctx, boolean value)
    {
        setConsumed(ctx, Boolean.valueOf(value));
    }


    public void setConsumed(boolean value)
    {
        setConsumed(getSession().getSessionContext(), value);
    }


    public Integer getCounter(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "counter");
    }


    public Integer getCounter()
    {
        return getCounter(getSession().getSessionContext());
    }


    public int getCounterAsPrimitive(SessionContext ctx)
    {
        Integer value = getCounter(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCounterAsPrimitive()
    {
        return getCounterAsPrimitive(getSession().getSessionContext());
    }


    public void setCounter(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "counter", value);
    }


    public void setCounter(Integer value)
    {
        setCounter(getSession().getSessionContext(), value);
    }


    public void setCounter(SessionContext ctx, int value)
    {
        setCounter(ctx, Integer.valueOf(value));
    }


    public void setCounter(int value)
    {
        setCounter(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        TASKHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Long getExpirationTimeMillis(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "expirationTimeMillis");
    }


    public Long getExpirationTimeMillis()
    {
        return getExpirationTimeMillis(getSession().getSessionContext());
    }


    public long getExpirationTimeMillisAsPrimitive(SessionContext ctx)
    {
        Long value = getExpirationTimeMillis(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getExpirationTimeMillisAsPrimitive()
    {
        return getExpirationTimeMillisAsPrimitive(getSession().getSessionContext());
    }


    public void setExpirationTimeMillis(SessionContext ctx, Long value)
    {
        setProperty(ctx, "expirationTimeMillis", value);
    }


    public void setExpirationTimeMillis(Long value)
    {
        setExpirationTimeMillis(getSession().getSessionContext(), value);
    }


    public void setExpirationTimeMillis(SessionContext ctx, long value)
    {
        setExpirationTimeMillis(ctx, Long.valueOf(value));
    }


    public void setExpirationTimeMillis(long value)
    {
        setExpirationTimeMillis(getSession().getSessionContext(), value);
    }


    public Boolean isFulfilled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "fulfilled");
    }


    public Boolean isFulfilled()
    {
        return isFulfilled(getSession().getSessionContext());
    }


    public boolean isFulfilledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFulfilled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFulfilledAsPrimitive()
    {
        return isFulfilledAsPrimitive(getSession().getSessionContext());
    }


    public void setFulfilled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "fulfilled", value);
    }


    public void setFulfilled(Boolean value)
    {
        setFulfilled(getSession().getSessionContext(), value);
    }


    public void setFulfilled(SessionContext ctx, boolean value)
    {
        setFulfilled(ctx, Boolean.valueOf(value));
    }


    public void setFulfilled(boolean value)
    {
        setFulfilled(getSession().getSessionContext(), value);
    }


    public Date getProcessedDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "processedDate");
    }


    public Date getProcessedDate()
    {
        return getProcessedDate(getSession().getSessionContext());
    }


    public void setProcessedDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "processedDate", value);
    }


    public void setProcessedDate(Date value)
    {
        setProcessedDate(getSession().getSessionContext(), value);
    }


    public Task getTask(SessionContext ctx)
    {
        return (Task)getProperty(ctx, "task");
    }


    public Task getTask()
    {
        return getTask(getSession().getSessionContext());
    }


    public void setTask(SessionContext ctx, Task value)
    {
        TASKHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setTask(Task value)
    {
        setTask(getSession().getSessionContext(), value);
    }


    public String getUniqueID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uniqueID");
    }


    public String getUniqueID()
    {
        return getUniqueID(getSession().getSessionContext());
    }


    public void setUniqueID(SessionContext ctx, String value)
    {
        setProperty(ctx, "uniqueID", value);
    }


    public void setUniqueID(String value)
    {
        setUniqueID(getSession().getSessionContext(), value);
    }
}
