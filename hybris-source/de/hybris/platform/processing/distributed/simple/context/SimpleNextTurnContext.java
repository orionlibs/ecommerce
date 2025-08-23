package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleNextTurnContext extends SimpleAbstractTurnContext
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleNextTurnContext.class);
    private static final String QUERY = "SELECT {PK} FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}>?retries";
    protected final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final String previousExecutionId;


    public SimpleNextTurnContext(DistributedProcessModel process, String previousExecutionId, ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        super(process);
        this.previousExecutionId = previousExecutionId;
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
    }


    public boolean processFailed()
    {
        return false;
    }


    public boolean processSucceeded()
    {
        return false;
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}>?retries");
        fQuery.setDisableCaching(true);
        fQuery.addQueryParameter("process", this.process);
        fQuery.addQueryParameter("currentExecutionId", this.previousExecutionId);
        fQuery.addQueryParameter("type", BatchType.INPUT);
        fQuery.addQueryParameter("retries", Integer.valueOf(0));
        return this.flexibleSearchService.search(fQuery).getResult().stream().map(this::createInputBatch);
    }


    protected DistributedProcessHandler.ModelWithDependencies<BatchModel> createInputBatch(SimpleBatchModel _inputBatch)
    {
        LOG.info("Batch id {} has failed in previous execution {}. Remaining number of retries: {}", new Object[] {_inputBatch.getId(), this.previousExecutionId,
                        Integer.valueOf(_inputBatch.getRetries())});
        SimpleBatchModel inputBatch = (SimpleBatchModel)this.modelService.create(SimpleBatchModel.class);
        inputBatch.setId(SimpleBatchID.asInputBatchID().toString());
        inputBatch.setType(BatchType.INPUT);
        inputBatch.setContext(_inputBatch.getContext());
        inputBatch.setScriptCode(_inputBatch.getScriptCode());
        inputBatch.setRemainingWorkLoad(10L);
        inputBatch.setRetries(_inputBatch.getRetries() - 1);
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)inputBatch);
    }
}
