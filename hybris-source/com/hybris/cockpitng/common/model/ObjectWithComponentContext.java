/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.model;

import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;

public class ObjectWithComponentContext extends DefaultCockpitContext
{
    private static final String CONTEXT_PARAMETER_INPUT_OBJECT = "_inputObject";
    private static final String CONTEXT_PARAMETER_CONFIG_CONTEXT = "_configurationContext";
    private String type;


    public ObjectWithComponentContext(final Object object)
    {
        type = null;
        ObjectWithComponentContext.this.setParameter(CONTEXT_PARAMETER_INPUT_OBJECT, object);
    }


    public ObjectWithComponentContext(final Object inputObject, final String componentContext)
    {
        this(inputObject);
        ObjectWithComponentContext.this.setParameter(CONTEXT_PARAMETER_CONFIG_CONTEXT, componentContext);
    }


    public ObjectWithComponentContext(final Object inputObject, final String componentContext, final String type)
    {
        this(inputObject, componentContext);
        this.type = type;
    }


    public Object getInputObject()
    {
        return getParameter(CONTEXT_PARAMETER_INPUT_OBJECT);
    }


    public String getComponentContext()
    {
        return (String)getParameter(CONTEXT_PARAMETER_CONFIG_CONTEXT);
    }


    public String getType()
    {
        return type;
    }
}
