/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaulttext;

import com.hybris.cockpitng.editor.text.AbstractTextEditor;
import com.hybris.cockpitng.editors.EditorContext;
import java.util.Base64;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

/**
 * Default Editor for {@link String} values.
 */
public class DefaultTextEditor extends AbstractTextEditor<String>
{
    public static final String ZK_FUNCTION_COERCE_FROM_STRING = "coerceFromString_";
    public static final String ZK_FUNCTION_COERCE_TO_STRING = "coerceToString_";
    public static final String COERCE_FROM_STRING_JS = "function(value) { return B64.encode(this.$coerceFromString_(value)); }";
    public static final String COERCE_TO_STRING_JS = "function(value) { try{ return B64.decode(this.$coerceToString_(value)); } catch(err) { return this.$coerceToString_(value); }}";
    /**
     * Base64 encoding parameter name
     */
    static final String BASE64_ENCODED_PARAM = "base64Encoded";


    public DefaultTextEditor()
    {
        super(java.lang.String.class);
    }


    @Override
    protected void initAdditionalParameters(final Textbox editorView, final EditorContext<String> context)
    {
        super.initAdditionalParameters(editorView, context);
        final Boolean base64EncodedParam = (Boolean)context.getParameter(BASE64_ENCODED_PARAM);
        final boolean base64EncodedComponent = BooleanUtils.toBooleanDefaultIfNull(base64EncodedParam, false);
        if(base64EncodedComponent)
        {
            editorView.setWidgetOverride(ZK_FUNCTION_COERCE_FROM_STRING, COERCE_FROM_STRING_JS);
            editorView.setWidgetOverride(ZK_FUNCTION_COERCE_TO_STRING, COERCE_TO_STRING_JS);
            editorView.setAttribute(BASE64_ENCODED_PARAM, Boolean.TRUE);
        }
    }


    @Override
    protected void setRawValue(final InputElement viewComponent, final String rawValue)
    {
        String rawValueToSet = rawValue;
        if(isBase64EncodedViewComponent(viewComponent))
        {
            rawValueToSet = encodeToBase64Value(rawValue);
        }
        super.setRawValue(viewComponent, rawValueToSet);
    }


    @Override
    protected String getRawValue(final InputElement viewComponent)
    {
        String rawValue = super.getRawValue(viewComponent);
        if(rawValue != null && isBase64EncodedViewComponent(viewComponent))
        {
            rawValue = decodeFromBase64Value(rawValue);
        }
        return rawValue;
    }


    @Override
    protected String convertToString(final InputElement editorView, final String initialValue)
    {
        final String stringValue;
        if(isBase64EncodedViewComponent(editorView))
        {
            stringValue = encodeToBase64Value(initialValue);
        }
        else
        {
            stringValue = super.convertToString(editorView, initialValue);
        }
        return stringValue;
    }


    boolean isBase64EncodedViewComponent(final InputElement viewComponent)
    {
        return Boolean.TRUE.equals(viewComponent.getAttribute(BASE64_ENCODED_PARAM));
    }


    private String encodeToBase64Value(final String rawValue)
    {
        String encoded = null;
        if(rawValue != null)
        {
            encoded = Base64.getEncoder().encodeToString(rawValue.getBytes());
        }
        return encoded;
    }


    private String decodeFromBase64Value(final String base64Value)
    {
        final byte[] decodedBytes = Base64.getDecoder().decode(base64Value.getBytes());
        return new String(decodedBytes);
    }
}
