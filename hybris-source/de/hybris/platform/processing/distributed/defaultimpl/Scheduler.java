package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessTransitionTaskModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Scheduler
{
    private final ModelService modelService;
    private final TaskService taskService;
    private final String dispatcherBeanName;


    public Scheduler(ModelService modelService, TaskService taskService, String dispatcherBeanName)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(taskService, "taskService mustn't be null");
        Objects.requireNonNull(dispatcherBeanName, "dispatcherBeanName mustn't be null");
        this.modelService = modelService;
        this.taskService = taskService;
        this.dispatcherBeanName = dispatcherBeanName;
    }


    public DistributedProcessTransitionTaskModel scheduleTransitionTask(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        return scheduleTransitionTask(process, Collections.emptySet());
    }


    public DistributedProcessTransitionTaskModel scheduleTransitionTask(DistributedProcessModel process, Set<TaskConditionModel> conditions)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        Objects.requireNonNull(conditions, "conditions mustn't be null");
        DistributedProcessTransitionTaskModel task = (DistributedProcessTransitionTaskModel)this.modelService.create(DistributedProcessTransitionTaskModel.class);
        task.setContextItem((ItemModel)process);
        task.setRunnerBean(this.dispatcherBeanName);
        task.setState(process.getState());
        task.setNodeGroup(process.getNodeGroup());
        task.setConditions(conditions);
        this.taskService.scheduleTask((TaskModel)task);
        return task;
    }


    public DistributedProcessWorkerTaskModel scheduleWorkerTask(BatchModel batch)
    {
        Objects.requireNonNull(batch, "batch mustn't be null");
        Preconditions.checkArgument(BatchType.INPUT.equals(batch.getType()), "batch must be of type '%s' but it's '%s'", BatchType.INPUT, batch
                        .getType());
        String conditionId = extractConditionId(batch);
        DistributedProcessWorkerTaskModel workerTask = (DistributedProcessWorkerTaskModel)this.modelService.create(DistributedProcessWorkerTaskModel.class);
        workerTask.setRunnerBean(this.dispatcherBeanName);
        workerTask.setContextItem((ItemModel)batch);
        workerTask.setConditionId(conditionId);
        workerTask.setNodeGroup(batch.getProcess().getNodeGroup());
        this.taskService.scheduleTask((TaskModel)workerTask);
        return workerTask;
    }


    private String extractConditionId(BatchModel batch)
    {
        return String.format("%s_%s_%s", new Object[] {batch.getProcess().getCode(), batch.getExecutionId(), batch.getId()});
    }
}
