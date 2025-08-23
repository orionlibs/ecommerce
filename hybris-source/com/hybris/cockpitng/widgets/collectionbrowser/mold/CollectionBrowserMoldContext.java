/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import java.util.Collection;

/**
 * A set of methods that allows {@link CollectionBrowserMoldStrategy} to communicate with
 * {@link com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController}
 */
public interface CollectionBrowserMoldContext
{
    /**
     * Gets all currently selected items
     *
     * @param <E>
     *           type of expected items
     * @return collection of all selected items
     */
    <E> Collection<E> getSelectedItems();


    /**
     * Gets currently focused item or <code>null</code> if no item is focused
     *
     * @param <E>
     *           type of expected item
     * @return focused item
     */
    <E> E getFocusedItem();


    /**
     * <P>
     * Notifies that multiple items have been selected.
     * </P>
     * <P>
     * Method should be called when selection is changed passing a full collection of selected items (even if a single
     * element is added to selection, whole selection should be passed).
     * </P>
     *
     * @param items
     *           all items that are selected
     */
    void notifyItemsSelected(final Collection<?> items);


    /**
     * <P>
     * Informs if collection browser supports hyperlinks.
     * </P>
     * <P>
     * Values in collection browser may be rendered in the way, that some
     * </P>
     *
     * @return
     */
    boolean areHyperlinksSupported();


    /**
     * <P>
     * Notifies that an user has clicked on element hyperlink.
     * </P>
     * <P>
     * Call will be ignored, if hyperlinks are not supported
     * </P>
     *
     * @param value
     *           hyperlink's value
     * @see #areHyperlinksSupported()
     */
    void notifyHyperlinkClicked(final Object value);


    /**
     * <P>
     * Notifies that an user has clicked on element, but did not select it.
     * </P>
     *
     * @param item
     *           item that is represented by clicked element
     * @see #notifyItemsSelected(Collection)
     */
    void notifyItemClicked(final Object item);


    /**
     * Returns the widget instance manager of current controller.
     *
     * @return widget instance manager
     */
    WidgetInstanceManager getWidgetInstanceManager();


    /**
     * Checks whether provided attribute is sortable
     *
     * @param attributeQualifier
     *           attributes qualifier
     * @return <code>true</code> if attribute is sortable
     */
    boolean isSortable(final String attributeQualifier);


    /**
     * @return currently loaded data page
     */
    SinglePage getCurrentPage();


    /**
     * Reads data sorted with provided parameters
     *
     * @param sortData
     *           sorting parameters
     */
    void sort(final SortData sortData);


    /**
     * Gets data type that is currently being displayed
     *
     * @return currently selected data type or <code>null</code> if selected type code does not match any of types
     * @see #getCurrentTypeCode()
     */
    DataType getCurrentType();


    /**
     * Gets data type code that is currently being displayed
     *
     * @return currently selected data type code
     * @see #getCurrentType()
     */
    String getCurrentTypeCode();


    /**
     * Allows to determine if the collection browser allows for multi-selection
     *
     * @return true if the collection browser allows for multi-selection
     */
    boolean isMultiSelectEnabled();


    DragAndDropStrategy getDragAndDropStrategy();


    boolean isDragEnabled();


    boolean isDropEnabled();
}
