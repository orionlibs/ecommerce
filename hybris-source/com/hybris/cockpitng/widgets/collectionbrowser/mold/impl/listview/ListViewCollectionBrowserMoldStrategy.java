/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
/*
/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview;

import com.google.common.collect.Iterables;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dnd.SelectionSupplier;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.event.ZulEvents;
import org.zkoss.zul.ext.Selectable;

/**
 * List view mold strategy for Collection Browser widget
 *
 * Renders data in simple list form with paging<br>
 * Uses zk {@link Listbox} component
 */
public class ListViewCollectionBrowserMoldStrategy extends AbstractMoldStrategy<Listitem, ListView, Object>
{
    public static final String SETTING_ITEM_RENDERER = "itemRenderer";
    public static final String SETTING_COMPONENT_CTX_CODE = "colConfigCtxCode";
    public static final String SETTING_INVALIDATION_SOCKET_OUTPUT_NAME = "invalidationSocketOutput";
    public static final String TOOLTIP_PROPERTY_KEY = "mold.listview.tooltip";
    public static final String MODEL_LIST = "listModel";
    public static final String MODEL_INDEXES = "listModelIndexes";
    public static final String SCLASS_COL_HEADER = "yw-listview-colheader";
    public static final String SCLASS_COL_HEADER_FILL = "yw-listview-colheader-fill";
    public static final String SCLASS_COL_HEAD = "yw-listview-colhead";
    public static final String SORTABLE_LIST_HEADER = "sortableListHeader";
    public static final String SORT_DIRECTION = "sortDirection";
    public static final String SORT_COLUMN_ID = "sortColumnId";
    public static final String ASCENDING_TOKEN = "ascending";
    public static final String DESCENDING_TOKEN = "descending";
    public static final String HFLEX_MIN = "min";
    public static final String SCLASS_LISTVIEW_CELL = "yw-listview-cell";
    public static final String HFLEX_COLUMN_MIN_WIDTH = "30";
    public static final String HFLEX_COLUMN_MIN_WIDTH_ATTR = "flexMinWidth";
    private static final String DEFAULT_HYPERLINK_VALUE = "#root";
    private static final Logger LOG = LoggerFactory.getLogger(ListViewCollectionBrowserMoldStrategy.class);
    private ExpressionResolverFactory resolverFactory;
    @Wire
    private Listbox listBox;
    @Wire
    private Listhead listBoxHead;
    private Component parent;


    protected Listheader createListHeader(final SortData sortData, final ListColumn column, final String typeCode)
    {
        final String qualifier = column.getQualifier();
        String propertyQualifier = qualifier;
        if(propertyQualifier != null && propertyQualifier.contains("."))
        {
            propertyQualifier = propertyQualifier.substring(propertyQualifier.lastIndexOf('.') + 1);
        }
        final String listColumnHeaderLabel = UILabelUtil.getColumnHeaderLabel(column, typeCode, getLabelService());
        final Listheader listHeader = new Listheader(listColumnHeaderLabel, null);
        YTestTools.modifyYTestId(listHeader, "listheader_" + qualifier);
        if(this.isSortableHeaders() && isSortableAttribute(column))
        {
            listHeader.setTooltiptext(getContext().getWidgetInstanceManager().getLabel("clickToSort", new Object[]
                            {listColumnHeaderLabel}));
            this.configureHeaderSort(listHeader, sortData, propertyQualifier);
        }
        listHeader.setValue(qualifier);
        return listHeader;
    }


    protected boolean isSortableAttribute(final ListColumn column)
    {
        return column.isSortable() && getContext().isSortable(column.getQualifier());
    }


    protected void configureHeaderSort(final Listheader header, final SortData sortData, final String propertyQualifier)
    {
        header.addEventListener(Events.ON_SORT, event -> onSortEvent((SortEvent)event));
        // just dummy comparator only to satisfy zk list header
        final Comparator<Object> ascComparator = (object1, object2) -> 0;
        header.setSortAscending(ascComparator);
        header.setSortDescending(Collections.reverseOrder(ascComparator));
        if(sortData != null && propertyQualifier.equals(sortData.getSortAttribute()))
        {
            header.setSortDirection(sortData.isAscending() ? ASCENDING_TOKEN : DESCENDING_TOKEN);
        }
        else
        {
            header.setSort(ASCENDING_TOKEN);
        }
    }


