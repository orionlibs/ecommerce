package de.hybris.platform.task;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Task extends GeneratedTask
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("runningOnClusterNode", Item.AttributeMode.INITIAL);
        allAttributes.put("runningOnClusterNode", Integer.valueOf(-1));
        allAttributes.setAttributeMode("executionTimeMillis", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("nodeId", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("retry", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("runnerBean", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("failed", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("expirationTimeMillis", Item.AttributeMode.INITIAL);
        if(allAttributes.get("failed") == null)
        {
            allAttributes.put("failed", Boolean.FALSE);
        }
        long executionTime = (allAttributes.get("executionTimeMillis") != null) ? ((Long)allAttributes.get("executionTimeMillis")).longValue() : System.currentTimeMillis();
        allAttributes.put("executionTimeMillis", Long.valueOf(executionTime));
        allAttributes.put("expirationTimeMillis", (allAttributes.get("expirationTimeMillis") != null) ?
                        allAttributes.get("expirationTimeMillis") : null);
        if(allAttributes.get("retry") == null)
        {
            allAttributes.put("retry", Integer.valueOf(0));
        }
        Task ret = (Task)super.createItem(ctx, type, allAttributes);
        return (Item)ret;
    }


    public Date getExpirationDate()
    {
        return getExpirationDate(getSession().getSessionContext());
    }


    public Date getExpirationDate(SessionContext ctx)
    {
        Long milliSeconds = getExpirationTimeMillis(ctx);
        return (milliSeconds != null) ? new Date(milliSeconds.longValue()) : null;
    }


    public void setExpirationDate(Date value)
    {
        setExpirationDate(getSession().getSessionContext(), value);
    }


    public void setExpirationDate(SessionContext ctx, Date value)
    {
        setExpirationTimeMillis(ctx, (value != null) ? Long.valueOf(value.getTime()) : null);
    }


    public Date getExecutionDate()
    {
        return getExecutionDate(getSession().getSessionContext());
    }


    public Date getExecutionDate(SessionContext ctx)
    {
        Long milliSeconds = getExecutionTimeMillis(ctx);
        return (milliSeconds != null) ? new Date(milliSeconds.longValue()) : null;
    }


    public void setExecutionDate(Date value)
    {
        setExecutionDate(getSession().getSessionContext(), value);
    }


    public void setExecutionDate(SessionContext ctx, Date value)
    {
        setExecutionTimeMillis(ctx, (value != null) ? Long.valueOf(value.getTime()) : null);
    }


    @ForceJALO(reason = "something else")
    public Set<TaskCondition> getConditions(SessionContext ctx)
    {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("disableCache", Boolean.valueOf(true));
        SessionContext.SessionContextAttributeSetter attributeSetter = SessionContext.setSessionContextAttributesLocally(ctx, sessionAttributes);
        try
        {
            Set<TaskCondition> set = (Set)CONDITIONSHANDLER.getValues(ctx, (Item)this);
            if(attributeSetter != null)
            {
                attributeSetter.close();
            }
            return set;
        }
        catch(Throwable throwable)
        {
            if(attributeSetter != null)
            {
                try
                {
                    attributeSetter.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    @ForceJALO(reason = "something else")
    public void setConditions(SessionContext ctx, Set<TaskCondition> value)
    {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("disableCache", Boolean.valueOf(true));
        SessionContext.SessionContextAttributeSetter attributeSetter = SessionContext.setSessionContextAttributesLocally(ctx, sessionAttributes);
        try
        {
            CONDITIONSHANDLER.setValues(ctx, (Item)this, value);
            if(attributeSetter != null)
            {
                attributeSetter.close();
            }
        }
        catch(Throwable throwable)
        {
            if(attributeSetter != null)
            {
                try
                {
                    attributeSetter.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}
