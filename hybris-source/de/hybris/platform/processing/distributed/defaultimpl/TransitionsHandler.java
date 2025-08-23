package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import de.hybris.platform.processing.distributed.ProcessConcurrentlyModifiedException;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessTransitionTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.util.Config;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransitionsHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(TransitionsHandler.class);
    private final int NUMBER_OF_RETRIES_FOR_SINGLE_TRANSITION = Config.getInt("distributed.processes.transitions.max.retries", 5);
    private final int MAX_WAIT_FOR_SAME_STATUS_MILLIS = Config.getInt("distributed.processes.transitions.stale.status.max.wait", 10000);
    private final ModelService modelService;
    private final Controller controller;


    public TransitionsHandler(ModelService modelService, Controller controller)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(controller, "controller mustn't be null");
        this.modelService = modelService;
        this.controller = controller;
    }


    public void runTransitionTask(DistributedProcessTransitionTaskModel task)
    {
        try
        {
            performTransition(task);
        }
        catch(InterruptedException e)
        {
            return;
        }
        catch(ProcessConcurrentlyModifiedException e)
        {
            Integer numberOfRetries = task.getRetry();
            if(numberOfRetries != null && numberOfRetries.intValue() < this.NUMBER_OF_RETRIES_FOR_SINGLE_TRANSITION)
            {
                RetryLaterException retryLater = new RetryLaterException("Transition must be executed once again.", (Throwable)e);
                retryLater.setRollBack(true);
                retryLater.setMethod(RetryLaterException.Method.EXPONENTIAL);
                retryLater.setDelay(10L);
                throw retryLater;
            }
            throw e;
        }
    }


    public void handleErrorDuringTransition(DistributedProcessTransitionTaskModel task)
    {
        DistributedProcessModel process = task.getContextItem();
        if(process != null)
        {
            process.setState(DistributedProcessState.FAILED);
            this.modelService.save(process);
        }
    }


    private void performTransition(DistributedProcessTransitionTaskModel task) throws InterruptedException
    {
        DistributedProcessModel process = requireProcessInTheSameState(task);
        if(process.isStopRequested())
        {
            this.controller.stopProcess(process);
            return;
        }
        switch(null.$SwitchMap$de$hybris$platform$processing$enums$DistributedProcessState[process.getState().ordinal()])
        {
            case 1:
                this.controller.initializeProcess(process);
                return;
            case 2:
                this.controller.scheduleExecution(process);
                return;
            case 3:
                this.controller.analyseExecutionResults(process);
                return;
        }
        throw new IllegalStateException(
                        String.format("Process %s in unexpected state '%s'", new Object[] {process, process.getState()}));
    }


    private DistributedProcessModel requireProcessInTheSameState(DistributedProcessTransitionTaskModel task) throws InterruptedException
    {
        DistributedProcessModel process = task.getContextItem();
        Preconditions.checkState((process != null), "Unexpected task %s with null process.", task);
        DistributedProcessState processState = process.getState();
        Preconditions.checkState((processState != null), "Unexpectedd null state for process %s with code '%s'.", process, process
                        .getCode());
        DistributedProcessState taskState = task.getState();
        if(processState.equals(taskState))
        {
            return process;
        }
        LOG.info("### Unexpected situation. Process {} is in state '{}', but task {} is in state '{}'!", new Object[] {process, processState, task, taskState});
        LOG.info("### process.getState() -> {}", process.getState());
        LOG.info("### task.getState() -> {}", task.getState());
        LOG.info("### Refreshing process {}", process);
        long waitMs;
        for(waitMs = 100L; waitMs < this.MAX_WAIT_FOR_SAME_STATUS_MILLIS; waitMs += waitMs / 10L)
        {
            Thread.sleep(waitMs);
            LOG.info("### Refreshing process and task");
            this.modelService.refresh(task);
            this.modelService.refresh(process);
            processState = process.getState();
            taskState = task.getState();
            LOG.info("### Process refreshed -> {}", process);
            LOG.info("### process.getState() -> {}", processState);
            LOG.info("### Task refreshed -> {}", task);
            LOG.info("### task.getState() -> {}", taskState);
            if(processState.equals(taskState))
            {
                return process;
            }
        }
        throw new IllegalStateException(
                        String.format("Processing task %s expected process %s to to be in state '%s' but it's in '%s' state", new Object[] {task, process, taskState, process.getState()}));
    }
}
