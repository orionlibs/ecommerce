package de.hybris.platform.task;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedTask extends GenericItem
{
    public static final String RUNNERBEAN = "runnerBean";
    public static final String EXECUTIONTIMEMILLIS = "executionTimeMillis";
    public static final String EXECUTIONHOURMILLIS = "executionHourMillis";
    public static final String FAILED = "failed";
    public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
    public static final String CONTEXT = "context";
    public static final String CONTEXTITEM = "contextItem";
    public static final String NODEID = "nodeId";
    public static final String NODEGROUP = "nodeGroup";
    public static final String RETRY = "retry";
    public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
    public static final String CONDITIONS = "conditions";
    protected static final OneToManyHandler<TaskCondition> CONDITIONSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.TASKCONDITION, true, "task", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("runnerBean", Item.AttributeMode.INITIAL);
        tmp.put("executionTimeMillis", Item.AttributeMode.INITIAL);
        tmp.put("executionHourMillis", Item.AttributeMode.INITIAL);
        tmp.put("failed", Item.AttributeMode.INITIAL);
        tmp.put("expirationTimeMillis", Item.AttributeMode.INITIAL);
        tmp.put("context", Item.AttributeMode.INITIAL);
        tmp.put("contextItem", Item.AttributeMode.INITIAL);
        tmp.put("nodeId", Item.AttributeMode.INITIAL);
        tmp.put("nodeGroup", Item.AttributeMode.INITIAL);
        tmp.put("retry", Item.AttributeMode.INITIAL);
        tmp.put("runningOnClusterNode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<TaskCondition> getConditions(SessionContext ctx)
    {
        return (Set<TaskCondition>)CONDITIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<TaskCondition> getConditions()
    {
        return getConditions(getSession().getSessionContext());
    }


    public void setConditions(SessionContext ctx, Set<TaskCondition> value)
    {
        CONDITIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConditions(Set<TaskCondition> value)
    {
        setConditions(getSession().getSessionContext(), value);
    }


    public void addToConditions(SessionContext ctx, TaskCondition value)
    {
        CONDITIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConditions(TaskCondition value)
    {
        addToConditions(getSession().getSessionContext(), value);
    }


    public void removeFromConditions(SessionContext ctx, TaskCondition value)
    {
        CONDITIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConditions(TaskCondition value)
    {
        removeFromConditions(getSession().getSessionContext(), value);
    }


    public Object getContext(SessionContext ctx)
    {
        return getProperty(ctx, "context");
    }


    public Object getContext()
    {
        return getContext(getSession().getSessionContext());
    }


    public void setContext(SessionContext ctx, Object value)
    {
        setProperty(ctx, "context", value);
    }


    public void setContext(Object value)
    {
        setContext(getSession().getSessionContext(), value);
    }


    public Item getContextItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "contextItem");
    }


    public Item getContextItem()
    {
        return getContextItem(getSession().getSessionContext());
    }


    public void setContextItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "contextItem", value);
    }


    public void setContextItem(Item value)
    {
        setContextItem(getSession().getSessionContext(), value);
    }


    public void setExecutionHourMillis(SessionContext ctx, Long value)
    {
        setProperty(ctx, "executionHourMillis", value);
    }


    public void setExecutionHourMillis(Long value)
    {
        setExecutionHourMillis(getSession().getSessionContext(), value);
    }


    public void setExecutionHourMillis(SessionContext ctx, long value)
    {
        setExecutionHourMillis(ctx, Long.valueOf(value));
    }


    public void setExecutionHourMillis(long value)
    {
        setExecutionHourMillis(getSession().getSessionContext(), value);
    }


    public Long getExecutionTimeMillis(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "executionTimeMillis");
    }


    public Long getExecutionTimeMillis()
    {
        return getExecutionTimeMillis(getSession().getSessionContext());
    }


    public long getExecutionTimeMillisAsPrimitive(SessionContext ctx)
    {
        Long value = getExecutionTimeMillis(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getExecutionTimeMillisAsPrimitive()
    {
        return getExecutionTimeMillisAsPrimitive(getSession().getSessionContext());
    }


    public void setExecutionTimeMillis(SessionContext ctx, Long value)
    {
        setProperty(ctx, "executionTimeMillis", value);
    }


    public void setExecutionTimeMillis(Long value)
    {
        setExecutionTimeMillis(getSession().getSessionContext(), value);
    }


    public void setExecutionTimeMillis(SessionContext ctx, long value)
    {
        setExecutionTimeMillis(ctx, Long.valueOf(value));
    }


    public void setExecutionTimeMillis(long value)
    {
        setExecutionTimeMillis(getSession().getSessionContext(), value);
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


    public Boolean isFailed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "failed");
    }


    public Boolean isFailed()
    {
        return isFailed(getSession().getSessionContext());
    }


    public boolean isFailedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFailed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFailedAsPrimitive()
    {
        return isFailedAsPrimitive(getSession().getSessionContext());
    }


    public void setFailed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "failed", value);
    }


    public void setFailed(Boolean value)
    {
        setFailed(getSession().getSessionContext(), value);
    }


    public void setFailed(SessionContext ctx, boolean value)
    {
        setFailed(ctx, Boolean.valueOf(value));
    }


    public void setFailed(boolean value)
    {
        setFailed(getSession().getSessionContext(), value);
    }


    public String getNodeGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "nodeGroup");
    }


    public String getNodeGroup()
    {
        return getNodeGroup(getSession().getSessionContext());
    }


    public void setNodeGroup(SessionContext ctx, String value)
    {
        setProperty(ctx, "nodeGroup", value);
    }


    public void setNodeGroup(String value)
    {
        setNodeGroup(getSession().getSessionContext(), value);
    }


    public Integer getNodeId(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "nodeId");
    }


    public Integer getNodeId()
    {
        return getNodeId(getSession().getSessionContext());
    }


    public int getNodeIdAsPrimitive(SessionContext ctx)
    {
        Integer value = getNodeId(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNodeIdAsPrimitive()
    {
        return getNodeIdAsPrimitive(getSession().getSessionContext());
    }


    public void setNodeId(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "nodeId", value);
    }


    public void setNodeId(Integer value)
    {
        setNodeId(getSession().getSessionContext(), value);
    }


    public void setNodeId(SessionContext ctx, int value)
    {
        setNodeId(ctx, Integer.valueOf(value));
    }


    public void setNodeId(int value)
    {
        setNodeId(getSession().getSessionContext(), value);
    }


    public Integer getRetry(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "retry");
    }


    public Integer getRetry()
    {
        return getRetry(getSession().getSessionContext());
    }


    public int getRetryAsPrimitive(SessionContext ctx)
    {
        Integer value = getRetry(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRetryAsPrimitive()
    {
        return getRetryAsPrimitive(getSession().getSessionContext());
    }


    public void setRetry(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "retry", value);
    }


    public void setRetry(Integer value)
    {
        setRetry(getSession().getSessionContext(), value);
    }


    public void setRetry(SessionContext ctx, int value)
    {
        setRetry(ctx, Integer.valueOf(value));
    }


    public void setRetry(int value)
    {
        setRetry(getSession().getSessionContext(), value);
    }


    public String getRunnerBean(SessionContext ctx)
    {
        return (String)getProperty(ctx, "runnerBean");
    }


    public String getRunnerBean()
    {
        return getRunnerBean(getSession().getSessionContext());
    }


    public void setRunnerBean(SessionContext ctx, String value)
    {
        setProperty(ctx, "runnerBean", value);
    }


    public void setRunnerBean(String value)
    {
        setRunnerBean(getSession().getSessionContext(), value);
    }


    public Integer getRunningOnClusterNode(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "runningOnClusterNode");
    }


    public Integer getRunningOnClusterNode()
    {
        return getRunningOnClusterNode(getSession().getSessionContext());
    }


    public int getRunningOnClusterNodeAsPrimitive(SessionContext ctx)
    {
        Integer value = getRunningOnClusterNode(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRunningOnClusterNodeAsPrimitive()
    {
        return getRunningOnClusterNodeAsPrimitive(getSession().getSessionContext());
    }


    public void setRunningOnClusterNode(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "runningOnClusterNode", value);
    }


    public void setRunningOnClusterNode(Integer value)
    {
        setRunningOnClusterNode(getSession().getSessionContext(), value);
    }


    public void setRunningOnClusterNode(SessionContext ctx, int value)
    {
        setRunningOnClusterNode(ctx, Integer.valueOf(value));
    }


    public void setRunningOnClusterNode(int value)
    {
        setRunningOnClusterNode(getSession().getSessionContext(), value);
    }
}
