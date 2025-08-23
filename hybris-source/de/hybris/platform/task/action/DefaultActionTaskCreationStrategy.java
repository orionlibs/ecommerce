package de.hybris.platform.task.action;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.task.TaskModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultActionTaskCreationStrategy implements ActionTaskCreationStrategy, ApplicationContextAware
{
    private static final String ADAPTER_TASK_RUNNER = "actionPerformableTaskRunner";
    private ApplicationContext appCtx;


    public TaskModel createTaskForAction(AbstractActionModel action, Object argument)
    {
        TaskModel task = (TaskModel)getModelService().create(TaskModel.class);
        String target = action.getTarget();
        if(isTaskRunner(target))
        {
            task.setRunnerBean(target);
        }
        else
        {
            task.setRunnerBean("actionPerformableTaskRunner");
        }
        task.setContextItem((ItemModel)action);
        task.setContext(argument);
        return task;
    }


    private ModelService getModelService()
    {
        return (ModelService)this.appCtx.getBean("modelService", ModelService.class);
    }


    protected boolean isTaskRunner(String target)
    {
        try
        {
            return this.appCtx.getBean(target) instanceof de.hybris.platform.task.TaskRunner;
        }
        catch(BeansException e)
        {
            throw new InvalidActionException("error getting bean for target '" + target + "'", e, ActionType.TASK, target);
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.appCtx = applicationContext;
    }
}
