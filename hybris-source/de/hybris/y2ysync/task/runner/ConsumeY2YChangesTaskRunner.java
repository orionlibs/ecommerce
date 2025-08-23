package de.hybris.y2ysync.task.runner;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.y2ysync.model.media.ConsumeMarkerMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.logging.Y2YSyncLoggingCtxFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.SerializationUtils;

public class ConsumeY2YChangesTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(ConsumeY2YChangesTaskRunner.class);
    public static final String EXECUTION_ID_CTX = "syncExecutionID";
    private Y2YSyncDAO y2ySyncDAO;
    private MediaService mediaService;
    private ChangeDetectionService changeDetectionService;
    private ModelService modelService;
    private Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory;


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        String executionId = extractExecutionIdFromModel(task);
        LOG.info("Began y2ysync changes consumption for execution id: {}", executionId);
        List<ConsumeMarkerMediaModel> mediasToConsume = this.y2ySyncDAO.findConsumeMarkerMedias(executionId);
        List<ItemChangeDTO> itemChangeDTOs = deserializeMedias(mediasToConsume);
        this.changeDetectionService.consumeChanges(itemChangeDTOs);
        removeConsumedMedias(mediasToConsume);
        LOG.info("Finished y2ysync changes consumption for execution id: {}", executionId);
    }


    private List<ItemChangeDTO> deserializeMedias(List<ConsumeMarkerMediaModel> mediasToConsume)
    {
        return (List<ItemChangeDTO>)mediasToConsume.stream().flatMap(i -> deserializeItemChangeDTOs(i).stream())
                        .collect(Collectors.toList());
    }


    private List<ItemChangeDTO> deserializeItemChangeDTOs(ConsumeMarkerMediaModel media)
    {
        byte[] data = this.mediaService.getDataFromMedia((MediaModel)media);
        return (List<ItemChangeDTO>)SerializationUtils.deserialize(data);
    }


    private void removeConsumedMedias(List<ConsumeMarkerMediaModel> medias)
    {
        Objects.requireNonNull(this.modelService);
        medias.forEach(this.modelService::remove);
    }


    public TaskLoggingCtx initLoggingCtx(TaskModel task)
    {
        return this.y2YSyncLoggingCtxFactory.createY2YSyncTaskLogger(task);
    }


    public void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
        this.y2YSyncLoggingCtxFactory.finish(taskCtx);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        LOG.error("Error happened when consuming item version markers");
        throw new RuntimeException(error);
    }


    private String extractExecutionIdFromModel(TaskModel task)
    {
        Object ctxObject = task.getContext();
        if(ctxObject == null)
        {
            throw new RuntimeException("Task doesn't have a context");
        }
        try
        {
            Map<String, String> context = (Map<String, String>)ctxObject;
            return Objects.<String>requireNonNull(context.get("syncExecutionID"));
        }
        catch(ClassCastException e)
        {
            throw new RuntimeException("Task doesn't have a context");
        }
    }


    @Required
    public void setY2ySyncDAO(Y2YSyncDAO y2ySyncDAO)
    {
        this.y2ySyncDAO = y2ySyncDAO;
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
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setY2YSyncLoggingCtxFactory(Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory)
    {
        this.y2YSyncLoggingCtxFactory = y2YSyncLoggingCtxFactory;
    }
}
