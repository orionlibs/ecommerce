package de.hybris.platform.impex.distributed.process;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ImportProcessInitializationContext implements DistributedProcessHandler.ProcessInitializationContext
{
    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;
    private final DistributedImportProcessModel process;
    private static final String QUERY = "select {id}, {remainingWorkLoad}, {group}, {importContentCode} from {ImportBatch} where {process}=?process and {type}=?type and {group}=?group";


    public ImportProcessInitializationContext(FlexibleSearchService flexibleSearchService, ModelService modelService, DistributedImportProcessModel process)
    {
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(process, "process mustn't be null");
        this.flexibleSearchService = flexibleSearchService;
        this.modelService = modelService;
        this.process = process;
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> initializeProcess()
    {
        this.process.setCurrentExecutionId(ProcessExecutionId.INITIAL.toString());
        ImpExImportCronJobModel cronJob = this.process.getImpExImportCronJob();
        cronJob.setStatus(CronJobStatus.RUNNING);
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }


    private static final List<Class<?>> RESULT_CLASSES = (List<Class<?>>)ImmutableList.of(String.class, Long.class, Integer.class, String.class);


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> firstExecutionInputBatches()
    {
        ImmutableMap immutableMap = ImmutableMap.of("process", this.process, "type", BatchType.INITIAL, "group",
                        Integer.valueOf(0));
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {id}, {remainingWorkLoad}, {group}, {importContentCode} from {ImportBatch} where {process}=?process and {type}=?type and {group}=?group", (Map)immutableMap);
        query.setDisableCaching(true);
        query.setResultClassList(RESULT_CLASSES);
        SearchResult<List<Object>> queryResult = this.flexibleSearchService.search(query);
        return queryResult.getResult().stream().map(row -> {
            String id = row.get(0);
            Long remainingWorkLoad = (Long)row.get(1);
            Integer group = (Integer)row.get(2);
            String contentCode = row.get(3);
            ImportBatchModel batch = (ImportBatchModel)this.modelService.create(ImportBatchModel.class);
            batch.setId(id);
            batch.setRemainingWorkLoad(remainingWorkLoad.longValue());
            batch.setGroup(group.intValue());
            batch.setImportContentCode(contentCode);
            batch.setType(BatchType.INPUT);
            return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)batch);
        });
    }
}