    public void onSortEvent(final SortEvent sortEvent)
    {
        final Listheader headerRef = (Listheader)sortEvent.getTarget();
        final String internalSortProperty = headerRef.getValue();
        final boolean internalAsc = sortEvent.isAscending();
        getWidgetModel().setValue(SORT_COLUMN_ID, internalSortProperty);
        getWidgetModel().setValue(SORT_DIRECTION, Boolean.valueOf(internalAsc));
        getContext().sort(new SortData(internalSortProperty, internalAsc));
    }


    /**
     * @deprecated since 2005
     *{@link ListViewCollectionBrowserMoldStrategy#initialize(SinglePage)} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void initialize()
    {
        // do nothing
    }


    protected void initialize(final SinglePage singlePage)
    {
        listBox = new Listbox();
        YTestTools.modifyYTestId(listBox, String.format("mainListBox-%s", getName()));
        listBox.setHflex("false");
        listBox.setVflex("true");
        listBox.setEmptyMessage(chooseEmptyMessageToDisplayFor(singlePage));
        listBox.addEventListener(Events.ON_SELECT, this::onSelectItemEvent);
        listBox.addEventListener(Events.ON_AFTER_SIZE, event -> Clients.resize(parent));
        listBox.addEventListener("onCheckSelectAll", event -> ((Listbox)event.getTarget()).setFocus(true));
        listBox.addEventListener(ZulEvents.ON_AFTER_RENDER, event -> {
            final TypedSettingsMap settings = getContext().getWidgetInstanceManager().getWidgetSettings();
            final String invalidSocketName = settings.getString(SETTING_INVALIDATION_SOCKET_OUTPUT_NAME);
            if(StringUtils.isNotBlank(invalidSocketName))
            {
                getContext().getWidgetInstanceManager().sendOutput(invalidSocketName, Collections.emptyMap());
            }
            listBox.renderAll();
        });
        listBox.setNonselectableTags("*");
        initSelectionMode();
    }


    @Override
    public void reset()
    {
        resetModel();
        resetView();
    }


    protected void resetModel()
    {
        getWidgetModel().setValue(MODEL_LIST, null);
        getWidgetModel().setValue(MODEL_COLUMNS_CONFIG, null);
    }


    protected void resetView()
    {
        buildHeaders(null, null, null);
        if(getListBox() != null)
        {
            getListBox().setModel((ListModel<?>)null);
        }
    }


    @Override
    public void render(final Component parent, final SinglePage singlePage)
    {
        this.parent = parent;
        initialize(singlePage);
        parent.appendChild(getListBox());
        if(!singlePage.equals(SinglePage.EMPTY))
        {
            setPage(singlePage);
        }
    }


    protected Component getParent()
    {
        return parent;
    }


    @Override
    public void setPage(final SinglePage singlePage)
    {
        Validate.notNull("Page may not be null", singlePage);
        if(getPermissionFacade().canReadType(singlePage.getTypeCode()))
        {
            processViewWith(singlePage);
        }
    }


    protected void processViewWith(final SinglePage singlePage)
    {
        final ListView columnConfiguration = getColumnConfiguration(singlePage.getTypeCode());
        if(isEmptyOrNotConfiguredPage(singlePage, columnConfiguration))
        {
            renderListWithoutConfiguration(singlePage);
            return;
        }
        getWidgetModel().setValue(MODEL_COLUMNS_CONFIG, columnConfiguration);
        setTypeCode(singlePage.getTypeCode());
        renderList(singlePage.getList(), columnConfiguration, singlePage.getSortData(), singlePage.getTypeCode());
    }


    protected boolean isEmptyOrNotConfiguredPage(final SinglePage singlePage, final ListView columnConfiguration)
    {
        return singlePage.equals(SinglePage.EMPTY) || columnConfiguration == null
                        || CollectionUtils.isEmpty(columnConfiguration.getColumn());
    }


    protected void renderListWithoutConfiguration(final SinglePage singlePage)
    {
        if(!singlePage.equals(SinglePage.EMPTY))
        {
            LOG.warn("Missing Column Configuration for type: {}", singlePage.getTypeCode());
        }
        final ListModelArray<Object> simpleListModel = createListModel(singlePage.getList());
        getWidgetModel().setValue(MODEL_LIST, simpleListModel);
        renderEntriesWithoutConfiguration(simpleListModel);
    }


    protected void renderEntriesWithoutConfiguration(final ListModelArray<?> simpleListModel)
    {
        try
        {
            updateSelectionStatus(simpleListModel);
            if(simpleListModel.getSize() > 0)
            {
                getListBox().setItemRenderer((listItem, object, index) -> {
                    final Listcell cell = new Listcell();
                    cell.setLabel(getLabelService().getObjectLabel(object));
                    cell.setSclass(SCLASS_LISTVIEW_CELL);
                    listItem.appendChild(cell);
                });
            }
        }
        catch(final RuntimeException e)
        {
            LOG.error("Could not load record page for list.", e);
        }
    }


    protected ListView getColumnConfiguration(final String typeCode)
    {
        if(isStrategyInitialized(typeCode))
        {
            return getWidgetModel().getValue(MODEL_COLUMNS_CONFIG, ListView.class);
        }
        if(StringUtils.isBlank(typeCode))
        {
            return new ListView();
        }
        return loadColumnConfiguration(typeCode);
    }


    protected boolean isStrategyInitialized(final String typeCode)
    {
        return StringUtils.isNotBlank(typeCode) && typeCode.equals(getTypeCode());
    }


    /**
     * Loads the (column) UI configuration for the provided type with code <p>typeCode</p>.
     *
     * @param typeCode
     *           type code
     * @return the column UI configuration
     */
    protected ListView loadColumnConfiguration(final String typeCode)
    {
        ListView config = null;
        final TypedSettingsMap settings = getContext().getWidgetInstanceManager().getWidgetSettings();
        final String componentCode = settings.getString(SETTING_COMPONENT_CTX_CODE);
        final DefaultConfigContext configContext = new DefaultConfigContext(componentCode);
        configContext.setType(typeCode);
        try
        {
            config = getContext().getWidgetInstanceManager().loadConfiguration(configContext, ListView.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationNotFoundException ccnfe)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
            }
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
        }
        return config;
    }


    @Override
    protected int getUiModelSize()
    {
        if(getListBox() == null)
        {
            return 0;
        }
        final ListModel model = getListBox().getModel();
        if(model == null)
        {
            return 0;
        }
        return model.getSize();
    }


    @Override
    protected Optional<Object> getUiElementAt(final int index)
    {
        if(getListBox() == null)
        {
            return Optional.empty();
        }
        final ListModel model = getListBox().getModel();
        if(model == null)
        {
            return Optional.empty();
        }
        return Optional.of(model.getElementAt(index));
    }


    /**
     * @deprecated use #handleChangeFocus(Object) instead
     * @see #handleChangeFocus(Object)
     * @since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void handleChangeSelection(final Object itemToSelect)
    {
        if(getListBox().getListModel() instanceof Selectable)
        {
            final Selectable<?> selectable = (Selectable<?>)getListBox().getListModel();
            if(getContext().isMultiSelectEnabled())
            {
                final Set<?> selection = selectable.getSelection();
                if(selection != null && selection.size() < 2)
                {
                    deselectItems();
                    select(itemToSelect, selectable);
                }
            }
            else
            {
                select(itemToSelect, selectable);
            }
        }
    }


    @Override
    protected void handleChangeFocus(final Object itemToFocus)
    {
        getContext().notifyItemClicked(itemToFocus);
    }


    protected void select(final Object itemToSelect, final Selectable<?> selectable)
    {
        final Collection newSelection = Collections.singleton(itemToSelect);
        selectable.setSelection(newSelection);
        getContext().notifyItemsSelected(newSelection);
    }


    @Override
    protected void handleCollectionUpdate(final Collection<Object> collection)
    {
        final ListModelArray<Object> listModel = (ListModelArray<Object>)getListBox().getModel();
        if(listModel != null)
        {
            for(final Object item : collection)
            {
                updateModelData(listModel, item);
            }
        }
    }


    @Override
    public void selectItems(final Set<?> items)
    {
        if(CollectionUtils.isEmpty(items))
        {
            deselectItems();
        }
        else
        {
            final ListModel model = getListModel();
            if(model instanceof Selectable)
            {
                ((Selectable<?>)model).setSelection((Collection)items);
            }
        }
    }


    @Override
    public void deselectItems()
    {
        final ListModel model = getListModel();
        if(model instanceof Selectable)
        {
            ((Selectable)model).clearSelection();
        }
    }


    protected void setItemFocused(final Listitem item, final boolean focused)
    {
        UITools.modifySClass(item, SCLASS_CELL_FOCUSED, focused);
    }


    @Override
    public void focusItem(final Object oldFocus, final Object newFocus)
    {
        final Map<Object, Integer> indexes = getWidgetModel().getValue(MODEL_INDEXES, Map.class);
        if(indexes != null)
        {
            if(indexes.containsKey(oldFocus))
            {
                final int oldIndex = indexes.get(oldFocus).intValue();
                final Listitem oldItem = getListBox().getItemAtIndex(oldIndex);
                setItemFocused(oldItem, false);
            }
            if(indexes.containsKey(newFocus))
            {
                final int newIndex = indexes.get(newFocus).intValue();
                final Listitem newItem = getListBox().getItemAtIndex(newIndex);
                setItemFocused(newItem, true);
            }
        }
    }


    @Override
    public void release()
    {
        if(getParent() != null)
        {
            getParent().getChildren().remove(getListBox());
            listBox = null;
            listBoxHead = null;
        }
        getWidgetModel().setValue(MODEL_LIST, null);
        getWidgetModel().remove(CollectionBrowserController.MODEL_EXPORT_COLUMNS_AND_DATA);
    }


    /**
     * Initializes the selection mode i.e. sets whether multi-selection is enabled or not (based on the widget setting
     * {@value CollectionBrowserController#SETTING_MULTI_SELECT}) and if so, enables checkbox selection.
     */
    protected void initSelectionMode()
    {
        final boolean isMultiSelectEnabled = getContext().isMultiSelectEnabled();
        if(getListBox() != null)
        {
            getListBox().setMultiple(isMultiSelectEnabled);
            getListBox().setCheckmark(isMultiSelectEnabled);
        }
    }


    protected void buildHeaders(final ListView columnConfig, final SortData sortData, final String typeCode)
    {
        if(getListBox() == null)
        {
            return;
        }
        final Listhead listhead = getListBox().getListhead();
        if(columnConfig == null || columnConfig.isShowHeader())
        {
            if(listhead == null)
            {
                listBoxHead = new Listhead();
                listBoxHead.setSizable(true);
                listBoxHead.setSclass(SCLASS_COL_HEAD);
                listBox.appendChild(listBoxHead);
            }
            else if(!listhead.equals(listBoxHead))
            {
                listBoxHead = listhead;
            }
            listBoxHead.getChildren().clear();
            listBoxHead.addEventListener(ZulEvents.ON_COL_SIZE, event -> Clients.resize(parent));
            if(columnConfig == null)
            {
                return;
            }
            final List<ListColumn> columns = columnConfig.getColumn();
            if(CollectionUtils.isNotEmpty(columns))
            {
                for(final ListColumn column : columns)
                {
                    final Listheader columnHeader = createListHeader(sortData, column, typeCode);
                    listBoxHead.appendChild(columnHeader);
                    applyColumnAttributes(column, columnHeader);
                    UITools.modifySClass(columnHeader, SCLASS_COL_HEADER, true);
                }
                final boolean shouldAddFillColumn = allColumnsHaveHflexMin(columns);
                if(shouldAddFillColumn)
                {
                    final Listheader fill = new Listheader();
                    listBoxHead.appendChild(fill);
                    UITools.modifySClass(fill, SCLASS_COL_HEADER, true);
                    UITools.modifySClass(fill, SCLASS_COL_HEADER_FILL, true);
                }
            }
        }
        else if(listhead != null)
        {
            listhead.setParent(null);
            listBoxHead = null;
        }
    }


    protected boolean allColumnsHaveHflexMin(final List<ListColumn> columns)
    {
        return columns.stream().allMatch(column -> StringUtils.isBlank(column.getHflex()) || column.getHflex().equals(HFLEX_MIN));
    }


    protected void applyColumnAttributes(final ListColumn column, final Listheader columnHeader)
    {
        if(StringUtils.isNotBlank(column.getWidth()))
        {
            columnHeader.setWidth(column.getWidth());
        }
        else if(StringUtils.isNotBlank(column.getHflex()))
        {
            columnHeader.setHflex(column.getHflex());
            columnHeader.setClientAttribute(HFLEX_COLUMN_MIN_WIDTH_ATTR, HFLEX_COLUMN_MIN_WIDTH);
        }
        else
        {
            columnHeader.setHflex(HFLEX_MIN);
        }
    }


    public void renderList(final List<?> list, final ListView config, final SortData sortData, final String typeCode)
    {
        buildHeaders(config, sortData, typeCode);
        renderEntries(list, config);
    }


    protected void updateModelData(final ListModelArray<Object> listModel, final Object data)
    {
        for(int i = 0; i < listModel.getSize(); i++)
        {
            if(Objects.equals(data, listModel.getElementAt(i)))
            {
                listModel.set(i, data);
                break;
            }
        }
    }


    protected void renderEntries(final List<?> currentPage, final ListView config)
    {
        final ListModelArray<Object> simpleListModel = createListModel(currentPage);
        simpleListModel.setMultiple(getContext().isMultiSelectEnabled());
        getWidgetModel().setValue(MODEL_LIST, simpleListModel);
        renderEntries(simpleListModel, config);
    }


    protected void renderEntries(final ListModelArray<?> simpleListModel, final ListView config)
    {
        try
        {
            updateSelectionStatus(simpleListModel);
            if(simpleListModel.getSize() > 0 && getRenderer() != null)
            {
                final SelectionSupplier selectionSupplier = () -> new ArrayList(getContext().getSelectedItems());
                final Map<Object, Integer> indexes = new HashedMap();
                getWidgetModel().setValue(MODEL_INDEXES, indexes);
                getListBox().setItemRenderer((row, entry, index) -> {
                    getRenderer().render(row, config, entry, getDataType(entry), getContext().getWidgetInstanceManager());
                    indexes.put(entry, Integer.valueOf(index));
                    final DefaultCockpitContext context = new DefaultCockpitContext();
                    final Map inputContext = getWidgetModel().getValue(CollectionBrowserController.MODEL_SELECTION_CONTEXT, Map.class);
                    if(inputContext != null)
                    {
                        context.setParameters(inputContext);
                    }
                    if(getContext().getDragAndDropStrategy() != null && getContext().isDragEnabled())
                    {
                        getContext().getDragAndDropStrategy().makeDraggable(row, entry, context, selectionSupplier);
                    }
                    if(getContext().getDragAndDropStrategy() != null && getContext().isDropEnabled())
                    {
                        getContext().getDragAndDropStrategy().makeDroppable(row, entry, context);
                    }
                });
            }
        }
        catch(final RuntimeException e)
        {
            LOG.error("Could not load record page for list.", e);
        }
    }


    protected WidgetComponentRendererListener<Listitem, ListView, Object> createRowRenderedListener()
    {
        return event -> {
            if(event.isFinal())
            {
                if(!getContext().areHyperlinksSupported())
                {
                    UITools.modifySClass(event.getParent(), SCLASS_CELL_HYPERLINK, true);
                }
                setItemFocused(event.getParent(), Objects.equals(event.getData(), getContext().getFocusedItem()));
            }
        };
    }


    protected WidgetComponentRendererListener<Listitem, ListView, Object> createLinkRenderedListener(
                    final ListColumn forcedLinkColumn)
    {
        return event -> {
            if(event.isFinal())
            {
                IntStream.range(0, event.getConfig().getColumn().size()).forEach(index -> {
                    final ListColumn column = event.getConfig().getColumn().get(index);
                    final Listcell cell = event.getParent().<Listcell>getChildren().get(index);
                    if(Boolean.TRUE.equals(column.isLink()) || Objects.equals(column, forcedLinkColumn))
                    {
                        addCellLink(cell, column, cell.getValue());
                    }
                });
            }
        };
    }


    protected WidgetComponentRendererListener<Listitem, ListView, Object> createCellRenderedListener()
    {
        return event -> {
            if(event.isFinal())
            {
                IntStream.range(0, event.getParent().getChildren().size()).forEach(index -> {
                    final Listcell cell = event.getParent().<Listcell>getChildren().get(index);
                    cell.addEventListener(Events.ON_CLICK, click -> onClickItemEvent(click, cell.getValue()));
                });
            }
        };
    }


    protected void addCellLink(final HtmlBasedComponent component, final ListColumn column, final Object entry)
    {
        final String linkValue = ObjectUtils.defaultIfNull(column.getLinkValue(), DEFAULT_HYPERLINK_VALUE);
        final ExpressionResolver resolver = getResolverFactory().createResolver();
        addLink(component, () -> resolver.getValue(entry, linkValue));
    }


    protected void updateSelectionStatus(final ListModelArray<?> simpleListModel)
    {
        simpleListModel.setMultiple(getListBox().isMultiple());
        this.setModelAndKeepSortedStatus4ListBox(simpleListModel);
        final Collection<?> selectionModel = getContext().getSelectedItems();
        final Collection selection = selectionModel == null ? Collections.emptySet() : selectionModel;
        simpleListModel.setSelection(selection);
    }


    protected void setModelAndKeepSortedStatus4ListBox(final ListModelArray<?> simpleListModel)
    {
        final Map<Listheader, String> headerToDirectionValues = new HashMap<>();
        final Listhead hds = getListBox().getListhead();
        if(null != hds)
        {
            hds.getChildren().forEach(listheader -> {
                final String sortDirection = ((Listheader)listheader).getSortDirection();
                if(ASCENDING_TOKEN.equals(sortDirection) || DESCENDING_TOKEN.equals(sortDirection))
                {
                    headerToDirectionValues.put((Listheader)listheader, sortDirection);
                }
            });
        }
        getListBox().setModel(simpleListModel);
        headerToDirectionValues.forEach((listheader, sortDirection) -> {
            listheader.setSortDirection(sortDirection);
        });
    }


    protected ListModelArray<Object> createListModel(final List<?> data)
    {
        return new UnsortableListModelArray<>(data);
    }


    protected void onClickItemEvent(final Event event, final Object item)
    {
        if(!getContext().areHyperlinksSupported() || !isLink(event.getTarget()))
        {
            getContext().notifyItemClicked(item);
        }
    }


    protected void onSelectItemEvent(final Event event)
    {
        if(event instanceof SelectEvent)
        {
            final LinkedHashSet<Object> effectiveSelection = new LinkedHashSet<>(getContext().getSelectedItems());
            ((SelectEvent<Listitem, Object>)event).getUnselectedItems().stream().map(this::getItemObject)
                            .forEach(effectiveSelection::remove);
            ((SelectEvent<Listitem, Object>)event).getSelectedItems().stream().map(this::getItemObject)
                            .forEach(effectiveSelection::add);
            getContext().notifyItemsSelected(effectiveSelection);
        }
    }


    protected Object getItemObject(final Listitem item)
    {
        return item.getValue();
    }


    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected void addPreviouslySelected(final Event event, final Set<Object> selection)
    {
        if(event instanceof SelectEvent)
        {
            selection.addAll(getContext().getSelectedItems());
            final SelectEvent selectEvent = (SelectEvent)event;
            selection.removeAll(selectEvent.getUnselectedObjects());
        }
    }


    @Override
    public String getName()
    {
        return "list-view";
    }


    @Override
    public String getTooltipText()
    {
        return getContext().getWidgetInstanceManager().getLabel(TOOLTIP_PROPERTY_KEY);
    }


    @Override
    protected void initializeRenderer(final NotifyingWidgetComponentRenderer<Listitem, ListView, Object> renderer)
    {
        super.initializeRenderer(renderer);
        if(getContext().areHyperlinksSupported())
        {
            final ListView config = getColumnConfiguration(getTypeCode());
            final ListColumn forcedLinkColumn;
            if(config.getColumn().stream().noneMatch(column -> column.isLink() != null && Boolean.TRUE.equals(column.isLink())))
            {
                final Optional<ListColumn> first = config.getColumn().stream().filter(column -> column.getSpringBean() == null)
                                .findFirst();
                forcedLinkColumn = first.orElse(null);
            }
            else
            {
                forcedLinkColumn = null;
            }
            renderer.addRendererListener(createLinkRenderedListener(forcedLinkColumn));
        }
        renderer.addRendererListener(createRowRenderedListener());
        renderer.addRendererListener(createCellRenderedListener());
    }


    @Override
    protected String getRendererSetting()
    {
        return SETTING_ITEM_RENDERER;
    }


    protected ListModel getListModel()
    {
        return getListBox() != null ? getListBox().getModel() : null;
    }


    protected boolean isSortableHeaders()
    {
        return getContext().getWidgetInstanceManager().getWidgetSettings().getBoolean(SORTABLE_LIST_HEADER);
    }


    @Override
    public NavigationItemSelectorContext getNavigationItemSelectorContext()
    {
        if(getFocusedObjectFromModel().isPresent())
        {
            final SinglePage singlePage = getContext().getCurrentPage();
            final Collection<Object> selectedItems = getContext().getSelectedItems();
            final Optional<Object> focusedItem = getFocusedObjectFromModel();
            final int pageSize = singlePage == null ? SinglePage.EMPTY.getPageSize() : singlePage.getListSize();
            final int indexOfLastSelectedItem = selectedItems.isEmpty() ? -1 : getIndexOf(Iterables.getLast(selectedItems));
            final int indexOfFocusedItem = focusedItem.map(this::getIndexOf).orElse(-1);
            return new NavigationItemSelectorContext(pageSize, indexOfLastSelectedItem, indexOfFocusedItem);
        }
        else
        {
            return EMPTY_NAVIGATION_ITEM_SELECTOR_CONTEXT;
        }
    }


    protected Set getSelectedObjectsFromModel()
    {
        final Collection<?> selected = getContext().getSelectedItems();
        return selected != null ? new HashSet<>(selected) : Collections.emptySet();
    }


    @Override
    protected WidgetModel getWidgetModel()
    {
        return getContext().getWidgetInstanceManager().getModel();
    }


    @Override
    public void handleObjectCreateEvent(final CockpitEvent event)
    {
        // NOOP
    }


    @Override
    public void handleObjectDeleteEvent(final CockpitEvent event)
    {
        // NOOP
    }


    public Listhead getListBoxHead()
    {
        return listBoxHead;
    }


    public Listbox getListBox()
    {
        return listBox;
    }


    protected ExpressionResolverFactory getResolverFactory()
    {
        return resolverFactory;
    }


    @Required
    public void setResolverFactory(final ExpressionResolverFactory resolverFactory)
    {
        this.resolverFactory = resolverFactory;
    }


    protected static class UnsortableListModelArray<E> extends ListModelArray<E>
    {
        public UnsortableListModelArray(final List<? extends E> data)
        {
            super(data == null ? Collections.emptyList() : data);
        }


        @Override
        public void sort(final Comparator<E> cmpr, final boolean ascending)
        {
            // do nothing - we use server side sorting
        }
    }
}
