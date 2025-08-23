package de.hybris.deltadetection.tasks;

import com.google.common.base.Preconditions;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class RemoveOrphanedVersionMarkersTaskRunner implements TaskRunner<TaskModel>
{
    private ChangeDetectionService changeDetectionService;


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        String streamId = getStreamId(task);
        if(StringUtils.isEmpty(streamId))
        {
            return;
        }
        this.changeDetectionService.deleteItemVersionMarkersForStream(streamId);
    }


    private String getStreamId(TaskModel task)
    {
        Object ctx = task.getContext();
        Preconditions.checkState(ctx instanceof String, "context must be instance of String");
        return (String)ctx;
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    @Required
    public void setChangeDetectionService(ChangeDetectionService changeDetectionService)
    {
        this.changeDetectionService = changeDetectionService;
    }
}
