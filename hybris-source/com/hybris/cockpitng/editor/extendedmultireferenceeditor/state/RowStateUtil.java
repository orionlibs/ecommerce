/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.state;

import com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer.CellContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public final class RowStateUtil
{
    public static final String EXTENDED_MULTI_REFERENCE_EDITORS_STATES = "extendedMultiReferenceEditorsStates";
    public static final String MODIFIED_CELL_CLASS = "ye-cell-modified";


    private RowStateUtil()
    {
        //NOOP
    }


    public static EditorState getExtendedMultiReferenceEditorState(final WidgetInstanceManager widgetInstanceManager,
                    final String property)
    {
        final Map<String, EditorState> propertyToEditorStateMap = getPropertyToEditorStateMap(widgetInstanceManager);
        EditorState editorState = propertyToEditorStateMap.get(property);
        if(editorState == null)
        {
            editorState = new EditorState(property);
            propertyToEditorStateMap.put(property, editorState);
        }
        return editorState;
    }


    public static void resetEditorState(final WidgetInstanceManager wim, final String property)
    {
        final Map<String, EditorState> propertyToEditorStateMap = getPropertyToEditorStateMap(wim);
        final EditorState editorState = propertyToEditorStateMap.get(property);
        if(editorState != null)
        {
            editorState.removeAllRowStates();
            propertyToEditorStateMap.remove(property);
        }
    }


    private static Map<String, EditorState> getPropertyToEditorStateMap(final WidgetInstanceManager widgetInstanceManager)
    {
        Map<String, EditorState> propertyToEditorStateMap = widgetInstanceManager.getModel()
                        .getValue(EXTENDED_MULTI_REFERENCE_EDITORS_STATES, Map.class);
        if(propertyToEditorStateMap == null)
        {
            propertyToEditorStateMap = new HashMap<>();
            widgetInstanceManager.getModel().setValue(EXTENDED_MULTI_REFERENCE_EDITORS_STATES, propertyToEditorStateMap);
        }
        return propertyToEditorStateMap;
    }


    /**
     * Converts a index in current view model into referring index in {@link EditorState}.
     *
     * @param listModel
     *           current view model
     * @param editorState
     *           editor state
     * @param viewIndex
     *           index in current view model
     * @return index in editor state
     */
    public static <T> int convertViewIndexToModel(final ListModel<T> listModel, final EditorState<T> editorState,
                    final int viewIndex)
    {
        final T row = listModel.getElementAt(viewIndex);
        final RowState<T> rowState = editorState.getRowState(row);
        return rowState != null ? rowState.getRowIndex() : -1;
    }


    /**
     * Converts a index in {@link EditorState} into referring index in current view model.
     *
     * @param listModel
     *           current view model
     * @param editorState
     *           editor state
     * @param modelIndex
     *           index in editor state
     * @return index in current view model
     */
    public static <T> int convertModelIndexToView(final ListModel<T> listModel, final EditorState<T> editorState,
                    final int modelIndex)
    {
        final T row = editorState.getRow(modelIndex);
        if(listModel instanceof ListModelList)
        {
            return ((ListModelList<T>)listModel).indexOf(row);
        }
        else
        {
            for(int idx = 0; idx < listModel.getSize(); idx++)
            {
                if(Objects.equals(row, listModel.getElementAt(idx)))
                {
                    return idx;
                }
            }
            return -1;
        }
    }


    /** Returns if model for specific identifier changed. */
    public static boolean isModelChanged(final WidgetInstanceManager widgetInstanceManager, final String inlineProperty)
    {
        final EditorState<?> extendedMultiReferenceEditorState = getExtendedMultiReferenceEditorState(widgetInstanceManager,
                        inlineProperty);
        return extendedMultiReferenceEditorState.getRowStates().stream().anyMatch(RowState::isRowModified);
    }


    /** Returns if model for specific identifier changed. */
    public static boolean isModelChanged(final WidgetInstanceManager widgetInstanceManager)
    {
        final Map<String, EditorState> propertyToEditorStateMap = getPropertyToEditorStateMap(widgetInstanceManager);
        for(final EditorState<?> editorState : propertyToEditorStateMap.values())
        {
            if(editorState.getRowStates().stream().anyMatch(RowState::isRowModified))
            {
                return true;
            }
        }
        return false;
    }


    /** Returns row state from cell context. */
    public static RowState getRowState(final CellContext cellContext)
    {
        final WidgetInstanceManager wim = cellContext.getWidgetInstanceManager();
        final String parentEditorProperty = cellContext.getParentEditorProperty();
        final Object rowEntry = cellContext.getRowEntry();
        return getExtendedMultiReferenceEditorState(wim, parentEditorProperty).getRowState(rowEntry);
    }
}
