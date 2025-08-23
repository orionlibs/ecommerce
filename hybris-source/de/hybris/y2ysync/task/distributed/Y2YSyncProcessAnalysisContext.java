package de.hybris.y2ysync.task.distributed;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import de.hybris.y2ysync.model.Y2YBatchModel;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Y2YSyncProcessAnalysisContext implements DistributedProcessHandler.ProcessExecutionAnalysisContext
{
    private static final Logger LOG = LoggerFactory.getLogger(Y2YSyncProcessAnalysisContext.class);
    private static final String COUNT_ERROR_RESULT_BATCH_FOR_TURN = "SELECT count({pk}) FROM {Y2YBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId AND {error}=?error";
    private static final String FIND_ERROR_RESULT_BATCH_FOR_FINALIZE_TURN = "SELECT {pk} FROM {Y2YBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId AND {error}=?error AND {finalize}=?finalize";
    private static final int DEFAULT_NUM_OF_UPLOAD_RETRIES = 3;
    private final Y2YDistributedProcessModel process;
    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;
    private final DistributedProcessService distributedProcessService;
    private final DistributedProcessHandler.ProcessExecutionAnalysisContext concreteCtx;


    public Y2YSyncProcessAnalysisContext(Y2YDistributedProcessModel process, FlexibleSearchService flexibleSearchService, ModelService modelService, DistributedProcessService distributedProcessService)
    {
        this.process = process;
        this.flexibleSearchService = flexibleSearchService;
        this.modelService = modelService;
        this.distributedProcessService = distributedProcessService;
        this.concreteCtx = createConcreteCtx();
    }


    private DistributedProcessHandler.ProcessExecutionAnalysisContext createConcreteCtx()
    {
        if(isExportTurn())
        {
            if(thereAreUnfinishedToExportBatches())
            {
                LOG.error("Process has failed in EXPORT phase [process code: {}]", this.process.getCode());
                this.distributedProcessService.requestToStop(this.process.getCode());
                updateCorrespondingCronJob(CronJobStatus.FINISHED, CronJobResult.ERROR);
                return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ProcessFailed((DistributedProcessModel)this.process);
            }
            return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ToUploadTurn((DistributedProcessModel)this.process, this.modelService);
        }
        if(isUploadTurn())
        {
            Optional<Y2YBatchModel> inputBatch = tryFindErrorResultBatch();
            if(inputBatch.isPresent())
            {
                if(((Y2YBatchModel)inputBatch.get()).getRetries().intValue() == 1)
                {
                    LOG.error("Process has failed in UPLOAD phase after {} num of attempts [process code: {}]",
                                    getNumberOfRetries(), this.process.getCode());
                    this.distributedProcessService.requestToStop(this.process.getCode());
                    updateCorrespondingCronJob(CronJobStatus.FINISHED, CronJobResult.ERROR);
                    return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new ProcessFailed((DistributedProcessModel)this.process);
                }
                return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new RetryToUploadTurn((DistributedProcessModel)this.process, this.modelService, inputBatch.get());
            }
        }
        updateCorrespondingCronJob(CronJobStatus.FINISHED, CronJobResult.SUCCESS);
        return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new FinishProcess((DistributedProcessModel)this.process);
    }


    private boolean isExportTurn()
    {
        return "EXPORT".equals(this.process.getCurrentExecutionId());
    }


    private boolean isUploadTurn()
    {
        return this.process.getCurrentExecutionId().startsWith("UPLOAD");
    }


    private boolean thereAreUnfinishedToExportBatches()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT count({pk}) FROM {Y2YBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId AND {error}=?error");
        fQuery.setDisableCaching(true);
        fQuery.setResultClassList((List)ImmutableList.of(Long.class));
        fQuery.addQueryParameter("process", this.process);
        fQuery.addQueryParameter("type", BatchType.RESULT);
        fQuery.addQueryParameter("executionId", this.process.getCurrentExecutionId());
        fQuery.addQueryParameter("error", Boolean.TRUE);
        Long result = this.flexibleSearchService.search(fQuery).getResult().get(0);
        return (result.longValue() > 0L);
    }


    private Optional<Y2YBatchModel> tryFindErrorResultBatch()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YBatch} WHERE {process}=?process AND {type}=?type AND {executionId}=?executionId AND {error}=?error AND {finalize}=?finalize");
        fQuery.setDisableCaching(true);
        fQuery.addQueryParameter("process", this.process);
        fQuery.addQueryParameter("type", BatchType.RESULT);
        fQuery.addQueryParameter("executionId", this.process.getCurrentExecutionId());
        fQuery.addQueryParameter("error", Boolean.TRUE);
        fQuery.addQueryParameter("finalize", Boolean.TRUE);
        SearchResult<Y2YBatchModel> searchResult = this.flexibleSearchService.search(fQuery);
        List<Y2YBatchModel> result = searchResult.getResult();
        return (searchResult.getCount() == 0) ? Optional.<Y2YBatchModel>empty() : Optional.<Y2YBatchModel>of(result.get(0));
    }


    private void updateCorrespondingCronJob(CronJobStatus status, CronJobResult result)
    {
        Y2YSyncCronJobModel cronJob = this.process.getY2ySyncCronJob();
        cronJob.setStatus(status);
        cronJob.setResult(result);
        this.modelService.save(cronJob);
    }


    static Integer getNumberOfRetries()
    {
        return Integer.valueOf(Config.getInt("y2ysync.datahub.upload.retries", 3));
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
        return this.concreteCtx.nextExecutionInputBatches();
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> prepareProcessForNextExecution()
    {
        return this.concreteCtx.prepareProcessForNextExecution();
    }
}
