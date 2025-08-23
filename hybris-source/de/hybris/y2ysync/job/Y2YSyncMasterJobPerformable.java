package de.hybris.y2ysync.job;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.cronjob.TypeAwareJobPerformable;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.logging.Logs;
import de.hybris.y2ysync.deltadetection.collector.ItemTypeGroupingCollectorWithBatching;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.internal.MediasForType;
import de.hybris.y2ysync.task.internal.SyncTaskFactory;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class Y2YSyncMasterJobPerformable extends AbstractJobPerformable<Y2YSyncCronJobModel> implements TypeAwareJobPerformable
{
    private static final Logger LOG = Logger.getLogger(Y2YSyncMasterJobPerformable.class);
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final String COLUMNS_DELIMITER = ";";
    private static final Joiner COMA_JOINER = Joiner.on(",");
    private ChangeDetectionService changeDetectionService;
    private MediaService mediaService;
    private SyncTaskFactory syncTaskFactory;


    public PerformResult perform(Y2YSyncCronJobModel cronJob)
    {
        Y2YSyncJobModel job = cronJob.getJob();
        Y2YStreamConfigurationContainerModel streamConfigurationContainer = job.getStreamConfigurationContainer();
        if(streamConfigurationContainer == null)
        {
            LOG.error("CronJob: " + cronJob.getCode() + " configuration container is missing");
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        List<MediasForType> allMedias = Lists.newArrayList();
        Map<String, Object> globalQueryParameters = new HashMap<>();
        fillParameters(globalQueryParameters, streamConfigurationContainer.getCatalogVersion());
        Set<StreamConfigurationModel> configurations = getActiveConfigurations(streamConfigurationContainer);
        if(configurations.isEmpty())
        {
            LOG.error("CronJob: " + cronJob.getCode() + " configuration container is empty");
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        for(StreamConfigurationModel streamConfiguration : configurations)
        {
            Y2YStreamConfigurationModel y2yStreamConfiguration = (Y2YStreamConfigurationModel)streamConfiguration;
            ItemTypeGroupingCollectorWithBatching collector = new ItemTypeGroupingCollectorWithBatching(cronJob.getCode(), getBatchSize(), this.modelService, getMediaService());
            StreamConfiguration configuration = toStreamConfiguration(globalQueryParameters, y2yStreamConfiguration);
            getChangeDetectionService().collectChangesForType(streamConfiguration
                            .getItemTypeForStream(), configuration, (ChangesCollector)collector);
            LOG.info("Collected changes for type: " + streamConfiguration.getItemTypeForStream() + " in medias: " + COMA_JOINER
                            .join(collector.getCreatedMedias()));
            MediasForType mediasForType = MediasForType.builder().withComposedTypeCode(y2yStreamConfiguration.getItemTypeForStream().getCode()).withImpExHeader(join(y2yStreamConfiguration, Y2YColumnDefinitionModel::getImpexHeader))
                            .withDataHubColumns(join(y2yStreamConfiguration, Y2YColumnDefinitionModel::getColumnName)).withMediaPks(collector.getCreatedMedias()).withDataHubType(StringUtils.defaultString(y2yStreamConfiguration.getDataHubType())).build();
            allMedias.add(mediasForType);
        }
        if(isChangesDetected(allMedias))
        {
            createAllTasksInTx(cronJob.getCode(), allMedias, job.getSyncType(), job.getNodeGroup());
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.RUNNING);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected boolean isChangesDetected(List<MediasForType> allMedias)
    {
        return allMedias.stream().anyMatch(m -> CollectionUtils.isNotEmpty(m.getMediaPks()));
    }


    protected String join(Y2YStreamConfigurationModel y2yStreamConfiguration, Function<Y2YColumnDefinitionModel, String> mapper)
    {
        String columns = y2yStreamConfiguration.getColumnDefinitions().stream().sorted(Comparator.comparing(Y2YColumnDefinitionModel::getPosition)).<CharSequence>map((Function)mapper).collect(Collectors.joining(";"));
        Logs.debug(LOG, () -> "StreamConfig Columns string for type: " + y2yStreamConfiguration.getItemTypeForStream().getCode() + " >>> " + columns);
        return columns;
    }


    protected int getBatchSize()
    {
        return (int)Config.getLong("y2ysync.batch.size", 100L);
    }


    protected Set<StreamConfigurationModel> getActiveConfigurations(Y2YStreamConfigurationContainerModel container)
    {
        return (Set<StreamConfigurationModel>)container.getConfigurations().stream().filter(c -> c.getActive().booleanValue()).collect(Collectors.toSet());
    }


    protected void createAllTasksInTx(String syncExecutionId, List<MediasForType> allMedias, Y2YSyncType syncType, String nodeGroup)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, syncExecutionId, syncType, allMedias, nodeGroup));
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Exception occurred while creating tasks", e);
        }
    }


    protected StreamConfiguration toStreamConfiguration(Map<String, Object> globalQueryParameters, Y2YStreamConfigurationModel y2YStreamConfigurationModel)
    {
        Map<String, Object> streamQueryParameters = new HashMap<>(globalQueryParameters);
        fillParameters(streamQueryParameters, y2YStreamConfigurationModel.getCatalogVersion());
        return StreamConfiguration.buildFor(y2YStreamConfigurationModel.getStreamId())
                        .withItemSelector(y2YStreamConfigurationModel.getWhereClause())
                        .withVersionValue(y2YStreamConfigurationModel.getVersionSelectClause())
                        .withExcludedTypeCodes(y2YStreamConfigurationModel.getExcludedTypes())
                        .withParameters(streamQueryParameters);
    }


    protected void fillParameters(Map<String, Object> globalQueryParameters, CatalogVersionModel catalogVersion)
    {
        if(catalogVersion == null)
        {
            return;
        }
        globalQueryParameters.put("catalog", catalogVersion.getCatalog());
        globalQueryParameters.put("catalogVersion", catalogVersion);
        globalQueryParameters.put("catalogVersionCode", catalogVersion.getVersion());
        String catalogName = catalogVersion.getCatalog().getName();
        if(StringUtils.isNotEmpty(catalogName))
        {
            globalQueryParameters.put("catalogName", catalogName);
        }
    }


    @Required
    public void setChangeDetectionService(ChangeDetectionService changeDetectionService)
    {
        this.changeDetectionService = changeDetectionService;
    }


    protected ChangeDetectionService getChangeDetectionService()
    {
        return this.changeDetectionService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setSyncTaskFactory(SyncTaskFactory syncTaskFactory)
    {
        this.syncTaskFactory = syncTaskFactory;
    }


    public String getType()
    {
        return null;
    }


    public boolean createDefaultJob()
    {
        return false;
    }
}
