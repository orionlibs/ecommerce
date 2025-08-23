/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser;

import static com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview.TreeViewCollectionBrowserMoldStrategy.MODEL_OPEN_OBJECTS;

import com.hybris.cockpitng.actions.create.CreateActionRenderer;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.service.ExceptionTranslationService;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.DefaultCollectionBrowserContext;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.SelectAndFocusDelegateController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.TypeCodeResolver;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.CollectionBrowserConfigurationLoader;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.DefaultSelectAndFocusDelegateController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.PagingDelegateController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.TitleDelegateController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.ListViewCollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;

public class CollectionBrowserController extends DefaultWidgetController
{
    public static final String SOCKET_OUT_SORTDATA = "sortData";
    public static final String SOCKET_IN_PAGEABLE = "pageable";
    public static final String SOCKET_IN_OBJECT_LIST = "list";
    public static final String SOCKET_PREVIOUS_ITEM_SELECTOR_INVOCATION = "previousItemSelectorInvocation";
    public static final String SOCKET_NEXT_ITEM_SELECTOR_INVOCATION = "nextItemSelectorInvocation";
    public static final String SOCKET_PREVIOUS_ITEM_SELECTOR_CONTEXT = "previousItemSelectorContext";
    public static final String SOCKET_NEXT_ITEM_SELECTOR_CONTEXT = "nextItemSelectorContext";
    public static final String SOCKET_IN_SELECT_ITEM = "selectItem";
    public static final String SOCKET_IN_SELECT_ITEMS = "selectItems";
    public static final String SOCKET_IN_DESELECT_ITEMS = "deselectItems";
    public static final String SOCKET_IN_DESELECT_ALL_ITEMS = "deselectAllItems";
    public static final String SOCKET_IN_SELECT_CONTEXT = "selectionContext";
    public static final String SOCKET_IN_FOCUS_ITEM = "focusItem";
    public static final String SOCKET_IN_RESET = "reset";
    public static final String SOCKET_IN_RESET_ACTION_SLOT = "resetActionSlot";
    public static final String SOCKET_IN_REFRESH = "refresh";
    public static final String SOCKET_IN_CHANGE_ACTION_SLOT_SOURCE = "changeActionSlotSource";
    public static final String SOCKET_IN_UPDATE_PAGEABLE = "updatePageable";
    public static final String DEFAULT_COLLECTION_BROWSER_CONFIG_CTX = "collection-browser";
    public static final String SETTING_COLLECTION_BROWSER_CONFIG_CTX = "collectionBrowserConfigCtx";
    public static final String SETTING_REFRESH_AFTER_OBJECT_CREATION = "refreshAfterObjectCreation";
    public static final String SHOULD_RELOAD_AFTER_UPDATE = "shouldReloadAfterUpdate";
    public static final String SETTING_PAGE_SIZE = "pageSize";
    public static final String SETTING_MULTI_SELECT = "multiSelect";
    public static final String SETTING_PAGING_POSITION = "pagingPosition";
    public static final String SETTING_SEND_ITEMS_ON_SELECT = "sendItemsOnSelect";
    public static final String SETTING_FALLBACK_TYPE_CODE = "fallbackTypeCode";
    public static final String SETTING_DRAG_ENABLED = "dragEnabled";
    public static final String SETTING_DROP_ENABLED = "dropEnabled";
    public static final String CTX_PARAM_UPDATED_OBJECT_IS_NEW = "updatedObjectIsNew";
    public static final String DESELECT_ALL_COMPONENT = "deselectAll";
    public static final String COMPONENT_ITEM_COUNT = "itemCount";
    public static final String ITEM_COUNT_ACTIVE_SCLASS = "yw-listview-statusbar-button-active";
    public static final String POPUP_POSITION = "before_start";
    public static final String SCLASS_ACTIVE_MOLD_SELECTOR = "yw-coll-browser-mold-sel-btn-%s-active";
    public static final String SCLASS_INACTIVE_MOLD_SELECTOR = "yw-coll-browser-mold-sel-btn-%s-inactive";
    public static final String SCLASS_COLL_BROWSER_MOLD_SEL_BTN = "yw-coll-browser-mold-sel-btn";
    public static final String SCLASS_COLL_BROWSER = "yw-coll-browser";
    public static final String SCLASS_MULTISELECT = SCLASS_COLL_BROWSER + "-multiselect";
    public static final String ACTION_SLOT_COMPONENT_ID = "actionSlotComponentId";
    public static final String ATTRIBUTE_MOLD_CLASS = "moldClass";
    /**
     * @deprecated since 6.7, please use {@link DefaultSelectAndFocusDelegateController#MODEL_SELECTED_OBJECTS}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String MODEL_SELECTED_OBJECTS = "selectedObjects";
    public static final String MODEL_PAGEABLE = "pageable";
    public static final String MODEL_EXPORT_COLUMNS_AND_DATA = "exportColumnsAndData";
    public static final String MODEL_ACTIVE_MOLD_NAME = "activeMoldName";
    public static final String MODEL_LAST_ACTIVE_PAGE_NUMBER = "lastActivePageNumber";
    public static final String MODEL_SELECTION_CONTEXT = "selectionContext";
    public static final String MODEL_MULTI_SELECT_ENABLED = "multiSelectEnabled";
    public static final CollectionBrowserMoldStrategy EMPTY_MOLD = new EmptyCollectionBrowserMoldStrategy();
    /**
     * @deprecated since 6.7, not used anymore
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String TYPE_CODE_KEY = "TYPE_CODE";
    public static final String GLOBAL_EVENT_ITEM_LOCKED_STATE_CHANGED = "itemLockedStateChangedGlobalEvent";
    /**
     * @deprecated since 6.7, please use {@link DefaultSelectAndFocusDelegateController#MODEL_FOCUSED_OBJECT}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected static final String MODEL_FOCUSED_OBJECT = "focusedObjects";
    /**
     * @deprecated since 6.7, please use {@link DefaultSelectAndFocusDelegateController#SOCKET_OUT_SELECTED_ITEM}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected static final String SOCKET_OUT_SELECTED_ITEM = "selectedItem";
    /**
     * @deprecated since 6.7, please use {@link DefaultSelectAndFocusDelegateController#SOCKET_OUT_SELECTED_ITEMS}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected static final String SOCKET_OUT_SELECTED_ITEMS = "selectedItems";
    /**
     * @deprecated since 6.7, please use {@link DefaultSelectAndFocusDelegateController#SOCKET_OUT_FOCUSED_ITEM}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected static final String SOCKET_OUT_FOCUSED_ITEM = "focusedItem";
    protected static final String SETTING_PAGING_POSITION_BOTTOM = "bottom";
    protected static final String SETTING_PAGING_POSITION_TOP = "top";
    protected static final String MODEL_DATA_TYPE = "dataType";
    protected static final String MODEL_DATA_TYPE_CODE = "dataTypeCode";
    protected static final String ACTION_SLOT_SOURCE = "actionSlotSource";
    protected static final String SETTING_HYPERLINKS_ENABLED = "hyperlinks";
    private static final Boolean DEFAULT_HYPERLINKS_ENABLED = Boolean.FALSE;
    private static final Boolean DEFAULT_SEND_ITEMS_ON_SELECT = Boolean.TRUE;
    private static final String URL_CONFIGURABLE_PAGING = WidgetLibConstants.RESOURCES_SUBFOLDER
                    + "/widgets/collectionBrowser/configurablePaging.zul";
    private static final String DEFAULT_MOLD_NAME = "unknown";
    private static final Logger LOG = LoggerFactory.getLogger(CollectionBrowserController.class);
    private final transient Object moldSelectorMonitor = new Object();
    private int selectedItemsCount = 0;
    private transient List<CollectionBrowserMoldStrategy> availableMolds;
    private transient CollectionBrowserMoldStrategy activeMold;
    @WireVariable
    private transient CollectionBrowserConfigurationLoader collectionBrowserConfigurationLoader;
    @WireVariable
    private transient PagingDelegateController pagingDelegateController;
    @WireVariable
    private transient TitleDelegateController titleDelegateController;
    @WireVariable
    private transient SelectAndFocusDelegateController selectAndFocusDelegateController;
    @WireVariable
    private transient FieldSearchFacade<?> fieldSearchFacade;
    @WireVariable
    private transient BackofficeTypeUtils backofficeTypeUtils;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient ObjectValueService objectValueService;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient PermissionFacade permissionFacade;
    @WireVariable
    private transient ExceptionTranslationService exceptionTranslationService;
    @WireVariable
    private transient TypeCodeResolver typeCodeResolver;
    @Wire
    private Label listTitle;
    @Wire
    private Label listSubtitle;
    @Wire
    private Button itemCount;
    @Wire
    private Button deselectAll;
    @Wire
    private Popup deselectPopup;
    @Wire
    private Paging paging;
    @Wire
    private Div statusBar;
    @Wire
    private Div browserContainer;
    @Wire
    private Hbox moldSelectorContainer;
    @Wire
    private Actions actionSlot;
    @Wire
    private Label numberOfItemsLabel;
    @Wire
    private Div contentsContainer;
    @Wire
    private Div pagingContainerTop;
    @Wire
    private Div pagingContainerBottom;
    @Wire
    private Div bottomBar;


    /**
     * Gets a single selection from multiple selected items
     *
     * @param selection
     *           selected items
     * @return item that should be treated as single selection
     * @deprecated since 6.7, please use {@link SelectAndFocusDelegateController#getSelectedItem}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static Object getSelectedItem(final Collection<?> selection)
    {
        return SelectAndFocusDelegateController.getSelectedItem(selection);
    }


    /**
     * Callback method for the input socket {@value CollectionBrowserController#SOCKET_IN_PAGEABLE}. Renders the values
     * contained in the provided <p>pageable</p> object and updates paging information if provided.
     *
     * @param pageable
     *           the pageable object to setPage
     */
    @SocketEvent(socketId = CollectionBrowserController.SOCKET_IN_PAGEABLE)
    public void process(final Pageable<?> pageable)
    {
        final Pageable oldPageable = getPagingDelegateController().getCurrentPageable();
        process(pageable, newPageable -> getSelectAndFocusDelegateController().handlePageLoaded(newPageable, oldPageable));
    }


