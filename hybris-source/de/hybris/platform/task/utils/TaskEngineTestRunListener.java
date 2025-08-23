package de.hybris.platform.task.utils;

import de.hybris.platform.task.TaskService;
import de.hybris.platform.testframework.runlistener.CustomRunListener;
import de.hybris.platform.util.Config;
import java.lang.annotation.Annotation;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Required;

public class TaskEngineTestRunListener extends CustomRunListener
{
    private static final Logger LOG = Logger.getLogger(TaskEngineTestRunListener.class);
    private TaskService taskService;
    private Description description;
    private boolean needsTaskService;
    private boolean taskProcessingEnabled;


    public void testRunStarted(Description description) throws Exception
    {
        this.description = description;
        this.taskProcessingEnabled = Config.getBoolean("task.engine.loadonstartup", true);
        if(hasNeedsTaskEngineAnnotation(description))
        {
            this.needsTaskService = true;
            if(!this.taskService.getEngine().isRunning())
            {
                this.taskService.getEngine().start();
            }
        }
        else if(this.taskProcessingEnabled)
        {
            startTaskEngineIfIsNotRunning(true);
        }
        else
        {
            stopTaskEngineIfIsRunning(true);
        }
    }


    public void testRunFinished(Result result) throws Exception
    {
        if(this.needsTaskService && !this.taskProcessingEnabled)
        {
            this.taskService.getEngine().stop();
        }
        else if(this.taskProcessingEnabled)
        {
            startTaskEngineIfIsNotRunning(true);
        }
        else
        {
            stopTaskEngineIfIsRunning(true);
        }
    }


    private void stopTaskEngineIfIsRunning(boolean beforeTest)
    {
        if(this.taskService.getEngine().isRunning())
        {
            LOG.warn("Stopping task engine, because it was RUNNING " + (beforeTest ? "before" : "after") + " the test: " + this.description
                            .getTestClass() + ". Tests should leave task engine in the same state as before the test.");
            this.taskService.getEngine().stop();
        }
    }


    private void startTaskEngineIfIsNotRunning(boolean beforeTest)
    {
        if(!this.taskService.getEngine().isRunning())
        {
            LOG.warn("Starting task engine, because it was NOT RUNNING " + (beforeTest ? "before" : "after") + " the test: " + this.description
                            .getTestClass() + ". Tests should leave task engine in the same state as before the test.");
            this.taskService.getEngine().start();
        }
    }


    private boolean hasNeedsTaskEngineAnnotation(Description description)
    {
        for(Annotation annotation : description.getAnnotations())
        {
            if(annotation.annotationType().equals(NeedsTaskEngine.class))
            {
                return true;
            }
        }
        return false;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }
}
