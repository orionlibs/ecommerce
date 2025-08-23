package de.hybris.platform.impex.distributed.process;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ImportProcessExecutionAnalysisContext implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    private final DistributedProcessModel process;
    private final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final DistributedProcessHandler.ProcessExecutionAnalysisContext concreteContext;
    private static final String INITIAL_BATCHES_GROUP_COUNT_QUERY = "select count({pk}) from {ImportBatch} where {process}=?process and {type}=?type and {group}=?group";
    private static final String COUNT_OF_UNFINISHED_FIRST_TURN_INPUT_BATCHES_QUERY = "select count({input.pk}) from {ImportBatch as input left join ImportBatch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process and ({result.pk} is null or {result.remainingWorkLoad} > 0)";
    private static final String COUNT_OF_UNFINISHED_INPUT_BATCHES_QUERY = "select count({input.pk}) from {ImportBatch as input left join ImportBatch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process and ({result.pk} is null or {result.remainingWorkLoad} > 0) and {input.executionId}=?executionId";
    private static final String REMAINING_WORKLOAD_TO_DO_IN_TURN_QUERY = "select sum({remainingWorkLoad}) from {ImportBatch} where {process}=?process and {type}=?type and {executionId}=?executionId";


    public ImportProcessExecutionAnalysisContext(DistributedProcessModel process, ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        this.process = process;
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
        this.concreteContext = createConcreteContext();
    }


    public boolean processFailed()
    {
        return this.concreteContext.processFailed();
    }


    public boolean processSucceeded()
    {
        return this.concreteContext.processSucceeded();
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        return this.concreteContext.nextExecutionInputBatches();
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        return this.concreteContext.prepareProcessForNextExecution();
    }


    private DistributedProcessHandler.ProcessExecutionAnalysisContext createConcreteContext()
    {
        ProcessExecutionId executionId = ProcessExecutionId.from(this.process.getCurrentExecutionId());
        if(executionId.isFirstTurn())
        {
            ProcessExecutionId nextGroup = executionId.nextGroup();
            if(isThereAnyInitialBatchFor(nextGroup))
            {
                return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ContinueExecutionOfInitialBatchesForTheNextGroup(this.process, nextGroup, this.modelService, this.flexibleSearchService);
            }
            if(allBatchesInFirstTurnSucceeded())
            {
                return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ProcessFinishedSucceesfully(this.process);
            }
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ContinueExecutionOfLeftoversFromFirstTurn(this.process, executionId.nextTurn(), this.modelService, this.flexibleSearchService);
        }
        if(allBatchesInTurnSucceeded(executionId))
        {
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ProcessFinishedSucceesfully(this.process);
        }
        if(thereIsNoProgression(executionId))
        {
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ProcessFailed(this.process);
        }
        return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ContinueExecutionOfLeftoversFromPreviousTurn(this.process, executionId.nextTurn(), this.modelService, this.flexibleSearchService);
    }


    private boolean isThereAnyInitialBatchFor(ProcessExecutionId id)
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "type", BatchType.INITIAL, "group",
                        Integer.valueOf(id.getGroup()));
        return (queryForLong("select count({pk}) from {ImportBatch} where {process}=?process and {type}=?type and {group}=?group", (Map<String, Object>)immutableMap) > 0L);
    }


    private boolean allBatchesInFirstTurnSucceeded()
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "inputType", BatchType.INPUT, "resultType", BatchType.RESULT);
        return (queryForLong(
                        "select count({input.pk}) from {ImportBatch as input left join ImportBatch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process and ({result.pk} is null or {result.remainingWorkLoad} > 0)",
                        (Map<String, Object>)immutableMap) == 0L);
    }


    private boolean allBatchesInTurnSucceeded(ProcessExecutionId executionId)
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "inputType", BatchType.INPUT, "resultType", BatchType.RESULT, "executionId", executionId
                        .toString());
        return (queryForLong(
                        "select count({input.pk}) from {ImportBatch as input left join ImportBatch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process and ({result.pk} is null or {result.remainingWorkLoad} > 0) and {input.executionId}=?executionId",
                        (Map<String, Object>)immutableMap) == 0L);
    }


    private boolean thereIsNoProgression(ProcessExecutionId executionId)
    {
        int anInt = Config.getInt("impex.distributed.noProgressionLimit", 3);
        int i = (anInt <= 0) ? 3 : anInt;
        if(executionId.getTurn() < i)
        {
            return false;
        }
        long currTurnWork = getRemainingWorkLoadToDoFor(executionId);
        ProcessExecutionId currExecutionId = executionId;
        for(int k = 0; k < i - 1; k++)
        {
            ProcessExecutionId prevExecutionId = currExecutionId.previousTurn();
            long prevTurnWork = getRemainingWorkLoadToDoFor(prevExecutionId);
            if(currTurnWork != prevTurnWork)
            {
                return false;
            }
            currExecutionId = prevExecutionId;
            currTurnWork = prevTurnWork;
        }
        return true;
    }


    private long getRemainingWorkLoadToDoFor(ProcessExecutionId executionId)
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "type", BatchType.INPUT, "executionId", executionId
                        .toString());
        return queryForLong("select sum({remainingWorkLoad}) from {ImportBatch} where {process}=?process and {type}=?type and {executionId}=?executionId", (Map<String, Object>)immutableMap);
    }


    private long queryForLong(String queryString, Map<String, Object> params)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
        query.setDisableCaching(true);
        query.setResultClassList((List)ImmutableList.of(Long.class));
        Long result = this.flexibleSearchService.search(query).getResult().get(0);
        return result.longValue();
    }
}
