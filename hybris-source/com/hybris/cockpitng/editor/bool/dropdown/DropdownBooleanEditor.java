/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.bool.dropdown;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.bool.AbstractBooleanEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 * Boolean Editor using {@link Combobox} component.
 */
public class DropdownBooleanEditor extends AbstractBooleanEditorRenderer
{
    @Override
    public void render(final Component parent, final EditorContext<Boolean> context, final EditorListener<Boolean> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final BooleanCombobox editorView = new BooleanCombobox(context);
        editorView.setReadonly(true);
        if(!context.isEditable())
        {
            editorView.setDisabled(true);
        }
        editorView.setValue(context.getInitialValue());
        editorView.addEventListener(Events.ON_CHANGE, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                handleChangeEvent(listener, editorView);
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
        editorView.addEventListener(Events.ON_CANCEL, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                handleCancelEvent(listener, editorView, context.getInitialValue());
            }
        });
        parent.appendChild(editorView);
    }


    private void handleCancelEvent(final EditorListener<Boolean> listener, final BooleanCombobox editorView,
                    final Boolean initialValue)
    {
        editorView.setValue(initialValue);
        listener.onValueChanged(initialValue);
        listener.onEditorEvent(EditorListener.ESCAPE_PRESSED);
    }


    private void handleOkEvent(final EditorListener<Boolean> listener, final BooleanCombobox editorView)
    {
        listener.onValueChanged(editorView.getSelectedItem() == null ? null : editorView.getEditorValue());
        listener.onEditorEvent(EditorListener.ENTER_PRESSED);
    }


    private void handleChangeEvent(final EditorListener<Boolean> listener, final BooleanCombobox editorView)
    {
        listener.onValueChanged(editorView.getSelectedItem() == null ? null : editorView.getEditorValue());
    }


    private class BooleanCombobox extends Combobox
    {
        private final Comboitem trueItem;
        private final Comboitem falseItem;
        private Comboitem nullItem;
        private final boolean primitive;


        public BooleanCombobox(final EditorContext<Boolean> context)
        {
            super();
            primitive = context.isPrimitive();
            trueItem = new Comboitem(getBooleanLabel(context, Boolean.TRUE));
            trueItem.setValue(Boolean.TRUE);
            BooleanCombobox.this.appendChild(trueItem);
            falseItem = new Comboitem(getBooleanLabel(context, Boolean.FALSE));
            falseItem.setValue(Boolean.FALSE);
            BooleanCombobox.this.appendChild(falseItem);
            if(BooleanCombobox.this.shouldShowOptionalField(context))
            {
                nullItem = new Comboitem(getBooleanLabel(context, null));
                BooleanCombobox.this.appendChild(nullItem);
            }
            if(nullItem != null)
            {
                BooleanCombobox.this.setSelectedItem(nullItem);
            }
        }


        public boolean shouldShowOptionalField(final EditorContext<Boolean> context)
        {
            final Boolean showOptionalField = getShowOptionalFieldParamValue(context);
            return showOptionalField != null ? showOptionalField : context.isOptional();
        }


        public void setValue(final Boolean value)
        {
            if(value == null)
            {
                if(nullItem != null)
                {
                    this.setSelectedItem(nullItem);
                }
            }
            else if(value.equals(Boolean.TRUE))
            {
                this.setSelectedItem(trueItem);
            }
            else
            {
                this.setSelectedItem(falseItem);
            }
        }


        public Boolean getEditorValue()
        {
            final Comboitem sel = this.getSelectedItem();
            Boolean ret = primitive ? Boolean.FALSE : null;
            if(sel != null)
            {
                if(sel.equals(trueItem))
                {
                    ret = Boolean.TRUE;
                }
                else if(sel.equals(falseItem))
                {
                    ret = Boolean.FALSE;
                }
            }
            return ret;
        }
    }
}
