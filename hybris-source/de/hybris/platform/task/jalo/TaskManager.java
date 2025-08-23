package de.hybris.platform.task.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.cronjob.jalo.GeneratedCronJobManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.JspContext;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

@Deprecated(since = "ages", forRemoval = false)
public class TaskManager extends GeneratedCronJobManager
{
    private static final Logger LOG = Logger.getLogger(TaskManager.class.getName());

    static
    {
        Registry.registerTenantListener((TenantListener)new Object());
    }

    @Deprecated(since = "ages", forRemoval = false)
    public static TaskManager getInstance()
    {
        return (TaskManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("processing");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static TaskManager getInstance(Tenant t)
    {
        return (TaskManager)t.getJaloConnection().getExtensionManager().getExtension("processing");
    }


    public TaskManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of TaskmanagerManager called.");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected TaskEngine getTaskEngine()
    {
        ApplicationContext applicationContext = Registry.getCoreApplicationContext();
        TaskService taskService = null;
        try
        {
            taskService = (TaskService)applicationContext.getBean("taskService");
        }
        catch(NoSuchBeanDefinitionException e)
        {
            LOG.warn("Couldn't find bean + taskService. ApplicationContext already/still down?");
        }
        if(taskService != null)
        {
            return taskService.getEngine();
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void startEngine()
    {
        TaskEngine engine = getTaskEngine();
        if(engine != null && engine.isAllowedToStart())
        {
            engine.start();
        }
        else
        {
            LOG.error("cannot start task engine since task service does not provide engine handle");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void stopEngine()
    {
        TaskEngine engine = getTaskEngine();
        if(engine != null)
        {
            engine.stop();
        }
        else
        {
            LOG.error("cannot stop task engine since task service does not provide engine handle");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void afterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        if(createdItem instanceof Task)
        {
            Date executeAt = ((Task)createdItem).getExecutionDate();
            if(executeAt == null)
            {
                LOG.debug("Skipping repoll event. Execution date not set.");
            }
            else if(executeAt.getTime() > System.currentTimeMillis())
            {
                LOG.debug("Skipping repoll event. Execution date in future.");
            }
            else
            {
                TaskEngine engine = getTaskEngine();
                if(engine != null)
                {
                    Task task = (Task)createdItem;
                    engine.triggerRepoll(task.getNodeId(), task.getNodeGroup());
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void notifyInitializationEnd(Map<String, String> params, JspContext ctx) throws Exception
    {
        startEngine();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void notifyInitializationStart(Map<String, String> params, JspContext ctx) throws Exception
    {
        stopEngine();
    }
}
