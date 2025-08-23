/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.SelectAndFocusDelegateController;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.event.PagingEvent;

/**
 * Default delegate for logic of the selection and focus for Collection Browser widget.
 */
public class DefaultSelectAndFocusDelegateController implements SelectAndFocusDelegateController
{
    protected static final String MODEL_FOCUSED_OBJECT = "focusedObjects";
    protected static final String MODEL_SELECTED_OBJECTS = "selectedObjects";
    protected static final String SOCKET_OUT_SELECTED_ITEM = "selectedItem";
    protected static final String SOCKET_OUT_SELECTED_ITEMS = "selectedItems";
    protected static final String SOCKET_OUT_FOCUSED_ITEM = "focusedItem";
    private CollectionBrowserController controller;


    @Override
    public void initialize()
    {
        initializeSelectedObjectsInModel();
    }


    protected void initializeSelectedObjectsInModel()
    {
        final CollectionBrowserController collectionBrowserController = getController();
        if(collectionBrowserController.getValue(MODEL_SELECTED_OBJECTS, Set.class) == null)
        {
            collectionBrowserController.setValue(MODEL_SELECTED_OBJECTS, new HashSet<>());
        }
        collectionBrowserController.getModel().addObserver(MODEL_FOCUSED_OBJECT, () -> sendNavigationItemSelectorContext(
                        collectionBrowserController.getActiveMold().getNavigationItemSelectorContext()));
    }


    protected void sendNavigationItemSelectorContext(final NavigationItemSelectorContext navigationContext)
    {
        getController().sendOutput(CollectionBrowserController.SOCKET_PREVIOUS_ITEM_SELECTOR_CONTEXT, navigationContext);
        getController().sendOutput(CollectionBrowserController.SOCKET_NEXT_ITEM_SELECTOR_CONTEXT, navigationContext);
    }


    @Override
    public <E> Collection<E> getSelectedItems()
    {
        final WidgetModel widgetModel = getController().getWidgetInstanceManager().getModel();
        final Optional<Collection> selected = Optional.ofNullable(widgetModel.getValue(MODEL_SELECTED_OBJECTS, Collection.class));
        Collection<E> result = selected.orElse(Collections.emptyList());
        if(!getController().isMultiSelectEnabled() && CollectionUtils.size(result) > 1)
        {
            result = Collections.singleton((E)SelectAndFocusDelegateController.getSelectedItem(result));
        }
        return result;
    }


    @Override
    public <E> E getFocusedItem()
    {
        return (E)getController().getValue(MODEL_FOCUSED_OBJECT, Object.class);
    }


    @Override
    public void selectItems(final Collection<?> selectedItems)
    {
        selectItemsInternal(selectedItems);
        if(!getController().isMultiSelectEnabled())
        {
            final Object selectedItem = SelectAndFocusDelegateController.getSelectedItem(selectedItems);
            focusItem(selectedItem);
        }
    }


    @Override
    public void selectItem(final Object itemToSelect)
    {
        final CollectionBrowserMoldStrategy activeMold = getController().getActiveMold();
        activeMold.selectItems(Sets.newHashSet(itemToSelect));
        sendNavigationItemSelectorContext(activeMold.getNavigationItemSelectorContext());
    }


    protected void selectItemsInternal(final Collection<?> itemsToSelect)
    {
        final Object selectedItem = SelectAndFocusDelegateController.getSelectedItem(itemsToSelect);
        final Set<Object> selectedSet = new LinkedHashSet<>();
        final CollectionBrowserController collectionBrowserController = getController();
        if(collectionBrowserController.isMultiSelectEnabled())
        {
            selectedSet.addAll(itemsToSelect);
        }
        else if(CollectionUtils.isNotEmpty(itemsToSelect))
        {
            selectedSet.add(selectedItem);
        }
        collectionBrowserController.setValue(MODEL_SELECTED_OBJECTS, selectedSet);
        collectionBrowserController.handleSelectionChanged(selectedSet);
        if(collectionBrowserController.isSendItemsOnSelectEnabled())
        {
            if(!collectionBrowserController.areHyperlinksEnabled())
            {
                sendItemSelectedNotification(selectedItem);
            }
            sendItemsSelectedNotification(new ArrayList<>(selectedSet));
        }
        collectionBrowserController.getActiveMold().selectItems(selectedSet);
    }


    protected void sendItemSelectedNotification(final Object item)
    {
        getController().sendOutput(SOCKET_OUT_SELECTED_ITEM, item);
    }


    protected void sendItemsSelectedNotification(final Collection<Object> items)
    {
        getController().sendOutput(SOCKET_OUT_SELECTED_ITEMS, items);
    }


    @Override
    public void deselectItems(final Collection<Object> itemsToDeselect)
    {
        final Collection<Object> contextSelection = getController().getSelectedItems();
        if(CollectionUtils.isNotEmpty(contextSelection) && CollectionUtils.isNotEmpty(itemsToDeselect))
        {
            final List<Object> selectedItems = new ArrayList<>(contextSelection);
            final boolean anyRemoved = selectedItems.removeAll(itemsToDeselect);
            if(anyRemoved)
            {
                getController().setSelectedItems(selectedItems);
                getController().getModel().changed(MODEL_SELECTED_OBJECTS);
            }
        }
    }


    @Override
    public void deselectAllItems()
    {
        focusItem(null);
        getController().setSelectedItems(Collections.emptyList());
    }


