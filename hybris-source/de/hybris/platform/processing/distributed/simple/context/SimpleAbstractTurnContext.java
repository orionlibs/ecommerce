package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.id.SimpleProcessExecutionID;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import java.util.stream.Stream;

public abstract class SimpleAbstractTurnContext implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    protected final DistributedProcessModel process;


    public SimpleAbstractTurnContext(DistributedProcessModel process)
    {
        this.process = process;
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        return Stream.empty();
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        this.process.setCurrentExecutionId(getNextExecutionId());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }


    protected String getNextExecutionId()
    {
        SimpleProcessExecutionID id = SimpleProcessExecutionID.fromString(this.process.getCurrentExecutionId());
        return id.getNexID().toString();
    }
}
