/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.FormattableNumberEditor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.impl.InputElement;

/**
 * Abstract text based Editor that all "single input" fields should extend.
 */
public abstract class AbstractTextBasedEditorRenderer<T> extends AbstractCockpitEditorRenderer<T>
{
    public static final String SETTING_INPUT_INSTANT = "inputInstant";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTextBasedEditorRenderer.class);
    private boolean primitive;


    /**
     * Adds event listeners to this editor component, parses the parameter for initial value etc.
     *
     * @param editorView
     *           the editor component previously created in overridden method
     *           {@link AbstractTextBasedEditorRenderer#render(org.zkoss.zk.ui.Component, com.hybris.cockpitng.editors.EditorContext, com.hybris.cockpitng.editors.EditorListener)}
     * @param context
     *           the editor context
     */
    protected void initViewComponent(final InputElement editorView, final EditorContext<T> context,
                    final EditorListener<T> listener)
    {
        final String initialEditText = getInitialEditString(context);
        primitive = context.isPrimitive();
        if(StringUtils.isNotBlank(initialEditText))
        {
            editorView.setText(initialEditText);
        }
        handleReadOnly(context, editorView);
        final Object isInputInstant = context.getParameter(SETTING_INPUT_INSTANT);
        editorView.setInstant(isInputInstant == null ? true : Boolean.parseBoolean(isInputInstant.toString()));
        if(this instanceof FormattableNumberEditor)
        {
            ((FormattableNumberEditor)this).setNumberFormat(editorView, context);
        }
        if(StringUtils.isBlank(editorView.getId()))
        {
            editorView.setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
        }
        editorView.addEventListener(Events.ON_FOCUS, event -> onFocusEvent(editorView, initialEditText));
        editorView.addEventListener(Events.ON_CHANGING, event -> {
            if(event instanceof InputEvent)
            {
                onChangingEvent(listener, editorView, (InputEvent)event);
            }
        });
        editorView.addEventListener(Events.ON_CHANGE, event -> onChangeEvent(listener, editorView));
        editorView.addEventListener(Events.ON_OK, event -> onOkEvent(listener, editorView));
        editorView.addEventListener(Events.ON_CANCEL, event -> onCancelEvent(listener, editorView, context.getInitialValue()));
        editorView.addEventListener(Events.ON_BLUR, event -> listener.onEditorEvent(EditorListener.FOCUS_LOST));
    }


    /**
     * Method called when something changed in the editor, for example every 1 sec while typing something
     *
     * @param editorView
     *           the Editor
     * @param event
     */
    protected void onChangingEvent(final EditorListener<T> listener, final InputElement editorView, final InputEvent event)
    {
        handleChangingEvent(listener, editorView, event.getValue());
    }


    /**
     * Method called when the editor value is changed and the editor is not focused anymore.
     *
     * @param listener
     * @param editorView
     *           the Editor
     */
    protected void onChangeEvent(final EditorListener<T> listener, final InputElement editorView)
    {
        handleChangeEvent(listener, editorView);
    }


    /**
     * Method called when the editor value is set
     *
     * @param listener
     * @param editorView
     *           the Editor
     */
    protected void onOkEvent(final EditorListener<T> listener, final InputElement editorView)
    {
        listener.onEditorEvent(EditorListener.ENTER_PRESSED);
    }


    /**
     * Method called when the user cancelled his input.
     *
     * @param listener
     * @param editorView
     *           the Editor
     */
    protected void onCancelEvent(final EditorListener<T> listener, final InputElement editorView, final T initialValue)
    {
        setRawValue(editorView, initialValue);
        clearInvalidInputAndNotify(listener, editorView);
        listener.onEditorEvent(EditorListener.ESCAPE_PRESSED);
    }


    /**
     * Method called when the user focuses the Editor.
     *
     * @param editorView
     *           the Editor
     * @param initialEditText
     */
    protected void onFocusEvent(final InputElement editorView, final String initialEditText)
    {
        if(StringUtils.isNotBlank(initialEditText) && StringUtils.isBlank(editorView.getText()))
        {
            editorView.setText(initialEditText);
            final int pos = initialEditText.length();
            editorView.setSelectionRange(pos, pos);
        }
    }


    protected void handleChangeEvent(final EditorListener<T> listener, final InputElement editorView)
    {
        handleChangeEvent(listener, getRawValue(editorView));
        clearInvalidInputAndNotify(listener, editorView);
    }


    protected void handleChangeEvent(final EditorListener<T> listener, final T value)
    {
        T result = value;
        if(value == null || StringUtils.isEmpty(value.toString()))
        {
            result = null;
        }
        listener.onValueChanged(result);
    }


    protected void handleChangingEvent(final EditorListener<T> listener, final InputElement editorView, final String stringValue)
    {
        try
        {
            coerceFromString(editorView, stringValue);
            clearInvalidInputAndNotify(listener, editorView);
        }
        catch(final WrongValueException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while handling changing event", e);
            }
            editorView.setAttribute(EditorListener.INVALID_INPUT, Boolean.TRUE);
            listener.onEditorEvent(EditorListener.INVALID_INPUT);
        }
    }


    protected void clearInvalidInputAndNotify(final EditorListener<T> listener, final InputElement editorView)
    {
        if(hasInvalidInput(editorView))
        {
            editorView.setAttribute(EditorListener.INVALID_INPUT, null);
            listener.onEditorEvent(EditorListener.INVALID_INPUT_CLEARED);
        }
    }


    protected boolean hasInvalidInput(final InputElement editorView)
    {
        return Boolean.TRUE.equals(editorView.getAttribute(EditorListener.INVALID_INPUT));
    }


    /**
     * Utility method that assures proper handling of <code>readonly</code>/<code>disabled</code> attributes across
     * different browsers.
     *
     * @param context
     *           editor context
     * @param editorView
     *           the view of the editor
     */
    protected void handleReadOnly(final EditorContext<T> context, final InputElement editorView)
    {
        editorView.setReadonly(!context.isEditable());
    }


    /**
     * Sets the value to the given editor component.
     *
     * @param editorView
     *           the underlying editor component
     * @param rawValue
     *           the value
     */
    protected abstract void setRawValue(final InputElement editorView, final T rawValue);


    /**
     * Extracts value of the given editor component.
     *
     * @param editorView
     *           the underlying editor component
     * @return the value
     */
    protected abstract T getRawValue(final InputElement editorView);


    /**
     * Converts string value to a real value.
     *
     * @param editorView
     *           the underlying editor component
     * @param text
     *           the string vale
     * @return the value
     */
    protected abstract T coerceFromString(final InputElement editorView, String text);


    /**
     * @return true if editor has been created for primitive type like int/boolean/decimal/byte/short/long/char.
     *         Otherwise it returns false.
     */
    public boolean isPrimitive()
    {
        return primitive;
    }
}