    @Override
    public void focusItem(final Object item)
    {
        final CollectionBrowserController collectionBrowserController = getController();
        sendItemFocusedNotification(item);
        final Object lastFocus = collectionBrowserController.getFocusedItem();
        collectionBrowserController.setValue(MODEL_FOCUSED_OBJECT, item);
        collectionBrowserController.getActiveMold().focusItem(lastFocus, item);
        sendNavigationItemSelectorContext(collectionBrowserController.getActiveMold().getNavigationItemSelectorContext());
    }


    protected void sendItemFocusedNotification(final Object item)
    {
        getController().sendOutput(SOCKET_OUT_FOCUSED_ITEM, item);
    }


    @Override
    public void handleItemClicked(final Object item)
    {
        focusItem(item);
        if(!getController().areHyperlinksEnabled() && !getController().isMultiSelectEnabled())
        {
            selectItemsInternal(Collections.singleton(item));
        }
    }


    @Override
    public void handleHyperlinkClicked(final Object item)
    {
        if(getController().areHyperlinksEnabled())
        {
            sendItemSelectedNotification(item);
        }
    }


    @Override
    public void handleObjectCreated(final CockpitEvent event)
    {
        //At this moment, there is no action necessary when an object is created.
    }


    @Override
    public void handleObjectUpdated(final CockpitEvent event)
    {
        final Collection collection = event.getDataAsCollection();
        handleSelectionRefresh(collection);
    }


    protected void handleSelectionRefresh(final Collection<?> updatedObjects)
    {
        final List<Object> oldObjects = new LinkedList<>();
        final Collection<Object> selectedItems = new ArrayList<>(getController().getSelectedItems());
        final List<Object> updatedSelectedObjects = updatedObjects.stream().filter(updatedObject -> {
            final List<Object> oldSelectedObjects = selectedItems.stream()
                            .filter(oldObject -> oldObject.equals(updatedObject))
                            .collect(Collectors.toList());
            oldObjects.addAll(oldSelectedObjects);
            return !oldSelectedObjects.isEmpty();
        }).collect(Collectors.toList());
        if(!oldObjects.isEmpty())
        {
            selectedItems.removeAll(oldObjects);
            selectedItems.addAll(updatedSelectedObjects);
            getController().setSelectedItems(selectedItems);
            getController().getModel().changed(CollectionBrowserController.MODEL_SELECTED_OBJECTS);
        }
        getController().handleSelectionRefresh(updatedSelectedObjects);
    }


    @Override
    public void handleObjectDeleted(final CockpitEvent event)
    {
        final Collection<Object> selectedItems = new ArrayList<>(getSelectedItems());
        final Collection<Object> deletedItems = new ArrayList<>(event.getDataAsCollection());
        final Object focusedItem = getFocusedItem();
        selectedItems.removeAll(deletedItems);
        getController().setSelectedItems(selectedItems);
        if(deletedItems.contains(focusedItem))
        {
            focusItem(null);
        }
        else
        {
            focusItem(focusedItem);
        }
    }


    @Override
    public void handleItemLockedStateChanged(final CockpitEvent event)
    {
        final Object data = event.getData();
        final CollectionBrowserController collectionBrowserController = getController();
        final Collection<Object> selectedItems = getController().getSelectedItems();
        if(collectionBrowserController.isDataAmongSelectedItems(data, selectedItems))
        {
            collectionBrowserController.getModel().changed(MODEL_SELECTED_OBJECTS);
        }
    }


    @Override
    public void handleTypeChange()
    {
        selectItemsInternal(Collections.emptySet());
    }


    @Override
    public void handlePaging(final PagingEvent event)
    {
        sendNavigationItemSelectorContext(
                        new NavigationItemSelectorContext(getController().getPagingDelegateController().getPageableCurrentPageSize(), -1));
    }


    @Override
    public void handleNewPageable()
    {
        sendNavigationItemSelectorContext(getController().getActiveMold().getNavigationItemSelectorContext());
    }


    @Override
    public void handleMoldChange()
    {
        handleNewPageable();
    }


    @Override
    public void handleNewSelectionContext(final Map<String, Object> inputContext)
    {
        final Map<String, Object> context = new HashMap<>();
        if(inputContext != null)
        {
            context.putAll(inputContext);
        }
        getController().setValue(CollectionBrowserController.MODEL_SELECTION_CONTEXT, context);
    }


    protected boolean isDataAmongSelectedItems(final Object data, final Collection<Object> selectedItems)
    {
        return selectedItems != null && CollectionUtils.containsAny(selectedItems,
                        data instanceof Collection ? (Collection)data : Collections.singletonList(data));
    }


    @Override
    public void resetModel()
    {
        final CollectionBrowserController collectionBrowserController = getController();
        collectionBrowserController.setValue(MODEL_SELECTED_OBJECTS, null);
        collectionBrowserController.setValue(MODEL_FOCUSED_OBJECT, null);
    }


    @Override
    public void handlePageLoaded(final Pageable newPageable, final Pageable oldPageable)
    {
        final Object focusedItem = getController().getFocusedItem();
        if(newPageable != null && oldPageable != null && StringUtils.equals(newPageable.getQueryId(), oldPageable.getQueryId()))
        {
            focusItem(focusedItem);
        }
        else
        {
            deselectAllItems();
            focusItem(null);
        }
    }


    protected CollectionBrowserController getController()
    {
        return controller;
    }


    @Override
    public void setController(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }
}
