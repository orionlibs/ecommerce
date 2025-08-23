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

class ContinueExecutionOfInitialBatchesForTheNextGroup extends AbstractImportProcessExecutionAnalysisContext
{
    private final DistributedProcessModel process;
    private final ProcessExecutionId nextGroup;


    public ContinueExecutionOfInitialBatchesForTheNextGroup(DistributedProcessModel process, ProcessExecutionId nextGroup, ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        super(modelService, flexibleSearchService);
        this.process = process;
        this.nextGroup = nextGroup;
    }


    private static final String INITIAL_BATCHES_GROUP_QUERY = INPUT_SELECT_CLAUSE + "from {ImportBatch as input} where {process}=?process and {type}=?type and {group}=?group";


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> nextExecutionInputBatches()
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "type", BatchType.INITIAL, "group",
                        Integer.valueOf(this.nextGroup.getGroup()));
        return queryForInputBatchRows(INITIAL_BATCHES_GROUP_QUERY, (Map)immutableMap)
                        .map(row -> DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)createInputBatch(row)));
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        this.process.setCurrentExecutionId(this.nextGroup.toString());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }
}
