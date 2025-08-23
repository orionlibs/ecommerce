package de.hybris.deltadetection.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.impl.DBSpecificStreamingHints;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class DefaultChangeDetectionService implements ChangeDetectionService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultChangeDetectionService.class);
    private static final int ITEMPK_INDEX = 0;
    private static final int COMPOSED_TYPE_INDEX = 1;
    private static final int VERSION_INDEX = 2;
    private static final int CHANGE_TYPE_INDEX = 3;
    private static final int INFO_INDEX = 4;
    private static final int VERSION_VALUE_INDEX = 5;
    private static final String BATCH_SIZE_PARAM = "deltadetection.changeconsumption.batch";
    private static final String BATCH_RETRIES_PARAM = "deltadetection.changeconsumption.batch.retries";
    private static final String CHANGES_CONSUMPTION_THREADS_PARAM = "deltadetection.changeconsumption.threads";
    private static final String DELETED_ITEMS_QUERY_TEMPLATE = "SELECT {ivm.itemPK} AS itemPK, {ivm.itemComposedType} AS itemComposedType, {ivm.versionTS} AS version, 'DELETED' AS changeType, {ivm.info} AS info, {ivm.versionValue} AS versionValue FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemComposedType} IN(%s) AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND NOT EXISTS({{SELECT {item.PK} FROM {%s as item} WHERE {ivm.itemPK}={item.PK}%s}})";
    private static final String MODIFIED_ITEMS_QUERY_TEMPLATE = "SELECT {item.PK} AS itemPK, {item.itemType} AS itemComposedType, {item.modifiedTime} AS version, 'MODIFIED' AS changeType, {ivm.info} AS info, %s AS versionValue FROM {%s AS item JOIN ItemVersionMarker AS ivm ON {ivm.itemPK}={item.PK}} WHERE {item.itemType} IN(%s)%s AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND ((?useVersionValue=1 AND ({ivm.versionValue}<>%s OR ({ivm.versionValue} is null AND %s is not null) OR ({ivm.versionValue} is not null AND %s is null))) OR (?useVersionValue<>1 AND {item.modifiedTime}>{ivm.versionTS}))";
    private static final String NEW_ITEMS_QUERY_TEMPLATE = "SELECT {item.PK} AS itemPK, {item.itemType} AS itemComposedType, {item.modifiedTime} AS version, 'NEW' AS changeType, 'created' AS info, %s AS versionValue FROM {%s AS item} WHERE {item.itemType} IN(%s)%s AND NOT EXISTS({{SELECT {ivm.PK} FROM {ItemVersionMarker as ivm} WHERE {ivm.itemPK}={item.PK} AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive}})";
    private static final String NEW_OR_MODIFIED_VERSION_MARKERS_QUERY_TEMPLATE = "SELECT {ivm.PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND EXISTS({{SELECT {item.PK} FROM {%s as item} WHERE {ivm.itemPK}={item.PK}}})";
    private static final String DELETED_VERSION_MARKERS_QUERY = "SELECT {ivm.PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId AND {ivm.status}=?statusDeleted";
    private static final ExpressionParser SPEL_EXPRESSION_PARSER = (ExpressionParser)new SpelExpressionParser();
    private static final TemplateParserContext PARSER_CONTEXT = new TemplateParserContext();
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private TypeService typeService;
    protected static final String VERSION_MARKERS_FOR_STREAM_BY_PKS_QRY = "SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemPK} in (?itemPK) AND {ivm.streamId} = ?streamId";
    protected static final String VERSION_MARKER_BY_PK = "SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemPK} = ?itemPK AND {ivm.streamId} = ?streamId";


    public ItemChangeDTO getChangeForExistingItem(ItemModel item, String streamId)
    {
        if(getSavedModel(item.getPk()) == null)
        {
            throw new IllegalStateException("Item with Pk=" + item.getPk() + " does NOT exist!");
        }
        ItemVersionMarkerModel marker = findVersionMarkerByItemPK(streamId, item.getPk().getLong());
        if(marker == null)
        {
            return new ItemChangeDTO(item.getPk().getLong(), item.getModifiedtime(), ChangeType.NEW, "New item created(" + item
                            .toString() + ")", item.getItemtype(), streamId);
        }
        if(marker.getVersionTS().before(item.getModifiedtime()))
        {
            return new ItemChangeDTO(item.getPk().getLong(), item.getModifiedtime(), ChangeType.MODIFIED, marker.getInfo(), item
                            .getItemtype(), streamId);
        }
        return null;
    }


    public ItemChangeDTO getChangeForRemovedItem(PK pk, String streamId)
    {
        if(getSavedModel(pk) != null)
        {
            throw new IllegalStateException("Item with Pk=" + pk + " still exists!");
        }
        ItemVersionMarkerModel marker = findVersionMarkerByItemPK(streamId, pk.getLong());
        if(isItemVersionMarkerActive(marker))
        {
            return new ItemChangeDTO(marker.getItemPK(), marker.getVersionTS(), ChangeType.DELETED, marker.getInfo(), marker
                            .getItemComposedType().getCode(), streamId);
        }
        return null;
    }


    protected boolean isItemVersionMarkerActive(ItemVersionMarkerModel marker)
    {
        return (marker != null && marker.getStatus().equals(ItemVersionMarkerStatus.ACTIVE));
    }


    public List<ItemChangeDTO> getChangesForRemovedItems(String streamId)
    {
        ArrayList<ItemChangeDTO> changes = new ArrayList<>();
        for(ItemVersionMarkerModel marker : getVersionMarkersForRemovedItems(streamId))
        {
            if(marker != null)
            {
                changes.add(new ItemChangeDTO(marker.getItemPK(), marker.getVersionTS(), ChangeType.DELETED, "Item has been removed(" + marker
                                .getInfo() + ")", marker
                                .getItemComposedType().getCode(), streamId));
            }
        }
        return changes;
    }


    public void collectChangesForType(ComposedTypeModel composedType, String streamId, ChangesCollector collector)
    {
        collectChangesForType(composedType, StreamConfiguration.buildFor(streamId), collector);
    }


    public void collectChangesForType(ComposedTypeModel composedType, StreamConfiguration configuration, ChangesCollector collector)
    {
        Stopwatch stopwatchTotal = Stopwatch.createStarted();
        Stopwatch stopwatchQuery = Stopwatch.createStarted();
        String query = prepareQueryForFindingChangesByType(composedType, configuration);
        FlexibleSearchQuery fq = new FlexibleSearchQuery(query);
        fq.addQueryParameter("streamId", configuration.getStreamId());
        fq.addQueryParameter("statusActive", ItemVersionMarkerStatus.ACTIVE);
        fq.addQueryParameters(configuration.getParameters());
        Integer useVersionValue = StringUtils.isBlank(configuration.getVersionValue()) ? Integer.valueOf(0) : Integer.valueOf(1);
        fq.addQueryParameter("useVersionValue", useVersionValue);
        fq.setResultClassList(
                        Arrays.asList((Class<?>[][])new Class[] {Long.class, ComposedTypeModel.class, Date.class, String.class, String.class, String.class}));
        fq.addHints(new Hint[] {(Hint)DBSpecificStreamingHints.getInstance()});
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Query to be executed: \n{}\n", query);
        }
        Stopwatch stopwatchCollect = Stopwatch.createUnstarted();
        try
        {
            getFlexibleSearchService().processSearchRows(fq,
                            getQueryCollector(configuration, collector, stopwatchQuery, stopwatchCollect));
        }
        finally
        {
            Stopwatch stopwatchFinish = Stopwatch.createStarted();
            collector.finish();
            stopwatchFinish.stop();
            LOG.info("CollectChangesForType has been finished for type: '{}', with streamId: '{}' using external collector. Total time: {}, QueryTime: {}, Collecting time: {}, Finishing time: {}", new Object[] {composedType
                            .getName(), configuration
                            .getStreamId(), stopwatchTotal, stopwatchQuery, stopwatchCollect, stopwatchFinish});
        }
    }


    private Predicate<List<Object>> getQueryCollector(StreamConfiguration configuration, ChangesCollector collector, Stopwatch stopwatchQuery, Stopwatch stopwatchCollect)
    {
        return row -> {
            if(stopwatchQuery.isRunning())
            {
                stopwatchQuery.stop();
            }
            stopwatchCollect.start();
            boolean collect = collector.collect(getChangeFromRow(row, configuration.getStreamId()));
            stopwatchCollect.stop();
            return collect;
        };
    }


    protected ItemChangeDTO getChangeFromRow(List<Object> row, String streamId)
    {
        String itemComposedType = ((ComposedTypeModel)row.get(1)).getCode();
        Long itemPK = (Long)row.get(0);
        Date version = (Date)row.get(2);
        ChangeType changeType = ChangeType.valueOf(((String)row.get(3)).trim());
        String info = (String)row.get(4);
        String versionValue = (String)row.get(5);
        return (new ItemChangeDTO(itemPK, version, changeType, info, itemComposedType, streamId)).withVersionValue(versionValue);
    }


    protected String prepareQueryForFindingChangesByType(ComposedTypeModel composedType, StreamConfiguration configuration)
    {
        StringBuilder typePksAsQueryParams = new StringBuilder();
        typePksAsQueryParams.append(composedType.getPk().getLong());
        Set<String> excludedTypeCodes = configuration.getExcludedTypeCodes();
        composedType.getAllSubTypes().stream().filter(type -> !excludedTypeCodes.contains(type.getCode()))
                        .forEach(type -> typePksAsQueryParams.append(",").append(type.getPk().getLong()));
        String typeCode = composedType.getCode();
        String itemSelector = configuration.getItemSelector();
        String versionValue = configuration.getVersionValue();
        String queryForDeleted = getQueryForDeletedItems(typePksAsQueryParams.toString(), typeCode, itemSelector);
        String queryForModified = getQueryForModifiedItems(typePksAsQueryParams.toString(), typeCode, itemSelector, versionValue);
        String queryForNew = getQueryForNewItems(typePksAsQueryParams.toString(), typeCode, itemSelector, versionValue);
        String combinedQuery = "SELECT t.itemPK, t.itemComposedType, t.version, t.changeType, t.info, t.versionValue FROM( {{" + queryForNew + "}} UNION ALL {{" + queryForModified + "}} UNION ALL {{" + queryForDeleted + "}} )t";
        return combinedQuery;
    }


    protected String getQueryForDeletedItems(String typePksAsQueryParams, String baseType, String itemSelector)
    {
        String wherePart = StringUtils.isBlank(itemSelector) ? "" : (" AND (" + itemSelector + ")");
        return String.format(
                        "SELECT {ivm.itemPK} AS itemPK, {ivm.itemComposedType} AS itemComposedType, {ivm.versionTS} AS version, 'DELETED' AS changeType, {ivm.info} AS info, {ivm.versionValue} AS versionValue FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemComposedType} IN(%s) AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND NOT EXISTS({{SELECT {item.PK} FROM {%s as item} WHERE {ivm.itemPK}={item.PK}%s}})",
                        new Object[] {typePksAsQueryParams, baseType, wherePart});
    }


    protected String getQueryForModifiedItems(String typePksAsQueryParams, String baseType, String itemSelector, String versionValuePart)
    {
        String wherePart = StringUtils.isBlank(itemSelector) ? "" : (" AND (" + itemSelector + ")");
        String versionValue = StringUtils.isBlank(versionValuePart) ? "('1')" : ("(" + versionValuePart + ")");
        return String.format(
                        "SELECT {item.PK} AS itemPK, {item.itemType} AS itemComposedType, {item.modifiedTime} AS version, 'MODIFIED' AS changeType, {ivm.info} AS info, %s AS versionValue FROM {%s AS item JOIN ItemVersionMarker AS ivm ON {ivm.itemPK}={item.PK}} WHERE {item.itemType} IN(%s)%s AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND ((?useVersionValue=1 AND ({ivm.versionValue}<>%s OR ({ivm.versionValue} is null AND %s is not null) OR ({ivm.versionValue} is not null AND %s is null))) OR (?useVersionValue<>1 AND {item.modifiedTime}>{ivm.versionTS}))",
                        new Object[] {versionValue, baseType, typePksAsQueryParams, wherePart, versionValue, versionValue, versionValue});
    }


    protected String getQueryForNewItems(String typePksAsQueryParams, String baseType, String itemSelector, String versionValuePart)
    {
        String wherePart = StringUtils.isBlank(itemSelector) ? "" : (" AND (" + itemSelector + ")");
        String versionValue = StringUtils.isBlank(versionValuePart) ? "('1')" : ("(" + versionValuePart + ")");
        return String.format(
                        "SELECT {item.PK} AS itemPK, {item.itemType} AS itemComposedType, {item.modifiedTime} AS version, 'NEW' AS changeType, 'created' AS info, %s AS versionValue FROM {%s AS item} WHERE {item.itemType} IN(%s)%s AND NOT EXISTS({{SELECT {ivm.PK} FROM {ItemVersionMarker as ivm} WHERE {ivm.itemPK}={item.PK} AND {ivm.streamId}=?streamId AND {ivm.status}=?statusActive}})",
                        new Object[] {versionValue, baseType, typePksAsQueryParams, wherePart});
    }


    protected Map<Long, ItemVersionMarkerModel> preloadVersionMarkers(List<ItemChangeDTO> changes, String streamId)
    {
        List<PK> itemVersionMarkersPKs = (List<PK>)changes.stream().map(i -> PK.fromLong(i.getItemPK().longValue())).collect(Collectors.toList());
        return findItemVersionMarkersForStreamByPKs(streamId, itemVersionMarkersPKs);
    }


    protected int consumeChangesBatch(List<ItemChangeDTO> changes, String streamId)
    {
        List<ItemVersionMarkerModel> markersToSave = new ArrayList<>(changes.size());
        Integer consumedChangesInBatch = (Integer)PersistenceUtils.doWithSLDPersistence(() -> {
            int consumedChanges = 0;
            Map<Long, ItemVersionMarkerModel> pkToVersionMarker = preloadVersionMarkers(changes, streamId);
            for(ItemChangeDTO change : changes)
            {
                if(change != null)
                {
                    ItemVersionMarkerModel ivm = pkToVersionMarker.get(change.getItemPK());
                    switch(null.$SwitchMap$de$hybris$deltadetection$enums$ChangeType[change.getChangeType().ordinal()])
                    {
                        case 1:
                            if(ivm == null)
                            {
                                ivm = (ItemVersionMarkerModel)getModelService().create(ItemVersionMarkerModel.class);
                                fillInitialVersionMarker(ivm, change.getItemPK(), change.getVersion(), change.getVersionValue(), change.getInfo(), getTypeService().getComposedTypeForCode(change.getItemComposedType()), change.getStreamId());
                            }
                            else
                            {
                                updateVersionMarker(ivm, change.getVersion(), change.getVersionValue(), change.getInfo());
                            }
                            markersToSave.add(ivm);
                            consumedChanges++;
                            continue;
                        case 2:
                            updateVersionMarker(ivm, change.getVersion(), change.getVersionValue(), change.getInfo());
                            markersToSave.add(ivm);
                            consumedChanges++;
                            continue;
                        case 3:
                            Preconditions.checkNotNull(ivm, "ItemVersionMarker cannot be null in this place");
                            ivm.setStatus(ItemVersionMarkerStatus.DELETED);
                            markersToSave.add(ivm);
                            consumedChanges++;
                            continue;
                    }
                    LOG.warn("Unexpected change type {} - ignored!", change.getChangeType());
                }
            }
            getModelService().saveAll(markersToSave);
            return Integer.valueOf(consumedChanges);
        });
        return consumedChangesInBatch.intValue();
    }


    public void consumeChanges(List<ItemChangeDTO> allChanges)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int changesConsumptionThreads = Registry.getCurrentTenant().getConfig().getInt("deltadetection.changeconsumption.threads", 8);
        ExecutorService executorService = Executors.newFixedThreadPool(changesConsumptionThreads, (ThreadFactory)new TenantAwareThreadFactory(
                        Registry.getCurrentTenantNoFallback()));
        int overallConsumed = 0;
        boolean errorDuringConsumption = false;
        try
        {
            for(Future<Integer> future : executorService.<Integer>invokeAll(prepareBatchExecutionCallables(allChanges)))
            {
                try
                {
                    overallConsumed += ((Integer)future.get()).intValue();
                }
                catch(ExecutionException e)
                {
                    errorDuringConsumption = true;
                    LOG.error("Error while consuming changed:" + e.getMessage(), e);
                }
            }
            stopwatch.stop();
            LOG.info("Consuming changes has been finished, {} threads were used to consume {} changes, in {} ", new Object[] {Integer.valueOf(changesConsumptionThreads),
                            Integer.valueOf(allChanges.size()), stopwatch});
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.error("interrupted while waiting for changes to be consumed", e);
        }
        finally
        {
            executorService.shutdown();
        }
        logConsumedChanges(overallConsumed);
        if(errorDuringConsumption)
        {
            throw new IllegalStateException("There were errors during changes consumption. Please check the logs for details.");
        }
    }


    protected void logConsumedChanges(int numConsumed)
    {
        LOG.debug("Amount of Consumed changes: {}", Integer.valueOf(numConsumed));
    }


    protected Map<String, List<List<ItemChangeDTO>>> getPartitionedBatchChangesByStreamId(List<ItemChangeDTO> allChanges)
    {
        Map<String, List<ItemChangeDTO>> streamIdToAllChanges = (Map<String, List<ItemChangeDTO>>)allChanges.stream().filter(i -> (i != null)).collect(Collectors.groupingBy(ItemChangeDTO::getStreamId));
        Map<String, List<List<ItemChangeDTO>>> streamIdToPartitionedBatchChanges = new HashMap<>();
        for(String streamId : streamIdToAllChanges.keySet())
        {
            int batchSize = Registry.getCurrentTenant().getConfig().getInt("deltadetection.changeconsumption.batch", 200);
            List<List<ItemChangeDTO>> partition = Lists.partition(streamIdToAllChanges.get(streamId), batchSize);
            streamIdToPartitionedBatchChanges.put(streamId, partition);
        }
        return streamIdToPartitionedBatchChanges;
    }


    protected List<Callable<Integer>> prepareBatchExecutionCallables(List<ItemChangeDTO> allChanges)
    {
        List<Callable<Integer>> ret = new ArrayList<>(allChanges.size());
        int maxRetries = Registry.getCurrentTenant().getConfig().getInt("deltadetection.changeconsumption.batch.retries", 10);
        for(Map.Entry<String, List<List<ItemChangeDTO>>> entry : getPartitionedBatchChangesByStreamId(allChanges)
                        .entrySet())
        {
            for(List<ItemChangeDTO> changes : entry.getValue())
            {
                ret.add(createConsumeBatchCallable(entry.getKey(), changes, maxRetries));
            }
        }
        return ret;
    }


    protected Callable<Integer> createConsumeBatchCallable(String streamId, List<ItemChangeDTO> changes, int maxRetries)
    {
        return (Callable<Integer>)new Object(this, changes, streamId, maxRetries);
    }


    public void deleteItemVersionMarkersForStream(String streamId)
    {
        String allStreamMarkersQry = "SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId} = ?streamId";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId} = ?streamId", (Map)ImmutableMap.of("streamId", streamId));
        List<ItemVersionMarkerModel> itemVersionMarkers = getFlexibleSearchService().search(query).getResult();
        getModelService().removeAll(itemVersionMarkers);
    }


    public void resetStream(String streamId)
    {
        List<ComposedTypeModel> composedTypes = getComposedTypesForStream(streamId);
        for(ComposedTypeModel composedType : composedTypes)
        {
            List<ItemVersionMarkerModel> newOrModifiedMarkers = getNewOrModifiedVersionMarkers(streamId, composedType);
            for(ItemVersionMarkerModel ivm : newOrModifiedMarkers)
            {
                ivm.setVersionTS(Date.from(Instant.EPOCH));
                ivm.setVersionValue(ivm.getLastVersionValue());
            }
            List<ItemVersionMarkerModel> deletedMarkers = getDeletedVersionMarkers(streamId);
            deletedMarkers.forEach(ivm -> ivm.setStatus(ItemVersionMarkerStatus.ACTIVE));
            getModelService().saveAll(newOrModifiedMarkers);
            getModelService().saveAll(deletedMarkers);
        }
    }


    protected Map<Long, ItemVersionMarkerModel> findItemVersionMarkersForStreamByPKs(String streamId, List<PK> PKs)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemPK} in (?itemPK) AND {ivm.streamId} = ?streamId");
        fsq.addQueryParameter("itemPK", PKs);
        fsq.addQueryParameter("streamId", streamId);
        List<ItemVersionMarkerModel> result = getFlexibleSearchService().search(fsq).getResult();
        Map<Long, ItemVersionMarkerModel> pkToItemVersionMarker = (Map<Long, ItemVersionMarkerModel>)result.stream().collect(Collectors.toMap(ItemVersionMarkerModel::getItemPK,
                        Function.identity()));
        return pkToItemVersionMarker;
    }


    protected ItemVersionMarkerModel findVersionMarkerByItemPK(String streamId, Long itemPk)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.itemPK} = ?itemPK AND {ivm.streamId} = ?streamId");
        fsq.addQueryParameter("itemPK", itemPk);
        fsq.addQueryParameter("streamId", streamId);
        List<ItemVersionMarkerModel> result = getFlexibleSearchService().search(fsq).getResult();
        return isNotSingleResult(result) ? null : result.get(0);
    }


    protected boolean isNotSingleResult(List<ItemVersionMarkerModel> result)
    {
        return (result == null || result.size() != 1);
    }


    protected List<ItemVersionMarkerModel> getNewOrModifiedVersionMarkers(String streamId, ComposedTypeModel model)
    {
        String newOrModifiedIvmQuery = String.format("SELECT {ivm.PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId AND {ivm.status}=?statusActive AND EXISTS({{SELECT {item.PK} FROM {%s as item} WHERE {ivm.itemPK}={item.PK}}})", new Object[] {model.getCode()});
        FlexibleSearchQuery newOrModifiedIvmFlexibleQuery = new FlexibleSearchQuery(newOrModifiedIvmQuery, (Map)ImmutableMap.of("streamId", streamId, "statusActive", ItemVersionMarkerStatus.ACTIVE));
        return getFlexibleSearchService().search(newOrModifiedIvmFlexibleQuery).getResult();
    }


    protected List<ItemVersionMarkerModel> getDeletedVersionMarkers(String streamId)
    {
        FlexibleSearchQuery deletedQry = new FlexibleSearchQuery("SELECT {ivm.PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId AND {ivm.status}=?statusDeleted", (Map)ImmutableMap.of("streamId", streamId));
        deletedQry.addQueryParameter("statusDeleted", ItemVersionMarkerStatus.DELETED);
        return getFlexibleSearchService().search(deletedQry).getResult();
    }


    protected List<ComposedTypeModel> getComposedTypesForStream(String streamId)
    {
        String composedTypesQry = "SELECT DISTINCT {itemComposedType} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId} = ?streamId";
        FlexibleSearchQuery secondQry = new FlexibleSearchQuery("SELECT DISTINCT {itemComposedType} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId} = ?streamId", (Map)ImmutableMap.of("streamId", streamId));
        return getFlexibleSearchService().search(secondQry).getResult();
    }


    protected Optional<StreamConfigurationModel> getStreamById(String streamId)
    {
        try
        {
            String queryStr = "SELECT {PK} FROM {StreamConfiguration AS sc} WHERE {sc.streamId} = ?streamId";
            FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {StreamConfiguration AS sc} WHERE {sc.streamId} = ?streamId", (Map)ImmutableMap.of("streamId", streamId));
            return Optional.of((StreamConfigurationModel)getFlexibleSearchService().searchUnique(query));
        }
        catch(ModelNotFoundException e)
        {
            LOG.debug("Stream configuration for ID: {} not found [reason: {}]", streamId, e.getMessage());
            return Optional.empty();
        }
    }


    protected List<ItemVersionMarkerModel> getVersionMarkersForRemovedItems(String streamId)
    {
        String query = "SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE NOT EXISTS({{SELECT {item.PK} FROM {Item as item} WHERE {ivm.itemPK}={item.PK}}}) AND {ivm.streamId}=?streamId";
        SearchResult<ItemVersionMarkerModel> result = getFlexibleSearchService().search("SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE NOT EXISTS({{SELECT {item.PK} FROM {Item as item} WHERE {ivm.itemPK}={item.PK}}}) AND {ivm.streamId}=?streamId",
                        (Map)ImmutableMap.of("streamId", streamId));
        return result.getResult();
    }


    protected void fillInitialVersionMarker(ItemVersionMarkerModel marker, Long itemPK, Date version, String versionValue, String info, ComposedTypeModel itemComposedType, String streamId)
    {
        Preconditions.checkNotNull(marker, "ItemVersionMarker cannot be null in this place");
        Preconditions.checkArgument(marker.getItemModelContext().isNew(), "ItemVersionMarker must be new");
        Optional<StreamConfigurationModel> streamConfiguration = getStreamById(streamId);
        if(streamConfiguration.isPresent() && StringUtils.isNotBlank(((StreamConfigurationModel)streamConfiguration.get()).getInfoExpression()))
        {
            String expressionValue = parseInfoExpression(itemPK, streamConfiguration.get());
            marker.setInfo(expressionValue);
        }
        else
        {
            marker.setInfo(info);
        }
        marker.setItemPK(itemPK);
        marker.setItemComposedType(itemComposedType);
        marker.setStreamId(streamId);
        marker.setVersionTS(version);
        marker.setVersionValue(versionValue);
        marker.setStatus(ItemVersionMarkerStatus.ACTIVE);
    }


    protected void updateVersionMarker(ItemVersionMarkerModel marker, Date version, String versionValue, String info)
    {
        Preconditions.checkNotNull(marker, "ItemVersionMarker cannot be null in this place");
        Preconditions.checkArgument(!marker.getItemModelContext().isNew(), "ItemVersionMarker must not be new");
        Optional<StreamConfigurationModel> streamConfiguration = getStreamById(marker.getStreamId());
        if(streamConfiguration.isPresent() && StringUtils.isNotBlank(((StreamConfigurationModel)streamConfiguration.get()).getInfoExpression()))
        {
            String expressionValue = parseInfoExpression(marker.getItemPK(), streamConfiguration.get());
            marker.setInfo(expressionValue);
        }
        else
        {
            marker.setInfo(info);
        }
        marker.setVersionTS(version);
        marker.setLastVersionValue(marker.getVersionValue());
        marker.setVersionValue(versionValue);
        marker.setStatus(ItemVersionMarkerStatus.ACTIVE);
    }


    protected String parseInfoExpression(Long itemPK, StreamConfigurationModel streamConfiguration)
    {
        Expression exp = SPEL_EXPRESSION_PARSER.parseExpression(streamConfiguration.getInfoExpression(), (ParserContext)PARSER_CONTEXT);
        ItemModel item = (ItemModel)getModelService().get(PK.fromLong(itemPK.longValue()));
        Object expressionValue = exp.getValue(item);
        return (expressionValue != null) ? expressionValue.toString() : null;
    }


    protected ItemModel getSavedModel(PK pk)
    {
        ItemModel savedModel = null;
        try
        {
            savedModel = (ItemModel)getModelService().get(pk);
            getModelService().refresh(savedModel);
        }
        catch(ModelLoadingException ignored)
        {
            return null;
        }
        return savedModel;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
