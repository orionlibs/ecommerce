package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class DistributedProcessTransitionTaskModel extends TaskModel
{
    public static final String _TYPECODE = "DistributedProcessTransitionTask";
    public static final String STATE = "state";


    public DistributedProcessTransitionTaskModel()
    {
    }


    public DistributedProcessTransitionTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessTransitionTaskModel(String _runnerBean, DistributedProcessState _state)
    {
        setRunnerBean(_runnerBean);
        setState(_state);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessTransitionTaskModel(ItemModel _owner, String _runnerBean, DistributedProcessState _state)
    {
        setOwner(_owner);
        setRunnerBean(_runnerBean);
        setState(_state);
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
    public DistributedProcessModel getContextItem()
    {
        return (DistributedProcessModel)super.getContextItem();
    }


    @Accessor(qualifier = "state", type = Accessor.Type.GETTER)
    public DistributedProcessState getState()
    {
        return (DistributedProcessState)getPersistenceContext().getPropertyValue("state");
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
    public void setContextItem(ItemModel value)
    {
        if(value == null || value instanceof DistributedProcessModel)
        {
            super.setContextItem(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.processing.model.DistributedProcessModel");
        }
    }


    @Accessor(qualifier = "state", type = Accessor.Type.SETTER)
    public void setState(DistributedProcessState value)
    {
        getPersistenceContext().setPropertyValue("state", value);
    }
}
