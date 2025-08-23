package de.hybris.platform.task.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class ScriptingTaskModel extends TaskModel
{
    public static final String _TYPECODE = "ScriptingTask";
    public static final String SCRIPTURI = "scriptURI";


    public ScriptingTaskModel()
    {
    }


    public ScriptingTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptingTaskModel(String _runnerBean, String _scriptURI)
    {
        setRunnerBean(_runnerBean);
        setScriptURI(_scriptURI);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptingTaskModel(ItemModel _owner, String _runnerBean, String _scriptURI)
    {
        setOwner(_owner);
        setRunnerBean(_runnerBean);
        setScriptURI(_scriptURI);
    }


    @Accessor(qualifier = "scriptURI", type = Accessor.Type.GETTER)
    public String getScriptURI()
    {
        return (String)getPersistenceContext().getPropertyValue("scriptURI");
    }


    @Accessor(qualifier = "scriptURI", type = Accessor.Type.SETTER)
    public void setScriptURI(String value)
    {
        getPersistenceContext().setPropertyValue("scriptURI", value);
    }
}
