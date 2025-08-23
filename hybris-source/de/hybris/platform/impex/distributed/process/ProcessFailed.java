package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import java.util.stream.Stream;

class ProcessFailed implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    private final DistributedProcessModel process;


    public ProcessFailed(DistributedProcessModel process)
    {
        this.process = process;
    }


    public boolean processFailed()
    {
        return true;
    }


    public boolean processSucceeded()
    {
        return false;
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        return Stream.empty();
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }
}
