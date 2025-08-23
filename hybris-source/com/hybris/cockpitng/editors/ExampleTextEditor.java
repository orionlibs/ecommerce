/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;

/**
 * An example of a Cockpit NG editor for text values.
 */
public class ExampleTextEditor implements CockpitEditorRenderer<String>
{
    /**
     * Parameter specifying if the editor should be rendered as multiline text box.
     */
    public static final String PARAM_IS_MULTILINE = "is-multiline";
    /**
     * Parameter specifying the number of rows for the multiline case.
     */
    public static final String PARAM_ROWS = "rows";


    @Override
    public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
    {
        // create UI component
        final Textbox editorView = new Textbox();
        // multiline parameter
        if(isMultiline(context))
        {
            editorView.setMultiline(true);
            // columns parameter
            editorView.setRows(getRows(context));
        }
        // set the initial value
        editorView.setValue(context.getInitialValue());
        // set the editable state
        if(!context.isEditable())
        {
            if(Executions.getCurrent().getUserAgent().contains("MSIE"))
            {
                editorView.setReadonly(true);
            }
            else
            {
                editorView.setDisabled(true);
            }
        }
        // handle events
        editorView.addEventListener(Events.ON_CHANGE, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                handleEvent(editorView, event, listener);
            }
        });
        editorView.addEventListener(Events.ON_OK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                handleEvent(editorView, event, listener);
            }
        });
        // add the UI component to the component tree
        editorView.setParent(parent);
    }


    private boolean isMultiline(final EditorContext<String> context)
    {
        boolean multiLine = false;
        final Object multiLineParam = context.getParameter(PARAM_IS_MULTILINE);
        if(multiLineParam instanceof Boolean)
        {
            multiLine = ((Boolean)multiLineParam).booleanValue();
        }
        else if(multiLineParam instanceof String)
        {
            multiLine = BooleanUtils.toBoolean((String)multiLineParam);
        }
        return multiLine;
    }


    private int getRows(final EditorContext<String> context)
    {
        int result = 1;
        final Object rows = context.getParameter(PARAM_ROWS);
        if(rows instanceof Integer)
        {
            result = ((Integer)rows).intValue();
        }
        else if(rows instanceof String)
        {
            try
            {
                result = NumberUtils.createInteger(((String)rows));
            }
            catch(final NumberFormatException e)
            {
                result = 1;
            }
        }
        return Math.max(1, result);
    }


    /**
     * Handle a view event on the editor view component.
     *
     * @param editorView
     *           the view component
     * @param event
     *           the event to be handled
     * @param listener
     *           the editor listener to send change notifications to
     */
    private void handleEvent(final Textbox editorView, final Event event, final EditorListener<String> listener)
    {
        final String result = (String)editorView.getRawValue();
        listener.onValueChanged(StringUtils.isEmpty(result) ? "" : result);
        if(Events.ON_OK.equals(event.getName()))
        {
            listener.onEditorEvent(EditorListener.ENTER_PRESSED);
        }
    }
}
