/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultbigdecimal;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.FormattableNumberEditor;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import java.math.BigDecimal;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.impl.InputElement;

/**
 * Support for {@link java.math.BigDecimal}
 */
public class DefaultBigDecimalEditor extends AbstractTextBasedEditorRenderer<BigDecimal> implements
                FormattableNumberEditor<BigDecimal>
{
    @Override
    protected void setRawValue(final InputElement editorView, final BigDecimal rawValue)
    {
        editorView.setRawValue(rawValue);
    }


    @Override
    protected BigDecimal getRawValue(final InputElement editorView)
    {
        return (BigDecimal)editorView.getRawValue();
    }


    @Override
    protected BigDecimal coerceFromString(final InputElement editorView, final String text) throws WrongValueException
    {
        return ((InternalDecimalbox)editorView).coerceFromString(text);
    }


    @Override
    public void render(final Component parent, final EditorContext<BigDecimal> context, final EditorListener<BigDecimal> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Decimalbox editorView = new InternalDecimalbox();
        editorView.setValue(context.getInitialValue());
        initViewComponent(editorView, context, listener);
        editorView.setParent(parent);
    }


    private static class InternalDecimalbox extends Decimalbox
    {
        @Override
        public BigDecimal coerceFromString(final String value) throws WrongValueException
        {
            return (BigDecimal)super.coerceFromString(value);
        }
    }
}
