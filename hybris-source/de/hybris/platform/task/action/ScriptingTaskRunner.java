package de.hybris.platform.task.action;

import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.model.ScriptingTaskModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ScriptingTaskRunner implements TaskRunner<ScriptingTaskModel>
{
    private static final Logger LOG = Logger.getLogger(ScriptingTaskRunner.class.getName());
    private ScriptingLanguagesService scriptingLanguagesService;


    public void run(TaskService taskService, ScriptingTaskModel task) throws RetryLaterException
    {
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByURI(task.getScriptURI());
        TaskRunner<ScriptingTaskModel> asInterface = (TaskRunner<ScriptingTaskModel>)executable.getAsInterface(TaskRunner.class);
        if(asInterface == null)
        {
            LOG.error("Cannot execute script: " + task.getScriptURI());
            LOG.error("The requested interface could not be found, probably the script contains errors");
            return;
        }
        LOG.info("### Starting executing script : " + task.getScriptURI() + " ###");
        asInterface.run(taskService, (TaskModel)task);
        LOG.info("### Finished executing script: " + task.getScriptURI() + " ###");
    }


    public void handleError(TaskService taskService, ScriptingTaskModel task, Throwable error)
    {
        LOG.info("###error handling for task: " + task.getScriptURI() + "###");
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByURI(task.getScriptURI());
        TaskRunner<ScriptingTaskModel> asInterface = (TaskRunner<ScriptingTaskModel>)executable.getAsInterface(TaskRunner.class);
        asInterface.handleError(taskService, (TaskModel)task, error);
    }


    @Required
    public void setScriptingLanguagesService(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = scriptingLanguagesService;
    }
}
