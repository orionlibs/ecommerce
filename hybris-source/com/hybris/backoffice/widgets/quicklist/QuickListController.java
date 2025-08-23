/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicklist;

import com.hybris.backoffice.widgets.quicklist.renderer.QuickListItemRenderer;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.config.quicklist.jaxb.QuickList;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRendererFactory;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Selectable;

public class QuickListController extends DefaultWidgetController
{
    public static final String SCLASS_YW_QUICK_LIST_TILE = "yw-quicklist-tile";
    public static final String SCLASS_YW_QUICK_LIST_TILE_SELECTED = "yw-quicklist-tile-selected";
    public static final String SCLASS_QUICKLIST_INFO_LABEL = "yw-quicklist-info-label";
    public static final String SETTING_CONFIGURATION_CONTEXT = "quickListConfigCtx";
    public static final String SETTING_CONFIGURATION_RENDERER = "quickListRenderer";
    public static final String SETTING_ACTION_SLOT_COMPONENT_ID = "actionSlotComponentId";
    public static final String SOCKET_IN_ITEMS = "items";
    public static final String SOCKET_IN_SELECT_ITEM = "selectItem";
    public static final String SOCKET_OUT_OPEN_ITEM = "openItem";
    public static final String SOCKET_OUT_ITEMS_REMOVED = "itemsRemoved";
    public static final String SOCKET_OUT_ITEMS_LIST_UPDATED = "itemsListUpdated";
    /**
     * @deprecated since 19.05, use {@link #SOCKET_OUT_ITEMS_REMOVED} instead
     */
    @Deprecated(since = "19.05", forRemoval = true)
    public static final String SOCKET_OUT_ITEMS_REMOVED_BY_CLICK = "itemsRemovedByClick";
    public static final String MODEL_KEY_ITEMS = "items";
    public static final String MODEL_KEY_SOURCE_WIDGET_ID = "sourceWidgetId";
    public static final String MODEL_KEY_ELEMENTS_TYPE = "elements_type";
    public static final String ATTRIBUTE_ITEM_DATA = "itemData";
    public static final String ATTRIBUTE_TILE_MARKER = "tile";
    public static final String ITEM_COUNT_LABEL = "item.count.label";
    private static final String MODEL_KEY_REQUESTED_SELECTION = "requestedSelection";
    private static final String TILE_YTESTID = "quickListTile";
    private static final Logger LOG = LoggerFactory.getLogger(QuickListController.class);
    @Wire
    private Label infoLabel;
    @Wire
    private Div itemContainer;
    @Wire
    private Actions actionSlot;
    @WireVariable
    private transient NotifyingWidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object> notifyingWidgetComponentRendererFactory;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient CockpitTypeUtils backofficeTypeUtils;
    private transient WidgetComponentRenderer<HtmlBasedComponent, QuickList, Object> renderer;
    @WireVariable
    private transient PermissionFacade permissionFacade;


