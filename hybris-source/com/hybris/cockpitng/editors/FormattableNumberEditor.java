/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import com.hybris.cockpitng.core.util.Validate;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.impl.FormatInputElement;
import org.zkoss.zul.impl.InputElement;

/**
 * Marks editor as being number editor with configurable number format. The
 * {@link FormattableNumberEditor#setNumberFormat(InputElement, EditorContext)} method is called automatically by
 * {@link com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer#initViewComponent(InputElement, EditorContext, EditorListener)}
 * .
 *
 * @param <T>
 *           subtype of {@link Number} the editor supports
 */
public interface FormattableNumberEditor<T extends Number>
{
    /**
     * Name of {@code editor-parameter} used to configure number format
     */
    String PARAMETER_NUMBER_FORMAT = "numberFormat";


    /**
     * Sets the number format of {@link InputElement}.
     * Default implementation works only for {@link FormatInputElement} view components and sets the number format to the
     * value of {@link FormattableNumberEditor#PARAMETER_NUMBER_FORMAT} parameter passed in {@code context} object. It
     * also switches off the {@code InputElement.instant} flag.
     *
     * @param editorView
     *           view component of editor
     * @param context
     */
    default void setNumberFormat(final InputElement editorView, final EditorContext<T> context)
    {
        Validate.notNull("All parameters are mandatory", editorView, context);
        if(editorView instanceof FormatInputElement)
        {
            final String numberFormat = (String)context.getParameter(PARAMETER_NUMBER_FORMAT);
            if(StringUtils.isNotEmpty(numberFormat))
            {
                ((FormatInputElement)editorView).setFormat(numberFormat);
            }
        }
    }
}
