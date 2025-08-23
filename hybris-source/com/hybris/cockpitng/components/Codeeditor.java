/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.InputElement;

/**
 * Wrapper component for a syntax-highlighting enabled editor.
 * By default it uses codemirror, but can also be used as component class for other client-side implementations.
 */
public class Codeeditor extends InputElement
{
    private static final long serialVersionUID = -5195134750152909776L;
    private String syntax;
    private boolean readonly;

    static
    {
        addClientEvent(Codeeditor.class, Events.ON_CHANGE, CE_IMPORTANT);
    }

    @Override
    public void service(final AuRequest request, final boolean everError)
    {
        super.service(request, everError);
    }


    /**
     * @return The syntax used by the editor to highlight content. If null, "xml" is used internally as fallback.
     */
    public String getSyntax()
    {
        return syntax;
    }


    private String getSyntaxFallback()
    {
        return syntax == null ? "xml" : syntax;
    }


    /**
     * @param syntax The syntax that should be used by the editor to highlight content.
     */
    public void setSyntax(final String syntax)
    {
        if((this.syntax == null && syntax != null) || (this.syntax != null && !this.syntax.equals(syntax)))
        {
            this.syntax = syntax;
            smartUpdate("syntax", getSyntaxFallback());
        }
    }


    @Override
    public int getMaxlength()
    {
        return super.getMaxlength();
    }


    @Override
    protected void renderProperties(final org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException
    {
        super.renderProperties(renderer);
        render(renderer, "syntax", getSyntaxFallback());
        render(renderer, "value", getValue());
        render(renderer, "readonly", isReadonly());
    }


    /**
     * @return The string that is currently displayed by the editor.
     */
    public String getValue()
    {
        return _value == null ? null : String.valueOf(_value);
    }


    /**
     * @param value The String that should be displayed by the editor.
     */
    public void setValue(final String value)
    {
        if((this._value == null && value != null) || (this._value != null && !this._value.equals(value)))
        {
            this._value = value;
            smartUpdate("value", value);
        }
    }


    /**
     * See {@link InputElement#isReadonly()}.
     */
    @Override
    public boolean isReadonly()
    {
        return readonly;
    }


    /**
     * See {@link InputElement#setReadonly(boolean)}.
     */
    @Override
    public void setReadonly(final boolean readonly)
    {
        this.readonly = readonly;
    }


    @Override
    protected Object coerceFromString(final String strValue)
    {
        return StringUtils.defaultIfBlank(strValue, "");
    }


    @Override
    protected String coerceToString(final Object objValue)
    {
        return ObjectUtils.toString(objValue, "");
    }
}
