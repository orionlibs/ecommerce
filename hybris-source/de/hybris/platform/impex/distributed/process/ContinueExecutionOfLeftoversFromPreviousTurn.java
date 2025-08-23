package de.hybris.platform.impex.distributed.process;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Map;
import java.util.stream.Stream;

class ContinueExecutionOfLeftoversFromPreviousTurn extends AbstractImportProcessExecutionAnalysisContext
{
    private final DistributedProcessModel process;
    private final ProcessExecutionId currentTurn;
    private final ProcessExecutionId nextTurn;


    public ContinueExecutionOfLeftoversFromPreviousTurn(DistributedProcessModel process, ProcessExecutionId nextTurn, ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        super(modelService, flexibleSearchService);
        this.process = process;
        this.currentTurn = ProcessExecutionId.from(process.getCurrentExecutionId());
        this.nextTurn = nextTurn;
    }


    private static final String LEFTOVER_BATCHES_FROM_ROUND_QUERY = ROWS_SELECT_CLAUSE
                    + "from {ImportBatch as input left join ImportBatch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process and ({result.pk} is null or {result.remainingWorkLoad} > 0) and {input.executionId}=?executionId";


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "inputType", BatchType.INPUT, "resultType", BatchType.RESULT, "executionId", this.currentTurn
                        .toString());
        return queryForBatchRows(LEFTOVER_BATCHES_FROM_ROUND_QUERY, (Map)immutableMap)
                        .map(row -> DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)createInputBatch(row)));
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        this.process.setCurrentExecutionId(this.nextTurn.toString());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }
}
