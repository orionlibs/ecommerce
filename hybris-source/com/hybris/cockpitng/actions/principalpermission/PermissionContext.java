/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.principalpermission;

public class PermissionContext
{
    private final String typeCode;
    private final Object currentObject;


    public PermissionContext(final String typeCode, final Object currentObject)
    {
        this.typeCode = typeCode;
        this.currentObject = currentObject;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public Object getCurrentObject()
    {
        return currentObject;
    }
}
