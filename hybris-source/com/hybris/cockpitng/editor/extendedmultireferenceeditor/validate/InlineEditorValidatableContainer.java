/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate;

import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.zkoss.zk.ui.Component;

/**
 * Controls validation handling for entire inline reference editor
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor} Note: This is
 * used in aforementioned editor only when the inline editing activated.
 */
public class InlineEditorValidatableContainer<T> extends AbstractInlineEditorValidatableContainer<T>
{
    private final ObjectValuePath rootPath;


    public InlineEditorValidatableContainer(final Component container, final EditorState<T> editorState, final String rootPath)
    {
        super(container, editorState);
        this.rootPath = ObjectValuePath.parse(rootPath);
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return new InlineEditorFocusTransfer(getEditorState());
    }


    @Override
    protected ValidationResult getRootValidationResult()
    {
        return getEditorState().collectValidationResult();
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return rootPath.startsWith(path);
    }


    @Override
    public T getCurrentObject(final String path)
    {
        final int rowIndex = EditorState.getRowIndex(path);
        if(rowIndex > -1)
        {
            return getEditorState().getRow(rowIndex);
        }
        else
        {
            return null;
        }
    }


    @Override
    public void addValidationObserver(final ValueObserver observer)
    {
        getEditorState().getValidationObservable().addObserver(observer);
    }


    @Override
    public void addValidationObserver(final String key, final ValueObserver observer)
    {
        getEditorState().getValidationObservable().addObserver(key, observer);
    }


    @Override
    public void removeValidationObserver(final ValueObserver observer)
    {
        getEditorState().getValidationObservable().removeObserver(observer);
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        return !this.preventBroadcastValidationChange && (getValidationResultPopup().isVisible() || getRoot().startsWith(path));
    }


    @Override
    public boolean reactOnValidationChange()
    {
        return false;
    }


    protected ObjectValuePath getRoot()
    {
        return ObjectValuePath.unmodifiable(rootPath);
    }
}