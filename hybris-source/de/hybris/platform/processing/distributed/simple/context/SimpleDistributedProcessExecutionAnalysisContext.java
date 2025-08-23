package de.hybris.platform.processing.distributed.simple.context;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDistributedProcessExecutionAnalysisContext implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleDistributedProcessExecutionAnalysisContext.class);
    private static final String COUNT_BATCHES = "SELECT count({pk}) FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL";
    private static final String COUNT_UNFINISHED_BATCHES = "SELECT count({pk}) FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}>?retries";
    private static final String COUNT_UNRETRYABLE_BATCHES = "SELECT count({pk}) FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}=?retries";
    protected final FlexibleSearchService flexibleSearchService;
    protected final ModelService modelService;
    protected final DistributedProcessService distributedProcessService;
    protected final DistributedProcessModel process;
    protected final DistributedProcessHandler.ProcessExecutionAnalysisContext concreteCtx;


    public SimpleDistributedProcessExecutionAnalysisContext(FlexibleSearchService flexibleSearchService, ModelService modelService, DistributedProcessService distributedProcessService, DistributedProcessModel process)
    {
        this.flexibleSearchService = flexibleSearchService;
        this.modelService = modelService;
        this.distributedProcessService = distributedProcessService;
        this.process = process;
        this.concreteCtx = createConcreteCtx();
    }


    protected DistributedProcessHandler.ProcessExecutionAnalysisContext createConcreteCtx()
    {
        if(thereAreUnfinishedBatches())
        {
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new SimpleNextTurnContext(this.process, this.process.getCurrentExecutionId(), this.modelService, this.flexibleSearchService);
        }
        if(thereAreUnretryableBatches())
        {
            LOG.error("Distributed process {} has failed after configured number of attempts. Requesting to stop.", this.process
                            .getCode());
            this.distributedProcessService.requestToStop(this.process.getCode());
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new SimpleProcessFailedContext(this.process);
        }
        return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new SimpleProcessFinishedSuccessfullyContext(this.process);
    }


    protected boolean thereAreUnfinishedBatches()
    {
        return (countBatches("SELECT count({pk}) FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}>?retries") > 0L);
    }


    protected boolean thereAreUnretryableBatches()
    {
        return (countBatches("SELECT count({pk}) FROM {SimpleBatch} WHERE {process}=?process AND {executionId}=?currentExecutionId AND {type}=?type AND {resultBatchId} IS NULL AND {retries}=?retries") > 0L);
    }


    protected long countBatches(String countQuery)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(countQuery);
        fQuery.addQueryParameter("process", this.process);
        fQuery.addQueryParameter("currentExecutionId", this.process.getCurrentExecutionId());
        fQuery.addQueryParameter("type", BatchType.INPUT);
        fQuery.addQueryParameter("retries", Integer.valueOf(0));
        fQuery.setResultClassList((List)ImmutableList.of(Long.class));
        Long res = this.flexibleSearchService.search(fQuery).getResult().get(0);
        return res.longValue();
    }


    public boolean processFailed()
    {
        return this.concreteCtx.processFailed();
    }


    public boolean processSucceeded()
    {
        return this.concreteCtx.processSucceeded();
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        return this.concreteCtx.nextExecutionInputBatches();
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        return this.concreteCtx.prepareProcessForNextExecution();
    }
}
