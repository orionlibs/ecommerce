package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.distributed.simple.id.SimpleProcessExecutionID;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.stream.Stream;

public class SimpleDistributedProcessInitializationContext implements DistributedProcessHandler.ProcessInitializationContext
{
    private static final String QUERY = "SELECT {pk} FROM {SimpleBatch} WHERE {process}=?process AND {type}=?type";
    protected final FlexibleSearchService flexibleSearchService;
    protected final ModelService modelService;
    protected final SimpleDistributedProcessModel process;


    public SimpleDistributedProcessInitializationContext(FlexibleSearchService flexibleSearchService, ModelService modelService, SimpleDistributedProcessModel process)
    {
        this.flexibleSearchService = flexibleSearchService;
        this.modelService = modelService;
        this.process = process;
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> initializeProcess()
    {
        this.process.setCurrentExecutionId(SimpleProcessExecutionID.firstTurn().toString());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> firstExecutionInputBatches()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {SimpleBatch} WHERE {process}=?process AND {type}=?type");
        fQuery.setDisableCaching(true);
        fQuery.addQueryParameter("process", this.process);
        fQuery.addQueryParameter("type", BatchType.INITIAL);
        SearchResult<SimpleBatchModel> result = this.flexibleSearchService.search(fQuery);
        return result.getResult().stream().map(this::prepareInputBatch);
    }


    protected DistributedProcessHandler.ModelWithDependencies<BatchModel> prepareInputBatch(SimpleBatchModel batch)
    {
        SimpleBatchModel inputBatch = (SimpleBatchModel)this.modelService.create(SimpleBatchModel.class);
        inputBatch.setId(SimpleBatchID.asInputBatchID().toString());
        inputBatch.setRetries(batch.getRetries());
        inputBatch.setType(BatchType.INPUT);
        inputBatch.setRemainingWorkLoad(batch.getRemainingWorkLoad());
        inputBatch.setContext(batch.getContext());
        inputBatch.setScriptCode(batch.getScriptCode());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)inputBatch);
    }
}
