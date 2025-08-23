/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import org.zkoss.zul.Listitem;

/**
 * Handles Drag and Drop in Reference Editor
 *
 * @param <T>
 */
public interface ReferenceEditorDndHandler<T>
{
    /**
     * Enables Drag and Drop for this list item
     *
     * @param item
     *           item to enable Drag and Drop for
     * @param referenceEditorLayout
     *           {@link ReferenceEditorLayout} of the editor that the item is contained in
     */
    void enableDragAndDrop(Listitem item, ReferenceEditorLayout<T> referenceEditorLayout);
}
