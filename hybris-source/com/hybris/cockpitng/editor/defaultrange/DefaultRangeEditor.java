/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultrange;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.Range;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Default implementation of the Range UI Editor.
 */
public class DefaultRangeEditor<T> extends AbstractCockpitEditorRenderer<Range<T>>
{
    private static final String DEFAULT_END_PARAMETER_L10N_KEY = "rangeeditor.end";
    private static final String DEFAULT_START_PARAMETER_L10N_KEY = "rangeeditor.start";
    private static final String END_PARAMETER = "endLabel";
    private static final String START_PARAMETER = "startLabel";
    public static final String ALLOW_INFINITE_ENDPOINTS_PARAMETER = "allowInfiniteEndpoints";
    private static final String YE_RANGE = "ye-range";
    private static final String YE_RANGE_ENDPOINT = "ye-range-endpoint";
    private static final String YE_RANGE_START_ENDPOINT = "ye-range-endpoint-start";
    private static final String YE_RANGE_END_ENDPOINT = "ye-range-endpoint-end";
    private static final String YE_RANGE_LABEL = "ye-range-label";
    private static final String YE_RANGE_EDITOR = "ye-range-editor";
    private Editor startEditor;
    private Editor endEditor;


    /**
     * Sets the given range value for the range editor.
     *
     * @param value
     *           Sets range value.
     */
    public void setCurrentValue(final Range<T> value)
    {
        if(value == null)
        {
            startEditor.setValue(null);
            endEditor.setValue(null);
        }
        else
        {
            startEditor.setValue(value.getStart());
            endEditor.setValue(value.getEnd());
        }
    }


    @Override
    public void render(final Component parent, final EditorContext<Range<T>> context, final EditorListener<Range<T>> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div container = new Div();
        container.setSclass(YE_RANGE);
        final Label startLabel = new Label(getL10nDecorator(context, START_PARAMETER, DEFAULT_START_PARAMETER_L10N_KEY));
        startLabel.setSclass(YE_RANGE_LABEL);
        final Label endLabel = new Label(getL10nDecorator(context, END_PARAMETER, DEFAULT_END_PARAMETER_L10N_KEY));
        endLabel.setSclass(YE_RANGE_LABEL);
        startEditor = createEditor(context, listener);
        endEditor = createEditor(context, listener);
        setCurrentValue(context.getInitialValue());
        startEditor.afterCompose();
        endEditor.afterCompose();
        final Div fromEndpoint = new Div();
        UITools.addSClass(fromEndpoint, YE_RANGE_ENDPOINT);
        UITools.addSClass(fromEndpoint, YE_RANGE_START_ENDPOINT);
        fromEndpoint.appendChild(startLabel);
        fromEndpoint.appendChild(startEditor);
        container.appendChild(fromEndpoint);
        final Div toEndpoint = new Div();
        UITools.addSClass(toEndpoint, YE_RANGE_ENDPOINT);
        UITools.addSClass(toEndpoint, YE_RANGE_END_ENDPOINT);
        toEndpoint.appendChild(endLabel);
        toEndpoint.appendChild(endEditor);
        container.appendChild(toEndpoint);
        container.setParent(parent);
    }


    /**
     * Creates single {@link Editor} component for the range embedded type.
     *
     * @return {@link Editor}
     */
    protected Editor createEditor(final EditorContext<Range<T>> context, final EditorListener<Range<T>> listener)
    {
        final String rangeType = extractEmbeddedType(context);
        final Editor editorContainer = new Editor();
        final StringBuilder sclass = new StringBuilder(YE_RANGE_EDITOR);
        if(StringUtils.isNotEmpty(rangeType))
        {
            final int indexOf = rangeType.indexOf('(');
            final String editorSclass;
            if(indexOf > -1)
            {
                editorSclass = StringUtils.lowerCase(rangeType.substring(0, indexOf));
            }
            else
            {
                editorSclass = StringUtils.lowerCase(rangeType);
            }
            sclass.append('-').append(editorSclass.replaceAll("\\.", "-"));
        }
        editorContainer.setReadableLocales(context.getReadableLocales());
        editorContainer.setWritableLocales(context.getWritableLocales());
        editorContainer.setSclass(sclass.toString());
        editorContainer.setType(rangeType);
        editorContainer.setReadOnly(!context.isEditable());
        editorContainer.setOptional(context.isOptional());
        editorContainer.addEventListener(com.hybris.cockpitng.components.Editor.ON_VALUE_CHANGED,
                        createEditorListener(context, listener));
        editorContainer.setOrdered(context.isOrdered());
        editorContainer.addParameters(context.getParameters());
        editorContainer.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        return editorContainer;
    }


    /**
     * Create a wrapper listener from embedded editor listeners to this range Editor listeners.
     *
     * @param editorContext
     *           range editor context
     * @param listener
     *           top level listener to be notified about value changes
     * @return the listener
     */
    protected EventListener<Event> createEditorListener(final EditorContext<Range<T>> editorContext,
                    final EditorListener<Range<T>> listener)
    {
        final EventListener<Event> closedOnlyListener = createEditorListener(listener);
        if(editorContext.getParameterAsBoolean(ALLOW_INFINITE_ENDPOINTS_PARAMETER, false))
        {
            return event -> {
                final T from = (T)startEditor.getValue();
                final T to = (T)endEditor.getValue();
                if(from == null ^ to == null)
                {
                    listener.onValueChanged(new Range<>(from, to));
                }
                else
                {
                    closedOnlyListener.onEvent(event);
                }
            };
        }
        else
        {
            return closedOnlyListener;
        }
    }


    /**
     * Create a wrapper listener from embedded editor listeners to Editor listeners.
     *
     * @return the listener
     * @deprecated since 6.5
     * @see #createEditorListener(EditorContext, EditorListener)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected EventListener<Event> createEditorListener(final EditorListener<Range<T>> listener)
    {
        return event -> {
            final T from = (T)startEditor.getValue();
            final T to = (T)endEditor.getValue();
            if(from == null && to == null)
            {
                listener.onValueChanged(new Range<>(null, null));
            }
            else if(from != null && to != null)
            {
                listener.onValueChanged(new Range<>(from, to));
            }
            else
            {
                listener.onValueChanged(null);
            }
        };
    }


    /**
     * @return the startEditor
     */
    public Editor getStartEditor()
    {
        return startEditor;
    }


    /**
     * @param startEditor
     *           the startEditor to set
     */
    public void setStartEditor(final Editor startEditor)
    {
        this.startEditor = startEditor;
    }


    /**
     * @return the endEditor
     */
    public Editor getEndEditor()
    {
        return endEditor;
    }


    /**
     * @param endEditor
     *           the endEditor to set
     */
    public void setEndEditor(final Editor endEditor)
    {
        this.endEditor = endEditor;
    }
}
