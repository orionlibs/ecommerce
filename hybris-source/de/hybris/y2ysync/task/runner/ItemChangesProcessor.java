package de.hybris.y2ysync.task.runner;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.logging.Y2YSyncLoggingCtxFactory;
import de.hybris.y2ysync.task.runner.internal.ExportScriptCreator;
import de.hybris.y2ysync.task.runner.internal.ImportScript;
import de.hybris.y2ysync.task.runner.internal.ImportScriptCreator;
import de.hybris.y2ysync.task.runner.internal.ProcessChangesTask;
import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ItemChangesProcessor implements TaskRunner<TaskModel>
{
    private ModelService modelService;
    private ExportService exportService;
    private MediaService mediaService;
    private ChangeDetectionService changeDetectionService;
    private TypeService typeService;
    private UserService userService;
    private Y2YSyncDAO y2YSyncDAO;
    private Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory;
    private static final Logger LOG = Logger.getLogger(ItemChangesProcessor.class);


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        this.userService.setCurrentUser((UserModel)this.userService.getAdminUser());
        ProcessChangesTask processChangesTask = extractTaskFromModel(taskService, task);
        try
        {
            processChangesTask.execute();
        }
        catch(Exception e)
        {
            LOG.error("Error occured during processing task: " + e);
            throw createError("Processing changes has failed.", e);
        }
    }


    public boolean isLoggingSupported()
    {
        return true;
    }


    public TaskLoggingCtx initLoggingCtx(TaskModel task)
    {
        return this.y2YSyncLoggingCtxFactory.createY2YSyncTaskLogger(task);
    }


    public void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
        this.y2YSyncLoggingCtxFactory.finish(taskCtx);
    }


    private ProcessChangesTask extractTaskFromModel(TaskService taskService, TaskModel task)
    {
        TaskContext context = extractContextFromModel(task);
        Collection<ImportScript> importScripts = getImportScriptCreator(context).createImportScripts();
        return new ProcessChangesTask(this.modelService, this.mediaService, this.changeDetectionService, taskService, this.typeService, this.y2YSyncDAO, context, importScripts);
    }


    private TaskContext extractContextFromModel(TaskModel task)
    {
        Map<String, Object> context;
        Object ctxObject = task.getContext();
        if(ctxObject == null)
        {
            throw createError("Task doesn't have a context");
        }
        try
        {
            context = (Map<String, Object>)ctxObject;
        }
        catch(ClassCastException e)
        {
            throw createError("Couldn't cast a context to an appropriate type", e);
        }
        return new TaskContext(this.modelService, this.mediaService, context);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setExportService(ExportService exportService)
    {
        this.exportService = exportService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setChangeDetectionService(ChangeDetectionService changeDetectionService)
    {
        this.changeDetectionService = changeDetectionService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setY2YSyncDAO(Y2YSyncDAO y2YSyncDAO)
    {
        this.y2YSyncDAO = y2YSyncDAO;
    }


    private ImportScriptCreator getImportScriptCreator(TaskContext context)
    {
        ExportScriptCreator exportScriptCreator = new ExportScriptCreator(context.getImpExHeader(), context.getTypeCode(), context.getChanges());
        return new ImportScriptCreator(this.modelService, this.exportService, exportScriptCreator, this.userService);
    }


    private static RuntimeException createError(String message)
    {
        return createError(message, null);
    }


    private static RuntimeException createError(String message, Throwable cause)
    {
        return new RuntimeException(message, cause);
    }


    @Required
    public void setY2YSyncLoggingCtxFactory(Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory)
    {
        this.y2YSyncLoggingCtxFactory = y2YSyncLoggingCtxFactory;
    }
}
