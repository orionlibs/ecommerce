package de.hybris.y2ysync.task.internal;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SyncTaskFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(SyncTaskFactory.class);
    public static final String MAIN_ZIP_TASK_RUNNER_BEAN_ID = "syncZipTaskRunner";
    public static final String MAIN_DATAHUB_TASK_RUNNER_BEAN_ID = "syncDataHubTaskRunner";
    public static final String CHUNK_TASK_RUNNER_BEAN_ID = "itemChangesProcessor";
    public static final String CONSUME_Y2Y_CHANGES_BEAN_ID = "consumeY2YChangesTaskRunner";
    public static final String SYNC_EXECUTION_ID_KEY = "syncExecutionID";
    public static final String MEDIA_PK_KEY = "mediaPK";
    public static final String CONDITION_NAME_KEY = "conditionName";
    public static final String IMPEX_HEADER_KEY = "impexHeader";
    public static final String TYPE_CODE_KEY = "typeCode";
    public static final String DATAHUB_COLUMNS_KEY = "dataHubColumns";
    public static final String SYNC_TYPE_KEY = "syncType";
    public static final String DATAHUB_TYPE_KEY = "dataHubType";
    private ModelService modelService;
    private TaskService taskService;


    @Deprecated(since = "6.0.0", forRemoval = true)
    public void runSyncTasksForZipResult(String syncExecutionId, List<MediasForType> mediasPerType)
    {
        runSyncTasks(syncExecutionId, Y2YSyncType.ZIP, mediasPerType);
    }


    @Deprecated(since = "6.0.0", forRemoval = true)
    public void runSyncTasksForDatahubResult(String syncExecutionId, List<MediasForType> mediasPerType)
    {
        runSyncTasks(syncExecutionId, Y2YSyncType.DATAHUB, mediasPerType);
    }


    public void runSyncTasks(String syncExecutionID, Y2YSyncType syncType, List<MediasForType> mediaForType)
    {
        runSyncTasks(syncExecutionID, syncType, mediaForType, null);
    }


    public void runSyncTasks(String syncExecutionID, Y2YSyncType syncType, List<MediasForType> mediaForType, String nodeGroup)
    {
        Set<TaskConditionModel> conditions = prepareAndScheduleTasks(syncExecutionID, syncType, mediaForType, nodeGroup);
        if(conditions.isEmpty())
        {
            LOG.info("No changes detected - no import tasks will be executed.");
            return;
        }
        LOG.info("Changes detected. Scheduling task: {} on node group: {}", getMainTaskRunnerBeanId(syncType), nodeGroup);
        prepareAndScheduleTaskOnConditions(getMainTaskRunnerBeanId(syncType), getMainTaskContext(syncExecutionID), conditions, nodeGroup);
    }


    private String getMainTaskRunnerBeanId(Y2YSyncType syncType)
    {
        if(Y2YSyncType.ZIP == syncType)
        {
            return "syncZipTaskRunner";
        }
        if(Y2YSyncType.DATAHUB == syncType)
        {
            return "syncDataHubTaskRunner";
        }
        throw new IllegalStateException("Y2YSyncType must be choosed");
    }


    public void resendToDataHub(String syncCronJobCode)
    {
        resendToDataHub(syncCronJobCode, null);
    }


    public void resendToDataHub(String syncCronJobCode, String nodeGroup)
    {
        prepareAndScheduleTaskNow("syncDataHubTaskRunner", getMainTaskContext(syncCronJobCode), nodeGroup);
    }


    public void runConsumeSyncChangesTask(String executionId)
    {
        runConsumeSyncChangesTask(executionId, null);
    }


    public void runConsumeSyncChangesTask(String executionId, String nodeGroup)
    {
        prepareAndScheduleTaskNow("consumeY2YChangesTaskRunner",
                        (Map<String, Object>)ImmutableMap.of("syncExecutionID", executionId), nodeGroup);
    }


    private Set<TaskConditionModel> prepareAndScheduleTasks(String syncExecutionId, Y2YSyncType syncType, List<MediasForType> mediasPerType, String nodeGroup)
    {
        Set<TaskConditionModel> conditions = new HashSet<>(mediasPerType.size());
        for(MediasForType mediasForType : mediasPerType)
        {
            for(PK mediaPK : mediasForType.getMediaPks())
            {
                prepareAndScheduleTaskNow("itemChangesProcessor",
                                getChunkTaskContext(syncExecutionId, syncType, mediaPK, mediasForType), nodeGroup);
                conditions.add(prepareTaskCondition(syncExecutionId, mediaPK));
            }
        }
        return conditions;
    }


    Map<String, Object> getChunkTaskContext(String syncExecutionId, Y2YSyncType syncType, PK mediaPK, MediasForType mediasForType)
    {
        ImmutableMap.Builder<String, Object> result = getTaskContextBuilder(syncExecutionId).put("mediaPK", mediaPK).put("conditionName", syncExecutionId + "_" + syncExecutionId).put("impexHeader", mediasForType.getImpexHeader()).put("typeCode", mediasForType.getComposedTypeCode())
                        .put("syncType", syncType);
        if(StringUtils.isNotBlank(mediasForType.getDataHubColumns()))
        {
            result.put("dataHubColumns", mediasForType.getDataHubColumns()).build();
        }
        if(StringUtils.isNotBlank(mediasForType.getDataHubType()))
        {
            result.put("dataHubType", mediasForType.getDataHubType()).build();
        }
        return (Map<String, Object>)result.build();
    }


    Map<String, Object> getMainTaskContext(String syncExecutionId)
    {
        return (Map<String, Object>)getTaskContextBuilder(syncExecutionId).build();
    }


    private ImmutableMap.Builder<String, Object> getTaskContextBuilder(String syncExecutionId)
    {
        return ImmutableMap.builder().put("syncExecutionID", syncExecutionId);
    }


    private void prepareAndScheduleTaskNow(String runnerBeanID, Map<String, Object> ctx, String nodeGroup)
    {
        TaskModel task = prepareTask(runnerBeanID, ctx, nodeGroup);
        task.setExecutionDate(new Date());
        this.taskService.scheduleTask(task);
    }


    private void prepareAndScheduleTaskOnConditions(String runnerBeanID, Map<String, Object> ctx, Set<TaskConditionModel> conditions, String nodeGroup)
    {
        TaskModel task = prepareTask(runnerBeanID, ctx, nodeGroup);
        task.setConditions(conditions);
        this.taskService.scheduleTask(task);
    }


    private TaskModel prepareTask(String runnerBeanID, Map<String, Object> ctx, String nodeGroup)
    {
        TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
        task.setContext(ctx);
        task.setRunnerBean(runnerBeanID);
        task.setNodeGroup(nodeGroup);
        return task;
    }


    private TaskConditionModel prepareTaskCondition(String syncExecutionId, PK mediaPK)
    {
        TaskConditionModel taskCondition = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
        String conditionUID = syncExecutionId + "_" + syncExecutionId;
        LOG.debug("Setting up UID for chunk task condition: {}", conditionUID);
        taskCondition.setUniqueID(conditionUID);
        return taskCondition;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }
}
