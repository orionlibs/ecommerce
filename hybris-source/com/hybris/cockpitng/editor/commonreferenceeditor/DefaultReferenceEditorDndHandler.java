/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import java.util.Optional;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * Default implementation of {@link ReferenceEditorDndHandler}
 *
 * @param <T>
 *           Type of the item inside Reference Editor
 */
public class DefaultReferenceEditorDndHandler<T> implements ReferenceEditorDndHandler<T>
{
    private static final String ON_START_DRAG = "onStartDrag";
    private static final String RENDER_ON_DEMAND_AND_DND_ENABLED = "_rodDndEnabled";
    private static final String CUSTOM_DRAGGED_ITEM_INDEX_DATA = "sap.custom.renderOnDemandDragData";


    @Override
    public void enableDragAndDrop(final Listitem item, final ReferenceEditorLayout<T> referenceEditorLayout)
    {
        final Listbox currentlySelectedList = item.getListbox();
        final ReferenceEditorLogic<T> referenceEditor = referenceEditorLayout.getReferenceEditor();
        final ListModelList<T> selectedElementsListModel = referenceEditorLayout.getSelectedElementsListModel();
        if(referenceEditorLayout.isOrdered() && referenceEditor.isEditable())
        {
            currentlySelectedList.setWidgetOverride(RENDER_ON_DEMAND_AND_DND_ENABLED, Boolean.TRUE.toString());
            item.setDraggable(currentlySelectedList.toString());
            item.setDroppable(currentlySelectedList.toString());
            item.addEventListener(ON_START_DRAG, event -> {
                final Listitem draggedItem = (Listitem)event.getTarget();
                storeDraggedItemIndex(draggedItem);
            });
            item.addEventListener(Events.ON_DROP, (final DropEvent event) -> {
                final Optional<Integer> optionalDraggedItemIndex = popDraggedItemIndex();
                if(optionalDraggedItemIndex.isPresent())
                {
                    final Integer draggedItemIndex = optionalDraggedItemIndex.get();
                    final Listitem droppedItem = (Listitem)event.getTarget();
                    dragAndDropItems(selectedElementsListModel, draggedItemIndex, droppedItem.getIndex());
                    currentlySelectedList.setModel(selectedElementsListModel);
                    referenceEditor.refreshObjects(selectedElementsListModel.getInnerList());
                }
            });
        }
    }


    private void storeDraggedItemIndex(final Listitem draggedItem)
    {
        Executions.getCurrent().getDesktop().setAttribute(CUSTOM_DRAGGED_ITEM_INDEX_DATA, draggedItem.getIndex());
    }


    private Optional<Integer> popDraggedItemIndex()
    {
        return Optional.ofNullable(Executions.getCurrent().getDesktop().removeAttribute(CUSTOM_DRAGGED_ITEM_INDEX_DATA))
                        .filter(Integer.class::isInstance).map(Integer.class::cast);
    }


    private void dragAndDropItems(final ListModelList<T> selectedElementsListModel, final int draggedIndex, final int droppedIndex)
    {
        selectedElementsListModel.add(droppedIndex, selectedElementsListModel.remove(draggedIndex));
    }
}
