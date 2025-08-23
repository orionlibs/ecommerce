/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.bool;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

public class DefaultBooleanEditor extends AbstractBooleanEditorRenderer
{
    private static final String BOOLEAN_WRAPPER_CLASS = "ye-default_boolean_editor_wrapper ye-com_hybris_cockpitng_editor_boolean_wrapper";
    private static final String BOOLEAN_RADIO_GROUP_SCLASS = "ye-default_boolean_editor_radiogroup";


    @Override
    public void render(final Component parent, final EditorContext<Boolean> context, final EditorListener<Boolean> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final BooleanRadioGroup editorView = new BooleanRadioGroup(context);
        editorView.setSclass(BOOLEAN_RADIO_GROUP_SCLASS);
        if(!context.isEditable())
        {
            editorView.setDisabled(true);
        }
        editorView.setValue(context.getInitialValue());
        editorView.addEventListener(Events.ON_BLUR, event -> handleOnBlurEvent(listener));
        editorView.addEventListener(Events.ON_CHECK, event -> handleOkEvent(listener, editorView));
        editorView.addEventListener(Events.ON_OK, event -> handleOkEvent(listener, editorView));
        editorView.addEventListener(Events.ON_CANCEL, event -> handleCancelEvent(listener, context));
        editorView.setParent(parent);
    }


    private static void handleOnBlurEvent(final EditorListener<Boolean> listener)
    {
        listener.onEditorEvent(EditorListener.FOCUS_LOST);
    }


    private static void handleOkEvent(final EditorListener<Boolean> listener, final BooleanRadioGroup editorView)
    {
        listener.onValueChanged(editorView.getValue());
        listener.onEditorEvent(EditorListener.ENTER_PRESSED);
    }


    private static void handleCancelEvent(final EditorListener<Boolean> listener, final EditorContext<Boolean> context)
    {
        listener.onValueChanged(context.getInitialValue());
        listener.onEditorEvent(EditorListener.ESCAPE_PRESSED);
    }


    private class BooleanRadioGroup extends Radiogroup
    {
        private final Radio trueRadio;
        private final Radio falseRadio;
        private Radio nullRadio;
        private final boolean primitive;


        public BooleanRadioGroup(final EditorContext<Boolean> context)
        {
            super();
            primitive = context.isPrimitive();
            trueRadio = new Radio(getBooleanLabel(context, Boolean.TRUE));
            trueRadio.setAttribute("val", Boolean.TRUE);
            falseRadio = new Radio(getBooleanLabel(context, Boolean.FALSE));
            falseRadio.setAttribute("val", Boolean.FALSE);
            final Hbox box = new Hbox();
            box.setSpacing("5px");
            final Div wrapper = new Div();
            wrapper.setSclass(BOOLEAN_WRAPPER_CLASS);
            box.setParent(wrapper);
            BooleanRadioGroup.this.appendChild(wrapper);
            box.appendChild(trueRadio);
            box.appendChild(falseRadio);
            if(BooleanRadioGroup.this.shouldShowOptionalField(context))
            {
                nullRadio = new Radio(getBooleanLabel(context, null));
                box.appendChild(nullRadio);
            }
            BooleanRadioGroup.this.applyProperties();
            if(nullRadio != null)
            {
                nullRadio.setChecked(true);
            }
        }


        public boolean shouldShowOptionalField(final EditorContext<Boolean> context)
        {
            final Boolean showOptionalField = getShowOptionalFieldParamValue(context);
            return showOptionalField != null ? showOptionalField : context.isOptional();
        }


        public void setDisabled(final boolean value)
        {
            trueRadio.setDisabled(value);
            falseRadio.setDisabled(value);
            if(nullRadio != null)
            {
                nullRadio.setDisabled(value);
            }
        }


        public void setValue(final Boolean value)
        {
            if(value == null)
            {
                if(nullRadio != null)
                {
                    nullRadio.setChecked(true);
                }
            }
            else if(value.equals(Boolean.TRUE))
            {
                trueRadio.setChecked(true);
            }
            else
            {
                falseRadio.setChecked(true);
            }
        }


        public Boolean getValue()
        {
            final Radio sel = getSelectedItem();
            Boolean ret = primitive ? Boolean.FALSE : null;
            if(sel != null)
            {
                if(sel.equals(trueRadio))
                {
                    ret = Boolean.TRUE;
                }
                else if(sel.equals(falseRadio))
                {
                    ret = Boolean.FALSE;
                }
            }
            return ret;
        }
    }
}
