package de.hybris.platform.task.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class TriggerTaskModel extends TaskModel
{
    public static final String _TYPECODE = "TriggerTask";
    public static final String TRIGGER = "trigger";


    public TriggerTaskModel()
    {
    }


    public TriggerTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TriggerTaskModel(String _runnerBean, TriggerModel _trigger)
    {
        setRunnerBean(_runnerBean);
        setTrigger(_trigger);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TriggerTaskModel(ItemModel _owner, String _runnerBean, TriggerModel _trigger)
    {
        setOwner(_owner);
        setRunnerBean(_runnerBean);
        setTrigger(_trigger);
    }


    @Accessor(qualifier = "trigger", type = Accessor.Type.GETTER)
    public TriggerModel getTrigger()
    {
        return (TriggerModel)getPersistenceContext().getPropertyValue("trigger");
    }


    @Accessor(qualifier = "trigger", type = Accessor.Type.SETTER)
    public void setTrigger(TriggerModel value)
    {
        getPersistenceContext().setPropertyValue("trigger", value);
    }
}
