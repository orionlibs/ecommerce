package de.hybris.platform.impex.distributed.process;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractImportProcessExecutionAnalysisContext implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    protected static String INPUT_SELECT_CLAUSE = "select {input.id}, {input.remainingWorkLoad}, {input.group}, {input.importContentCode}, {input.metadata} ";
    protected static String ROWS_SELECT_CLAUSE = INPUT_SELECT_CLAUSE + ", {result.id}, {result.remainingWorkLoad}, {result.group}, {result.importContentCode}, {result.metadata} ";
    private static final List<Class<?>> QUERY_RESULT_CLASSES = (List<Class<?>>)ImmutableList.of(String.class, Long.class, Integer.class, String.class, String.class, String.class, Long.class, Integer.class, String.class, String.class);
    private static final List<Class<?>> ONLY_INPUT_BATCH_CLASSES = QUERY_RESULT_CLASSES.subList(0, QUERY_RESULT_CLASSES
                    .size() / 2);
    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;


    public AbstractImportProcessExecutionAnalysisContext(ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        this.flexibleSearchService = flexibleSearchService;
        this.modelService = modelService;
    }


    public boolean processFailed()
    {
        return false;
    }


    public boolean processSucceeded()
    {
        return false;
    }


    protected ImportBatchModel createInputBatch(BatchExecutionData executionData)
    {
        ImportBatchModel batch = (ImportBatchModel)this.modelService.create(ImportBatchModel.class);
        BatchRow row = executionData.hasResultBatch() ? executionData.getResultBatch() : executionData.getInputBatch();
        batch.setId(row.getId());
        batch.setRemainingWorkLoad(row.getRemainingWorkLoad());
        batch.setGroup(row.getGroup());
        batch.setImportContentCode(row.getContentCode());
        batch.setMetadata(row.getMetadata());
        return batch;
    }


    protected Stream<BatchExecutionData> queryForInputBatchRows(String queryString, Map<String, Object> params)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
        query.setDisableCaching(true);
        query.setResultClassList(ONLY_INPUT_BATCH_CLASSES);
        SearchResult<List<Object>> queryResult = this.flexibleSearchService.search(query);
        return queryResult.getResult().stream().map(row -> new BatchExecutionData(createBatchRow(row, false), null));
    }


    protected Stream<BatchExecutionData> queryForBatchRows(String queryString, Map<String, Object> params)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
        query.setDisableCaching(true);
        query.setResultClassList(QUERY_RESULT_CLASSES);
        SearchResult<List<Object>> queryResult = this.flexibleSearchService.search(query);
        return queryResult.getResult().stream()
                        .map(row -> new BatchExecutionData(createBatchRow(row, false), createBatchRow(row, true)));
    }


    private BatchRow createBatchRow(List<Object> row, boolean result)
    {
        int idx = result ? 5 : 0;
        String id = (String)row.get(idx);
        if(id == null)
        {
            return null;
        }
        Long remainingWorkLoad = (Long)row.get(idx + 1);
        Integer group = (Integer)row.get(idx + 2);
        String contentCode = (String)row.get(idx + 3);
        String metadata = (String)row.get(idx + 4);
        return new BatchRow(id, remainingWorkLoad.longValue(), group.intValue(), contentCode, metadata);
    }
}
