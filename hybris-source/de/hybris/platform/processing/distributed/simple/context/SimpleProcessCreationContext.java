package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.data.SimpleAbstractDistributedProcessCreationData;
import de.hybris.platform.processing.distributed.simple.data.SimpleBatchCreationData;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.stream.Stream;

public class SimpleProcessCreationContext implements DistributedProcessHandler.ProcessCreationContext
{
    protected final ModelService modelService;
    protected final SimpleAbstractDistributedProcessCreationData creationData;


    public SimpleProcessCreationContext(ModelService modelService, SimpleAbstractDistributedProcessCreationData creationData)
    {
        this.modelService = modelService;
        this.creationData = creationData;
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> createProcessModel()
    {
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)createDistributedProcessModel());
    }


    protected DistributedProcessModel createDistributedProcessModel()
    {
        SimpleDistributedProcessModel process = (SimpleDistributedProcessModel)this.modelService.create(getProcessModelClass());
        process.setCode(this.creationData.getProcessId());
        process.setNodeGroup(this.creationData.getNodeGroup());
        process.setHandlerBeanId(this.creationData.getHandlerBeanId());
        process.setBatchSize(this.creationData.getBatchSize());
        return (DistributedProcessModel)process;
    }


    protected Class<? extends SimpleDistributedProcessModel> getProcessModelClass()
    {
        return (this.creationData.getProcessModelClass() == null) ? SimpleDistributedProcessModel.class : this.creationData.getProcessModelClass();
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> initialBatches()
    {
        return this.creationData.initialBatches().map(this::prepareBatch);
    }


    protected DistributedProcessHandler.ModelWithDependencies<BatchModel> prepareBatch(SimpleBatchCreationData data)
    {
        SimpleBatchModel initialBatch = (SimpleBatchModel)this.modelService.create(SimpleBatchModel.class);
        initialBatch.setId(SimpleBatchID.asInitialBatch().toString());
        initialBatch.setType(BatchType.INITIAL);
        initialBatch.setContext(data.getContext());
        initialBatch.setRemainingWorkLoad(10L);
        initialBatch.setRetries(this.creationData.getNumOfRetries());
        initialBatch.setScriptCode(this.creationData.getScriptCode());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)initialBatch);
    }
}
