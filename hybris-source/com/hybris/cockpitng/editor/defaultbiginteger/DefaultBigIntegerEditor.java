/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultbiginteger;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.FormattableNumberEditor;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import java.math.BigInteger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

/**
 * Editor for BigInteger types
 */
public class DefaultBigIntegerEditor extends AbstractTextBasedEditorRenderer<BigInteger> implements
                FormattableNumberEditor<BigInteger>
{
    @Override
    protected void setRawValue(final InputElement editorView, final BigInteger rawValue)
    {
        editorView.setRawValue(rawValue != null ? rawValue.toString() : "");
    }


    @Override
    protected BigInteger getRawValue(final InputElement editorView)
    {
        try
        {
            return new BigInteger((String)editorView.getRawValue());
        }
        catch(final NumberFormatException ex)
        {
            return null;
        }
    }


    @Override
    protected BigInteger coerceFromString(final InputElement editorView, final String text) throws WrongValueException
    {
        try
        {
            return new BigInteger(text);
        }
        catch(final NumberFormatException ex)
        {
            return null;
        }
    }


    @Override
    public void render(final Component parent, final EditorContext<BigInteger> context, final EditorListener<BigInteger> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Textbox textbox = new Textbox();
        final StringBuilder constraintBuilder = new StringBuilder();
        if(!context.isOptional())
        {
            constraintBuilder.append("no empty,");
        }
        constraintBuilder.append("/(-[0-9]+)|([0-9]*)/");
        textbox.setConstraint(constraintBuilder.toString());
        textbox.setParent(parent);
        if(context.getInitialValue() != null)
        {
            textbox.setValue(context.getInitialValue().toString());
        }
        initViewComponent(textbox, context, listener);
    }
}
