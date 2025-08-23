/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.text;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

/**
 * @param <T> supported text classes: {@link java.lang.String}, {@link java.lang.Character}
 */
public abstract class AbstractTextEditor<T> extends AbstractTextBasedEditorRenderer<T>
{
    private static final String ROWS_PARAM = "rows";
    private static final String TEXT_EDITOR_SCLASS = "ye-input-text";
    private static final String PLACEHOLDER_PARAM = "placeholder";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTextEditor.class);
    private final Class<T> valueType;


    public AbstractTextEditor(final Class<T> valueType)
    {
        this.valueType = valueType;
    }


    @Override
    public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Textbox editorView = new Textbox();
        editorView.setSclass(TEXT_EDITOR_SCLASS);
        if(java.lang.Character.class.equals(valueType))
        {
            editorView.setMaxlength(1);
        }
        final Object rows = context.getParameter(ROWS_PARAM);
        if(rows instanceof String)
        {
            try
            {
                editorView.setRows(Integer.parseInt((String)rows));
                UITools.modifySClass(editorView, "ye-rows-height", true);
            }
            catch(final WrongValueException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        initAdditionalParameters(editorView, context);
        editorView.setValue(convertToString(editorView, context.getInitialValue()));
        final String placeholderstr = (String)context.getParameter(PLACEHOLDER_PARAM);
        if(StringUtils.isNotEmpty(placeholderstr))
        {
            editorView.setPlaceholder(placeholderstr);
        }
        initViewComponent(editorView, context, listener);
        editorView.setParent(parent);
    }


    /**
     * Additional initialization of ui component according to specified context
     * @param editorView ui component
     * @param context context
     */
    protected void initAdditionalParameters(final Textbox editorView, final EditorContext<T> context)
    {
        // default empty implementation
    }


    @Override
    protected void setRawValue(final InputElement viewComponent, final T rawValue)
    {
        viewComponent.setRawValue(rawValue);
    }


    @Override
    protected T getRawValue(final InputElement viewComponent)
    {
        return (T)viewComponent.getRawValue();
    }


    @Override
    protected T coerceFromString(final InputElement editorView, final String text) throws WrongValueException
    {
        return convertFromString(text);
    }


    /**
     * Converts component value to String
     * @param editorView ui component
     * @param initialValue vomponent value
     * @return string value
     */
    protected String convertToString(final InputElement editorView, final T initialValue)
    {
        String ret = null;
        if(initialValue != null)
        {
            if(java.lang.String.class.equals(valueType))
            {
                ret = (String)initialValue;
            }
            else if(java.lang.Character.class.equals(valueType))
            {
                ret = initialValue.toString();
            }
        }
        return ret;
    }


    private T convertFromString(final String value)
    {
        T ret = null;
        if(value != null)
        {
            if(java.lang.String.class.equals(valueType))
            {
                ret = (T)value;
            }
            else if(java.lang.Character.class.equals(valueType))
            {
                ret = value.length() > 0 ? (T)new java.lang.Character(value.charAt(0)) : null;
            }
        }
        return ret;
    }
}
