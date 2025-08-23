package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.ProcessStatus;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.task.TaskConditionModel;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Controller implements ApplicationContextAware
{
    private static final String INITIAL_EXECUTION_ID = DistributedProcessState.CREATED.toString();
    private final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final Scheduler scheduler;
    private ApplicationContext applicationContext;
    private final String INPUT_BATCHES_QUERY = "select {pk} from {Batch} where {process}=?process and {type}=?type and {executionId}=?executionId";
    private static final int FLUSH_SIZE = 500;


    public Controller(ModelService modelService, FlexibleSearchService flexibleSearchService, Scheduler scheduler)
    {
        this.INPUT_BATCHES_QUERY = "select {pk} from {Batch} where {process}=?process and {type}=?type and {executionId}=?executionId";
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        Objects.requireNonNull(scheduler, "scheduler mustn't be null");
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
        this.scheduler = scheduler;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public DistributedProcessModel createProcess(ProcessCreationData processData)
    {
        Objects.requireNonNull(processData, "processData mustn't be null");
        DistributedProcessHandler handler = getHandler(processData.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING, DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN})
                        .apply(() -> {
                            DistributedProcessHandler.ProcessCreationContext creationCtx = handler.createProcessCreationContext(processData);
                            DistributedProcessHelper.FlushInBatchesContext flushCtx = createFlushContext();
                            DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> models = creationCtx.createProcessModel();
                            DistributedProcessModel process = (DistributedProcessModel)models.getModel();
                            process.setState(DistributedProcessState.CREATED);
                            process.setCurrentExecutionId(INITIAL_EXECUTION_ID);
                            process.setNodeGroup(processData.getNodeGroup());
                            Preconditions.checkState(!Strings.isNullOrEmpty(process.getCode()), "Code of new distributed process must be given");
                            handler.updateStatusInformation(process);
                            flushCtx.add(models.getAllModels());
                            creationCtx.initialBatches().forEach(());
                            flushCtx.finish();
                            return process;
                        });
    }


    public DistributedProcessModel startProcess(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHelper.requireProcessToBeInState(process, DistributedProcessState.CREATED);
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            process.setState(DistributedProcessState.INITIALIZING);
            this.scheduler.scheduleTransitionTask(process);
            handler.updateStatusInformation(process);
            this.modelService.save(process);
            return process;
        });
    }


    public DistributedProcessModel initializeProcess(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHelper.requireProcessToBeInState(process, DistributedProcessState.INITIALIZING);
        return DistributedProcessHelper.runInEnvironment(getHandler(process.getHandlerBeanId()),
                        new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
            DistributedProcessHandler.ProcessInitializationContext initializationCtx = handler.createProcessInitializationContext(process);
            DistributedProcessHelper.FlushInBatchesContext flushCtx = createFlushContext();
            DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> models = initializationCtx.initializeProcess();
            AtomicBoolean anyInputBatchCreated = new AtomicBoolean(false);
            initializationCtx.firstExecutionInputBatches().forEach(());
            if(!anyInputBatchCreated.get())
            {
                process.setState(DistributedProcessState.SUCCEEDED);
            }
            else
            {
                process.setState(DistributedProcessState.SCHEDULING_EXECUTION);
                this.scheduler.scheduleTransitionTask(process);
            }
            handler.updateStatusInformation(process);
            flushCtx.add(models.getAllModels()).finish();
            return process;
        });
    }


    public DistributedProcessModel scheduleExecution(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHelper.requireProcessToBeInState(process, DistributedProcessState.SCHEDULING_EXECUTION);
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            ImmutableMap immutableMap = ImmutableMap.of("process", process, "type", BatchType.INPUT, "executionId", process.getCurrentExecutionId());
            FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {Batch} where {process}=?process and {type}=?type and {executionId}=?executionId", (Map)immutableMap);
            query.setDisableCaching(true);
            List<BatchModel> inputBatches = this.flexibleSearchService.search(query).getResult();
            Set<TaskConditionModel> workersConditions = new HashSet<>();
            for(BatchModel batch : inputBatches)
            {
                DistributedProcessWorkerTaskModel workerTask = this.scheduler.scheduleWorkerTask(batch);
                String conditionId = workerTask.getConditionId();
                TaskConditionModel condition = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
                condition.setUniqueID(conditionId);
                workersConditions.add(condition);
            }
            process.setState(DistributedProcessState.WAITING_FOR_EXECUTION);
            handler.updateStatusInformation(process);
            this.modelService.save(process);
            this.scheduler.scheduleTransitionTask(process, workersConditions);
            return process;
        });
    }


    public DistributedProcessModel analyseExecutionResults(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHelper.requireProcessToBeInState(process, DistributedProcessState.WAITING_FOR_EXECUTION);
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            String previousExecutionId = process.getCurrentExecutionId();
            DistributedProcessHandler.ProcessExecutionAnalysisContext analysisCtx = handler.createProcessExecutionAnalysisContext(process);
            boolean failed;
            if((failed = analysisCtx.processFailed()) || analysisCtx.processSucceeded())
            {
                process.setState(failed ? DistributedProcessState.FAILED : DistributedProcessState.SUCCEEDED);
                handler.updateStatusInformation(process);
                this.modelService.save(process);
                return process;
            }
            DistributedProcessHelper.FlushInBatchesContext flushCtx = createFlushContext();
            DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> models = analysisCtx.prepareProcessForNextExecution();
            Preconditions.checkState(!previousExecutionId.equals(process.getCurrentExecutionId()), "execution id of process %s must be changed but it stays the same '%s'", process, previousExecutionId);
            AtomicBoolean anyInputBatchCreated = new AtomicBoolean(false);
            analysisCtx.nextExecutionInputBatches().forEach(());
            if(!anyInputBatchCreated.get())
            {
                process.setState(DistributedProcessState.SUCCEEDED);
            }
            else
            {
                process.setState(DistributedProcessState.SCHEDULING_EXECUTION);
                this.scheduler.scheduleTransitionTask(process);
            }
            handler.updateStatusInformation(process);
            flushCtx.add(models.getAllModels()).finish();
            return process;
        });
    }


    public DistributedProcessModel requestToStopProcess(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            if(DistributedProcessHelper.isFinished(process) || process.isStopRequested())
            {
                return process;
            }
            process.setStopRequested(true);
            if(DistributedProcessHelper.isCreated(process))
            {
                process.setState(DistributedProcessState.STOPPED);
            }
            handler.updateStatusInformation(process);
            this.modelService.save(process);
            return process;
        });
    }


    public DistributedProcessModel stopProcess(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        return DistributedProcessHelper.runInEnvironment(handler, new DistributedProcessHelper.EnvFeature[] {DistributedProcessHelper.EnvFeature.TX, DistributedProcessHelper.EnvFeature.OPTIMISTIC_LOCKING, DistributedProcessHelper.EnvFeature.SLD, DistributedProcessHelper.EnvFeature.POSTPROCESSING,
                        DistributedProcessHelper.EnvFeature.RUN_AS_ADMIN}).apply(() -> {
            process.setState(DistributedProcessState.STOPPED);
            handler.updateStatusInformation(process);
            this.modelService.save(process);
            return process;
        });
    }


    public DistributedProcessModel waitForProcess(DistributedProcessModel process, long maxWaitTimeInSeconds) throws InterruptedException
    {
        Objects.requireNonNull(process, "process mustn't be null");
        if(maxWaitTimeInSeconds <= 0L || DistributedProcessHelper.isFinished(process))
        {
            return process;
        }
        long sleepIntervalMillis = 100L;
        long iterations = 1L + TimeUnit.SECONDS.toMillis(maxWaitTimeInSeconds) / 100L;
        while(iterations-- > 0L)
        {
            Thread.sleep(100L);
            this.modelService.refresh(process);
            if(DistributedProcessHelper.isFinished(process))
            {
                return process;
            }
        }
        return process;
    }


    public ProcessStatus getProcessStatus(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        DistributedProcessHandler handler = getHandler(process.getHandlerBeanId());
        ProcessStatus.Builder statusBuilder = ProcessStatus.builder();
        statusBuilder.withState(process.getState());
        if(StringUtils.isNotEmpty(process.getStatus()))
        {
            if(StringUtils.isNotEmpty(process.getExtendedStatus()))
            {
                statusBuilder.withStatus(process.getStatus(), process.getExtendedStatus());
            }
            else
            {
                statusBuilder.withStatus(process.getStatus());
            }
        }
        if(process.getProgress() != null)
        {
            statusBuilder.withProgress(process.getProgress().doubleValue());
        }
        OptionalDouble realTimeProgress = handler.calculateProgress(process);
        realTimeProgress.ifPresent(p -> statusBuilder.withProgress(p));
        return statusBuilder.build();
    }


    private void assureBatchState(BatchModel batch, DistributedProcessModel process, BatchType type)
    {
        batch.setType(type);
        batch.setExecutionId(process.getCurrentExecutionId());
        batch.setProcess(process);
        checkRequiredBatchFields(batch);
    }


    private void checkRequiredBatchFields(BatchModel batch)
    {
        Preconditions.checkState(!Strings.isNullOrEmpty(batch.getId()), "Id of input batch must be given");
        Preconditions.checkState((batch.getRemainingWorkLoad() > 0L), "Amount of work of input batch must be greater than 0");
    }


    DistributedProcessHandler getHandler(String handlerBeanId)
    {
        return DistributedProcessHelper.getHandler(this.applicationContext, handlerBeanId);
    }


    private DistributedProcessHelper.FlushInBatchesContext createFlushContext()
    {
        return DistributedProcessHelper.flushInBatches(this.modelService, 500);
    }
}
