package de.hybris.platform.task;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class TaskConditionModel extends ItemModel
{
    public static final String _TYPECODE = "TaskCondition";
    public static final String _TASKCONDITIONRELATION = "TaskConditionRelation";
    public static final String UNIQUEID = "uniqueID";
    public static final String EXPIRATIONDATE = "expirationDate";
    public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
    public static final String PROCESSEDDATE = "processedDate";
    public static final String FULFILLED = "fulfilled";
    public static final String CONSUMED = "consumed";
    public static final String CHOICE = "choice";
    public static final String COUNTER = "counter";
    public static final String TASK = "task";


    public TaskConditionModel()
    {
    }


    public TaskConditionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaskConditionModel(String _uniqueID)
    {
        setUniqueID(_uniqueID);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TaskConditionModel(ItemModel _owner, String _uniqueID)
    {
        setOwner(_owner);
        setUniqueID(_uniqueID);
    }


    @Accessor(qualifier = "choice", type = Accessor.Type.GETTER)
    public String getChoice()
    {
        return (String)getPersistenceContext().getPropertyValue("choice");
    }


    @Accessor(qualifier = "consumed", type = Accessor.Type.GETTER)
    public Boolean getConsumed()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("consumed");
    }


    @Accessor(qualifier = "counter", type = Accessor.Type.GETTER)
    public Integer getCounter()
    {
        return (Integer)getPersistenceContext().getPropertyValue("counter");
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


    @Accessor(qualifier = "fulfilled", type = Accessor.Type.GETTER)
    public Boolean getFulfilled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("fulfilled");
    }


    @Accessor(qualifier = "processedDate", type = Accessor.Type.GETTER)
    public Date getProcessedDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("processedDate");
    }


    @Accessor(qualifier = "task", type = Accessor.Type.GETTER)
    public TaskModel getTask()
    {
        return (TaskModel)getPersistenceContext().getPropertyValue("task");
    }


    @Accessor(qualifier = "uniqueID", type = Accessor.Type.GETTER)
    public String getUniqueID()
    {
        return (String)getPersistenceContext().getPropertyValue("uniqueID");
    }


    @Accessor(qualifier = "choice", type = Accessor.Type.SETTER)
    public void setChoice(String value)
    {
        getPersistenceContext().setPropertyValue("choice", value);
    }


    @Accessor(qualifier = "consumed", type = Accessor.Type.SETTER)
    public void setConsumed(Boolean value)
    {
        getPersistenceContext().setPropertyValue("consumed", value);
    }


    @Accessor(qualifier = "counter", type = Accessor.Type.SETTER)
    public void setCounter(Integer value)
    {
        getPersistenceContext().setPropertyValue("counter", value);
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


    @Accessor(qualifier = "fulfilled", type = Accessor.Type.SETTER)
    public void setFulfilled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("fulfilled", value);
    }


    @Accessor(qualifier = "processedDate", type = Accessor.Type.SETTER)
    public void setProcessedDate(Date value)
    {
        getPersistenceContext().setPropertyValue("processedDate", value);
    }


    @Accessor(qualifier = "task", type = Accessor.Type.SETTER)
    public void setTask(TaskModel value)
    {
        getPersistenceContext().setPropertyValue("task", value);
    }


    @Accessor(qualifier = "uniqueID", type = Accessor.Type.SETTER)
    public void setUniqueID(String value)
    {
        getPersistenceContext().setPropertyValue("uniqueID", value);
    }
}
