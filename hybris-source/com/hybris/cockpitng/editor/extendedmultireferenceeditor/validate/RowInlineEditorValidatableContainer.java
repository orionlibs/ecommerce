/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate;

import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

/**
 * Controls validation handling in single row for inline reference editor
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor} Note: This is
 * used in aforementioned editor only when the inline editing activated.
 */
public class RowInlineEditorValidatableContainer<T> extends AbstractInlineEditorValidatableContainer<T>
{
    private static final int ON_CLOSE_PRIORITY = 2 << 6;
    private final Map<ValueObserver, ValueObserverProxy> proxies = new HashMap<>();
    private int lastRow = -1;
    private Window validationResultPopup;
    private EventListener popupCloseListener;


    public RowInlineEditorValidatableContainer(final Component container, final EditorState<T> editorState)
    {
        super(container, editorState);
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return new InlineEditorFocusTransfer(getEditorState());
    }


    @Override
    protected ValidationResult getRootValidationResult()
    {
        if(lastRow > -1)
        {
            final EditorState<T> editorState = getEditorState();
            final T row = editorState.getRow(lastRow);
            final ValidationResult validationResult = editorState.getValidationResult(row);
            return validationResult;
        }
        else
        {
            return ValidationResult.EMPTY;
        }
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return StringUtils.isEmpty(path)
                        || (lastRow > -1 && ObjectValuePath.parse(getEditorState().getRowPath(lastRow)).startsWith(path));
    }


    @Override
    public T getCurrentObject(final String path)
    {
        final int rowIndex = EditorState.getRowIndex(path);
        if(rowIndex == lastRow && rowIndex > -1)
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
        if(!proxies.containsKey(observer))
        {
            final ValueObserverProxy proxy = new ValueObserverProxy(observer);
            proxies.put(observer, proxy);
            getEditorState().getValidationObservable().addObserver(proxy);
        }
    }


    @Override
    public void addValidationObserver(final String key, final ValueObserver observer)
    {
        if(!proxies.containsKey(observer))
        {
            final ValueObserverProxy proxy = new ValueObserverProxy(observer);
            proxies.put(observer, proxy);
            getEditorState().getValidationObservable().addObserver(key, proxy);
        }
    }


    @Override
    public void removeValidationObserver(final ValueObserver observer)
    {
        if(proxies.containsKey(observer))
        {
            final ValueObserverProxy proxy = proxies.remove(observer);
            getEditorState().getValidationObservable().removeObserver(proxy);
        }
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        final int currentRow = EditorState.getRowIndex(path);
        return !preventBroadcastValidationChange && currentRow > -1 && lastRow == currentRow
                        && (isRootPath(path) || getValidationResultPopup().isVisible());
    }


    public int getLastRow()
    {
        return lastRow;
    }


    private void clearLastValidatedRow()
    {
        this.lastRow = -1;
    }


    @Override
    public Window getValidationResultPopup()
    {
        return validationResultPopup;
    }


    @Override
    public void setValidationResultPopup(final Window validationResultPopup)
    {
        if(this.validationResultPopup != null && popupCloseListener != null)
        {
            this.validationResultPopup.removeEventListener(Events.ON_CLOSE, popupCloseListener);
            this.popupCloseListener = null;
        }
        this.validationResultPopup = validationResultPopup;
        popupCloseListener = event -> clearLastValidatedRow();
        this.validationResultPopup.addEventListener(ON_CLOSE_PRIORITY, Events.ON_CLOSE, popupCloseListener);
    }


    protected class ValueObserverProxy implements ValueObserver
    {
        private final ValueObserver observer;


        public ValueObserverProxy(final ValueObserver observer)
        {
            this.observer = observer;
        }


        @Override
        public void modelChanged()
        {
            if(lastRow > -1)
            {
                observer.modelChanged(getEditorState().getRowPath(lastRow));
            }
        }


        @Override
        public void modelChanged(final String property)
        {
            final int rowIndex = EditorState.getRowIndex(property);
            if(StringUtils.isEmpty(EditorState.getRowPath(property)))
            {
                lastRow = rowIndex;
            }
            if(rowIndex == lastRow)
            {
                observer.modelChanged(property);
            }
        }
    }
}