    protected <E> void process(final Pageable<E> pageable, final Consumer<Pageable<E>> callback)
    {
        setValue(MODEL_MULTI_SELECT_ENABLED, null);
        setValue(MODEL_LAST_ACTIVE_PAGE_NUMBER, null);
        setValue(MODEL_OPEN_OBJECTS, null);
        if(pageable != null)
        {
            setValue(MODEL_PAGEABLE, pageable);
            getPagingDelegateController().process(pageable, callback);
        }
    }


    /**
     * Callback method for the socket event {@value #SOCKET_IN_OBJECT_LIST}.
     * <p>
     * Wraps the provided <p>objects</p> in a {@link com.hybris.cockpitng.search.data.pageable.PageableList} and calls
     * {@link #process(com.hybris.cockpitng.search.data.pageable.Pageable)} (if the list is not empty).
     *
     * @param objects
     *           objects to be displayed
     */
    @SocketEvent(socketId = SOCKET_IN_OBJECT_LIST)
    public void setList(final List<Object> objects)
    {
        Pageable pageable = null;
        final String typeCode = resolveTypeCodeFromCollection(objects);
        if(StringUtils.isNotBlank(typeCode))
        {
            pageable = new PageableList<>(ObjectUtils.defaultIfNull(objects, new ArrayList<>()),
                            getWidgetSettings().getInt(SETTING_PAGE_SIZE), typeCode);
            pageable.setQueryId(Integer.toString(Objects.hashCode(objects)));
        }
        process(pageable);
    }


    @SocketEvent(socketId = CollectionBrowserController.SOCKET_PREVIOUS_ITEM_SELECTOR_INVOCATION)
    public void previousItemSelectorInvocation()
    {
        getActiveMold().previousItemSelectorInvocation();
    }


    @SocketEvent(socketId = CollectionBrowserController.SOCKET_NEXT_ITEM_SELECTOR_INVOCATION)
    public void nextItemSelectorInvocation()
    {
        getActiveMold().nextItemSelectorInvocation();
    }


