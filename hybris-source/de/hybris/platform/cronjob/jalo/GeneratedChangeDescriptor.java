package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedChangeDescriptor extends GenericItem
{
    public static final String CRONJOB = "cronJob";
    public static final String STEP = "step";
    public static final String CHANGEDITEM = "changedItem";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String SAVETIMESTAMP = "saveTimestamp";
    public static final String PREVIOUSITEMSTATE = "previousItemState";
    public static final String CHANGETYPE = "changeType";
    public static final String DESCRIPTION = "description";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("cronJob", Item.AttributeMode.INITIAL);
        tmp.put("step", Item.AttributeMode.INITIAL);
        tmp.put("changedItem", Item.AttributeMode.INITIAL);
        tmp.put("sequenceNumber", Item.AttributeMode.INITIAL);
        tmp.put("saveTimestamp", Item.AttributeMode.INITIAL);
        tmp.put("previousItemState", Item.AttributeMode.INITIAL);
        tmp.put("changeType", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Item getChangedItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "changedItem");
    }


    public Item getChangedItem()
    {
        return getChangedItem(getSession().getSessionContext());
    }


    public void setChangedItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "changedItem", value);
    }


    public void setChangedItem(Item value)
    {
        setChangedItem(getSession().getSessionContext(), value);
    }


    public String getChangeType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "changeType");
    }


    public String getChangeType()
    {
        return getChangeType(getSession().getSessionContext());
    }


    public void setChangeType(SessionContext ctx, String value)
    {
        setProperty(ctx, "changeType", value);
    }


    public void setChangeType(String value)
    {
        setChangeType(getSession().getSessionContext(), value);
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
        setProperty(ctx, "cronJob", value);
    }


    protected void setCronJob(CronJob value)
    {
        setCronJob(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Map getPreviousItemState(SessionContext ctx)
    {
        return (Map)getProperty(ctx, "previousItemState");
    }


    public Map getPreviousItemState()
    {
        return getPreviousItemState(getSession().getSessionContext());
    }


    public void setPreviousItemState(SessionContext ctx, Map value)
    {
        setProperty(ctx, "previousItemState", value);
    }


    public void setPreviousItemState(Map value)
    {
        setPreviousItemState(getSession().getSessionContext(), value);
    }


    public Date getSaveTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "saveTimestamp");
    }


    public Date getSaveTimestamp()
    {
        return getSaveTimestamp(getSession().getSessionContext());
    }


    public void setSaveTimestamp(SessionContext ctx, Date value)
    {
        setProperty(ctx, "saveTimestamp", value);
    }


    public void setSaveTimestamp(Date value)
    {
        setSaveTimestamp(getSession().getSessionContext(), value);
    }


    public Integer getSequenceNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sequenceNumber");
    }


    public Integer getSequenceNumber()
    {
        return getSequenceNumber(getSession().getSessionContext());
    }


    public int getSequenceNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getSequenceNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSequenceNumberAsPrimitive()
    {
        return getSequenceNumberAsPrimitive(getSession().getSessionContext());
    }


    protected void setSequenceNumber(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sequenceNumber' is not changeable", 0);
        }
        setProperty(ctx, "sequenceNumber", value);
    }


    protected void setSequenceNumber(Integer value)
    {
        setSequenceNumber(getSession().getSessionContext(), value);
    }


    protected void setSequenceNumber(SessionContext ctx, int value)
    {
        setSequenceNumber(ctx, Integer.valueOf(value));
    }


    protected void setSequenceNumber(int value)
    {
        setSequenceNumber(getSession().getSessionContext(), value);
    }


    public Step getStep(SessionContext ctx)
    {
        return (Step)getProperty(ctx, "step");
    }


    public Step getStep()
    {
        return getStep(getSession().getSessionContext());
    }


    protected void setStep(SessionContext ctx, Step value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'step' is not changeable", 0);
        }
        setProperty(ctx, "step", value);
    }


    protected void setStep(Step value)
    {
        setStep(getSession().getSessionContext(), value);
    }
}
