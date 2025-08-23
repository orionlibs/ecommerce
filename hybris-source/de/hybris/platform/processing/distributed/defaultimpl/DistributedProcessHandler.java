package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import java.util.OptionalDouble;
import java.util.Set;

public interface DistributedProcessHandler
{
    default OptionalDouble calculateProgress(DistributedProcessModel process)
    {
        return OptionalDouble.empty();
    }


    default void updateStatusInformation(DistributedProcessModel process)
    {
        OptionalDouble progress = calculateProgress(process);
        progress.ifPresent(p -> process.setProgress(Double.valueOf(p)));
        switch(null.$SwitchMap$de$hybris$platform$processing$enums$DistributedProcessState[process.getState().ordinal()])
        {
            case 1:
                process.setStatus("Created");
                process.setExtendedStatus("Process has been created.");
                return;
            case 2:
                process.setStatus("Initializing");
                process.setExtendedStatus("Process is being initialized.");
                return;
            case 3:
                process.setStatus("Running");
                process.setExtendedStatus("Scheduling execution of '" + process.getCurrentExecutionId() + "' turn.");
                return;
            case 4:
                process.setStatus("Running");
                process.setExtendedStatus("Executing '" + process.getCurrentExecutionId() + "' turn.");
                return;
            case 5:
                process.setStatus("Stopped");
                process.setExtendedStatus("Process has been stopped.");
                return;
            case 6:
                process.setStatus("Succeeded");
                process.setExtendedStatus("Process has finished successfully.");
                return;
            case 7:
                process.setStatus("Failed");
                process.setExtendedStatus("Process execution has failed.");
                return;
        }
        throw new UnsupportedOperationException("Couldn't map state '" + process.getState() + "' to status.");
    }


    default Set<String> getTypesWithDisabledUniquenessCheck()
    {
        return (Set<String>)ImmutableSet.of("Batch", "DistributedProcessWorkerTask", "TaskCondition");
    }


    ProcessCreationContext createProcessCreationContext(ProcessCreationData paramProcessCreationData);


    ProcessInitializationContext createProcessInitializationContext(DistributedProcessModel paramDistributedProcessModel);


    ModelWithDependencies<BatchModel> createResultBatch(BatchModel paramBatchModel);


    ProcessExecutionAnalysisContext createProcessExecutionAnalysisContext(DistributedProcessModel paramDistributedProcessModel);


    void onFinished(DistributedProcessModel paramDistributedProcessModel);
}