    @SocketEvent(socketId = CollectionBrowserController.SOCKET_IN_DESELECT_ALL_ITEMS)
    public void deselectAllItems()
    {
        getSelectAndFocusDelegateController().deselectAllItems();
        getDeselectPopup().close();
        removeActiveSclassForItemCount();
    }


    /**
     * Resets the widget to the initial state, displaying no data.
     */
    @SocketEvent(socketId = SOCKET_IN_RESET)
    public void reset(final Map<Object, Object> params)
    {
        reset();
    }


    /**
     * Refreshes the widget's data.
     */
    @SocketEvent(socketId = SOCKET_IN_REFRESH)
    public void refresh()
    {
        refreshPageable();
    }


    /**
     * Resets the widget's actions.
     */
    @SocketEvent(socketId = SOCKET_IN_RESET_ACTION_SLOT)
    public void resetActionSlot()
    {
        getWidgetInstanceManager().getModel().remove(CreateActionRenderer.CREATE_ACTION_USER_CHOSEN_TYPE);
        getWidgetInstanceManager().getModel().remove(CreateActionRenderer.CREATE_ACTION_ROOT_TYPE);
        getWidgetInstanceManager().getModel().remove(CreateActionRenderer.CREATE_ACTION_EXPANDED_PATHS);
        initializeActionSlot();
    }


    @SocketEvent(socketId = SOCKET_IN_CHANGE_ACTION_SLOT_SOURCE)
    public void changeActionSlotSource(final Object obj)
    {
        if(obj instanceof String)
        {
            setActionSlotSource((String)obj);
        }
        else
        {
            setActionSlotSource(null);
        }
    }


    @SocketEvent(socketId = CollectionBrowserController.SOCKET_IN_UPDATE_PAGEABLE)
    public void updatePageable(final Pageable<?> pageable)
    {
        final Pageable currentPageable = getPagingDelegateController().getCurrentPageable();
        if(currentPageable == null)
        {
            process(pageable);
        }
        else if(pageable != null)
        {
            pageable.setPageNumber(currentPageable.getPageNumber());
            process(pageable);
        }
    }


    /**
     * Method to select item
     *
     * @param itemToSelect
     *           an item to be selected
     */
    @SocketEvent(socketId = SOCKET_IN_SELECT_ITEM)
    public void selectItem(final Object itemToSelect)
    {
        getSelectAndFocusDelegateController().selectItem(itemToSelect);
    }


    /**
     * Method to select multiple items
     *
     * @param itemsToSelect
     *           an item to be selected
     */
    @SocketEvent(socketId = SOCKET_IN_SELECT_ITEMS)
    public void selectMultipleItems(final List<Object> itemsToSelect)
    {
        getSelectAndFocusDelegateController().selectItems(itemsToSelect);
    }


    /**
     * Removes given items from the current selection
     *
     * @param itemsToDeselect
     *           objects to remove from selection
     */
    @SocketEvent(socketId = SOCKET_IN_DESELECT_ITEMS)
    public void deselectItems(final List<Object> itemsToDeselect)
    {
        getSelectAndFocusDelegateController().deselectItems(itemsToDeselect);
    }


