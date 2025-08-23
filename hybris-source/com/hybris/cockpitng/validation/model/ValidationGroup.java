/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

public class ValidationGroup
{
    private String id;


    public ValidationGroup()
    {
    }


    public ValidationGroup(final String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return id;
    }


    public void setId(final String id)
    {
        this.id = id;
    }
}
