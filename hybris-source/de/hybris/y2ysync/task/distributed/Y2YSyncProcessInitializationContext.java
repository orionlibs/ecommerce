package de.hybris.y2ysync.task.distributed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.deltadetection.collector.ItemTypeGroupingCollectorWithBatching;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Y2YSyncProcessInitializationContext implements DistributedProcessHandler.ProcessInitializationContext
{
    private static final String INITIAL_BATCHES_QUERY = "SELECT {id}, {remainingWorkLoad}, {context} FROM {Y2YBatch} WHERE {process}=?process and {type}=?type";
    private static final List<Class<?>> RESULT_CLASSES = (List<Class<?>>)ImmutableList.of(String.class, Integer.class, StreamConfiguration.class);
    private final Y2YDistributedProcessModel process;
    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;
    private final MediaService mediaService;
    private final TypeService typeService;
    private final ChangeDetectionService changeDetectionService;


    public Y2YSyncProcessInitializationContext(Y2YDistributedProcessModel process, FlexibleSearchService flexibleSearchService, ModelService modelService, MediaService mediaService, TypeService typeService, ChangeDetectionService changeDetectionService)
    {
        this.process = Objects.<Y2YDistributedProcessModel>requireNonNull(process, "process is required");
        this.flexibleSearchService = Objects.<FlexibleSearchService>requireNonNull(flexibleSearchService, "flexibleSearchService is required");
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService is required");
        this.mediaService = Objects.<MediaService>requireNonNull(mediaService, "mediaService is required");
        this.typeService = Objects.<TypeService>requireNonNull(typeService, "typeService is required");
        this.changeDetectionService = Objects.<ChangeDetectionService>requireNonNull(changeDetectionService, "changeDetectionService is required");
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> initializeProcess()
    {
        this.process.setCurrentExecutionId("EXPORT");
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)this.process);
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> firstExecutionInputBatches()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {id}, {remainingWorkLoad}, {context} FROM {Y2YBatch} WHERE {process}=?process and {type}=?type");
        query.addQueryParameter("process", this.process);
        query.addQueryParameter("type", BatchType.INITIAL);
        query.setResultClassList(RESULT_CLASSES);
        SearchResult<List> result = this.flexibleSearchService.search(query);
        Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> res = result.getResult().stream().flatMap(row -> {
            String id = row.get(0);
            int remainingWorkLoad = ((Integer)row.get(1)).intValue();
            StreamConfiguration streamConfiguration = (StreamConfiguration)row.get(2);
            ItemTypeGroupingCollectorWithBatching collector = collectChanges(streamConfiguration);
            return collector.getCreatedMedias().stream().map(());
        });
        return res;
    }


    private ItemTypeGroupingCollectorWithBatching collectChanges(StreamConfiguration streamConfiguration)
    {
        ItemTypeGroupingCollectorWithBatching collector = getGroupingCollector();
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(streamConfiguration.getItemTypeCode());
        this.changeDetectionService.collectChangesForType(type, streamConfiguration, (ChangesCollector)collector);
        return collector;
    }


    private ItemTypeGroupingCollectorWithBatching getGroupingCollector()
    {
        return new ItemTypeGroupingCollectorWithBatching(this.process.getCode(), this.process.getBatchSize(), this.modelService, this.mediaService);
    }


    private Map<String, Object> getContextForInputBatch(PK mediaPk, StreamConfiguration streamConfiguration)
    {
        return (Map<String, Object>)ImmutableMap.builder()
                        .put("mediaPK", mediaPk)
                        .put("impexHeader", streamConfiguration
                                        .getImpExHeader())
                        .put("typeCode", streamConfiguration
                                        .getItemTypeCode())
                        .put("dataHubColumns", streamConfiguration
                                        .getDataHubColumns())
                        .put("dataHubType", streamConfiguration
                                        .getDataHubType())
                        .put("syncType", streamConfiguration.getSyncType())
                        .build();
    }
}
