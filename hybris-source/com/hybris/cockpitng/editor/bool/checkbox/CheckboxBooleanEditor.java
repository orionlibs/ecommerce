/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.bool.checkbox;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.bool.AbstractBooleanEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;

/**
 * Checkbox style boolean editor.
 */
public class CheckboxBooleanEditor extends AbstractBooleanEditorRenderer
{
    private static final String TREAT_NULL_VALUES_AS_TRUE = "treatNullValuesAsTrue";
    /** @deprecated since 6.7 - renderer should be stateless */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean treatNullValuesAsTrue;


    @Override
    public void render(final Component parent, final EditorContext<Boolean> context, final EditorListener<Boolean> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Boolean treatNullValuesAs = Boolean.valueOf((String)context.getParameter(TREAT_NULL_VALUES_AS_TRUE));
        final Checkbox editorView = new Checkbox();
        if(!context.isEditable())
        {
            editorView.setDisabled(true);
        }
        Boolean value = context.getInitialValue();
        if(value == null)
        {
            value = treatNullValuesAs;
        }
        editorView.setChecked(value.booleanValue());
        editorView.addEventListener(Events.ON_CHECK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                handleCheckEvent(listener, editorView);
            }
        });
        editorView.addEventListener(Events.ON_OK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                handleOkEvent(listener, editorView);
            }
        });
        editorView.setParent(parent);
    }


    private void handleCheckEvent(final EditorListener<Boolean> listener, final Checkbox editorView)
    {
        listener.onValueChanged(Boolean.valueOf(editorView.isChecked()));
    }


    private void handleOkEvent(final EditorListener<Boolean> listener, final Checkbox editorView)
    {
        listener.onValueChanged((Boolean)editorView.getValue());
        listener.onEditorEvent(EditorListener.ENTER_PRESSED);
    }
}
