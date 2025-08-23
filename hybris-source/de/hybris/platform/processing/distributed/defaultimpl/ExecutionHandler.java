package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskService;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ExecutionHandler implements ApplicationContextAware
{
    private final TaskService taskService;
    private final ModelService modelService;
    private ApplicationContext applicationContext;


    public ExecutionHandler(TaskService taskService, ModelService modelService)
    {
        Objects.requireNonNull(taskService, "taskService mustn't be null");
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        this.taskService = taskService;
        this.modelService = modelService;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public void runWorkerTask(DistributedProcessWorkerTaskModel task)
    {
        Objects.requireNonNull(task, "task mustn't be null");
        DistributedProcessHelper.doInTransaction(() -> {
            BatchModel batch = task.getContextItem();
            Preconditions.checkState((batch != null), "expected batch to be not null for task %s", task);
            DistributedProcessModel process = batch.getProcess();
            Preconditions.checkState((process != null), "expected process not to be null for batch %s", batch);
            TaskEvent event = TaskEvent.fulfilByRemoval(task.getConditionId());
            if(isExecutionNoLongerRelevant(process, batch))
            {
                this.taskService.triggerEvent(event);
                return null;
            }
            DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
            DistributedProcessHandler.ModelWithDependencies<BatchModel> models = handler.createResultBatch(batch);
            BatchModel resultBatch = (BatchModel)models.getModel();
            resultBatch.setType(BatchType.RESULT);
            resultBatch.setExecutionId(batch.getExecutionId());
            resultBatch.setProcess(process);
            resultBatch.setId(batch.getId());
            this.modelService.saveAll(models.getAllModels());
            this.taskService.triggerEvent(event);
            return resultBatch;
        });
    }


    public void handleErrorDuringBatchExecution(DistributedProcessWorkerTaskModel task)
    {
        Objects.requireNonNull(task, "task mustn't be null");
        TaskEvent event = TaskEvent.fulfilByRemoval(task.getConditionId());
        this.taskService.triggerEvent(event);
    }


    private boolean isExecutionNoLongerRelevant(DistributedProcessModel process, BatchModel batch)
    {
        return (process.isStopRequested() || DistributedProcessHelper.isFinished(process) ||
                        !batch.getExecutionId().equals(process.getCurrentExecutionId()));
    }


    DistributedProcessHandler getHandler(String handlerBeanId)
    {
        return DistributedProcessHelper.getHandler(this.applicationContext, handlerBeanId);
    }
}
