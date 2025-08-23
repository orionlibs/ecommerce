package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableSet;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.media.ConsumeMarkerMediaModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.TaskContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.springframework.util.SerializationUtils;

public class ProcessChangesTask
{
    private static final Logger LOG = Logger.getLogger(ProcessChangesTask.class);
    private final ModelService modelService;
    private final MediaService mediaService;
    private final ChangeDetectionService changeDetectionService;
    private final TaskService taskService;
    private final TypeService typeService;
    private final Y2YSyncDAO y2YSyncDAO;
    private final List<ItemChangeDTO> changes;
    private final Collection<ImportScript> scripts;
    private final String conditionToTrigger;
    private final String syncExecutionId;
    private final String dataHubColumns;
    private final Y2YSyncType syncType;
    private final String dataHubType;


    public ProcessChangesTask(ModelService modelService, MediaService mediaService, ChangeDetectionService changeDetectionService, TaskService taskService, TypeService typeService, Y2YSyncDAO y2YSyncDAO, TaskContext context, Collection<ImportScript> importScripts)
    {
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService can't be null");
        this.mediaService = Objects.<MediaService>requireNonNull(mediaService, "mediaService can't be null");
        this.changeDetectionService = Objects.<ChangeDetectionService>requireNonNull(changeDetectionService, "changeDetectionService can't be null");
        this.taskService = Objects.<TaskService>requireNonNull(taskService, "taskService can't be null");
        this.typeService = Objects.<TypeService>requireNonNull(typeService, "typeService can't be null");
        this.y2YSyncDAO = Objects.<Y2YSyncDAO>requireNonNull(y2YSyncDAO, "y2YSyncDAO can't be null");
        this.scripts = Objects.<Collection<ImportScript>>requireNonNull(importScripts, "imporScripts can't be null");
        Objects.requireNonNull(context, "task context can't be null");
        this.changes = context.getChanges();
        this.conditionToTrigger = context.getConditionToTrigger();
        this.syncExecutionId = context.getSyncExecutionId();
        this.dataHubColumns = context.getDataHubColumns();
        this.syncType = context.getSyncType();
        this.dataHubType = context.getDataHubType();
    }


    public Collection<String> execute() throws Exception
    {
        Collection<String> createdMediaCodes = createMediaForScripts();
        consumeChangesOrCreateConsumeMarkers();
        triggerTaskCondition();
        return createdMediaCodes;
    }


    private Collection<String> createMediaForScripts()
    {
        ImmutableSet.Builder<String> resultBuilder = ImmutableSet.builder();
        for(ImportScript script : this.scripts)
        {
            SyncImpExMediaModel media = createMediaForScript(script);
            LOG.info("Created SyncImpExMediaModel >>>> " + media + " [syncExecutionID: " + this.syncExecutionId + "]");
            resultBuilder.add(media.getCode());
        }
        return (Collection<String>)resultBuilder.build();
    }


    private void consumeChangesOrCreateConsumeMarkers()
    {
        if(this.syncType == Y2YSyncType.ZIP)
        {
            this.changeDetectionService.consumeChanges(this.changes);
        }
        else if(this.syncType == Y2YSyncType.DATAHUB)
        {
            createConsumeMarkerMedia();
        }
    }


    private ConsumeMarkerMediaModel createConsumeMarkerMedia()
    {
        ConsumeMarkerMediaModel media = (ConsumeMarkerMediaModel)this.modelService.create(ConsumeMarkerMediaModel.class);
        media.setCode(UUID.randomUUID().toString());
        media.setSyncExecutionID(this.syncExecutionId);
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, SerializationUtils.serialize(this.changes));
        return media;
    }


    private void triggerTaskCondition()
    {
        this.taskService.triggerEvent(this.conditionToTrigger);
    }


    private SyncImpExMediaModel createMediaForScript(ImportScript script)
    {
        SyncImpExMediaModel media = (SyncImpExMediaModel)this.modelService.create(SyncImpExMediaModel.class);
        String code = script.getTypeCode() + "-" + script.getTypeCode();
        media.setCode(code);
        media.setMime("text/plain");
        media.setRealFileName(code);
        media.setImpexHeader(script.getHeader());
        media.setExportCronJob(this.y2YSyncDAO.findSyncCronJobByCode(this.syncExecutionId));
        media.setDataHubColumns(this.dataHubColumns);
        media.setDataHubType(this.dataHubType);
        media.setSyncType(this.typeService.getComposedTypeForCode(script.getTypeCode()));
        media.setMediaArchive((script.getMediaArchivePk() == null) ? null : (MediaModel)this.modelService.get(script.getMediaArchivePk()));
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, script.getContent().getBytes());
        return media;
    }
}
