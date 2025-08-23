/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.model;

import java.util.List;

public class GroupDescriptor
{
    private final String name;
    private final List<CompareAttributeDescriptor> compareAttributes;


    public GroupDescriptor(final String name, final List<CompareAttributeDescriptor> compareAttributes)
    {
        this.name = name;
        this.compareAttributes = compareAttributes;
    }


    public String getName()
    {
        return name;
    }


    public List<CompareAttributeDescriptor> getCompareAttributes()
    {
        return compareAttributes;
    }
}
