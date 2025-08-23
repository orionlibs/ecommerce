/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.synctracker;

import com.hybris.backoffice.cronjob.CronJobHistoryFacade;
import com.hybris.backoffice.events.processes.ProcessFinishedEvent;
import com.hybris.backoffice.sync.SyncTaskExecutionInfo;
import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class SyncTrackerController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_SYNC_TASK = "syncTaskExecutionInfo";
    protected static final String SOCKET_OUT_SYNCED_ITEMS = "synchronizedItems";
    protected static final String MODEL_TRACKED_SYNCHRONIZATIONS = "trackedSynchronizations";
    protected static final String MODEL_ITEMS_TO_SYNC = "itemsToSync";
    protected static final String MODEL_ITEMS_DELETED_DURING_SYNC = "itemsDeletedDuringSync";
    protected static final String SETTING_FIND_SYNC_COUNTERPARTS = "findSyncCounterparts";
    protected static final String SETTING_SEND_GLOBAL_EVENT = "sendGlobalEvent";
    private static final Logger LOG = LoggerFactory.getLogger(SyncTrackerController.class);
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient CronJobHistoryFacade cronJobHistoryFacade;
    @WireVariable
    private transient SynchronizationFacade synchronizationFacade;
    @WireVariable
    private transient AfterSyncItemsHandler afterSyncItemsHandler;


    @InextensibleMethod
    private static String extractPk(final AbstractItemModel itemModel)
    {
        return String.valueOf(itemModel.getPk());
    }


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        if(isTrackingProcesses())
        {
            UITools.postponeExecution(comp, this::updateTrackedProcesses);
        }
    }


    protected void updateTrackedProcesses()
    {
        getCronJobHistoryFacade().getCronJobHistory(new ArrayList<>(getTrackingMap().keySet())).stream()
                        .filter(cjh -> cjh.getEndTime() != null).forEach(cjh -> finishTracking(cjh.getCronJobCode()));
    }


    @SocketEvent(socketId = SOCKET_IN_SYNC_TASK)
    public void onSyncStarted(final SyncTaskExecutionInfo executionInfo)
    {
        if(executionInfo != null && executionInfo.getSyncTask() != null
                        && StringUtils.isNotBlank(executionInfo.getSyncCronJobCode()))
        {
            startTrackingSynchronization(executionInfo.getSyncCronJobCode(),
                            withCounterparts(executionInfo.getSyncTask().getItems(), executionInfo.getSyncTask().getSyncItemJob()));
        }
    }


    protected List<? extends ItemModel> withCounterparts(final List<? extends ItemModel> items, final SyncItemJobModel syncItemJob)
    {
        if(CollectionUtils.isEmpty(items) || !getWidgetSettings().getBoolean(SETTING_FIND_SYNC_COUNTERPARTS))
        {
            return items;
        }
        final List<ItemModel> itemsWithCounterParts = new ArrayList<>(items);
        items.forEach(item -> findCounterpart(item, syncItemJob).ifPresent(itemsWithCounterParts::add));
        return itemsWithCounterParts;
    }


    protected Optional<ItemModel> findCounterpart(final ItemModel item, final SyncItemJobModel syncItemJob)
    {
        if(item instanceof CatalogVersionModel)
        {
            if(Objects.equals(item, syncItemJob.getTargetVersion()))
            {
                return Optional.ofNullable(syncItemJob.getSourceVersion());
            }
            else if(Objects.equals(item, syncItemJob.getSourceVersion()))
            {
                return Optional.ofNullable(syncItemJob.getTargetVersion());
            }
        }
        else
        {
            return getSynchronizationFacade().findSyncCounterpart(item, syncItemJob);
        }
        return Optional.empty();
    }


    @GlobalCockpitEvent(eventName = ProcessFinishedEvent.EVENT_NAME, scope = CockpitEvent.APPLICATION)
    public void onProcessFinished(final CockpitEvent cockpitEvent)
    {
        if(isTrackingProcesses() && cockpitEvent.getData() instanceof ProcessFinishedEvent)
        {
            final AfterCronJobFinishedEvent processEvent = ((ProcessFinishedEvent)cockpitEvent.getData()).getProcessEvent();
            if(isTrackedProcess(processEvent.getCronJob()))
            {
                finishTracking(processEvent.getCronJob());
            }
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.APPLICATION)
    public void onObjectUpdatedEvent(final CockpitEvent event)
    {
        final List<ItemModel> updatedItems = getItemsFromEvent(event);
        getSyncCatalogVersionPk(updatedItems).ifPresent(syncCatalogVersionPk ->
                        getTrackingMap().forEach((trackedCronJobCode, trackedSyncItems) -> {
                            if(trackedSyncItems.contains(syncCatalogVersionPk))
                            {
                                final List<String> updatedItemsPks = updatedItems.stream().map(SyncTrackerController::extractPk)
                                                .collect(Collectors.toList());
                                trackedSyncItems.addAll(updatedItemsPks);
                            }
                        }));
    }


    protected List<ItemModel> getItemsFromEvent(final CockpitEvent event)
    {
        return event.getDataAsCollection().stream().filter(ItemModel.class::isInstance).map(ItemModel.class::cast)
                        .collect(Collectors.toList());
    }


    @InextensibleMethod
    private Optional<String> getSyncCatalogVersionPk(final List<ItemModel> updatedItems)
    {
        return getSynchronizationFacade().getSyncCatalogVersion(updatedItems).map(SyncTrackerController::extractPk);
    }


    protected void finishTracking(final String cronJobCode)
    {
        notifySyncFinished(cronJobCode);
        stopTrackingProcess(cronJobCode);
    }


    protected void notifySyncFinished(final String cronJobCode)
    {
        final Set<String> syncedItems = getTrackingMap().get(cronJobCode);
        if(CollectionUtils.isNotEmpty(syncedItems))
        {
            final List<ItemModel> items = loadSyncedItemModels(syncedItems, cronJobCode);
            sendOutput(SOCKET_OUT_SYNCED_ITEMS, items);
            final boolean sendGlobalEvents = getWidgetSettings().getBoolean(SETTING_SEND_GLOBAL_EVENT);
            getAfterSyncItemsHandler().handleUpdatedItems(new HashSet<>(items), sendGlobalEvents);
            getAfterSyncItemsHandler().handleDeletedItems(getItemsDeletedDuringSync(cronJobCode), sendGlobalEvents);
        }
    }


    protected boolean isTrackingProcesses()
    {
        return !getTrackingMap().isEmpty();
    }


    protected boolean isTrackedProcess(final String cronJobCode)
    {
        return StringUtils.isNotBlank(cronJobCode) && getTrackingMap().containsKey(cronJobCode);
    }


    protected Set<String> stopTrackingProcess(final String cronJobCode)
    {
        getItemsToSync().remove(cronJobCode);
        getItemsDeletedDuringSync().remove(cronJobCode);
        return getTrackingMap().remove(cronJobCode);
    }


    protected void startTrackingSynchronization(final String syncJobCode, final List<? extends ItemModel> items)
    {
        if(StringUtils.isNotBlank(syncJobCode) && CollectionUtils.isNotEmpty(items))
        {
            final Set<String> pks = items.stream().map(item -> item.getPk().toString()).collect(Collectors.toSet());
            getTrackingMap().put(syncJobCode, pks);
            getItemsToSync().put(syncJobCode, new HashSet<>(items));
        }
    }


    /**
     * @deprecated since 1905. Use {@link #loadSyncedItemModels(Set, String)} instead
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected List<Object> loadSyncedItems(final Set<String> syncedItems)
    {
        return loadSyncedItemModels(syncedItems).stream().map(Object.class::cast).collect(Collectors.toList());
    }


    /**
     * @deprecated since 2005. use {@link #loadSyncedItemModels(Set, String)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected List<ItemModel> loadSyncedItemModels(final Set<String> syncedItems)
    {
        return loadSyncedItemModels(syncedItems, null);
    }


    protected List<ItemModel> loadSyncedItemModels(final Set<String> syncedItems, final String cronjobCode)
    {
        return syncedItems.stream().map(pk -> {
            try
            {
                return getObjectFacade().<ItemModel>load(pk);
            }
            catch(ObjectNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Cannot load items for PK %s", pk), e);
                }
                handleNotFoundItem(pk, cronjobCode);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    protected void handleNotFoundItem(final String pk, final String cronjobCode)
    {
        final Optional<ItemModel> optionalItemModel = getItemsToSync().get(cronjobCode).stream()
                        .filter(itemModel -> extractPk(itemModel).equals(pk)).findFirst();
        optionalItemModel.ifPresent(itemModel -> getItemsDeletedDuringSync(cronjobCode).add(itemModel));
    }


    protected Map<String, Set<String>> getTrackingMap()
    {
        Map trackedSynchronizations = getValue(MODEL_TRACKED_SYNCHRONIZATIONS, Map.class);
        if(trackedSynchronizations == null)
        {
            trackedSynchronizations = new HashMap<>();
            setValue(MODEL_TRACKED_SYNCHRONIZATIONS, trackedSynchronizations);
        }
        return trackedSynchronizations;
    }


    protected Map<String, Set<ItemModel>> getItemsToSync()
    {
        Map<String, Set<ItemModel>> itemsToSync = getValue(MODEL_ITEMS_TO_SYNC, Map.class);
        if(itemsToSync == null)
        {
            itemsToSync = new HashMap<>();
            setValue(MODEL_ITEMS_TO_SYNC, itemsToSync);
        }
        return itemsToSync;
    }


    protected Map<String, Set<ItemModel>> getItemsDeletedDuringSync()
    {
        Map<String, Set<ItemModel>> deletedItems = getValue(MODEL_ITEMS_DELETED_DURING_SYNC, Map.class);
        if(deletedItems == null)
        {
            deletedItems = new HashMap<>();
            setValue(MODEL_ITEMS_DELETED_DURING_SYNC, deletedItems);
        }
        return deletedItems;
    }


    protected Set<ItemModel> getItemsDeletedDuringSync(final String cronjobCode)
    {
        return getItemsDeletedDuringSync().computeIfAbsent(cronjobCode, code -> new HashSet<>());
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public CronJobHistoryFacade getCronJobHistoryFacade()
    {
        return cronJobHistoryFacade;
    }


    public SynchronizationFacade getSynchronizationFacade()
    {
        return synchronizationFacade;
    }


    public AfterSyncItemsHandler getAfterSyncItemsHandler()
    {
        return afterSyncItemsHandler;
    }
}
