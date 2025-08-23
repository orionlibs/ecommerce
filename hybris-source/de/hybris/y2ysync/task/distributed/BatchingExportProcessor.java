package de.hybris.y2ysync.task.distributed;

import com.google.common.base.Preconditions;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YBatchModel;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.media.ConsumeMarkerMediaModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.runner.internal.ExportScriptCreator;
import de.hybris.y2ysync.task.runner.internal.ImportScript;
import de.hybris.y2ysync.task.runner.internal.ImportScriptCreator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

public class BatchingExportProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(BatchingExportProcessor.class);
    private final ModelService modelService;
    private final MediaService mediaService;
    private final ExportService exportService;
    private final TypeService typeService;
    private final ChangeDetectionService changeDetectionService;
    private final UserService userService;


    public BatchingExportProcessor(ModelService modelService, MediaService mediaService, ExportService exportService, TypeService typeService, ChangeDetectionService changeDetectionService, UserService userService)
    {
        this.modelService = modelService;
        this.mediaService = mediaService;
        this.exportService = exportService;
        this.typeService = typeService;
        this.changeDetectionService = changeDetectionService;
        this.userService = userService;
    }


    public BatchModel getResultBatch(Y2YBatchModel inputBatch)
    {
        Objects.requireNonNull(inputBatch, "inputBatch is required");
        InputBatchContext inputBatchContext = getInputBatchContext(inputBatch);
        List<ItemChangeDTO> changes = getChanges(inputBatchContext.getMediaPk());
        ImportScriptCreator importScriptCreator = getImportScriptCreator(inputBatchContext, changes);
        Collection<ImportScript> importScripts = importScriptCreator.createImportScripts();
        createMediaForScripts(importScripts, inputBatchContext, getCronJobForBatch(inputBatch));
        Y2YBatchModel resultBatch = createResultBatch(inputBatch);
        consumeChangesOrCreateConsumeMarkers(changes, inputBatchContext.getSyncType(), inputBatch.getProcess().getCode());
        return (BatchModel)resultBatch;
    }


    private Y2YSyncCronJobModel getCronJobForBatch(Y2YBatchModel inputBatch)
    {
        Y2YDistributedProcessModel process = (Y2YDistributedProcessModel)inputBatch.getProcess();
        return process.getY2ySyncCronJob();
    }


    private InputBatchContext getInputBatchContext(Y2YBatchModel inputBatch)
    {
        Object context = inputBatch.getContext();
        Preconditions.checkState(context instanceof Map, "context must be an instance of a map");
        return new InputBatchContext(this, (Map)context);
    }


    private List<ItemChangeDTO> getChanges(PK mediaPk)
    {
        MediaModel media = (MediaModel)this.modelService.get(mediaPk);
        byte[] data = this.mediaService.getDataFromMedia(media);
        return (List<ItemChangeDTO>)SerializationUtils.deserialize(data);
    }


    private ImportScriptCreator getImportScriptCreator(InputBatchContext context, List<ItemChangeDTO> changes)
    {
        ExportScriptCreator exportScriptCreator = new ExportScriptCreator(context.getImpExHeader(), context.getTypeCode(), changes);
        return new ImportScriptCreator(this.modelService, this.exportService, exportScriptCreator, this.userService);
    }


    private void createMediaForScripts(Collection<ImportScript> scripts, InputBatchContext inputBatchContext, Y2YSyncCronJobModel cronJob)
    {
        if(CollectionUtils.isEmpty(scripts))
        {
            return;
        }
        for(ImportScript script : scripts)
        {
            SyncImpExMediaModel media = createMediaForScript(script, inputBatchContext, cronJob);
            LOG.debug("Created SyncImpExMediaModel >>>> {}", media);
        }
    }


    private SyncImpExMediaModel createMediaForScript(ImportScript script, InputBatchContext inputBatchContext, Y2YSyncCronJobModel cronJob)
    {
        SyncImpExMediaModel media = (SyncImpExMediaModel)this.modelService.create(SyncImpExMediaModel.class);
        String code = script.getTypeCode() + "-" + script.getTypeCode();
        media.setCode(code);
        media.setMime("text/plain");
        media.setRealFileName(code);
        media.setExportCronJob(cronJob);
        media.setImpexHeader(script.getHeader());
        media.setDataHubColumns(inputBatchContext.getDataHubColumns());
        media.setDataHubType(inputBatchContext.getDataHubType());
        media.setSyncType(this.typeService.getComposedTypeForCode(script.getTypeCode()));
        media.setMediaArchive((script.getMediaArchivePk() == null) ? null : (MediaModel)this.modelService.get(script.getMediaArchivePk()));
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, script.getContent().getBytes());
        return media;
    }


    private void consumeChangesOrCreateConsumeMarkers(List<ItemChangeDTO> changes, Y2YSyncType syncType, String syncExecutionId)
    {
        if(syncType == Y2YSyncType.ZIP)
        {
            this.changeDetectionService.consumeChanges(changes);
        }
        else if(syncType == Y2YSyncType.DATAHUB)
        {
            createConsumeMarkerMedia(syncExecutionId, changes);
        }
    }


    private ConsumeMarkerMediaModel createConsumeMarkerMedia(String syncExecutionId, List<ItemChangeDTO> changes)
    {
        ConsumeMarkerMediaModel media = (ConsumeMarkerMediaModel)this.modelService.create(ConsumeMarkerMediaModel.class);
        media.setCode(UUID.randomUUID().toString());
        media.setSyncExecutionID(syncExecutionId);
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, SerializationUtils.serialize(changes));
        return media;
    }


    private Y2YBatchModel createResultBatch(Y2YBatchModel inputBatch)
    {
        Y2YBatchModel resultBatch = (Y2YBatchModel)this.modelService.create(Y2YBatchModel.class);
        resultBatch.setId(inputBatch.getId());
        resultBatch.setType(BatchType.RESULT);
        resultBatch.setContext(inputBatch.getContext());
        resultBatch.setRemainingWorkLoad(0L);
        return resultBatch;
    }
}
