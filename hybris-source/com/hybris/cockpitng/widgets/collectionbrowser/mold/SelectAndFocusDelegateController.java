/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zul.event.PagingEvent;

/**
 * Contains the logic of the selection and focus functionality. Moreover it contains methods(which logic is related to
 * selection and focus) which should be called in case of some events in Collection Browser.
 */
public interface SelectAndFocusDelegateController extends CollectionBrowserDelegateController
{
    /**
     * Gets a single selection from multiple selected items.
     *
     * @param selection
     *           selected items
     * @return item that should be treated as single selection
     */
    static Object getSelectedItem(final Collection<?> selection)
    {
        if(CollectionUtils.isEmpty(selection))
        {
            return null;
        }
        final LinkedList<Object> linkedList = new LinkedList<>(selection);
        final Iterator<Object> descendingIterator = linkedList.descendingIterator();
        return descendingIterator.hasNext() ? descendingIterator.next() : null;
    }


    /**
     * Initializes delegate controller.
     */
    void initialize();


    /**
     * Gets currently selected items.
     *
     * @param <E>
     *           expected type of selected items
     * @return selected items
     * @see #selectItems(Collection)
     */
    <E> Collection<E> getSelectedItems();


    /**
     * Gets focused item.
     *
     * @param <E>
     *           expected type of the focused item
     * @return focused item
     */
    <E> E getFocusedItem();


    /**
     * Selects multiple items.
     *
     * @param selectedItems
     *           items that should be selected
     */
    void selectItems(Collection<?> selectedItems);


    /**
     * Selects an item.
     *
     * @param itemToSelect
     *           item that should be selected
     */
    void selectItem(Object itemToSelect);


    /**
     * Deselects multiple items.
     *
     * @param itemsToDeselect
     *           collection of the items to deselect
     */
    void deselectItems(Collection<Object> itemsToDeselect);


    /**
     * Deselects all items.
     */
    void deselectAllItems();


    /**
     * Focuses an item.
     *
     * @param item
     *           item to focus
     */
    void focusItem(Object item);


    /**
     * Handles item clicked event.
     *
     * @param item
     *           item that was clicked
     */
    void handleItemClicked(Object item);


    /**
     * Handles hyperlink clicked event.
     *
     * @param item
     *           that this hyperlink is assigned to
     */
    void handleHyperlinkClicked(Object item);


    /**
     * Handles object created event.
     *
     * @param event
     *           object created event
     */
    void handleObjectCreated(CockpitEvent event);


    /**
     * Handles object updated event.
     *
     * @param event
     *           object updated event
     */
    void handleObjectUpdated(CockpitEvent event);


    /**
     * Handles object deleted event.
     *
     * @param event
     *           object deleted event
     */
    void handleObjectDeleted(CockpitEvent event);


    /**
     * Handles item locked state changed event.
     *
     * @param event
     *           item locked state changed event
     */
    void handleItemLockedStateChanged(CockpitEvent event);


    /**
     * Handles paging event.
     *
     * @param event
     *           paging event
     */
    void handlePaging(PagingEvent event);


    /**
     * Handles item type change.
     */
    void handleTypeChange();


    /**
     * Handles change of the pageable.
     */
    void handleNewPageable();


    /**
     * Handles change of the mold.
     */
    void handleMoldChange();


    /**
     * Handles new selection context.
     *
     * @param inputContext
     *           new context
     */
    void handleNewSelectionContext(Map<String, Object> inputContext);


    /**
     * Resets part of the model connected with select and focus.
     */
    void resetModel();


    /**
     * Handles loading new page.
     *
     * @param newPageable
     *           new pageable
     * @param oldPageable
     *           current pageable
     */
    void handlePageLoaded(Pageable newPageable, Pageable oldPageable);
}
