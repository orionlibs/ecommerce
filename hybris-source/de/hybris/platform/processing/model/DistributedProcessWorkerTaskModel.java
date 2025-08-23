package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class DistributedProcessWorkerTaskModel extends TaskModel
{
    public static final String _TYPECODE = "DistributedProcessWorkerTask";
    public static final String CONDITIONID = "conditionId";


    public DistributedProcessWorkerTaskModel()
    {
    }


    public DistributedProcessWorkerTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessWorkerTaskModel(String _conditionId, String _runnerBean)
    {
        setConditionId(_conditionId);
        setRunnerBean(_runnerBean);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessWorkerTaskModel(String _conditionId, ItemModel _owner, String _runnerBean)
    {
        setConditionId(_conditionId);
        setOwner(_owner);
        setRunnerBean(_runnerBean);
    }


    @Accessor(qualifier = "conditionId", type = Accessor.Type.GETTER)
    public String getConditionId()
    {
        return (String)getPersistenceContext().getPropertyValue("conditionId");
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
    public BatchModel getContextItem()
    {
        return (BatchModel)super.getContextItem();
    }


    @Accessor(qualifier = "conditionId", type = Accessor.Type.SETTER)
    public void setConditionId(String value)
    {
        getPersistenceContext().setPropertyValue("conditionId", value);
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
    public void setContextItem(ItemModel value)
    {
        if(value == null || value instanceof BatchModel)
        {
            super.setContextItem(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.processing.model.BatchModel");
        }
    }
}
