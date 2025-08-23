/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import java.util.Collection;

/**
 * Common interface for {@link DefaultReferenceEditor} and {@link DefaultMultiReferenceEditor}, used by
 * {@link ReferenceEditorLayout}
 */
public interface ReferenceEditorLogic<T>
{
    /**
     * @return pageable collection
     */
    Pageable<T> getPageable();


    /**
     *
     * @param obj
     * @return Label for given object
     */
    String getStringRepresentationOfObject(final T obj);


    /**
     *
     * @return whether editor is in editable or readonly state
     */
    boolean isEditable();


    /**
     * Inform editor that user select new object
     *
     * @param obj
     */
    void addSelectedObject(final T obj);


    boolean isDisableDisplayingDetails();


    default boolean isDisableRemoveReference()
    {
        return false;
    }


    /**
     * Creates new object within the context.
     */
    void createNewReference();


    /**
     * Creates new object within the context.
     */
    void createNewReference(String typeCode);


    /**
     * Inform editor that user remove object from selected items list
     *
     * @param obj
     */
    void removeSelectedObject(final T obj);


    /**
     * Perform search for all elements and refresh paging component
     */
    void updateReferencesListBoxModel();


    /**
     * Perform search with given textQuery and refresh paging component
     *
     * @param textQuery
     */
    void updateReferencesListBoxModel(final String textQuery);


    /**
     * Determines if nested object creation controlls should be rendered.
     *
     * @return
     */
    boolean allowNestedObjectCreation();


    /**
     * Triggers selected reference socktet output to outside widgets
     *
     * @param selectedReference
     *           reference currently selected
     */
    void triggerReferenceSelected(final Object selectedReference);


    /**
     * Informs editor to update objects in model
     *
     * @param objects
     */
    void refreshObjects(final Collection<T> objects);


    /**
     * @return code of type which instance would be created
     */
    String getTypeCode();


    /**
     * Determines if choosing existing item is possible.
     *
     * @return
     */
    boolean isOnlyCreateMode();


    /**
     * Forwards event to editor listener.
     *
     * @param eventCode
     *           event code.
     */
    default void forwardEditorEvent(final String eventCode)
    {
    }


    /**
     * Sends a socked event on {@link AbstractReferenceEditor#SOCKET_OUT_REFERENCE_SEARCH_CTX}
     */
    default void openReferenceAdvancedSearch(final Collection<T> currentlySelected)
    {
    }


    /**
     * Tells if search button should open advanced reference search in a popup.
     */
    default boolean isReferenceAdvancedSearchEnabled()
    {
        return false;
    }


    /**
     * Sets focus on parent editor
     */
    default void preserveFocus()
    {
    }
}
