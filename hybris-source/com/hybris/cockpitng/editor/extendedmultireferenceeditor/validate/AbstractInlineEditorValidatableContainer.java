/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate;

import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.impl.ObserverProxy;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

/**
 * An abstract validation container that's dedicated for
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor}.
 * <P>
 * Note: Validation feature inside aforementioned editor is enabled only when inline editing is activated.
 */
public abstract class AbstractInlineEditorValidatableContainer<T> implements ValidatableContainer
{
    protected boolean preventBroadcastValidationChange;
    private final Component container;
    private final EditorState<T> editorState;
    private ValidationFocusTransferHandler focusTransfer;
    private Window validationResultPopup;


    public AbstractInlineEditorValidatableContainer(final Component container, final EditorState<T> editorState)
    {
        this.container = container;
        this.editorState = editorState;
    }


    protected abstract ValidationFocusTransferHandler createFocusTransferHandler();


    @Override
    public String getCurrentObjectPath(final String path)
    {
        return EditorState.getRowPath(path);
    }


    @Override
    public ValidationFocusTransferHandler getFocusTransfer()
    {
        if(focusTransfer == null)
        {
            focusTransfer = createFocusTransferHandler();
        }
        return focusTransfer;
    }


    protected abstract ValidationResult getRootValidationResult();


    @Override
    public ValidationResult getCurrentValidationResult(final String path)
    {
        ValidationResult result = getRootValidationResult();
        if(!isRootPath(path))
        {
            result = result.find(path).wrap();
            result.addObserver(ObserverProxy.createWeakProxy(getEditorState().getValidationObservable(), ObjectValuePath.parse(path)));
        }
        return result;
    }


    @Override
    public void setPreventBroadcastValidationChange(final boolean preventBroadcastValidationChange)
    {
        this.preventBroadcastValidationChange = preventBroadcastValidationChange;
    }


    @Override
    public Component getContainer()
    {
        return container;
    }


    public EditorState<T> getEditorState()
    {
        return editorState;
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        return true;
    }


    public Window getValidationResultPopup()
    {
        return validationResultPopup;
    }


    public void setValidationResultPopup(final Window validationResultPopup)
    {
        this.validationResultPopup = validationResultPopup;
    }
}