    protected void refreshActionSlot()
    {
        if(actionSlot != null)
        {
            final StringBuilder componentContext = new StringBuilder();
            final String actionSlotComponentId = getWidgetSettings().getString(SETTING_ACTION_SLOT_COMPONENT_ID);
            if(StringUtils.isNotBlank(actionSlotComponentId))
            {
                componentContext.append("component=").append(actionSlotComponentId);
            }
            final String typeCode = getValue(MODEL_KEY_ELEMENTS_TYPE, String.class);
            if(StringUtils.isNotBlank(typeCode))
            {
                if(StringUtils.isNotBlank(componentContext.toString()))
                {
                    componentContext.append(",");
                }
                componentContext.append("type=").append(typeCode);
            }
            if(StringUtils.isNotBlank(componentContext.toString()))
            {
                actionSlot.setConfig(componentContext.toString());
            }
            else
            {
                actionSlot.setConfig(null);
            }
            actionSlot.reload();
        }
    }


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        renderer = createItemRenderer();
        final ListModelList<Object> oldModel = findModel();
        final ListModelList<Object> newModel = createModel();
        storeModel(newModel);
        if(oldModel != null)
        {
            newModel.addAll(oldModel);
            newModel.setSelection(oldModel.getSelection());
        }
        if(getInfoLabel() != null)
        {
            getInfoLabel().setSclass(SCLASS_QUICKLIST_INFO_LABEL);
        }
        refreshActionSlot();
    }


    protected ListModelList<Object> createModel()
    {
        final ListModelList<Object> model = new ListModelList<>();
        model.addListDataListener(this::onModelChange);
        return model;
    }


    protected WidgetComponentRenderer<HtmlBasedComponent, QuickList, Object> createItemRenderer()
    {
        final String configuredRenderer = getWidgetInstanceManager().getWidgetSettings().getString(SETTING_CONFIGURATION_RENDERER);
        final NotifyingWidgetComponentRenderer<HtmlBasedComponent, QuickList, Object> notifyingWidgetComponentRenderer = getRendererFactory()
                        .createWidgetComponentRenderer(configuredRenderer);
        if(notifyingWidgetComponentRenderer instanceof QuickListItemRenderer)
        {
            final QuickListItemRenderer quickListItemRenderer = (QuickListItemRenderer)notifyingWidgetComponentRenderer;
            notifyingWidgetComponentRenderer.addRendererListener(renderEvent -> {
                final Component source = renderEvent.getSource();
                final Object data = renderEvent.getData();
                if(quickListItemRenderer.isOpenItemHyperlink(source))
                {
                    source.addEventListener(Events.ON_CLICK, event -> onItemTitleClick(event, data));
                }
                else if(quickListItemRenderer.isRemoveItemButton(source))
                {
                    source.addEventListener(Events.ON_CLICK, event -> onItemRemoveClick(event, data));
                }
            });
        }
        return notifyingWidgetComponentRenderer;
    }


    protected Component findTileInParents(final Component component)
    {
        if(component == null)
        {
            return null;
        }
        else if(component.hasAttribute(ATTRIBUTE_TILE_MARKER))
        {
            return component;
        }
        else
        {
            return findTileInParents(component.getParent());
        }
    }


    protected void onModelChange(final ListDataEvent event)
    {
        switch(event.getType())
        {
            case ListDataEvent.INTERVAL_ADDED:
                renderTiles(event.getIndex0(), event.getIndex1(), event.getModel(), true);
                break;
            case ListDataEvent.CONTENTS_CHANGED:
                renderTiles(event.getIndex0(), event.getIndex1(), event.getModel(), false);
                break;
            case ListDataEvent.INTERVAL_REMOVED:
                removeTiles(event.getIndex0(), event.getIndex1());
                break;
            case ListDataEvent.SELECTION_CHANGED:
                final ListModel listModel = event.getModel();
                if(listModel instanceof Selectable)
                {
                    selectTilesByData(((Selectable)listModel).getSelection());
                }
                break;
            default:
                break;
        }
        if(getInfoLabel() != null)
        {
            updateItemsCount(event.getModel().getSize());
        }
    }


    protected void selectTilesByData(final Collection data)
    {
        if(data != null)
        {
            final Div itemContainerDiv = getItemContainer();
            for(final HtmlBasedComponent tile : itemContainerDiv.<HtmlBasedComponent>getChildren())
            {
                markTileSelected(tile, data.contains(tile.getAttribute(ATTRIBUTE_ITEM_DATA)));
            }
        }
    }


    protected void markTileSelected(final HtmlBasedComponent tile, final boolean isSelected)
    {
        UITools.modifySClass(tile, SCLASS_YW_QUICK_LIST_TILE_SELECTED, isSelected);
    }


    protected void renderTiles(final int indexBegin, final int indexEnd, final ListModel model, final boolean createNew)
    {
        setValue(MODEL_KEY_ELEMENTS_TYPE, findElementsType(model));
        final Map<String, QuickList> quickListConfigurations = new HashMap<>();
        final Div itemContainerDiv = getItemContainer();
        final WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager();
        final DragAndDropStrategy dragAndDropStrategy = getDragAndDropStrategy();
        final Collection selected = model instanceof Selectable ? ((Selectable)model).getSelection() : Collections.emptyList();
        for(int i = indexBegin; i <= indexEnd; i++)
        {
            final Object data = model.getElementAt(i);
            final QuickList configuration = retrieveQuickListConfiguration(quickListConfigurations, getDataType(data).getCode());
            final DataType dataType = data == null ? null : getDataType(data);
            if(data != null)
            {
                final HtmlBasedComponent tile;
                if(createNew)
                {
                    tile = new Div();
                    tile.setSclass(SCLASS_YW_QUICK_LIST_TILE);
                    tile.setAttribute(ATTRIBUTE_ITEM_DATA, data);
                    tile.setAttribute(ATTRIBUTE_TILE_MARKER, null);
                    YTestTools.modifyYTestId(tile, TILE_YTESTID);
                    itemContainerDiv.appendChild(tile);
                    dragAndDropStrategy.makeDraggable(tile, data, new DefaultCockpitContext());
                    dragAndDropStrategy.makeDroppable(tile, data, new DefaultCockpitContext());
                }
                else
                {
                    tile = (HtmlBasedComponent)itemContainerDiv.getChildren().get(i);
                    tile.getChildren().clear();
                }
                final boolean isTileSelected = selected.contains(data);
                if(isTileSelected)
                {
                    markTileSelected(tile, true);
                }
                renderer.render(tile, configuration, data, dataType, widgetInstanceManager);
            }
        }
    }


    protected String findElementsType(final ListModel model)
    {
        if(model != null && model.getSize() > 0)
        {
            final List<Object> elements = new ArrayList<>(model.getSize());
            for(int i = 0; i < model.getSize(); i++)
            {
                elements.add(model.getElementAt(i));
            }
            return getCockpitTypeUtils().findClosestSuperType(elements);
        }
        return Object.class.getCanonicalName();
    }


    protected QuickList retrieveQuickListConfiguration(final Map<String, QuickList> quickListConfigurations, final String dataType)
    {
        return quickListConfigurations.computeIfAbsent(dataType, type -> loadConfiguration(dataType));
    }


    protected void removeTiles(final int indexBegin, final int indexEnd)
    {
        final Div itemContainerDiv = getItemContainer();
        for(int i = indexEnd; i >= indexBegin; i--)
        {
            if(itemContainerDiv.getChildren().size() > i)
            {
                itemContainerDiv.getChildren().remove(i);
            }
        }
    }


    protected void onItemRemoveClick(final Event clickEvent, final Object data)
    {
        final Component tile = findTileInParents(clickEvent.getTarget());
        final ListModelList<Object> items = findModel();
        final List<Object> itemsBeforeRemoval = new ArrayList<>(items.getInnerList());
        if(tile != null)
        {
            final int removedIndex = getItemContainer().getChildren().indexOf(tile);
            if(removedIndex >= 0)
            {
                items.remove(removedIndex);
            }
        }
        if(data != null)
        {
            sendOutput(SOCKET_OUT_ITEMS_REMOVED, Collections.singletonList(data));
            sendOutput(SOCKET_OUT_ITEMS_REMOVED_BY_CLICK, Collections.singletonList(data));
            final List<Object> itemsAfterRemoval = new ArrayList<>(items.getInnerList());
            if(!itemsBeforeRemoval.equals(itemsAfterRemoval))
            {
                final Pair<List, List> pair = Pair.of(itemsBeforeRemoval, itemsAfterRemoval);
                sendOutput(SOCKET_OUT_ITEMS_LIST_UPDATED, pair);
            }
            sendRemovedItemsViaVirtualSockets(data);
            if(items.getSelection().isEmpty() && !items.isEmpty())
            {
                items.setSelection(Collections.singleton(items.get(0)));
                notifyOpenItem();
            }
        }
    }


    @InextensibleMethod
    private void sendRemovedItemsViaVirtualSockets(final Object data)
    {
        try
        {
            final String sourceWidgetId = getValue(MODEL_KEY_SOURCE_WIDGET_ID, String.class);
            if(StringUtils.isNotBlank(sourceWidgetId))
            {
                final Widget widget = getWidgetslot().getWidgetInstance().getWidget();
                final String virtualSocketsId = SOCKET_OUT_ITEMS_REMOVED + "_" + sourceWidgetId;
                final Optional<WidgetSocket> socket = widget.getVirtualOutputs().stream()
                                .filter(out -> virtualSocketsId.equals(out.getId())).findFirst();
                if(socket.isPresent())
                {
                    sendOutput(virtualSocketsId, Collections.singletonList(data));
                }
            }
        }
        catch(final Exception e)
        {
            LOG.warn("Could not send virtual socket due to exception: ", e);
        }
    }


    protected void onItemTitleClick(final Event clickEvent, final Object data)
    {
        if(data != null)
        {
            findModel().clearSelection();
            findModel().setSelection(Collections.singleton(data));
            notifyOpenItem();
        }
    }


    protected void notifyOpenItem()
    {
        final Set<Object> selection = findModel().getSelection();
        if(!selection.isEmpty())
        {
            sendOutput(SOCKET_OUT_OPEN_ITEM, selection.iterator().next());
        }
    }


    @SocketEvent(socketId = SOCKET_IN_SELECT_ITEM)
    public void socketSelectItem(final Object item)
    {
        final ListModelList<Object> model = findModel();
        if(model == null)
        {
            LOG.warn("Could not set selection of [{}] on null model. Initialize the collection first.", item);
        }
        else if(model.contains(item))
        {
            getModel().remove(MODEL_KEY_REQUESTED_SELECTION);
            model.setSelection(Collections.singleton(item));
        }
        else
        {
            setValue(MODEL_KEY_REQUESTED_SELECTION, item);
            model.clearSelection();
        }
    }


    @SocketEvent(socketId = SOCKET_IN_ITEMS)
    public void socketIncomingItems(final Event event)
    {
        Validate.notNull("All arguments are mandatory", event);
        if(event instanceof com.hybris.cockpitng.events.SocketEvent)
        {
            setValue(MODEL_KEY_SOURCE_WIDGET_ID, ((com.hybris.cockpitng.events.SocketEvent)event).getSourceWidgetID());
        }
        else
        {
            LOG.warn("Expected an instance of type {} but received: {}",
                            com.hybris.cockpitng.events.SocketEvent.class.getCanonicalName(), event);
        }
        final List<Object> itemsBeforeReplacement = new ArrayList<>(findModel().getInnerList());
        replaceItems(ObjectUtils.defaultIfNull((List<Object>)event.getData(), Collections.emptyList()));
        final List<Object> itemsAfterReplacement = new ArrayList<>(findModel().getInnerList());
        if(!itemsBeforeReplacement.equals(itemsAfterReplacement))
        {
            sendOutput(SOCKET_OUT_ITEMS_LIST_UPDATED, Pair.of(itemsBeforeReplacement, itemsAfterReplacement));
        }
        final Object requestedSelection = getModel().getValue(MODEL_KEY_REQUESTED_SELECTION, Object.class);
        if(requestedSelection != null)
        {
            socketSelectItem(requestedSelection);
        }
    }


    protected void replaceItems(final List<Object> items)
    {
        final ListModelList<Object> listModel = findModel();
        final List<Object> accessibleItems = applyAccessRights(items);
        final Optional<Object> selectedItems = listModel.getSelection().stream().findFirst();
        listModel.clear();
        listModel.addAll(accessibleItems);
        refreshActionSlot();
        selectedItems.filter(items::contains).ifPresent(this::socketSelectItem);
    }


    protected List<Object> applyAccessRights(final List<Object> items)
    {
        return items.stream().filter(this::hasAccessRights).collect(Collectors.toList());
    }


    protected boolean hasAccessRights(final Object item)
    {
        final boolean canReadInstance = item != null && permissionFacade.canReadInstance(item);
        if(canReadInstance)
        {
            LOG.debug("Item cannot be displayed: No access");
        }
        return canReadInstance;
    }


    protected void updateItemsCount(final int count)
    {
        getInfoLabel().setValue(getLabel(ITEM_COUNT_LABEL, new Object[]
                        {Integer.valueOf(count)}));
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectUpdatedEvent(final CockpitEvent event)
    {
        final Collection<Object> elements = extractDataCollection(event);
        final ListModelList<Object> model = findModel();
        final List<Object> elementsBeforeReplacement = new ArrayList<>(model.getInnerList());
        final boolean replacementOccurred = elements.stream().map(element -> replaceModelElements(model, element))
                        .collect(Collectors.toSet()).contains(Boolean.TRUE);
        final List<Object> elementsAfterReplacement = new ArrayList<>(model.getInnerList());
        if(replacementOccurred)
        {
            sendOutput(SOCKET_OUT_ITEMS_LIST_UPDATED, Pair.of(elementsBeforeReplacement, elementsAfterReplacement));
        }
    }


    protected boolean replaceModelElements(final ListModelList<Object> model, final Object element)
    {
        boolean replacementOccurred = false;
        for(int i = 0; i < model.getSize(); i++)
        {
            if(model.get(i).equals(element))
            {
                model.set(i, element);
                replacementOccurred = true;
            }
        }
        replaceElementsInModel(model, element);
        return replacementOccurred;
    }


    /**
     * @deprecated since 19.05, please use {@link #replaceModelElements(ListModelList, Object)}
     */
    @Deprecated(since = "19.05", forRemoval = true)
    protected void replaceElementsInModel(final ListModelList<Object> model, final Object element)
    {
        /**
         * Left for compatibility reasons
         */
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectDeletedEvent(final CockpitEvent event)
    {
        final Collection<Object> elements = new ArrayList<>(extractDataCollection(event));
        final ListModelList<Object> model = findModel();
        elements.retainAll(model);
        final List<Object> elementsBeforeRemoval = new ArrayList<>(model.getInnerList());
        model.removeAll(elements);
        final List<Object> elementsAfterRemoval = new ArrayList<>(model.getInnerList());
        sendOutput(SOCKET_OUT_ITEMS_REMOVED, elements);
        if(!elementsBeforeRemoval.equals(elementsAfterRemoval))
        {
            sendOutput(SOCKET_OUT_ITEMS_LIST_UPDATED, Pair.of(elementsBeforeRemoval, elementsAfterRemoval));
        }
        if(model.getSelection().isEmpty() && !model.isEmpty())
        {
            model.setSelection(Collections.singleton(model.get(0)));
            notifyOpenItem();
        }
    }


    protected Collection<Object> extractDataCollection(final CockpitEvent event)
    {
        final Collection<Object> elements;
        final Object data = event.getData();
        if(data instanceof Collection)
        {
            elements = (Collection)data;
        }
        else
        {
            elements = Collections.singleton(data);
        }
        return elements;
    }


    protected DataType getDataType(final Object data)
    {
        try
        {
            return getTypeFacade().load(getTypeFacade().getType(data));
        }
        catch(final TypeNotFoundException ec)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("DataType for typeCode %s cannot be found.", getTypeFacade().getType(data)), ec);
            }
        }
        return null;
    }


    /**
     * @deprecated Use {@link #loadConfiguration(String)}. QuickList can render context of tile for each type individually.
     * @since 1905
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected QuickList loadConfiguration()
    {
        return loadConfiguration(getElementsType());
    }


    protected QuickList loadConfiguration(final String type)
    {
        final String componentConfContext = getWidgetSettings().getString(SETTING_CONFIGURATION_CONTEXT);
        QuickList quickListConfig = null;
        try
        {
            quickListConfig = getWidgetInstanceManager().loadConfiguration(new DefaultConfigContext(componentConfContext, type),
                            QuickList.class);
            if(quickListConfig == null)
            {
                LOG.warn("Cannot retrieve refine by widget configuration");
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Cannot retrieve quick list widget configuration", e);
        }
        return quickListConfig;
    }


    protected String getElementsType()
    {
        final String type = getValue(MODEL_KEY_ELEMENTS_TYPE, String.class);
        if(StringUtils.isBlank(type))
        {
            LOG.warn("Type of collection elements not set, defaults to: {}", Object.class.getCanonicalName());
            return Object.class.getCanonicalName();
        }
        return type;
    }


    protected ListModelList<Object> findModel()
    {
        return getWidgetInstanceManager().getModel().getValue(MODEL_KEY_ITEMS, ListModelList.class);
    }


    protected void storeModel(final ListModelList<Object> items)
    {
        getWidgetInstanceManager().getModel().setValue(MODEL_KEY_ITEMS, items);
    }


    protected Div getItemContainer()
    {
        return itemContainer;
    }


    protected void setItemContainer(final Div itemContainer)
    {
        this.itemContainer = itemContainer;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    protected CockpitTypeUtils getCockpitTypeUtils()
    {
        return backofficeTypeUtils;
    }


    protected NotifyingWidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object> getRendererFactory()
    {
        return (NotifyingWidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object>)getWidgetComponentRendererFactory();
    }


    protected void setRendererFactory(
                    final NotifyingWidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object> notifyingWidgetComponentRenderer)
    {
        setWidgetComponentRendererFactory(notifyingWidgetComponentRenderer);
    }


    /**
     * @deprecated since 6.5
     * @see #getRendererFactory()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected WidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object> getWidgetComponentRendererFactory()
    {
        return notifyingWidgetComponentRendererFactory;
    }


    /**
     * @deprecated since 6.5
     * @see #setRendererFactory(NotifyingWidgetComponentRendererFactory)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void setWidgetComponentRendererFactory(
                    final WidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object> widgetComponentRendererFactory)
    {
        if(widgetComponentRendererFactory instanceof NotifyingWidgetComponentRendererFactory)
        {
            this.notifyingWidgetComponentRendererFactory = (NotifyingWidgetComponentRendererFactory<HtmlBasedComponent, QuickList, Object>)widgetComponentRendererFactory;
        }
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    protected Label getInfoLabel()
    {
        return infoLabel;
    }


    protected void setInfoLabel(final Label infoLabel)
    {
        this.infoLabel = infoLabel;
    }


    protected Actions getActionSlot()
    {
        return actionSlot;
    }


    protected void setActionSlot(final Actions actionSlot)
    {
        this.actionSlot = actionSlot;
    }
}
