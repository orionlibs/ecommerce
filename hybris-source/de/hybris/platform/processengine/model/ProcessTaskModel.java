package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class ProcessTaskModel extends TaskModel
{
    public static final String _TYPECODE = "ProcessTask";
    public static final String _PROCESS2TASKRELATION = "Process2TaskRelation";
    public static final String ACTION = "action";
    public static final String PROCESS = "process";


    public ProcessTaskModel()
    {
    }


    public ProcessTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskModel(String _action, BusinessProcessModel _process, String _runnerBean)
    {
        setAction(_action);
        setProcess(_process);
        setRunnerBean(_runnerBean);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskModel(String _action, ItemModel _owner, BusinessProcessModel _process, String _runnerBean)
    {
        setAction(_action);
        setOwner(_owner);
        setProcess(_process);
        setRunnerBean(_runnerBean);
    }


    @Accessor(qualifier = "action", type = Accessor.Type.GETTER)
    public String getAction()
    {
        return (String)getPersistenceContext().getPropertyValue("action");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public BusinessProcessModel getProcess()
    {
        return (BusinessProcessModel)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "action", type = Accessor.Type.SETTER)
    public void setAction(String value)
    {
        getPersistenceContext().setPropertyValue("action", value);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(BusinessProcessModel value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }
}