    @SocketEvent(socketId = SOCKET_IN_SELECT_CONTEXT)
    public void handleSelectionContext(final Map<String, Object> inputContext)
    {
        getSelectAndFocusDelegateController().handleNewSelectionContext(inputContext);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectDeleteEvent(final CockpitEvent event)
    {
        if(canHandleEvent(event))
        {
            final Pageable<?> pageable = getPagingDelegateController().getCurrentPageable();
            setValue(MODEL_LAST_ACTIVE_PAGE_NUMBER, Integer.valueOf(pageable.getPageNumber()));
            getActiveMold().handleObjectDeleteEvent(event);
            try
            {
                pageable.refresh();
                pageable.setCockpitEvent(event);
                process(pageable, null);
            }
            finally
            {
                pageable.setCockpitEvent(null);
            }
            getSelectAndFocusDelegateController().handleObjectDeleted(event);
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsUpdatedEvent(final CockpitEvent event)
    {
        if(canHandleEvent(event))
        {
            if(shouldReloadAfterUpdate(event) || isModifiedItemDisplayed(event.getDataAsCollection()))
            {
                refreshPageable();
            }
            else
            {
                handleViewRefresh(event);
            }
            getActiveMold().handleObjectsUpdateEvent(event);
            getSelectAndFocusDelegateController().handleObjectUpdated(event);
        }
    }


    protected boolean isModifiedItemDisplayed(final Collection<Object> itemsToUpdate)
    {
        final List<?> currentPage = getPagingDelegateController().getCurrentPageable().getCurrentPage();
        return currentPage.stream().anyMatch(itemsToUpdate::contains);
    }


    @GlobalCockpitEvent(eventName = GLOBAL_EVENT_ITEM_LOCKED_STATE_CHANGED, scope = CockpitEvent.SESSION)
    public void itemLockingChanged(final CockpitEvent event)
    {
        handleObjectsUpdatedEvent(event);
    }


    /**
     * @deprecated since 6.7, please use the
     *             {@link DefaultSelectAndFocusDelegateController#isDataAmongSelectedItems(Object, Collection)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public boolean isDataAmongSelectedItems(final Object data, final Collection<Object> selectedItems)
    {
        return selectedItems != null && CollectionUtils.containsAny(selectedItems,
                        data instanceof Collection ? (Collection)data : Collections.singletonList(data));
    }


    /**
     * @deprecated since 6.7, please use the {@link SelectAndFocusDelegateController#handleObjectUpdated(CockpitEvent)}
     *             instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void handleSelectionRefresh(final CockpitEvent event)
    {
        getSelectAndFocusDelegateController().handleObjectUpdated(event);
    }


    /**
     * @deprecated since 6.7, please use the
     *             {@link DefaultSelectAndFocusDelegateController#handleSelectionRefresh(Collection)}} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void handleSelectionRefresh(final Collection<?> updatedObjects)
    {
        //Method kept because of the backward compatibility requirement.
    }


    protected void handleViewRefresh(final CockpitEvent event)
    {
        if(shouldRefreshAfterObjectCreation() && isNewTheUpdatedObject(event))
        {
            final Collection objects = event.getDataAsCollection();
            final Pageable<?> pageable = getPagingDelegateController().getCurrentPageable();
            if(pageable != null && isAssignableFrom(pageable.getTypeCode(), objects))
            {
                pageable.refresh();
                process(pageable);
            }
        }
    }


    protected void setActionSlotSource(final String source)
    {
        setValue(ACTION_SLOT_SOURCE, source);
        initializeActionSlot();
    }


    protected boolean isAssignableFrom(final String superType, final Collection<?> subTypeObjects)
    {
        return subTypeObjects.stream()
                        .anyMatch(object -> backofficeTypeUtils.isAssignableFrom(superType, typeFacade.getType(object)));
    }


    protected boolean shouldRefreshAfterObjectCreation()
    {
        final ListView listView = getValue(AbstractMoldStrategy.MODEL_COLUMNS_CONFIG, ListView.class);
        if(listView != null && listView.getRefreshAfterObjectCreation() != null)
        {
            return BooleanUtils.isTrue(listView.getRefreshAfterObjectCreation());
        }
        return getWidgetSettings().getBoolean(SETTING_REFRESH_AFTER_OBJECT_CREATION);
    }


    protected boolean isNewTheUpdatedObject(final CockpitEvent event)
    {
        if(!(event instanceof DefaultCockpitEvent) || ((DefaultCockpitEvent)event).getContext() == null)
        {
            return false;
        }
        final Map<String, Object> context = ((DefaultCockpitEvent)event).getContext();
        if(context.get(CTX_PARAM_UPDATED_OBJECT_IS_NEW) instanceof Map)
        {
            final Map isNewObjectMap = (Map)context.getOrDefault(CTX_PARAM_UPDATED_OBJECT_IS_NEW, new HashMap<>());
            return event.getDataAsCollection().stream().anyMatch(object -> Boolean.TRUE.equals(isNewObjectMap.get(object)));
        }
        return Boolean.TRUE.equals(context.get(CTX_PARAM_UPDATED_OBJECT_IS_NEW));
    }


    protected boolean shouldReloadAfterUpdate(final CockpitEvent event)
    {
        return isShouldReloadAfterUpdateEnabled(event) || areUpdatedObjectsVisibleOnCurrentPage(event);
    }


    protected boolean isShouldReloadAfterUpdateEnabled(final CockpitEvent event)
    {
        return event instanceof DefaultCockpitEvent && ((DefaultCockpitEvent)event).getContext() != null
                        && Boolean.TRUE.equals(((DefaultCockpitEvent)event).getContext().get(SHOULD_RELOAD_AFTER_UPDATE));
    }


    protected boolean areUpdatedObjectsVisibleOnCurrentPage(final CockpitEvent event)
    {
        final List<?> currentPageElements = Optional.ofNullable(getPagingDelegateController()) //
                        .map(PagingDelegateController::getCurrentSinglePage) //
                        .map(SinglePage::getList) //
                        .orElseGet(Collections::emptyList);
        return currentPageElements.stream().anyMatch(event.getDataAsCollection()::contains);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECT_CREATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectCreateEvent(final CockpitEvent event)
    {
        if(canHandleEvent(event))
        {
            getActiveMold().handleObjectCreateEvent(event);
            getSelectAndFocusDelegateController().handleObjectCreated(event);
        }
    }


    @ViewEvent(componentID = CollectionBrowserController.COMPONENT_ITEM_COUNT, eventName = Events.ON_CLICK)
    public void showPopup()
    {
        UITools.modifySClass(itemCount, ITEM_COUNT_ACTIVE_SCLASS, true);
        deselectPopup.open(statusBar, POPUP_POSITION);
        deselectAll.focus();
    }


    @ViewEvent(componentID = CollectionBrowserController.DESELECT_ALL_COMPONENT, eventName = Events.ON_CLICK)
    public void deselectItems()
    {
        deselectAllItems();
    }


    @ViewEvent(componentID = CollectionBrowserController.DESELECT_ALL_COMPONENT, eventName = Events.ON_BLUR)
    public void removeActiveSclassForItemCount()
    {
        UITools.modifySClass(itemCount, ITEM_COUNT_ACTIVE_SCLASS, false);
    }


    @Override
    public void initialize(final Component component)
    {
        super.initialize(component);
        setValue(MODEL_MULTI_SELECT_ENABLED, null);
        initializePagingContainer(component);
        initializeCollectionBrowserConfigurationLoader();
        initializePagingDelegateController();
        initializeTitleDelegateController();
        initializeSelectAndFocusDelegateController();
        initializeAvailableMolds();
        initializeMoldSelector();
        initializeObserverForModelPageable();
        initializeObserverForModelColumnsConfig();
        initializeModelSelectedObjects();
        initializeDataType();
        initializeTitle();
        initializeActionSlot();
        getPagingDelegateController().initializeOnPagingListener();
        getPagingDelegateController().renderAndProcessCurrentPageable();
        final Collection<Object> selectedItems = getSelectAndFocusDelegateController().getSelectedItems();
        if(CollectionUtils.isNotEmpty(selectedItems))
        {
            setSelectedItemsCount(CollectionUtils.size(selectedItems));
        }
        UITools.modifySClass(contentsContainer, isMultiSelectEnabled() ? (SCLASS_COLL_BROWSER + ' ' + SCLASS_MULTISELECT) : SCLASS_COLL_BROWSER, true);
    }


    protected void initializePagingContainer(final Component parent)
    {
        if(StringUtils.equals(SETTING_PAGING_POSITION_BOTTOM, getWidgetSettings().getString(SETTING_PAGING_POSITION)))
        {
            Executions.createComponents(URL_CONFIGURABLE_PAGING, getPagingContainerBottom(), Collections.emptyMap());
            getPagingContainerBottom().setVisible(true);
            getBottomBar().setVisible(true);
        }
        else
        {
            Executions.createComponents(URL_CONFIGURABLE_PAGING, getPagingContainerTop(), Collections.emptyMap());
            getPagingContainerBottom().setVisible(false);
        }
        Selectors.wireComponents(parent, this, true);
    }


    protected void initializeCollectionBrowserConfigurationLoader()
    {
        getCollectionBrowserConfigurationLoader().setController(this);
    }


    protected void initializePagingDelegateController()
    {
        getPagingDelegateController().setController(this);
    }


    protected void initializeTitleDelegateController()
    {
        getTitleDelegateController().setController(this);
    }


    protected void initializeSelectAndFocusDelegateController()
    {
        final SelectAndFocusDelegateController sfdController = getSelectAndFocusDelegateController();
        sfdController.setController(this);
        sfdController.initialize();
    }


    protected void initializeAvailableMolds()
    {
        if(CollectionUtils.isEmpty(availableMolds))
        {
            loadAvailableMolds();
        }
    }


    public void loadAvailableMolds()
    {
        this.availableMolds = getCollectionBrowserConfigurationLoader().loadAvailableMolds();
    }


    public void initializeMoldSelector()
    {
        moldSelectorContainer.getChildren().clear();
        for(final CollectionBrowserMoldStrategy mold : availableMolds)
        {
            final String moldName = StringUtils.defaultIfBlank(mold.getName(), DEFAULT_MOLD_NAME).toLowerCase(Locale.ENGLISH);
            final Div moldSelector = new Div();
            applyMoldSelectorStyle(getActiveMold(), mold, moldName, moldSelector);
            registerMoldSelectorEvents(mold, moldSelector);
            moldSelector.setTooltiptext(mold.getTooltipText());
            moldSelectorContainer.appendChild(moldSelector);
        }
        moldSelectorContainer.setVisible(availableMolds.size() > 1);
    }


    protected void applyMoldSelectorStyle(final CollectionBrowserMoldStrategy activeMold, final CollectionBrowserMoldStrategy mold,
                    final String moldName, final Div moldSelector)
    {
        moldSelector.setAttribute(ATTRIBUTE_MOLD_CLASS, mold);
        moldSelector.setSclass(SCLASS_COLL_BROWSER_MOLD_SEL_BTN);
        final boolean isMoldActive = StringUtils.equals(activeMold.getName(), mold.getName());
        UITools.modifySClass(moldSelector, String.format(SCLASS_ACTIVE_MOLD_SELECTOR, moldName), isMoldActive);
        UITools.modifySClass(moldSelector, String.format(SCLASS_INACTIVE_MOLD_SELECTOR, moldName), !isMoldActive);
    }


    protected void registerMoldSelectorEvents(final CollectionBrowserMoldStrategy mold, final Div moldSelector)
    {
        moldSelector.addEventListener(Events.ON_CLICK, event -> {
            synchronized(moldSelectorMonitor)
            {
                if(ObjectUtils.notEqual(getActiveMold(), mold))
                {
                    for(final Component component : moldSelectorContainer.getChildren())
                    {
                        updateMoldSelector(component, mold, moldSelector);
                    }
                }
            }
        });
    }


    protected void updateMoldSelector(final Component component, final CollectionBrowserMoldStrategy mold, final Div moldSelector)
    {
        final CollectionBrowserMoldStrategy moldClass = (CollectionBrowserMoldStrategy)component
                        .getAttribute(ATTRIBUTE_MOLD_CLASS);
        final String activeMoldSelector = String.format(SCLASS_ACTIVE_MOLD_SELECTOR, moldClass.getName());
        final String inactiveMoldSelector = String.format(SCLASS_INACTIVE_MOLD_SELECTOR, moldClass.getName());
        if(ObjectUtils.notEqual(moldClass, mold))
        {
            UITools.modifySClass((HtmlBasedComponent)component, activeMoldSelector, false);
            UITools.modifySClass((HtmlBasedComponent)component, inactiveMoldSelector, true);
            moldClass.release();
        }
        else
        {
            UITools.modifySClass(moldSelector, activeMoldSelector, true);
            UITools.modifySClass(moldSelector, inactiveMoldSelector, false);
            setActiveMold(mold);
            setValue(CollectionBrowserController.MODEL_MULTI_SELECT_ENABLED, null);
            mold.render(browserContainer, getPagingDelegateController()
                            .getCurrentSinglePageWithTypeCode(getValue(MODEL_PAGEABLE, Pageable.class).getTypeCode()));
            getSelectAndFocusDelegateController().handleMoldChange();
        }
    }


    protected void initializeObserverForModelPageable()
    {
        getModel().addObserver(MODEL_PAGEABLE, () -> {
            updateModelExportColumnsAndData();
            getSelectAndFocusDelegateController().handleNewPageable();
        });
    }


    protected void initializeObserverForModelColumnsConfig()
    {
        getModel().addObserver(AbstractMoldStrategy.MODEL_COLUMNS_CONFIG, this::updateModelExportColumnsAndData);
    }


    protected void updateModelExportColumnsAndData()
    {
        HashMap<Object, Object> exportColumnsAndData = null;
        if(getActiveMold() instanceof ListViewCollectionBrowserMoldStrategy)
        {
            final Pageable<?> pageable = getPagingDelegateController().getCurrentPageable();
            final ListView listView = getValue(AbstractMoldStrategy.MODEL_COLUMNS_CONFIG, ListView.class);
            exportColumnsAndData = new HashMap<>();
            exportColumnsAndData.put(MODEL_PAGEABLE, pageable);
            exportColumnsAndData.put(AbstractMoldStrategy.MODEL_COLUMNS_CONFIG, listView);
        }
        setValue(MODEL_EXPORT_COLUMNS_AND_DATA, exportColumnsAndData);
    }


    /**
     * @deprecated since 6.7, please use the {@link SelectAndFocusDelegateController#initialize()} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void initializeModelSelectedObjects()
    {
        //Method kept because of the backward compatibility requirement.
    }


    protected void initializeTitle()
    {
        final Pageable pageable = getPagingDelegateController().getCurrentPageable();
        if(pageable == null)
        {
            getTitleDelegateController().resetTitle();
        }
        else
        {
            getTitleDelegateController().updateTitle(pageable.getTypeCode(), pageable.getTotalCount());
        }
    }


    protected void initializeDataType()
    {
        final Pageable pageable = getPagingDelegateController().getCurrentPageable();
        if(pageable != null)
        {
            initializeDataType(pageable.getTypeCode());
        }
    }


    public void initializeDataType(final String typeCode)
    {
        DataType dataType = null;
        try
        {
            dataType = typeFacade.load(typeCode);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type '" + typeCode + "' could not be loaded", e);
            }
        }
        setValue(MODEL_DATA_TYPE, dataType);
        setValue(MODEL_DATA_TYPE_CODE, typeCode);
    }


    public void initializeActionSlot()
    {
        final String typeCodeFromModel = getValue(MODEL_DATA_TYPE_CODE, String.class);
        final String typeCode = StringUtils.isNotBlank(typeCodeFromModel) ? typeCodeFromModel
                        : getWidgetSettings().getString(SETTING_FALLBACK_TYPE_CODE);
        final String actionSlotSource = getValue(ACTION_SLOT_SOURCE, String.class);
        if(StringUtils.isNotBlank(typeCode))
        {
            final String componentContext = getWidgetSettings().getString(ACTION_SLOT_COMPONENT_ID);
            actionSlot.setConfig(String.format("component=%s,type=%s,source=%s", componentContext, typeCode, actionSlotSource));
        }
        else
        {
            actionSlot.setConfig(null);
        }
        actionSlot.reload();
    }


    public String getCurrentTypeCode()
    {
        return getValue(MODEL_DATA_TYPE_CODE, String.class);
    }


    public DataType getCurrentType()
    {
        return getValue(MODEL_DATA_TYPE, DataType.class);
    }


    public void releaseAllMolds()
    {
        setActiveMold(null);
        for(final Component component : moldSelectorContainer.getChildren())
        {
            ((CollectionBrowserMoldStrategy)component.getAttribute(ATTRIBUTE_MOLD_CLASS)).release();
        }
        moldSelectorContainer.getChildren().clear();
        EMPTY_MOLD.release();
    }


    protected void refreshPageable()
    {
        final Pageable<?> pageable = getPagingDelegateController().getCurrentPageable();
        if(pageable != null)
        {
            setValue(MODEL_LAST_ACTIVE_PAGE_NUMBER, pageable.getPageNumber());
            pageable.refresh();
            getPagingDelegateController().process(pageable);
        }
    }


    public void reset()
    {
        resetModel();
        resetView();
        availableMolds.forEach(CollectionBrowserMoldStrategy::reset);
        activeMold.setPage(SinglePage.EMPTY);
    }


    protected void resetModel()
    {
        setValue(MODEL_PAGEABLE, null);
        setValue(MODEL_DATA_TYPE, null);
        setValue(MODEL_DATA_TYPE_CODE, null);
        setValue(MODEL_ACTIVE_MOLD_NAME, null);
        setValue(MODEL_LAST_ACTIVE_PAGE_NUMBER, null);
        setValue(MODEL_MULTI_SELECT_ENABLED, null);
        getSelectAndFocusDelegateController().resetModel();
    }


    protected void resetView()
    {
        getTitleDelegateController().resetTitle();
        initializeActionSlot();
        paging.setVisible(false);
    }


    public void sort(final SortData sortData)
    {
        final Pageable pageable = getPagingDelegateController().getCurrentPageable();
        pageable.setSortData(sortData);
        getPagingDelegateController().fetchPage(pageable, event -> {
            final SortData pageableSortData = pageable.getSortData();
            sendOutput(SOCKET_OUT_SORTDATA, pageableSortData);
            getModel().put(CollectionBrowserController.MODEL_PAGEABLE, pageable);
            final SinglePage page = new SinglePage(pageable.getCurrentPage(), pageable.getTypeCode(), pageableSortData,
                            pageable.getPageSize());
            getModel().put(PagingDelegateController.MODEL_SINGLE_PAGE, page);
            getPagingDelegateController().buildPaging(pageable);
            getActiveMold().setPage(getPagingDelegateController().getCurrentSinglePage());
        });
    }


    protected boolean canHandleEvent(final CockpitEvent event)
    {
        final Pageable<?> pageable = getPagingDelegateController().getCurrentPageable();
        final boolean nullOrEmptyValues = event == null || event.getData() == null || (pageable == null)
                        || StringUtils.isBlank(pageable.getTypeCode());
        return !(nullOrEmptyValues) && isActiveMoldHandlingEventData(event.getData());
    }


    protected boolean isActiveMoldHandlingEventData(final Object eventData)
    {
        if(eventData instanceof Collection)
        {
            return ((Collection<Object>)eventData).stream().anyMatch(this::isActiveMoldHandlingEventData);
        }
        return getActiveMold().isHandlingObjectEvents(resolveTypeCodeFromObject(eventData));
    }


    /**
     * @deprecated since 6.7, please use the
     *             {@link TypeCodeResolver#resolveTypeCodeFromObject(Object, WidgetInstanceManager)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String resolveTypeCodeFromObject(final Object object)
    {
        return getTypeCodeResolver().resolveTypeCodeFromObject(object, getWidgetInstanceManager());
    }


    /**
     * @deprecated since 6.7, please use the
     *             {@link TypeCodeResolver#resolveTypeCodeFromCollection(Collection, WidgetInstanceManager)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String resolveTypeCodeFromCollection(final Collection<?> objects)
    {
        return getTypeCodeResolver().resolveTypeCodeFromCollection(objects, getWidgetInstanceManager());
    }


    /**
     * Notifies that a hyperlink with specified value has been clicked.
     * <P>
     * If {@linkplain #SETTING_HYPERLINKS_ENABLED} is enabled the item will be sent through
     * {@linkplain #SOCKET_OUT_SELECTED_ITEM} output socket.<br>
     * It will not affect the selection.
     * </P>
     *
     * @param value
     *           hyperlink's value
     * @see #areHyperlinksEnabled()
     * @deprecated since 6.7, please use {@link SelectAndFocusDelegateController#handleHyperlinkClicked(Object)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void notifyHyperlinkClicked(final Object value)
    {
        getSelectAndFocusDelegateController().handleHyperlinkClicked(value);
    }


    /**
     * Notifies that a specified item has been clicked.
     * <P>
     * Item will be sent through {@linkplain #SOCKET_OUT_FOCUSED_ITEM} output socket and mold will be asked to focus
     * provided item.<br>
     * If {@linkplain #SETTING_HYPERLINKS_ENABLED} is set to <code>true</code>, selection will not be affected. Otherwise
     * event will be also treated as item selection.
     * </P>
     *
     * @param item
     *           clicked item
     * @see CollectionBrowserMoldStrategy#focusItem(Object, Object)
     * @see DefaultSelectAndFocusDelegateController#selectItems(Collection)
     * @deprecated since 6.7, {@link DefaultSelectAndFocusDelegateController#handleItemClicked(Object)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void notifyItemClicked(final Object item)
    {
        getSelectAndFocusDelegateController().handleItemClicked(item);
    }


    @SocketEvent(socketId = SOCKET_IN_FOCUS_ITEM)
    public void focusItem(final Object item)
    {
        setItemFocused(item);
    }


    /**
     * @deprecated since 6.7, please use the {@link SelectAndFocusDelegateController#focusItem(Object)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void setItemFocused(final Object item)
    {
        getSelectAndFocusDelegateController().focusItem(item);
    }


    /**
     * Gets currently selected items.
     * <P>
     * Method returns a value provided in last call to {@link #setSelectedItems(Collection)}
     * </P>
     *
     * @param <E>
     *           expected type of selected items
     * @return selected items
     * @see #setSelectedItems(Collection)
     * @deprecated since 6.7, please use {@link SelectAndFocusDelegateController#getSelectedItems()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public <E> Collection<E> getSelectedItems()
    {
        return getSelectAndFocusDelegateController().getSelectedItems();
    }


    /**
     * Sets new collection of selected items. All values needed are updated and socket messages are being sent.
     *
     * @deprecated since 6.7, please use the {@link SelectAndFocusDelegateController#selectItems(Collection)} instead
     * @param selectedItems
     *           items that has been selected
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void setSelectedItems(final Collection<?> selectedItems)
    {
        getSelectAndFocusDelegateController().selectItems(selectedItems);
    }


    /**
     * Gets an item that is currently focused.
     * <P>
     * Method returns a value provided in last call to {@link #notifyItemClicked(Object)}
     * </P>
     *
     * @param <E>
     *           expected type of focused item
     * @return focused item
     * @see #notifyItemClicked(Object)
     * @deprecated since 6.7, please use {@link SelectAndFocusDelegateController#getFocusedItem()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public <E> E getFocusedItem()
    {
        return getSelectAndFocusDelegateController().getFocusedItem();
    }


    /**
     * @deprecated since 6.7, please use the {@link SelectAndFocusDelegateController#selectItems(Collection)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void selectItems(final Collection<?> selectedItems)
    {
        getSelectAndFocusDelegateController().selectItems(selectedItems);
    }


    public void handleSelectionChanged(final Collection<?> selectedItems)
    {
        setSelectedItemsCount(selectedItems.size());
    }


    public int getSelectedItemsCount()
    {
        return selectedItemsCount;
    }


    protected void setSelectedItemsCount(final int count)
    {
        final String settingPagingPosition = getWidgetSettings().getString(SETTING_PAGING_POSITION);
        final boolean isPagingInTheBottom = StringUtils.equals(settingPagingPosition, SETTING_PAGING_POSITION_BOTTOM);
        getBottomBar().setVisible(isPagingInTheBottom || isMultiSelectEnabled());
        setStatusBarVisible(isMultiSelectEnabled());
        updateItemCount(count);
    }


    protected void updateItemCount(final int count)
    {
        selectedItemsCount = count;
        itemCount.setLabel(getLabel(COMPONENT_ITEM_COUNT, new Object[]
                        {String.valueOf(selectedItemsCount)}));
        itemCount.setDisabled(selectedItemsCount < 1);
    }


    public void callExecuteOperation(final Operation operation, final EventListener<Event> callbackEvent, final String busyMessage)
    {
        executeOperation(operation, callbackEvent, busyMessage);
    }


    public void setStatusBarVisible(final boolean visible)
    {
        statusBar.setVisible(visible);
    }


    /**
     * @deprecated since 6.7, please use the
     *             {@link DefaultSelectAndFocusDelegateController#sendNavigationItemSelectorContext(NavigationItemSelectorContext)}
     *             instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void sendNavigationItemSelectorContext(final NavigationItemSelectorContext navigationContext)
    {
        sendOutput(SOCKET_PREVIOUS_ITEM_SELECTOR_CONTEXT, navigationContext);
        sendOutput(SOCKET_NEXT_ITEM_SELECTOR_CONTEXT, navigationContext);
    }


    public CollectionBrowserMoldStrategy getActiveMold()
    {
        if(activeMold != null)
        {
            return activeMold;
        }
        if(CollectionUtils.isEmpty(availableMolds))
        {
            EMPTY_MOLD.setContext(new DefaultCollectionBrowserContext(this));
            setActiveMold(EMPTY_MOLD);
            return activeMold;
        }
        final String modelActiveMold = getModel().getValue(MODEL_ACTIVE_MOLD_NAME, String.class);
        final String defaultMold = getCollectionBrowserConfigurationLoader().loadDefaultMold();
        final String activeMoldName = StringUtils.defaultIfBlank(modelActiveMold, defaultMold);
        if(StringUtils.isNotBlank(activeMoldName))
        {
            for(final CollectionBrowserMoldStrategy mold : availableMolds)
            {
                if(StringUtils.equals(mold.getName(), activeMoldName))
                {
                    setActiveMold(mold);
                    return activeMold;
                }
            }
        }
        setActiveMold(availableMolds.iterator().next());
        return activeMold;
    }


    public void setActiveMold(final CollectionBrowserMoldStrategy activeMold)
    {
        this.activeMold = activeMold;
        getModel().put(MODEL_ACTIVE_MOLD_NAME, activeMold != null ? activeMold.getName() : null);
    }


    /**
     * @return <code>true</code> if widget supports hyperlinks in displayed values
     */
    public boolean areHyperlinksEnabled()
    {
        return (Boolean)getWidgetSettings().getOrDefault(SETTING_HYPERLINKS_ENABLED, DEFAULT_HYPERLINKS_ENABLED)
                        && hasPermissionToType();
    }


    protected boolean hasPermissionToType()
    {
        return Optional.ofNullable(getModel().getValue(MODEL_DATA_TYPE_CODE, String.class)).map(getPermissionFacade()::canReadType)
                        .orElse(true);
    }


    public boolean isMultiSelectEnabled()
    {
        Boolean multiSelectEnabled = getValue(MODEL_MULTI_SELECT_ENABLED, Boolean.class);
        if(multiSelectEnabled == null)
        {
            // moldConfig > browserConfig > widgetSetting
            multiSelectEnabled = BooleanUtils.toBooleanDefaultIfNull(
                            getCollectionBrowserConfigurationLoader().loadEnableMultiSelect(getActiveMold()),
                            getWidgetSettings().getBoolean(SETTING_MULTI_SELECT));
            setValue(MODEL_MULTI_SELECT_ENABLED, BooleanUtils.toBooleanDefaultIfNull(multiSelectEnabled, false));
        }
        return multiSelectEnabled;
    }


    public boolean isSendItemsOnSelectEnabled()
    {
        return (Boolean)getWidgetSettings().getOrDefault(SETTING_SEND_ITEMS_ON_SELECT, DEFAULT_SEND_ITEMS_ON_SELECT);
    }


    /**
     * @return returns if items can be dragged from this widget
     */
    public boolean isDragEnabled()
    {
        return getWidgetSettings().getBoolean(SETTING_DRAG_ENABLED);
    }


    /**
     * @return returns if items can be dropped on this widget
     */
    public boolean isDropEnabled()
    {
        return getWidgetSettings().getBoolean(SETTING_DROP_ENABLED);
    }


    public Label getListTitle()
    {
        return listTitle;
    }


    public Label getListSubtitle()
    {
        return listSubtitle;
    }


    public Button getItemCount()
    {
        return itemCount;
    }


    public Button getDeselectAll()
    {
        return deselectAll;
    }


    public Popup getDeselectPopup()
    {
        return deselectPopup;
    }


    public Paging getPaging()
    {
        return paging;
    }


    public Div getStatusBar()
    {
        return statusBar;
    }


    public Div getBrowserContainer()
    {
        return browserContainer;
    }


    public Hbox getMoldSelectorContainer()
    {
        return moldSelectorContainer;
    }


    public Actions getActionSlot()
    {
        return actionSlot;
    }


    public Label getNumberOfItemsLabel()
    {
        return numberOfItemsLabel;
    }


    public void setNumberOfItemsLabel(final Label numberOfItemsLabel)
    {
        this.numberOfItemsLabel = numberOfItemsLabel;
    }


    public Div getContentsContainer()
    {
        return contentsContainer;
    }


    public Div getPagingContainerTop()
    {
        return pagingContainerTop;
    }


    public void setPagingContainerTop(final Div pagingContainerTop)
    {
        this.pagingContainerTop = pagingContainerTop;
    }


    public Div getPagingContainerBottom()
    {
        return pagingContainerBottom;
    }


    public void setPagingContainerBottom(final Div pagingContainerBottom)
    {
        this.pagingContainerBottom = pagingContainerBottom;
    }


    public Div getBottomBar()
    {
        return bottomBar;
    }


    public void setBottomBar(final Div bottomBar)
    {
        this.bottomBar = bottomBar;
    }


    /**
     * Sets the list of available mold strategies. If the list is not set before call to
     * {@link #initialize(org.zkoss.zk.ui.Component)} method, controller tries to load all Spring beans implementing
     * {@link com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy} interface
     *
     * @param availableMolds
     *           list of available mold strategies
     */
    public void setAvailableMolds(final List<CollectionBrowserMoldStrategy> availableMolds)
    {
        this.availableMolds = availableMolds;
    }


    protected CollectionBrowserConfigurationLoader getCollectionBrowserConfigurationLoader()
    {
        return collectionBrowserConfigurationLoader;
    }


    public void setCollectionBrowserConfigurationLoader(
                    final CollectionBrowserConfigurationLoader collectionBrowserConfigurationLoader)
    {
        this.collectionBrowserConfigurationLoader = collectionBrowserConfigurationLoader;
    }


    public PagingDelegateController getPagingDelegateController()
    {
        return pagingDelegateController;
    }


    public void setPagingDelegateController(final PagingDelegateController pagingDelegateController)
    {
        this.pagingDelegateController = pagingDelegateController;
    }


    public TitleDelegateController getTitleDelegateController()
    {
        return titleDelegateController;
    }


    public void setTitleDelegateController(final TitleDelegateController titleDelegateController)
    {
        this.titleDelegateController = titleDelegateController;
    }


    public SelectAndFocusDelegateController getSelectAndFocusDelegateController()
    {
        return selectAndFocusDelegateController;
    }


    public void setSelectAndFocusDelegateController(final SelectAndFocusDelegateController selectAndFocusDelegateController)
    {
        this.selectAndFocusDelegateController = selectAndFocusDelegateController;
    }


    protected BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return backofficeTypeUtils;
    }


    public void setBackofficeTypeUtils(final BackofficeTypeUtils backofficeTypeUtils)
    {
        this.backofficeTypeUtils = backofficeTypeUtils;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public ExceptionTranslationService getExceptionTranslationService()
    {
        return exceptionTranslationService;
    }


    public void setExceptionTranslationService(final ExceptionTranslationService exceptionTranslationService)
    {
        this.exceptionTranslationService = exceptionTranslationService;
    }


    public FieldSearchFacade getFieldSearchFacade()
    {
        return fieldSearchFacade;
    }


    public void setFieldSearchFacade(final FieldSearchFacade<?> fieldSearchFacade)
    {
        this.fieldSearchFacade = fieldSearchFacade;
    }


    protected TypeCodeResolver getTypeCodeResolver()
    {
        return typeCodeResolver;
    }


    public void setTypeCodeResolver(final TypeCodeResolver typeCodeResolver)
    {
        this.typeCodeResolver = typeCodeResolver;
    }
}
