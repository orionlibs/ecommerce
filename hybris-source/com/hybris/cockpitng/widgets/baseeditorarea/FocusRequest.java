/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

/**
 * DTO that describes request to focus editor on specified attribute
 */
public class FocusRequest
{
    private final Object object;
    private final String qualifier;


    public FocusRequest(final Object object, final String qualifier)
    {
        this.object = object;
        this.qualifier = qualifier;
    }


    /**
     * An item which attribute is to be focused
     *
     * @return item
     */
    public Object getObject()
    {
        return object;
    }


    /**
     * Attribute qualifier to be focused
     *
     * @return qualifier
     */
    public String getQualifier()
    {
        return qualifier;
    }
}
