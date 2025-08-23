/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean;

/**
 * Modification bean to set new value of a property of specified bean from any spring contexts in hierarchy
 */
public class PropertyBeanExtender extends AbstractBeanExtender
{
    public static final String BEAN_NAME = "propertyExtender";
    private Object value;


    public PropertyBeanExtender(final String extendedBeanName)
    {
        super(extendedBeanName);
    }


    @Override
    protected void checkProperty(final Object target)
    {
        // not implemented
    }


    @Override
    protected Object modifyProperty(final Object target)
    {
        return value;
    }


    protected Object getValue()
    {
        return value;
    }


    public void setValue(final Object value)
    {
        this.value = value;
    }
}
