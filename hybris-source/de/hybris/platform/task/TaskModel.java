package de.hybris.platform.task;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

public class TaskModel extends ItemModel
{
    public static final String _TYPECODE = "Task";
    public static final String RUNNERBEAN = "runnerBean";
    public static final String EXECUTIONDATE = "executionDate";
    public static final String EXECUTIONTIMEMILLIS = "executionTimeMillis";
    public static final String EXECUTIONHOURMILLIS = "executionHourMillis";
    public static final String FAILED = "failed";
    public static final String EXPIRATIONDATE = "expirationDate";
    public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
    public static final String CONTEXT = "context";
    public static final String CONTEXTITEM = "contextItem";
    public static final String NODEID = "nodeId";
    public static final String NODEGROUP = "nodeGroup";
    public static final String RETRY = "retry";
    public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
    public static final String CONDITIONS = "conditions";


    public TaskModel()
    {
    }


    public TaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaskModel(String _runnerBean)
    {
        setRunnerBean(_runnerBean);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaskModel(ItemModel _owner, String _runnerBean)
    {
        setOwner(_owner);
        setRunnerBean(_runnerBean);
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
    public Set<TaskConditionModel> getConditions()
    {
        return (Set<TaskConditionModel>)getPersistenceContext().getPropertyValue("conditions");
    }


    @Accessor(qualifier = "context", type = Accessor.Type.GETTER)
    public Object getContext()
    {
        return getPersistenceContext().getPropertyValue("context");
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
    public ItemModel getContextItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("contextItem");
    }


    @Accessor(qualifier = "executionDate", type = Accessor.Type.GETTER)
    public Date getExecutionDate()
    {
        return (Date)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "executionDate");
    }


    @Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.GETTER)
    public Long getExecutionTimeMillis()
    {
        return (Long)getPersistenceContext().getPropertyValue("executionTimeMillis");
    }


    @Accessor(qualifier = "expirationDate", type = Accessor.Type.GETTER)
    public Date getExpirationDate()
    {
        return (Date)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "expirationDate");
    }


    @Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.GETTER)
    public Long getExpirationTimeMillis()
    {
        return (Long)getPersistenceContext().getPropertyValue("expirationTimeMillis");
    }


    @Accessor(qualifier = "failed", type = Accessor.Type.GETTER)
    public Boolean getFailed()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("failed");
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
    public String getNodeGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("nodeGroup");
    }


    @Accessor(qualifier = "nodeId", type = Accessor.Type.GETTER)
    public Integer getNodeId()
    {
        return (Integer)getPersistenceContext().getPropertyValue("nodeId");
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
    public Integer getRetry()
    {
        return (Integer)getPersistenceContext().getPropertyValue("retry");
    }


    @Accessor(qualifier = "runnerBean", type = Accessor.Type.GETTER)
    public String getRunnerBean()
    {
        return (String)getPersistenceContext().getPropertyValue("runnerBean");
    }


    @Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.GETTER)
    public Integer getRunningOnClusterNode()
    {
        return (Integer)getPersistenceContext().getPropertyValue("runningOnClusterNode");
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
    public void setConditions(Set<TaskConditionModel> value)
    {
        getPersistenceContext().setPropertyValue("conditions", value);
    }


    @Accessor(qualifier = "context", type = Accessor.Type.SETTER)
    public void setContext(Object value)
    {
        getPersistenceContext().setPropertyValue("context", value);
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
    public void setContextItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("contextItem", value);
    }


    @Accessor(qualifier = "executionDate", type = Accessor.Type.SETTER)
    public void setExecutionDate(Date value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "executionDate", value);
    }


    @Accessor(qualifier = "executionHourMillis", type = Accessor.Type.SETTER)
    public void setExecutionHourMillis(Long value)
    {
        getPersistenceContext().setPropertyValue("executionHourMillis", value);
    }


    @Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.SETTER)
    public void setExecutionTimeMillis(Long value)
    {
        getPersistenceContext().setPropertyValue("executionTimeMillis", value);
    }


    @Accessor(qualifier = "expirationDate", type = Accessor.Type.SETTER)
    public void setExpirationDate(Date value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "expirationDate", value);
    }


    @Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.SETTER)
    public void setExpirationTimeMillis(Long value)
    {
        getPersistenceContext().setPropertyValue("expirationTimeMillis", value);
    }


    @Accessor(qualifier = "failed", type = Accessor.Type.SETTER)
    public void setFailed(Boolean value)
    {
        getPersistenceContext().setPropertyValue("failed", value);
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
    public void setNodeGroup(String value)
    {
        getPersistenceContext().setPropertyValue("nodeGroup", value);
    }


    @Accessor(qualifier = "nodeId", type = Accessor.Type.SETTER)
    public void setNodeId(Integer value)
    {
        getPersistenceContext().setPropertyValue("nodeId", value);
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
    public void setRetry(Integer value)
    {
        getPersistenceContext().setPropertyValue("retry", value);
    }


    @Accessor(qualifier = "runnerBean", type = Accessor.Type.SETTER)
    public void setRunnerBean(String value)
    {
        getPersistenceContext().setPropertyValue("runnerBean", value);
    }


    @Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.SETTER)
    public void setRunningOnClusterNode(Integer value)
    {
        getPersistenceContext().setPropertyValue("runningOnClusterNode", value);
    }
}
