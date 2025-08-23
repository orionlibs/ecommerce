package de.hybris.platform.impex.distributed.process;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import de.hybris.platform.impex.distributed.log.DistributedImpexLogService;
import de.hybris.platform.impex.distributed.task.ProcessImpexTask;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.DistributedImportSplitErrorDumpModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.ImpexDocumentIdModel;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.defaultimpl.RemainingWorkloadBasedProgressCalculator;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.impex.impl.ImportServiceHelper;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDistributedImportProcessHandler implements DistributedProcessHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDistributedImportProcessHandler.class);
    private static final String CLEAR_DOCID_QUERY = "SELECT {PK} FROM {ImpexDocumentId} WHERE {processCode}=?processcode";
    private static final Set<String> TYPES_WITH_DISABLED_UNIQUENESS = (Set<String>)ImmutableSet.of("ImportBatch", "DistributedProcessWorkerTask", "TaskCondition", "ImportBatchContent", "LogFile");
    private final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final ProcessImpexTask processImpexTask;
    private final MediaService mediaService;
    private static final String LAST_TURN_BATCHES_QUERY = "SELECT {pk} FROM {ImportBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId";
    private static final String SPLIT_PHASE_DUMPED_LINES_QUERY = "SELECT {pk} FROM {DistributedImportSplitErrorDump} WHERE {processCode}=?code";
    private static final String GET_CONTENT_QUERY = "select {content} from {ImportBatchContent} where {code}=?code";


    public DefaultDistributedImportProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, ProcessImpexTask processImpexTask, MediaService mediaService)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        Objects.requireNonNull(processImpexTask, "processImpexTask mustn't be null");
        Objects.requireNonNull(mediaService, "mediaService mustn't be null");
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
        this.processImpexTask = processImpexTask;
        this.mediaService = mediaService;
    }


    @Deprecated(since = "6.2.0", forRemoval = true)
    public DefaultDistributedImportProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, ProcessImpexTask processImpexTask, DistributedImpexLogService logService, MediaService mediaService)
    {
        this(modelService, flexibleSearchService, processImpexTask, mediaService);
    }


    public Set<String> getTypesWithDisabledUniquenessCheck()
    {
        return TYPES_WITH_DISABLED_UNIQUENESS;
    }


    public ImportProcessCreationContext createProcessCreationContext(ProcessCreationData data)
    {
        Objects.requireNonNull(data, "data mustn't be null");
        ImportProcessCreationData processData = assureInstanceOf(data, ImportProcessCreationData.class);
        return new ImportProcessCreationContext(this.modelService, processData);
    }


    public ImportProcessInitializationContext createProcessInitializationContext(DistributedProcessModel process)
    {
        return new ImportProcessInitializationContext(this.flexibleSearchService, this.modelService,
                        asDistributedImportProcessModel(process));
    }


    public ImportProcessExecutionAnalysisContext createProcessExecutionAnalysisContext(DistributedProcessModel process)
    {
        return new ImportProcessExecutionAnalysisContext(process, this.modelService, this.flexibleSearchService);
    }


    public DistributedProcessHandler.ModelWithDependencies<BatchModel> createResultBatch(BatchModel batch)
    {
        ImportBatchModel inputBatch = assureInstanceOf(batch, ImportBatchModel.class);
        if(inputBatch == null)
        {
            throw new IllegalStateException("inputBatch must not be null");
        }
        DistributedImportProcessModel process = asDistributedImportProcessModel(inputBatch.getProcess());
        String content = getBatchContent(inputBatch);
        ImportMetadata batchMetadata = ImportMetadata.fromMetadata(inputBatch.getMetadata());
        ImportMetadata processMetadata = ImportMetadata.fromMetadata(process.getMetadata());
        Importer importer = new Importer(content, batchMetadata, processMetadata);
        LOG.debug("Importing batch {} (id={}, executionId={})", new Object[] {batch, batch.getId(), batch.getExecutionId()});
        this.processImpexTask.execute((ImportBatchHandler)importer);
        long remainingWorkLoad = importer.getRemainingWorkLoad();
        if(remainingWorkLoad == 0L)
        {
            LOG.info("Successfully imported batch {} (id={}, executionId={})", new Object[] {batch, batch.getId(), batch.getExecutionId()});
            return createBatchWithoutDump(inputBatch, importer);
        }
        LOG.info("Import of batch {} (id={}, executionId={}) finished with remaining workload computed to {}", new Object[] {batch, batch
                        .getId(), batch
                        .getExecutionId(), Long.valueOf(remainingWorkLoad)});
        return createBatchWithDump(inputBatch, importer);
    }


    public OptionalDouble calculateProgress(DistributedProcessModel process)
    {
        double progress = (new RemainingWorkloadBasedProgressCalculator(process, this.flexibleSearchService)).calculateProgress();
        return OptionalDouble.of(progress);
    }


    public void onFinished(DistributedProcessModel process)
    {
        clearDocumentIds(process.getCode());
        finishCronJob(process);
        clearImportBatchContent(process);
    }


    private void clearImportBatchContent(DistributedProcessModel process)
    {
        if(!isRemoveOnSuccessEnabledForImportBatchContent())
        {
            return;
        }
        ImpExImportCronJobModel cronJob = asDistributedImportProcessModel(process).getImpExImportCronJob();
        if(cronJob.getResult() == CronJobResult.SUCCESS && process.getState() == DistributedProcessState.SUCCEEDED)
        {
            ImportServiceHelper.clearImportBatchContent(this.modelService, process);
        }
    }


    private void clearDocumentIds(String processCode)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {ImpexDocumentId} WHERE {processCode}=?processcode", (Map)ImmutableMap.of("processcode", processCode));
        List<ImpexDocumentIdModel> allImpexDocumentIDs = this.flexibleSearchService.search(fQuery).getResult();
        this.modelService.removeAll(allImpexDocumentIDs);
    }


    private void finishCronJob(DistributedProcessModel process)
    {
        ImpExMediaModel lastTurnDump = createLastTurnDump(process);
        ImpExImportCronJobModel cronJob = asDistributedImportProcessModel(process).getImpExImportCronJob();
        cronJob.setStatus(CronJobStatus.FINISHED);
        if(lastTurnDump != null && process.getState() == DistributedProcessState.SUCCEEDED)
        {
            cronJob.setResult(CronJobResult.ERROR);
        }
        else
        {
            cronJob.setResult(getCorrespondingCronJobStatus(process.getState()));
        }
        cronJob.setUnresolvedDataStore(lastTurnDump);
        this.modelService.save(cronJob);
    }


    private ImpExMediaModel createLastTurnDump(DistributedProcessModel process)
    {
        String dumpedLines = collectLastTurnDumps(process);
        if(StringUtils.isEmpty(dumpedLines))
        {
            return null;
        }
        ImpExMediaModel media = (ImpExMediaModel)this.modelService.create(ImpExMediaModel.class);
        media.setCode(process.getCode());
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, dumpedLines.getBytes());
        return media;
    }


    private String collectLastTurnDumps(DistributedProcessModel process)
    {
        return collectFromSplitPhaseDump(process) + collectFromSplitPhaseDump(process);
    }


    private String collectFromBatchesDump(DistributedProcessModel process)
    {
        List<ImportBatchModel> result = findLastExecutionIdBatches(process);
        if(result.isEmpty())
        {
            return "";
        }
        String dump = result.stream().filter(b -> (b.getImportBatchContent() != null)).map(b -> b.getImportBatchContent().getContent()).filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
        return StringUtils.isEmpty(dump) ? "" : dump;
    }


    private String collectFromSplitPhaseDump(DistributedProcessModel process)
    {
        List<DistributedImportSplitErrorDumpModel> result = findDumpedLinesFromSplitPhase(process.getCode());
        if(result.isEmpty())
        {
            return "";
        }
        String dump = result.stream().map(b -> b.getContent()).filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
        return StringUtils.isEmpty(dump) ? "" : dump;
    }


    private List<ImportBatchModel> findLastExecutionIdBatches(DistributedProcessModel process)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ImportBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId");
        fQuery.addQueryParameter("process", process);
        fQuery.addQueryParameter("type", BatchType.RESULT);
        fQuery.addQueryParameter("executionId", process.getCurrentExecutionId());
        fQuery.setDisableCaching(true);
        SearchResult<ImportBatchModel> result = this.flexibleSearchService.search(fQuery);
        return result.getResult();
    }


    private List<DistributedImportSplitErrorDumpModel> findDumpedLinesFromSplitPhase(String processCode)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {DistributedImportSplitErrorDump} WHERE {processCode}=?code");
        fQuery.addQueryParameter("code", processCode);
        SearchResult<DistributedImportSplitErrorDumpModel> result = this.flexibleSearchService.search(fQuery);
        return result.getResult();
    }


    private CronJobResult getCorrespondingCronJobStatus(DistributedProcessState state)
    {
        switch(null.$SwitchMap$de$hybris$platform$processing$enums$DistributedProcessState[state.ordinal()])
        {
            case 1:
                status = CronJobResult.SUCCESS;
                return status;
            case 2:
                status = CronJobResult.ERROR;
                return status;
            case 3:
                status = CronJobResult.UNKNOWN;
                return status;
        }
        CronJobResult status = CronJobResult.UNKNOWN;
        return status;
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> createBatchWithoutDump(ImportBatchModel inputBatch, Importer importer)
    {
        ImportBatchModel resultBatch = createResultBasedOn(inputBatch, importer);
        resultBatch.setRemainingWorkLoad(0L);
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)resultBatch);
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> createBatchWithDump(ImportBatchModel inputBatch, Importer importer)
    {
        ImportBatchModel resultBatch = createResultBasedOn(inputBatch, importer);
        resultBatch.setRemainingWorkLoad(importer.getRemainingWorkLoad());
        if(inputBatch.getRemainingWorkLoad() == resultBatch.getRemainingWorkLoad() && importer
                        .getImportResult().equals(inputBatch.getImportBatchContent().getContent()))
        {
            resultBatch.setImportContentCode(inputBatch.getImportContentCode());
            return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)resultBatch);
        }
        ImportBatchContentModel content = (ImportBatchContentModel)this.modelService.create(ImportBatchContentModel.class);
        content.setCode(getUniqueCode(inputBatch));
        content.setContent(importer.getImportResult());
        resultBatch.setImportContentCode(content.getCode());
        return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)resultBatch, new ItemModel[] {(ItemModel)content});
    }


    private String getUniqueCode(ImportBatchModel inputBatch)
    {
        return inputBatch.getProcess().getCode() + "_" + inputBatch.getProcess().getCode() + "_" + inputBatch.getExecutionId();
    }


    private ImportBatchModel createResultBasedOn(ImportBatchModel inputBatch, Importer importer)
    {
        ImportBatchModel result = (ImportBatchModel)this.modelService.create(ImportBatchModel.class);
        result.setId(inputBatch.getId());
        result.setExecutionId(inputBatch.getExecutionId());
        result.setType(BatchType.RESULT);
        result.setGroup(inputBatch.getGroup());
        result.setMetadata(importer.dumpMetadata());
        return result;
    }


    private String getBatchContent(ImportBatchModel batch)
    {
        ImmutableMap immutableMap = ImmutableMap.of("code", batch.getImportContentCode());
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {content} from {ImportBatchContent} where {code}=?code", (Map)immutableMap);
        query.setResultClassList((List)ImmutableList.of(String.class));
        query.setDisableCaching(true);
        SearchResult<String> queryResult = this.flexibleSearchService.search(query);
        Preconditions.checkState((queryResult.getCount() == 1), "Expected exactly one %s with code '%s' but %s has been found.", "ImportBatchContent", batch
                                        .getImportContentCode(),
                        Integer.valueOf(queryResult.getCount()));
        return queryResult.getResult().get(0);
    }


    private static <T> T assureInstanceOf(Object object, Class<T> expctedClass)
    {
        if(object == null)
        {
            return null;
        }
        Preconditions.checkArgument(expctedClass.isInstance(object), "object %s is not instance of %s", object, expctedClass);
        return (T)object;
    }


    private DistributedImportProcessModel asDistributedImportProcessModel(DistributedProcessModel process)
    {
        Preconditions.checkState(process instanceof DistributedImportProcessModel, "process must be instance of DistributedImportProcessModel");
        return (DistributedImportProcessModel)process;
    }


    private boolean isRemoveOnSuccessEnabledForImportBatchContent()
    {
        return Config.getBoolean("impex.distributed.importbatchcontent.removeonsuccess", false);
    }
}
